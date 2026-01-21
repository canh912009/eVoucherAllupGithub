package com.castis.publishservice.service;

import com.castis.publishservice.client.BeServiceClient;
import com.castis.publishservice.dto.GoodsDTO;
import com.castis.publishservice.dto.PublishDTO;
import com.castis.publishservice.dto.PublishDetailDTO;
import com.castis.publishservice.dto.PurchaseChildRequest;
import com.castis.publishservice.dto.queue.*;
import com.castis.publishservice.dto.request.*;
import com.castis.publishservice.dto.response.BaseResponse;
import com.castis.publishservice.dto.response.DataResponse;
import com.castis.publishservice.dto.response.HandoverResponse;
import com.castis.publishservice.dto.response.VoucherResponse;
import com.castis.publishservice.entity.ExtPin;
import com.castis.publishservice.entity.MessageTemplate;
import com.castis.publishservice.exception.GenericError;
import com.castis.publishservice.exception.defineException.CustomCodeException;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import com.castis.publishservice.mapper.VoucherMapper;
import com.castis.publishservice.producer.RabbitMQProducer;
import com.castis.publishservice.repository.MessageTemplateRepository;
import com.castis.publishservice.service.common.LockingService;
import com.castis.publishservice.service.external_pin.ExtServiceFactory;
import com.castis.publishservice.utils.Constants;
import com.castis.publishservice.utils.Utils;
import com.castis.publishservice.utils.enum_template.ExtPinStatus;
import com.castis.publishservice.utils.enum_template.SystemType;
import com.castis.publishservice.utils.status.EnumYN;
import com.castis.publishservice.utils.status.GenerateMessageType;
import com.castis.publishservice.utils.status.PublishDetailStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class PrepareDataService {
    private final BrandService brandService;
    private final PublishDetailService detailService;
    private final PublishService publishService;
    private final RabbitMQProducer rabbitMQProducer;
    private final VoucherService voucherService;
    private final BeServiceClient beServiceClient;
    private final CustomerService customerService;
    private final ExtPinService extPinService;
    private final GoodsService goodsService;
    private final MessageTemplateRepository messageTemplateRepository;
    private final RestTemplate restTemplate;
    private final ExtServiceFactory extServiceFactory;
    private final UserService userService;

    private final LockingService lockingService;
    private static final VoucherMapper VOUCHER_MAPPER = VoucherMapper.INSTANCE;

    @Value("${message.transfer.template-id}")
    private Integer transferTemplateId;

    @Value("${e-voucher.url.service-be}")
    private String evoucherServiceBeUrl;


    public void createPublishDetailAndVouchers(PublishDTO publishDTO, PublishRequest publishReq) {
        log.info("Start creating publish detail and voucher publishId={}, publishRequest={}", publishDTO.getId(), Utils.toJson(publishReq));
        List<PublishDetailDTO> detailList;
        VoucherResponse createVoucherResponse = null;
        // create publish detail for each user
        List<PublishDetailDTO> initDetails = createPublishDetails(publishDTO, publishReq);
        log.info("New publish details={}", Utils.toJson(initDetails));

        List<ExtPin> extPins = new ArrayList<>();

        //check remaining external pin count
        log.info("Find Goods with goodsId={}", publishDTO.getGood().getId());
        GoodsDTO goods = goodsService.findById(publishDTO.getGood().getId());

        // only type SI, PP need to set external pin
        if (goodsService.hasExtPinByType(goods.getType())) {
            // only set external pin for voucher not is choice type
            extPins = setExternalPinForPublish(publishDTO, initDetails, goods);
        }

        detailList = detailService.saveAll(initDetails);
        log.info("Finish creating publish details, publishId={}", publishDTO.getId());

        try {
            createVoucherResponse = createVouchers(publishDTO, detailList);

            if (Objects.isNull(createVoucherResponse.getData()) || createVoucherResponse.getData().isEmpty()) {
                String message = "Error while requesting BE to create voucher but got empty data";
                log.error(message);
                throw new ServerRuntimeException(message);
            }

            if (!extPins.isEmpty()) {
                extPins.forEach(o -> o.setStatus(ExtPinStatus.USED));
                log.info("Update ext pins status to USED={}", extPins);
                extPinService.updateExtPins(extPins);
            }

            log.info("Create vouchers successfully={}", Utils.toJson(createVoucherResponse.getData()));
            List<VoucherRequest> voucherRequests = voucherService.getVoucherRequestByVoucherId(createVoucherResponse.getData());
            if (publishDTO.getGood().getSystem() == SystemType.CHOICE) {
                //with choice type: get child good info
                setGoodChoice(voucherRequests, publishDTO.getGood().getId());
            }
            PublishQueueRequest publishRequest = publishService.findRequestById(publishDTO.getId());
            List<PublishDetailQueueRequest> detailRequest = createPublishDetailRequestToQueue(voucherRequests, publishDTO);
            if (StringUtils.isNotBlank(publishRequest.getCustomerId())) {
                publishRequest.setCustomer(customerService.findRequestById(publishRequest.getCustomerId()));
            }

            publishRequest.setPublishDetails(detailRequest);
            publishRequest.setType(GenerateMessageType.NORMAL);
            log.info("Update publish detail, publishDetailIds={}, status->{}",
                    detailList.stream().map(PublishDetailDTO::getPublishDtlId).collect(Collectors.toList()), PublishDetailStatus.END_PUB);
            detailList.forEach(o -> o.setPublishStatusCd(PublishDetailStatus.END_PUB));

            //change status to end publish
            detailService.saveAll(detailList);
            //send to rabbit mq
            log.info("Send voucher info to queue");
            rabbitMQProducer.publishEvoucher(publishRequest);
            log.info("Finish creating and publishing voucher for publishId={}", publishDTO.getId());
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            log.info("Update publish detail status to :{}", PublishDetailStatus.FAIL_PUB);
            detailList.forEach(o -> o.setPublishStatusCd(PublishDetailStatus.FAIL_PUB));
            //change status to end publish
            detailService.saveAll(detailList);
            //change ext pin to AVAILABLE STATUS
            log.info("Change ext pin to AVAILABLE status");
            extPins.forEach(o -> o.setStatus(ExtPinStatus.AVAILABLE));
            extPinService.updateExtPins(extPins);

            if (Objects.nonNull(createVoucherResponse)
                    && Objects.nonNull(createVoucherResponse.getData())) {
                log.error("Revert voucher={}", createVoucherResponse.getData());
                //revert voucher
                beServiceClient.revertVoucher(createVoucherResponse.getData());
            }
            throw e;
        } finally {
            // mark pin as processed
            lockingService.removePrcDonePins(publishDTO.getGood().getId(), extPins.stream().map(ExtPin::getId).collect(Collectors.toList()));
        }
    }

    private @NotNull List<PublishDetailDTO> createPublishDetails(PublishDTO publishDTO, PublishRequest publishReq) {
        return publishReq.getUsers().stream().map(user -> PublishDetailDTO.builder()
                .publishStatusCd(PublishDetailStatus.STRT_PUB)
                .publishId(publishDTO.getId())
                .receiverMobileNo(user.getUserMobileNum())
                .userId(user.getId())
                .smsType(publishDTO.getSmsType())
                .user(user)
                .build()).collect(Collectors.toList());
    }

    private List<ExtPin> setExternalPinForPublish(PublishDTO publishDTO,
                                                  List<PublishDetailDTO> publishDetails, GoodsDTO goods) {
        List<ExtPin> extPins = new ArrayList<>();

        log.info("Set ext pin for publish: {}", publishDTO);
        if (extServiceFactory.isSupportType(publishDTO.getGood().getSystem())) {
            Date bookingDate = new Date();
            // Get ext pins
            if (publishDTO.getBookingYn().equalsIgnoreCase(EnumYN.Y.name())) {
                bookingDate = publishDTO.getBookingDate();
                log.info("Booking type -> get {} reserved pins, booking date={}", publishDetails.size(), bookingDate);
                extPins = extServiceFactory.getServiceByType(publishDTO.getGood().getSystem())
                        .getReservedPin(publishDetails.size(), goods, bookingDate);
            } else {
                log.info("None booking type -> get {} available pins", publishDetails.size());
                extPins = extServiceFactory.getServiceByType(publishDTO.getGood().getSystem())
                        .getAvailablePin(publishDetails.size(), goods, bookingDate);
            }

            AtomicInteger count = new AtomicInteger(0);
            // set pin to publish detail
            extPins.forEach(o -> publishDetails.get(count.getAndIncrement()).setExtPinId(o.getId()));

        }
        return extPins;
    }

    private void setGoodChoice(List<VoucherRequest> vouchers, Long parentId) {
        List<GoodsRequest> childGoods = goodsService.findAllGoodByParentId(parentId);
        vouchers.forEach(o -> o.getGoods().setChoices(childGoods));
    }

    //    @Transactional
    public void handoverVoucher(PublishDetailDTO detail, HandOverQueueMessage newUserInfo) {
        log.info("Handover voucher with publish detail={}, new user info={}", detail.getPublishDtlId(), Utils.toJson(newUserInfo));
        VoucherHandoverBERequest beRequest = new VoucherHandoverBERequest(
                newUserInfo.getOldEv(),
                detail.getPublishDtlId(),
                newUserInfo.getNewUser(),
                newUserInfo.getMessage()
        );
        log.info("Call BE to create handover voucher={}", Utils.toJson(beRequest));
        HandoverResponse response = beServiceClient.createNewVoucherForHandOver(beRequest);
        log.info("Response from BE: {}", Utils.toJson(response));

        if (Objects.isNull(response) || Objects.isNull(response.getData()) || response.getData().isBlank()) {
            String message = "Error while requesting create handover voucher to BE. No voucher is created";
            log.error(message);
            throw new ServerRuntimeException(message);
        }

        try {
            PublishDTO publishDTO = publishService.findById(detail.getPublishId());
            PublishQueueRequest publishRequest = publishService.findRequestById(detail.getPublishId());
            List<VoucherRequest> voucherRequests = voucherService.getVoucherRequestByVoucherId(List.of(response.getData()));
            List<PublishDetailQueueRequest> detailRequest = createPublishDetailRequestToQueue(voucherRequests, publishDTO);
            if (StringUtils.isNotBlank(publishRequest.getCustomerId())) {
                publishRequest.setCustomer(customerService.findRequestById(publishRequest.getCustomerId()));
            }
            publishRequest.setTransferMessage(newUserInfo.getMessage());
            publishRequest.setType(GenerateMessageType.TRANSFER);
            publishRequest.setPublishDetails(detailRequest);

            MessageTemplate messageTemplate = messageTemplateRepository.findById(transferTemplateId)
                    .orElseThrow(
                            () ->
                                    new IllegalArgumentException("Message template for Transfer voucher not found"));
            publishRequest.getCampaign().setMessageTemplate(MessageTemplateService.toRequest(messageTemplate));

            detail.setPublishStatusCd(PublishDetailStatus.END_PUB);
            //change status to end publish
            detailService.save(detail);

            log.info("Send handover request to queue");
            rabbitMQProducer.publishEvoucher(publishRequest);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            //revert new voucher
            log.info("Revert voucher: {}", response.getData());
            beServiceClient.revertVoucher(List.of(response.getData()));
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }

    private List<PublishDetailQueueRequest> createPublishDetailRequestToQueue(List<VoucherRequest> voucherRequests,
                                                                              PublishDTO publishDTO) {
        log.info("Create publish details by publishId={}, voucherIds={}",
                publishDTO.getId(), Utils.toJson(voucherRequests.stream().map(VoucherRequest::getId).collect(Collectors.toList())));
        HashMap<String, Long> detailVoucherIdMap = new HashMap<>();
        HashMap<String, VoucherRequest> voucherMap = new HashMap<>();
        // find all brand
        List<String> brandIds = new ArrayList<>();
        voucherRequests.forEach(o -> {
            voucherMap.put(o.getId(), o);
            getBrandIds(o, brandIds);
            detailVoucherIdMap.put(o.getId(), o.getPublishDetailId());
        });
        setBrandForVoucher(voucherRequests, brandIds);
        //create publish detail
        List<PublishDetailQueueRequest> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : detailVoucherIdMap.entrySet()) {
            VoucherRequest voucherRequest = voucherMap.getOrDefault(entry.getKey(), new VoucherRequest());
            result.add(new PublishDetailQueueRequest(entry.getValue(), publishDTO.getId(), null, publishDTO.getSmsType(), voucherRequest));
        }
        return result;
    }

    private void setBrandForVoucher(List<VoucherRequest> voucherRequests, List<String> brandIds) {

        //find all brand
        if (!brandIds.isEmpty()) {
            List<BrandRequest> brandRequests = brandService.findRequestByIds(brandIds);
            HashMap<String, BrandRequest> brandMap = new HashMap<>();
            brandRequests.forEach(o -> brandMap.put(o.getId(), o));
            // set brand for goods
            voucherRequests.forEach(o -> {
                o.getGoods().setBrand(brandMap.getOrDefault(o.getGoods().getBrandId(), null));
                if (o.getGoods().getChoices() != null && !o.getGoods().getChoices().isEmpty()) {
                    o.getGoods().getChoices().forEach(choice -> {
                        if (choice.getBrandId() != null && !choice.getBrandId().isBlank()) {
                            choice.setBrand(brandMap.getOrDefault(choice.getBrandId(), null));
                        }
                    });
                }
            });
        }
    }

    private VoucherResponse createVouchers(PublishDTO publishDTO, List<PublishDetailDTO> detailList) throws ServerRuntimeException {
        try {
            log.info("Create voucher publish={}, detail={}", Utils.toJson(publishDTO.getId()), Utils.toJson(detailList));
            List<PublishDetailRequest> detailRequests = new ArrayList<>();
            detailList.forEach(o -> detailRequests.add(new PublishDetailRequest(o.getPublishDtlId(), o.getReceiverMobileNo(), o.getUserId())));
            EvoucherRequest request = makeEvoucherRequest(publishDTO, detailRequests);
            // call to service be to create e voucher
            log.info("Call BE to create vouchers for publishId={}, body={}", publishDTO.getId(), Utils.toJson(request));
            var response = beServiceClient.createEvouchers(request);
            log.info("Response from BE={}", Utils.toJson(response));
            if (Constants.SUCCESS_CODE != response.getCode()) {
                log.error("Error while requesting create voucher to BE, code={}, msg={}", response.getCode(), response.getMessage());
                throw new ServerRuntimeException(String.format("Service BE returns fail code=%d", response.getCode()));
            }
            return response;
        } catch (Exception e) {
            log.error("Error while requesting create vouchers to BE for publishId={}", publishDTO.getId());
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }

    private @NotNull EvoucherRequest makeEvoucherRequest(PublishDTO publishDTO, List<PublishDetailRequest> detailRequests) {
        EvoucherRequest request = new EvoucherRequest();
        request.setPublishId(publishDTO.getId());
        request.setPublishDetails(detailRequests);
        request.setCampaignId(publishDTO.getCampaign().getId());
        request.setGoodsId(publishDTO.getGood().getId());
        request.setContractId(voucherService.getContractIdByPublishId(publishDTO.getId()));
        return request;
    }

    public void restoreReservedPins(PublishDTO publish, CancelRequest request) throws ServerRuntimeException {
        log.info("Restore reserved pins publish={}, cancelRequest={}", publish.getId(), Utils.toJson(request));
        try {
            List<ExtPin> extPins = new ArrayList<>();
            Integer countPublishDetails = request.getNumbersCount();
            if (publish.getGood().getSystem() == SystemType.EXTERNAL) {
                log.info("External product, need to restore reserved pin, productId={}", publish.getGood().getId());
                int countReservedPins = extPinService.countAllExtPinByGoodsIdAndStatus(publish.getGood().getId(),
                        publish.getBookingDate(), ExtPinStatus.RESERVED).intValue();

                if (countReservedPins < countPublishDetails) {
                    log.warn("Number of reserved pins ({}) is smaller than number of publish details({})", countReservedPins, countPublishDetails);
                    countPublishDetails = countReservedPins;
                }
                log.info("Get {} reserved pins to restore to AVAILABLE", countPublishDetails);
                extPins = extPinService.getOldestExtPinByNumberAndGoodsIdAndStatus(publish.getGood().getId(),
                        publish.getBookingDate(), ExtPinStatus.RESERVED, countPublishDetails);

            }
            extPins.forEach(o -> o.setStatus(ExtPinStatus.AVAILABLE));
            extPinService.updateExtPins(extPins);
            ;
            log.info("Restore reserved pins successfully. Ext pin no={}",
                    Utils.toJson(extPins.stream().map(ExtPin::getExtPinNo).collect(Collectors.toList())));
        } catch (Exception e) {
            log.error("Error when restore reserved pins with publish id={}", publish.getId());
            log.error(e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }

    private void getBrandIds(VoucherRequest voucherRequest, List<String> brandIds) {
        if (voucherRequest.getGoods() != null) {
            if (voucherRequest.getGoods().getBrandId() != null && !voucherRequest.getGoods().getBrandId().isBlank()) {
                brandIds.add(voucherRequest.getGoods().getBrandId());
            }
            if (voucherRequest.getGoods().getChoices() != null && !voucherRequest.getGoods().getChoices().isEmpty()) {
                voucherRequest.getGoods().getChoices().forEach(choice -> {
                    if (choice.getBrandId() != null && !choice.getBrandId().isBlank()) {
                        brandIds.add(choice.getBrandId());
                    }
                });
            }
        }
    }

    protected BaseResponse createChoiceItem(Long publishId, ChoiceChosenRequest uiRequest) throws ServerRuntimeException {
        log.info("Create CHOICE/BULK items for publish={}, request={}", publishId, Utils.toJson(uiRequest));
        try {
            CreateChoiceItemRequest createRequest = VOUCHER_MAPPER.toCreateChoiceRequest(uiRequest);

            createRequest.setPublishId(publishId);
            VoucherResponse response = callAPICreateChildOfChoiceVoucher(createRequest);
            log.info("Response from BE={}", Utils.toJson(response));

            if (Constants.SUCCESS_CODE != response.getCode()
                    || Objects.isNull(response.getData()) || response.getData().isEmpty()) {
                log.error("Error while requesting BE to create CHOICE/BULK items.");
                return response;
            }

            List<VoucherRequest> voucherRequests = voucherService.getVoucherRequestByVoucherId(response.getData());
            log.info("Create CHOICE/BULK items successfully");

            List<String> brandIds = new ArrayList<>();
            voucherRequests.forEach(o -> getBrandIds(o, brandIds));

            setBrandForVoucher(voucherRequests, brandIds);

            return new DataResponse(voucherRequests);

        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    "Error while creating CHOICE/BULK voucher items",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected BaseResponse createChoiceItemV2(Long publishId, PurchaseChildRequest uiRequest) throws ServerRuntimeException {
        log.info("V2 Create CHOICE/BULK items for publish={}, request={}", publishId, Utils.toJson(uiRequest));
        try {
            VoucherResponse response = callAPICreateChildOfChoiceVoucher(uiRequest, publishId);
            log.info("Response from BE={}", Utils.toJson(response));

            if (Constants.SUCCESS_CODE != response.getCode()
                    || Objects.isNull(response.getData()) || response.getData().isEmpty()) {
                log.error("Error while requesting BE to create CHOICE/BULK items.");
                return response;
            }

            List<VoucherRequest> voucherRequests = voucherService.getVoucherRequestByVoucherId(response.getData());
            log.info("Create CHOICE/BULK items successfully");

            List<String> brandIds = new ArrayList<>();
            voucherRequests.forEach(o -> getBrandIds(o, brandIds));

            setBrandForVoucher(voucherRequests, brandIds);

            return new DataResponse(voucherRequests);
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error create choice item: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    "Create child of choice voucher error",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    private VoucherResponse callAPICreateChildOfChoiceVoucher(CreateChoiceItemRequest request) {
        try {
            log.info("Call BE to create CHOICE/BULK child items={}", Utils.toJson(request));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CreateChoiceItemRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<VoucherResponse> response =
                    restTemplate.exchange(
                            evoucherServiceBeUrl + "/create/choice-voucher",
                            HttpMethod.POST,
                            entity,
                            VoucherResponse.class);
            log.info("Response from BE={}", Utils.toJson(response));
            return response.getBody();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            log.error(e.getMessage(), e);
            String errorMessage = e.getMessage();
            int errorCode = 0;
            try {
                GenericError error = Utils.gson.fromJson(e.getResponseBodyAsString(), GenericError.class);
                errorMessage = error.getMessage();
                if (error.getListMessage() != null) {
                    errorMessage = errorMessage.concat(":").concat(Utils.toJson(error.getListMessage()));
                }
                errorCode = error.getCode();
            } catch (Exception ex) {
                // Ignore and do not need to be processed
            }
            throw new CustomCodeException(
                    errorMessage,
                    errorCode,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    e.getMessage(),
                    Constants.ERROR_CODE.INTERNAL_ERROR_CODE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private VoucherResponse callAPICreateChildOfChoiceVoucher(PurchaseChildRequest uiRequest, Long publishId) {
        try {
            var request = VOUCHER_MAPPER.toCreateChoiceRequest(uiRequest);

            request.setPublishId(publishId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(request, headers);

            log.info("Call BE to create CHOICE/BULK child items={}", Utils.toJson(request));
            ResponseEntity<VoucherResponse> response =
                    restTemplate.exchange(
                            evoucherServiceBeUrl + "/create/choice-voucher/v2",
                            HttpMethod.POST,
                            entity,
                            VoucherResponse.class);
            log.info("Response from BE={}", Utils.toJson(response));
            return response.getBody();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            log.error(e.getMessage(), e);
            String errorMessage = e.getMessage();
            int errorCode = 0;
            try {
                GenericError error = Utils.gson.fromJson(e.getResponseBodyAsString(), GenericError.class);
                errorMessage = error.getMessage();
                if (error.getListMessage() != null) {
                    errorMessage = errorMessage.concat(":").concat(Utils.toJson(error.getListMessage()));
                }
                errorCode = error.getCode();
            } catch (Exception ex) {
                // Ignore and do not need to be processed
            }
            throw new CustomCodeException(
                    errorMessage,
                    errorCode,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    e.getMessage(),
                    Constants.ERROR_CODE.INTERNAL_ERROR_CODE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
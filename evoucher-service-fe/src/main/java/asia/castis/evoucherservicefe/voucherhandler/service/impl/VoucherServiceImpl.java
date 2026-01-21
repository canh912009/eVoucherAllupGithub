package asia.castis.evoucherservicefe.voucherhandler.service.impl;

import asia.castis.evoucherservicefe.common.client.PublishServiceClient;
import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.Voucher;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.PublishMessage;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.RequestToPushAgent;
import asia.castis.evoucherservicefe.common.dto.response.ResponseData;
import asia.castis.evoucherservicefe.common.enums.EnumMessageType;
import asia.castis.evoucherservicefe.common.enums.EnumVoucherStatus;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.common.model.publish.PublishModel;
import asia.castis.evoucherservicefe.common.service.BeConnector;
import asia.castis.evoucherservicefe.common.service.EsPublishService;
import asia.castis.evoucherservicefe.common.service.EsVoucherService;
import asia.castis.evoucherservicefe.common.utils.*;
import asia.castis.evoucherservicefe.exceptions.*;
import asia.castis.evoucherservicefe.publishrequest.dto.ChosenRequest;
import asia.castis.evoucherservicefe.publishrequest.dto.PublishResponse;
import asia.castis.evoucherservicefe.publishrequest.sender.PublishRequestSender;
import asia.castis.evoucherservicefe.voucherhandler.dto.ActivateRequest;
import asia.castis.evoucherservicefe.voucherhandler.dto.UsingVoucherRequest;
import asia.castis.evoucherservicefe.voucherhandler.sender.VoucherHandlerSender;
import asia.castis.evoucherservicefe.voucherhandler.service.VoucherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static asia.castis.evoucherservicefe.common.utils.Const.gson;
import static asia.castis.evoucherservicefe.publishrequest.service.impl.PublishRequestServiceImpl.getExceptStoreList;

@RequiredArgsConstructor
@Service
@Slf4j
public class VoucherServiceImpl implements VoucherService {
    public static final int BE_SUCCESS_CODE = 0;
    private final EsVoucherService esVoucherService;
    private final PublishServiceClient client;
    private final ModelMapper modelMapper;

    private final RestTemplate restTemplate;
    private final EsPublishService esPublishService;
    private final VoucherHandlerSender voucherHandlerSender;
    private final PublishRequestSender publishRequestSender;
    private final CryptoUtils cryptoUtils;
    private final BeConnector beConnector;
    @Value("${castis.component.publish-service.url}")
    private String publishServiceUrl;
    @Value("${push.zalo.templateId}")
    public String templateId;
    @Value("${push.brandName}")
    public String pushBrandName;

    @Override
    public @NotNull VoucherModel parseVoucher(Voucher voucherFromRequest, Long publishDetailId, EnumMessageType messageType) {
        VoucherModel voucherModel = modelMapper.map(voucherFromRequest, VoucherModel.class);
        voucherModel.setCreateDate(DateUtils.toDateTime(voucherFromRequest.getCreateDate()));
        voucherModel.setExpireDate(DateUtils.toDateTime(voucherFromRequest.getExpireDate()));
        voucherModel.setPublishDate(DateUtils.toDateTime(voucherFromRequest.getPublishDate()));
        voucherModel.setLastExchangeDate(DateUtils.toDateTime(voucherFromRequest.getLastExchangeDate()));
        voucherModel.setDisuseDate(DateUtils.toDateTime(voucherFromRequest.getDisuseDate()));
        voucherModel.setCancelDate(DateUtils.toDateTime(voucherFromRequest.getCancelDate()));
        voucherModel.setTransferDate(DateUtils.toDateTime(voucherFromRequest.getTransferDate()));
        // Set Goods date
        voucherModel.getGoods().setStartDate(DateUtils.toDateTime(voucherFromRequest.getGoods().getStartDate()));
        voucherModel.getGoods().setEndDate(DateUtils.toDateTime(voucherFromRequest.getGoods().getEndDate()));
        voucherModel.getGoods().setPeriodExpireDate(DateUtils.toDateTime(voucherFromRequest.getGoods().getPeriodExpireDate()));
        voucherModel.getGoods().setExceptStoreIds(getExceptStoreList(voucherFromRequest));
        voucherModel.setChoiceVoucherEv(voucherFromRequest.getParentVoucherEv());
        voucherModel.setChoiceToken(voucherFromRequest.getParentVoucherToken());

        voucherModel.setPublishDetailId(publishDetailId);// Save to ES
        voucherModel.setSmsType(messageType);
        return voucherModel;
    }


    @Override
    public ResponseData<List<String>> chooseChoiceItem(ChosenRequest choiceRequest) {
        try {
            log.info("create choice item {}", choiceRequest);
            VoucherModel parentVoucher = esVoucherService.findById(choiceRequest.getParentVoucherId());
            if (!choiceRequest.getToken().equals(parentVoucher.getChoiceToken())) {
                log.error("invalid token");
                return new ResponseData<>(ErrorCode.INVALID_TOKEN, "invalid token");
            }
            PublishResponse<List<Voucher>> publishResponse =
                    callAPICreateChildOfChoiceVoucher(choiceRequest);

            if (!publishResponse.getErrorCode().equals("0") || publishResponse.getData() == null || publishResponse.getData().isEmpty()) {
                log.error("publish service return error: {}", publishResponse);
                ResponseData<List<String>> response = new ResponseData<>();
                response.setCode(Integer.parseInt(publishResponse.getErrorCode()));
                response.setMessage(publishResponse.getMessage());
                return response;
            }
            List<VoucherModel> voucherModels = new ArrayList<>();
            for (Voucher voucher : publishResponse.getData()) {
                if (Objects.nonNull(voucher)) {
                    String voucherType = voucher.getVoucherType();
                    EnumMessageType enumMessageType = Objects.isNull(voucherType) || voucherType.isEmpty()
                            ? EnumMessageType.SMS : EnumMessageType.getEnum(voucherType);
                    VoucherModel voucherModel = parseVoucher(voucher, voucher.getPublishDetailId(), enumMessageType);
                    voucherModels.add(voucherModel);
                }
            }
            log.info("save all vouchers");
            esVoucherService.saveAll(voucherModels);

            log.info("create voucher item successfully..");
            return ResponseData.ok(voucherModels.stream().map(VoucherModel::getId).collect(Collectors.toList()));

        } catch (ServerException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (
                NotFoundException e) {
            log.error("can not find voucher with id: {}", choiceRequest.getParentVoucherId());
            throw new ServerException(ErrorCode.VOUCHER_NOT_FOUND, "voucher not found");
        }
    }

    @Override
    public void activateVoucher(ActivateRequest activateReq) throws Exception {
        log.info("Start activating voucher: {}", activateReq);
        VoucherModel voucherModel = esVoucherService.findBySerialNo(activateReq.getSerialNumber());
        if (Objects.isNull(voucherModel)) {
            throw new ClientRequestException(ErrorCode.VOUCHER_NOT_FOUND_BY_SERIAL_NO,
                    "Can not find voucher by serial number=" + activateReq.getSerialNumber());
        }

        validateFrontEnd(voucherModel);

        String encryptedUserName = cryptoUtils.encrypt(activateReq.getUserName());
        String encryptedPhoneNumber = cryptoUtils.encrypt(activateReq.getPhoneNumber());

        // Associate voucher with user's info
        voucherModel.setUserName(encryptedUserName);
        voucherModel.setUserMobileNumber(encryptedPhoneNumber);
        esVoucherService.save(voucherModel);

        activateReq.setEv(voucherModel.getId());
        sendActivateRequest(activateReq);

        // Send request SMS to push-agent via queue
        RequestToPushAgent requestToPushAgent = makeOutGoingPublish(voucherModel);
        // DOWNLOAD Type send SMS
        requestToPushAgent.setSmsType(EnumMessageType.SMS.getValue());
        sendOutgoingPublish(requestToPushAgent);
        log.info("Finish activating voucher, ev={},serialNo={}",
                voucherModel.getId(), voucherModel.getSerialNo());
    }

    private void validateFrontEnd(VoucherModel voucherModel) {
        if (EnumVoucherStatus.NORMAL != voucherModel.getVoucherStatus()) {
            throw new ClientRequestException(ErrorCode.VOUCHER_ALREADY_ACTIVATED, "Voucher has invalid status " + voucherModel.getVoucherStatus());
        }

        if (voucherIsActivated(voucherModel.getUserMobileNumber())) {
            throw new ClientRequestException(ErrorCode.VOUCHER_ALREADY_ACTIVATED, "Voucher is already activated");
        }
    }

    private void sendActivateRequest(ActivateRequest activateReq) {
        log.info("Start sending activate request={}", activateReq.getEv());
        ResponseData<?> responseData = beConnector.activate(activateReq);

        if (responseData.getCode() != BE_SUCCESS_CODE) {
            throw new ApplicationException(responseData.getMessage(), responseData.getCode());
        }
        log.info("Finish sending activate request");
    }

    private void sendOutgoingPublish(RequestToPushAgent publish) throws SendMessageToQueueException {
        log.info("Start sending outgoing publish={}", publish.getPublishId());
        publishRequestSender.sendRequestToPushAgent(publish);
        log.info("Finish sending outgoing publish");
    }

    private RequestToPushAgent makeOutGoingPublish(VoucherModel voucherModel) throws NotFoundException, JsonProcessingException {
        PublishModel publishModel = esPublishService.findByPublishDetailId(voucherModel.getPublishDetailId());
        if (Objects.isNull(publishModel)) {
            throw new NotFoundException(String.format("Can not find publish by publishDetailId=%d", voucherModel.getPublishDetailId()));
        }
        int sendOnlyOneMsg = 1;
        if (Objects.isNull(voucherModel.getOutgoingRequest()) || voucherModel.getOutgoingRequest().isEmpty()) {
            throw new NotFoundException(String.format("Can not get message body by voucher=%s", voucherModel.getId()));
        }
        PublishMessage publishMessage = (new JsonMapper()).readValue(voucherModel.getOutgoingRequest(), PublishMessage.class);
        publishMessage.setReceiverMobileNumber(voucherModel.getUserMobileNumber());

        return RequestToPushAgent.builder()
                .publishId(publishModel.getId())
                .campaignId(publishModel.getCampaign().getId())
                .smsType(voucherModel.getSmsType().getValue())
                .brandName(pushBrandName)
                .templateId(templateId)
                .totalCount(sendOnlyOneMsg)
                .publishMessageList(List.of(publishMessage))
                .build();
    }

    private PublishResponse<List<Voucher>> callAPICreateChildOfChoiceVoucher(
            ChosenRequest request) {
        try {
            log.info("Call Publish service api create child of choice voucher");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(request, headers);

            log.info("Call Publish service with payload: {}", request.toString());
            ResponseEntity<PublishResponse<List<Voucher>>> response =
                    restTemplate.exchange(
                            publishServiceUrl + "/voucher/choose-choice",
                            HttpMethod.POST,
                            entity,
                            new ParameterizedTypeReference<>() {
                            });
            log.info("Publish service response: {}", response);

            return response.getBody();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            log.error(String.format("Error response Publish service create child of choice voucher: %s", e.getMessage()), e);
            throw gson.fromJson(e.getResponseBodyAsString(), ServerException.class);
        } catch (Exception e) {
            log.error("Error call Publish service create child of choice voucher: {}", e.getMessage(), e);
            throw new ServerException(
                    ErrorCode.CAN_NOT_CREATE_CHOICE_VOUCHER,
                    "Call api Publish service create child of choice voucher error");
        }
    }

    private boolean voucherIsActivated(String phoneNumber) {
        return Objects.nonNull(phoneNumber) && !phoneNumber.isEmpty();
    }

    @Override
    public void saveVouchers(List<VoucherModel> vouchers) {
        esVoucherService.saveAll(vouchers);
        log.info("Finish saving vouchers count={}", vouchers.size());
    }

    @Override
    public VoucherModel findById(String ev) throws NotFoundException {
        return esVoucherService.findById(ev);
    }

    @Override
    public void saveOne(VoucherModel voucherModel) {
        esVoucherService.save(voucherModel);
        log.info("Finish saving voucher ev={}", voucherModel.getId());
    }

    @Override
    public void delete(Set<String> tobeDeleteVouchers) {
        log.info("Start deleting vouchers [{}]", String.join(",", tobeDeleteVouchers));
        esVoucherService.delete(tobeDeleteVouchers);
        log.info("Finish deleting vouchers count={}", tobeDeleteVouchers.size());
    }
}

package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.*;
import com.evoucher.adminapi.admin.dao.models.*;
import com.evoucher.adminapi.admin.enums.PublishStatusCode;
import com.evoucher.adminapi.admin.enums.SMSType;
import com.evoucher.adminapi.admin.enums.UploadDataType;
import com.evoucher.adminapi.admin.mapper.CampaignMapper;
import com.evoucher.adminapi.admin.mapper.PublishMapper;
import com.evoucher.adminapi.admin.service.models.*;
import com.evoucher.adminapi.admin.service.publish.impl.PublishHandlingStrategy;
import com.evoucher.adminapi.auth.service.AdminService;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.*;
import com.evoucher.adminapi.cms.dao.models.*;
import com.evoucher.adminapi.cms.mapper.GoodsMapper;
import com.evoucher.adminapi.cms.service.GoodsService;
import com.evoucher.adminapi.cms.service.models.BrandDTO;
import com.evoucher.adminapi.cms.service.models.GoodsDTO;
import com.evoucher.adminapi.cms.service.models.SupplierDTO;
import com.evoucher.adminapi.cms.service.models.request.EndUserRequest;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.config.PublishHandlingStrategyFactory;
import com.evoucher.adminapi.common.enums.*;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.utils.MessageUtils;
import com.evoucher.adminapi.good.service.GoodServiceFactory;
import com.evoucher.adminapi.partner.dao.ExternalPublishRepository;
import com.evoucher.adminapi.partner.dao.model.ExternalPublish;
import com.evoucher.adminapi.partner.service.model.request.ExternalPublishConvert;
import com.evoucher.adminapi.partner.service.model.request.OrderPinRequest;
import com.evoucher.adminapi.partner.service.model.response.ExternalPublishOrderPinResponse;
import com.evoucher.adminapi.partner.service.model.response.ExternalPublishResponse;
import com.evoucher.adminapi.partner.service.model.response.OrderPinResponse;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublishServiceImpl implements PublishService {

    protected static final String EVOUCHER_PUBLISH_NOT_FOUND = "evoucher.publish.not.found";
    public static final String EVOUCHER_PUBLISH_APPROVE_STATUS_CODE_NOT_VALID = "evoucher.publish.approve.status.code.not.valid";
    public static final int MAX_DOWNLOAD_VOUCHERS_ALLOWED = 1000;
    public static final String FIXED_MOBILE_NUMBER = "0000000000";
    private final PublishRepository publishRepository;
    private final CampaignRepository campaignRepository;
    private final CampaignGoodsRepository campaignGoodsRepository;
    private final PublishApproveHistoryRepository publishApproveHistoryRepository;
    private final EndUserRepository endUserRepository;
    private final CustomerContractRepository customerContractRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    private final GoodsRepository goodsRepository;
    private final PublishDetailRepository publishDetailRepository;
    private final ExternalPinRepository externalPinRepository;
    private final EVoucherRepository eVoucherRepository;
    private final ExternalPublishRepository externalPublishRepository;
    private final BrandRepository brandRepository;
    private final RestTemplate restTemplate;

    private final PublishMapper publishMapper;
    private final CampaignMapper campaignMapper;
    private final GoodsMapper goodsMapper;
    private final GoodsService goodsService;
    private final AdminService adminService;
    private final PublishHandlingStrategyFactory publishFactory;

    @Value("${servers.publishServer}")
    private String publishServiceUrl;

    @Override
    public PublishDTO findById(Integer id) {
        log.info("Find publish with id: {}", id);
        Publish publish = publishRepository.findById(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_PUBLISH_NOT_FOUND), HttpStatus.BAD_REQUEST));
        log.info("Validate permission with publishId: {}", id);
        adminService.validateCorpIdPermission(publish.getCustomerId());

        log.info("Find campaign with campaignId: {}", publish.getCampaignId());
        CampaignDTO campaign = campaignRepository.findById(publish.getCampaignId())
                .map(campaignMapper::toCampaignDTO)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.not.found"),
                        HttpStatus.BAD_REQUEST));

        log.info("Find list Goods information with campaignId: {}", campaign.getId());
        List<Tuple> tupleList = campaignGoodsRepository.findGoodsSupplerBrandByCampaignIdAndValidYn(campaign.getId(), EnumValidYn.Y);
        List<GoodsDTO> goodsDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tupleList)) {
            goodsDTOS = tupleList.stream().map(tuple -> {
                Goods goods = tuple.get(0, Goods.class);
                Supplier supplier = tuple.get(1, Supplier.class);
                Brand brand = tuple.get(2, Brand.class);
                GoodsDTO goodsDTO = goodsMapper.toDTO(goods);
                goodsDTO.setSupplier(SupplierDTO.builder()
                        .id(supplier.getId())
                        .supplierName(supplier.getSupplierName())
                        .build());
                goodsDTO.setBrand(BrandDTO.builder()
                        .id(brand.getId())
                        .brandName(brand.getBrandName())
                        .build());
                return goodsDTO;
            }).collect(Collectors.toList());
        }
        campaign.setListGoods(goodsDTOS);

        PublishDTO publishDTO = publishMapper.toPublishDTO(publish);
        // Set Publish data
        publishDTO.setCampaign(campaign);

        publishFactory.getStrategy(publish.getSmsType())
                .setTypeSpecificMetaData(publishDTO);
        return publishDTO;
    }


    @Override
    @Transactional
    public PublishDTO createPublish(PublishRequest publishRequest) {

        PublishHandlingStrategy deliveryService =
                publishFactory.getStrategy(publishRequest.getSmsType());
        log.info("Validate PublishRequest");
        validatePublishRequest(publishRequest);

        // generate Publish
        Publish publish = publishMapper.toPublish(publishRequest);

        log.info("validate relevant information and set data publish");
        validateAndSetDataPublish(publishRequest, publish);

        if (Objects.nonNull(publishRequest.getApproveStatusCode())) {
            // case create Publish => approveStatusCode must is REQ => publishStatusCode = WAIT_APPRV
            publish.setPublishStatusInfo(publishRequest.getApproveStatusCode(), null);
        }
        // Handle Publish end users for DOWNLOAD type
        deliveryService.setUserInfoToPublishRequest(publishRequest);

        publish.setUploadText(Constant.gson.toJson(publishRequest.getEndUsers()));

        log.info("Save Publish");
        publish = publishRepository.save(publish);

        // Save publishApproveHistory
        savePublishApproveHistory(publish.getId(), publishRequest.getApproveStatusCode(), null);

        return publishMapper.toPublishDTO(publish);
    }

    /**
     * Generates a list of end user requests based on the specified publish request.
     *
     * @param request the publish request containing the campaign ID and the number of vouchers
     * @return the list of end user requests
     * @throws CustomCodeException if the campaign or customer is not found
     */
    public List<EndUserRequest> generateDownloadTypedEndUsers(PublishRequest request) {
        String customerId = campaignRepository.findById(request.getCampaignId())
                .map(Campaign::getCustomerId)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.not.found"),
                        HttpStatus.BAD_REQUEST));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.customer.not.found"),
                HttpStatus.BAD_REQUEST));
        List<EndUserRequest> endUsers = new ArrayList<>();
        for (int i = 0; i < request.getNumberOfVouchers(); i++) {
            EndUserRequest endUser = EndUserRequest.builder()
                    .userNm(customer.getCustomerName())
                    .userMobileNum(FIXED_MOBILE_NUMBER)
                    .build();
            endUsers.add(endUser);
        }
        return endUsers;
    }

    @Override
    public List<EndUserRequest> generatePaperTypedEndUsers(PublishRequest request) {
        List<EndUserRequest> endUsers = new ArrayList<>();
        for (int i = 0; i < request.getNumberOfVouchers(); i++) {
            EndUserRequest endUser = EndUserRequest.builder().build();
            endUsers.add(endUser);
        }
        return endUsers;
    }

    @Override
    public PublishDTO updatePublish(Integer id, PublishRequest publishRequest) {
        PublishHandlingStrategy deliveryService = publishFactory.getStrategy(publishRequest.getSmsType());
        log.info("update publish: {}", publishRequest);
        validatePublishRequest(publishRequest);

        // check exists
        log.info("Find publish with id: {}", id);
        Publish publishOld = publishRepository.findById(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_PUBLISH_NOT_FOUND),
                        HttpStatus.BAD_REQUEST));

        Publish publishBackUp = publishMapper.copyPublish(publishOld);

        // Check publishStatus can update Publish
        validateStatusPublishForUpdate(publishOld);

        deliveryService.setUserInfoToPublishRequest(publishRequest);

        // generate Publish
        Publish publishNew = publishMapper.toPublish(publishRequest);

        log.info("validate relevant information and set data publish");
        validateAndSetDataPublish(publishRequest, publishNew);

        publishNew.setId(id);
        publishNew.setUploadText(Constant.gson.toJson(publishRequest.getEndUsers()));
        publishNew.setRejectId(publishOld.getRejectId());
        publishNew.setRejectDate(publishOld.getRejectDate());
        publishNew.setRejectReason(publishOld.getRejectReason());

        if (Objects.nonNull(publishRequest.getApproveStatusCode())) {
            // case update Publish => approveStatusCode are REQ/APPRV => set publish status
            if (ApproveStatus.APPRV.equals(publishRequest.getApproveStatusCode())) {
                UserPrincipal userPrincipal = LoggedInUserContext.getLoggedInUser();

                log.info("Validate permission admin account when approve publishId: {}", id);
                validatePermissionAdminUser(id, userPrincipal);

                log.info("Validate before approve publishId: {}", id);
                validatePublishBeforeApprove(publishOld);
            }

            publishNew.setPublishStatusInfo(publishRequest.getApproveStatusCode(), null);
        } else {
            publishNew.setStatusCode(publishOld.getStatusCode());
            publishNew.setApproveStatusCode(publishOld.getApproveStatusCode());
            publishNew.setApproveRequestId(publishOld.getApproveRequestId());
            publishNew.setApproveRequestDate(publishOld.getApproveRequestDate());
        }

        log.info("Update Publish with id: {}", id);
        publishNew = publishRepository.save(publishNew);

        if (ApproveStatus.APPRV.equals(publishRequest.getApproveStatusCode())) {
            log.info("Process publishing delivery voucher for publishId: {}", id);
            publishingDeliveryVoucher(id, publishNew, publishBackUp);
        }

        // Save publishApproveHistory
        savePublishApproveHistory(id, publishRequest.getApproveStatusCode(), null);

        return publishMapper.toPublishDTO(publishNew);
    }

    @Override
    public Integer deletePublishById(Integer id) {
        log.info("Find publish with id: {}", id);
        Publish publish = publishRepository.findById(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_PUBLISH_NOT_FOUND),
                        HttpStatus.BAD_REQUEST
                ));

        // Check publishStatus can update Publish
        validateStatusPublishForUpdate(publish);

        log.info("Delete publish with id: {}", id);
        publishRepository.delete(publish);

        return id;
    }

    @Override
    public Page<SearchPublishResponse> searchPublish(FilterSearchAdmin filterSearchAdmin) {
        int page = ObjectUtils.isEmpty(filterSearchAdmin.getPage()) ? 0 : filterSearchAdmin.getPage() - 1; //pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchAdmin.getPageSize()) ? 10 : filterSearchAdmin.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        List<SearchPublishResponse> publishDTOS = publishRepository.searchPublish(filterSearchAdmin, pageable);
        long publishNumber = 0;
        if (!CollectionUtils.isEmpty(publishDTOS)) {
            publishNumber = publishRepository.countPublish(filterSearchAdmin);
        }

        return new PageImpl<>(publishDTOS, pageable, publishNumber);
    }

    @Override
    public Integer updateStatusPublish(Integer id, ApproveRequest approveRequest) {
        ApproveStatus approveStatus = approveRequest.getApproveStatusCode();

        log.info("Find Publish with id: {}", id);
        Publish publish = publishRepository.findById(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_PUBLISH_NOT_FOUND),
                        HttpStatus.BAD_REQUEST));

        Publish publishBackUp = publishMapper.copyPublish(publish);
        UserPrincipal userPrincipal = LoggedInUserContext.getLoggedInUser();

        switch (approveStatus) {
            case REQ: {
                // Role Admin/Operator only request status is REQ with publish status can update
                validateStatusPublishForUpdate(publish);

                publish.setPublishStatusInfo(approveStatus, null);
                break;
            }
            case CANCEL_REQ: {
                // Role Admin/Operator only request status is CANCEL_REQ with publish status is WAIT_APPRV
                if (!PublishStatusCode.WAIT_APPRV.getValue().equals(publish.getStatusCode())) {
                    throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.approve.status.code.not.valid"),
                            HttpStatus.BAD_REQUEST);
                }
                publish.setPublishStatusInfo(approveStatus, null);
                break;
            }
            case APPRV: {
                validatePermissionAdminUser(id, userPrincipal);

                validatePublishBeforeApprove(publish);

                publish.setPublishStatusInfo(approveStatus, null);
                break;
            }
            case REJCT: {
                validatePermissionAdminUser(id, userPrincipal);
                // Only change status if publishStatus is WAIT_APPRV (Wait approve)
                if (!PublishStatusCode.WAIT_APPRV.getValue().equals(publish.getStatusCode())) {
                    throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.approve.status.code.not.valid"),
                            HttpStatus.BAD_REQUEST);
                }
                publish.setPublishStatusInfo(approveStatus, approveRequest.getRejectReason());
                break;
            }
            case CANCEL_APPRV: {
                validatePermissionAdminUser(id, userPrincipal);
                // Only change status if publishStatus is APPROVED
                if (!PublishStatusCode.APPROVED.getValue().equals(publish.getStatusCode())) {
                    throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.approve.status.code.not.valid"),
                            HttpStatus.BAD_REQUEST);
                }

                // Cancel Pin for publish
                cancelPublishProcess(List.of(publish));

                publish.setPublishStatusInfo(approveStatus, null);
                break;
            }
            default: {
                throw new CustomCodeException(MessageUtils.getMessage(EVOUCHER_PUBLISH_APPROVE_STATUS_CODE_NOT_VALID),
                        HttpStatus.BAD_REQUEST);
            }
        }

        log.info("Save update approve status Publish with id: {}", id);
        publish = publishRepository.save(publish);

        if (ApproveStatus.APPRV.equals(approveStatus)) {
            log.info("Process publishing delivery voucher for publishId: {}", id);
            publishingDeliveryVoucher(id, publish, publishBackUp);
        }
        // Save publishApproveHistory
        savePublishApproveHistory(id, approveStatus, approveRequest.getRejectReason());

        return id;
    }

    @Override
    public ExternalPublishResponse createListPublishForCustomerChannel(
            ExternalPublishConvert externalPublish) {
        String transactionId = externalPublish.getTransactionId();
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        String customerId = user.getAdminCorpId();

        log.info("Find Customer with id: {}", customerId);
        Customer customer = customerRepository
                .findByIdAndApproveStatusCodeAndValidYn(customerId, ApproveStatus.APPRV, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.customer.not.found"),
                        HttpStatus.BAD_REQUEST));

        ExternalPublish externalPublishDB = externalPublishRepository.findById(transactionId)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.channel.external.publish.entity.not.found"),
                        HttpStatus.INTERNAL_SERVER_ERROR));
        externalPublishDB.setCustomerId(customerId);
        log.info("Update customerId: {} for External publish entity", customerId);
        externalPublishRepository.save(externalPublishDB);

        if (!CustomerType.CHANNEL.equals(customer.getCustomerTypeCode())) {
            log.info("Customer does not belong to the channel customer type with customerId: {}", customerId);
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.account.permission"),
                    HttpStatus.FORBIDDEN);
        }

        log.info("Get Campaign for customer: {}", customerId);
        Campaign campaign = campaignRepository.findFirstByCustomerIdOrderByEndDateDesc(customerId)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.channel.campaign.not.found"),
                        HttpStatus.FORBIDDEN));
        // Campaign must be approved
        if (!ApproveStatus.APPRV.equals(campaign.getApproveStatusCode())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.campaign.has.not.been.approved"),
                    HttpStatus.BAD_REQUEST);
        }
        Integer campaignId = campaign.getId();

        log.info("Find Contract with id: {}", campaign.getCustomerContractId());
        CustomerContract customerContract = customerContractRepository
                .findByIdAndValidYn(campaign.getCustomerContractId(), EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.contract.not.found"),
                        HttpStatus.BAD_REQUEST));

        List<OrderPinRequest> orderPinRequests = externalPublish.getOrders();

        boolean isSendSms = externalPublish.getIsSendSms();
        // have send sms and smsSchedule not null => bookingYn = Y
        final EnumValidYn bookingYn = isSendSms && Objects.nonNull(externalPublish.getSmsSchedule())
                ? EnumValidYn.Y : EnumValidYn.N;

        List<Publish> listPublish = orderPinRequests.stream()
                .map(
                        order -> {
                            Integer goodsId = order.getGoodsId();
                            EndUserRequest endUserRequest = EndUserRequest.builder()
                                    .userMobileNum(order.getUserPhoneNo())
                                    .userNm(order.getUserName())
                                    .build();
                            List<EndUserRequest> endUserRequests = List.of(endUserRequest);
                            log.info("Check goods belong campaign with campaignId: {}, and goodsId: {}", campaignId, goodsId);
                            boolean containGoods  = campaignGoodsRepository
                                    .existsByCampaignIdAndGoodsIdAndValidYn(campaignId, goodsId, EnumValidYn.Y);
                            if (!containGoods) {
                                throw new CustomCodeException(
                                        MessageUtils.getMessage("evoucher.campaign.goods.id.not.belong.campaign"),
                                        HttpStatus.BAD_REQUEST);
                            }

                            log.info("Find Goods with id: {}", goodsId);
                            Goods goods = goodsRepository.findByIdAndValidYn(goodsId, EnumValidYn.Y)
                                    .orElseThrow(() -> new CustomCodeException(
                                            MessageUtils.getMessage("evoucher.goods.not.found"),
                                            HttpStatus.BAD_REQUEST));

                            return Publish.builder()
                                    .campaignId(campaign.getId())
                                    .goods(goods)
                                    .publishName("Publish External")
                                    .messageSubject(externalPublish.getSubject())
                                    .messageContent(externalPublish.getContentText())
                                    .bookingYn(bookingYn)
                                    .bookingDate(EnumValidYn.Y.equals(bookingYn)
                                            ? externalPublish.getSmsSchedule()
                                            : null)
                                    .receiverNoDuplicateAllowYn(EnumValidYn.N)
                                    .uploadType(UploadDataType.TEXT)
                                    .uploadText(Constant.gson.toJson(endUserRequests))
                                    .smsType(isSendSms ? SMSType.SMS : null)
                                    .supplierId(goods.getSupplierId())
                                    .customerId(customerId)
                                    .sellPrice(goods.getSellPrice())
                                    .sellListPrice(goods.getListPrice())
                                    .sellSettlementMethodCode(customerContract.getSellSettlementMethodCode())
                                    .sellDiscountAmount(customerContract.getSellDiscountAmount())
                                    .sellCommissionRate(customerContract.getSellCommissionRate())
                                    .sellVatIncludeYn(customerContract.getSellVatIncludeYn())
                                    .transactionId(transactionId)
                                    .statusCode(PublishStatusCode.APPROVED.getValue())
                                    .approveStatusCode(ApproveStatus.APPRV)
                                    .approveId("system")
                                    .approveDate(new Date())
                                    .contentLink(campaign.getContentLink())
                                    .contentImagePath(campaign.getContentImagePath())
                                    .contentImageName(campaign.getContentImageName())
                                    .showPopupYn(EnumValidYn.N)
                                    .build();
                        })
                .collect(Collectors.toList());

        log.info("Save list publish for transactionId: {}", transactionId);
        List<Publish> listPublishDb = publishRepository.saveAll(listPublish);

        listPublishDb.forEach(
                publish -> {
                    // backup publish
                    Publish publishBackUp = publishMapper.copyPublish(publish);

                    log.info("Process publishing delivery voucher for publishId: {}", publish.getId());
                    publishingDeliveryVoucher(publish.getId(), publish, publishBackUp);
                });

        log.info("Save publishApproveHistory for transactionId: {}", transactionId);
        publishApproveHistoryRepository.saveAll(listPublishDb.stream()
                .map(publish -> PublishApproveHistory.builder()
                        .publishId(publish.getId())
                        .approveStatusCode(ApproveStatus.APPRV)
                        .build())
                .collect(Collectors.toList()));

        if (EnumValidYn.N.equals(bookingYn)) {
            return getExternalPublishWithTransactionId(transactionId);
        }

        return ExternalPublishResponse.builder()
                .transactionId(transactionId)
                .orders(listPublishDb.stream()
                        .map(
                                publish -> OrderPinResponse.builder()
                                            .orderId(publish.getId())
                                            .build())
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public ExternalPublishResponse getExternalPublishWithTransactionId(String transactionId) {
        log.info("Get list publish for transactionId: {}", transactionId);
        List<Publish> publishes = publishRepository.findAllByTransactionId(transactionId);

        if (publishes.isEmpty()) {
            return ExternalPublishResponse.builder()
                    .transactionId(transactionId)
                    .build();
        }

        List<OrderPinResponse> orderPins = publishes.stream()
                .map(publish -> getOrderPinResponse(publish))
                .collect(Collectors.toList());

        return ExternalPublishResponse.builder()
                .transactionId(transactionId)
                .orders(orderPins)
                .build();
    }

    @Override
    public ExternalPublishOrderPinResponse getExternalPublishWithOrderId(Integer orderId, String customerId) {
        log.info("Get Publish with publishId: {} and customerId: {}", orderId, customerId);
        Publish publish = publishRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.channel.order.pin.not.found"),
                        HttpStatus.BAD_REQUEST));

        return ExternalPublishOrderPinResponse.builder()
                .transactionId(publish.getTransactionId())
                .order(getOrderPinResponse(publish))
                .build();
    }

    @Override
    public void cancelExternalPublishForCustomerChannel(UUID transactionId) {
        log.info("Get list publish for transactionId: {}", transactionId);
        List<Publish> publishes = publishRepository.findAllByTransactionId(transactionId.toString());
        publishes = publishes.stream()
                .filter(
                        publish -> {
                            if (PublishStatusCode.APPROVED.getValue().equals(publish.getStatusCode())) {
                                // Only change status if publishStatus is APPROVED
                                log.info("Cancel Approve status for publishId: {}", publish.getId());
                                return true;
                            } else {
                                log.info("Publish Id: {} cannot Cancel Approve because status: {}",
                                        publish.getId(), publish.getStatusCode());
                                return false;
                            }
                        })
                .map(
                        publish -> {
                            publish.setPublishStatusInfo(ApproveStatus.CANCEL_APPRV, null);
                            return publish;
                        }
                )
                .collect(Collectors.toList());

        log.info("Cancel Pin for transactionId: {}", transactionId);
        cancelPublishProcess(publishes);

        log.info("Save cancel approve status for transactionId: {}", transactionId);
        publishRepository.saveAll(publishes);

        log.info("Save PublishApproveHistory for all publish with transactionId: {}", transactionId);
        publishApproveHistoryRepository.saveAll(
                publishes.stream()
                        .map(publish -> PublishApproveHistory.builder()
                                .publishId(publish.getId())
                                .approveStatusCode(ApproveStatus.CANCEL_APPRV)
                                .build())
                        .collect(Collectors.toList()));
    }

    @Override
    public void cancelExternalPublishForCustomerChannel(Integer orderId, String customerId) {
        log.info("Get Publish with publishId: {} and customerId: {}", orderId, customerId);
        Publish publish = publishRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.channel.order.pin.not.found"),
                        HttpStatus.BAD_REQUEST));

        if (!PublishStatusCode.APPROVED.getValue().equals(publish.getStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.channel.publish.not.cancel"),
                    HttpStatus.BAD_REQUEST);
        }

        log.info("Cancel Pin for publish: {}", publish.getId());
        cancelPublishProcess(List.of(publish));

        // Only change status if publishStatus is APPROVED
        log.info("Cancel Approve status for publishId: {}", publish.getId());
        publish.setPublishStatusInfo(ApproveStatus.CANCEL_APPRV, null);
        publishRepository.save(publish);

        // Save publishApproveHistory
        savePublishApproveHistory(publish.getId(), ApproveStatus.CANCEL_APPRV, null);
    }

    private void publishingDeliveryVoucherMetaData(Integer id, Publish publish, Publish publishBackUp) {
        try {
            List<EndUser> endUsers = new ArrayList<>();
            Goods goods = publish.getGoods();
            if (SystemType.EXTERNAL.equals(goods.getSystem())) {
                int numberOfVouchers = publish.getNumberOfVouchers();
                Date bookingDate = publish.getBookingYn() == EnumValidYn.Y ? publish.getBookingDate() : null;
                validateAvailableVouchers(goods, numberOfVouchers, bookingDate);
            }

            if (SMSType.DOWNLOAD.equals(publish.getSmsType())) {
                endUsers = getEndUsers(publish);
            }

            // call Publish-service create PublishDetail
            log.info("Start publish process with id: {}", id);
            createPublishProcess(id, publish, endUsers);
        } catch (Exception e) {
            log.info("Rollback Publish with id: {}", id);
            publishRepository.save(publishBackUp);
            throw e;
        }
    }
    private void publishingDeliveryVoucherSendSMS(Integer id, Publish publish, Publish publishBackUp) {
        try {
            List<EndUser> endUsers = getEndUsers(publish);
            // call Publish-service create PublishDetail
            log.info("Start publish process with id: {}", id);
            createPublishProcess(id, publish, endUsers);
        } catch (Exception e) {
            log.info("Rollback Publish with id: {}", id);
            publishRepository.save(publishBackUp);
            throw e;
        }
    }

    private @NotNull List<EndUser> getEndUsers(Publish publish) {
        log.info("Map string to list endUser: {}", publish.getUploadType());
        Type listType = new TypeToken<ArrayList<EndUser>>(){}.getType();
        List<EndUser> endUsers = Constant.gson.fromJson(publish.getUploadText(), listType);

        Goods goods = publish.getGoods();
        if (SystemType.EXTERNAL.equals(goods.getSystem())) {
            int numberOfVouchers= endUsers.size();

            if (EnumValidYn.Y.equals(publish.getReceiverNoDuplicateAllowYn())) {
                List<EndUser> distinctEndUsers = new ArrayList<>(endUsers.stream()
                        .collect(
                                Collectors.toMap(
                                        EndUser::getUserMobileNum,
                                        endUser -> endUser,
                                        (existing, replacement) -> existing))
                        .values());
                numberOfVouchers = distinctEndUsers.size();
            }
            Date bookingDate = publish.getBookingYn() == EnumValidYn.Y ? publish.getBookingDate() : null;
            validateAvailableVouchers(goods, numberOfVouchers, bookingDate);
        }

        log.info("Create list EndUser");
        endUsers = endUserRepository.saveAll(endUsers);
        return endUsers;
    }
    private @NotNull List<EndUserRequest> getPublishUsers(Publish publish) {
        log.info("Map string to list endUser: {}", publish.getUploadType());
        Type listType = new TypeToken<ArrayList<EndUserRequest>>(){}.getType();
        List<EndUserRequest> endUsers = Constant.gson.fromJson(publish.getUploadText(), listType);

        List<EndUserRequest> distinctEndUsers = endUsers;

        if (EnumValidYn.N.equals(publish.getReceiverNoDuplicateAllowYn())) {
            if (publish.getSmsType() == SMSType.EMAIL) {
                 distinctEndUsers = new ArrayList<>(endUsers.stream()
                        .collect(
                                Collectors.toMap(
                                        EndUserRequest::getEmail,
                                        endUser -> endUser,
                                        (existing, replacement) -> existing))
                        .values());
            } else if (!EnumSet.of(SMSType.PAPER, SMSType.DOWNLOAD).contains(publish.getSmsType()))  {
                distinctEndUsers = new ArrayList<>(endUsers.stream()
                        .collect(
                                Collectors.toMap(
                                        EndUserRequest::getUserMobileNum,
                                        endUser -> endUser,
                                        (existing, replacement) -> existing))
                        .values());
            }

        }
        return distinctEndUsers;

    }

    private void publishingDeliveryVoucher(Integer id, Publish publish, Publish publishBackUp) {
        List<EndUserRequest> endUsers = getPublishUsers(publish);
        Goods goods = publish.getGoods();
        if (SystemType.EXTERNAL.equals(goods.getSystem())) {
            int numberOfVouchers = endUsers.size();
            Date bookingDate = publish.getBookingYn() == EnumValidYn.Y ? publish.getBookingDate() : null;
            validateAvailableVouchers(goods, numberOfVouchers, bookingDate);
        }

        publishEmail(publish, endUsers);
    }

    private void validateAvailableVouchers(Goods goods, int numberOfVouchers, Date expireTime) throws CustomCodeException {
        // only validate with good that require pin before create voucher like external
        if (GoodServiceFactory.REQUIRE_PIN_BEFORE_APPROVE.contains(goods.getSystem())) {
            if (expireTime == null) expireTime = new Date();
            log.info("Check Pin with goodsId: {}", goods.getId());
            List<ExternalPin> externalPinsAvailable =
                    externalPinRepository.findByGoodsIdAndStatusAndExpireTimeAfter(goods.getId(), ExternalPinStatus.AVAILABLE, expireTime);

            if (CollectionUtils.isEmpty(externalPinsAvailable) || numberOfVouchers > externalPinsAvailable.size()) {
                throw new CustomCodeException(
                        MessageUtils.getMessage("evoucher.pin.number.require.invalid", externalPinsAvailable.size()),
                        HttpStatus.BAD_REQUEST);
            }
        }

    }

//    private void validatePermission(Publish publish) {
//        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
//        String adminType = user.getAdminType();
//        EnumRole role = Enum.valueOf(EnumRole.class, adminType);
//        String adminCorpId = user.getAdminCorpId();
//
//        switch (role) {
//            case ROLE_ADMIN:
//            case ROLE_OPERATOR:
//                break;
//            case ROLE_CUSTOMER: {
//                if (!adminCorpId.equals(publish.getCustomerId())) {
//                    log.info("AdminCorpId {} does not have permission!", adminCorpId);
//                    throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
//                            HttpStatus.FORBIDDEN);
//                }
//                break;
//            }
//            default:
//                log.info("Account {} does not have permission!", user.getId());
//                throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
//                        HttpStatus.FORBIDDEN);
//        }
//    }

//    private void validateForDownloadAndPaperType(PublishRequest publishRequest) {
//
//        if (Objects.isNull(publishRequest.getNumberOfVouchers()) || publishRequest.getNumberOfVouchers() <= 0) {
//            throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.empty.numberOfVouchers"), HttpStatus.BAD_REQUEST);
//        }
//        if (publishRequest.getNumberOfVouchers().intValue() > MAX_DOWNLOAD_VOUCHERS_ALLOWED) {
//            throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.max.numberOfVouchers", MAX_DOWNLOAD_VOUCHERS_ALLOWED), HttpStatus.BAD_REQUEST);
//        }
//    }

    public void validatePublishRequest(PublishRequest publishRequest) {
        // validate by delivery type
        publishFactory.getStrategy(publishRequest.getSmsType())
                .validatePublishRequest(publishRequest);
        // validate approveStatusCode = REQ/APPRV if exist
        if (Objects.nonNull(publishRequest.getApproveStatusCode())
                && !ApproveStatus.REQ.equals(publishRequest.getApproveStatusCode())
                && !ApproveStatus.APPRV.equals(publishRequest.getApproveStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.approve.status.code.not.valid"), HttpStatus.BAD_REQUEST);
        }
        // validate length of senderName > 18 byte
        if (publishRequest.getSenderName().getBytes(StandardCharsets.UTF_8).length > 18) {
            throw new CustomCodeException(MessageUtils.getMessage(
                    "evoucher.publish.length.of.sender.name.invalid"),
                    HttpStatus.BAD_REQUEST);
        }

    }

//    private void validateForSMSType(PublishRequest publishRequest) {
//        if (Objects.isNull(publishRequest.getReceiverNoDuplicateAllowYn())) {
//            throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.empty.duplicateKey"), HttpStatus.BAD_REQUEST);
//        }
//        if (Objects.isNull(publishRequest.getUploadType())) {
//            throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.empty.uploadType"), HttpStatus.BAD_REQUEST);
//        }
//        if (Objects.isNull(publishRequest.getEndUsers()) || publishRequest.getEndUsers().isEmpty()) {
//            throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.empty.users"), HttpStatus.BAD_REQUEST);
//        }
//        if (EnumValidYn.Y.equals(publishRequest.getBookingYn())) {
//            // BookingDate not null
//            if (Objects.isNull(publishRequest.getBookingDate()))
//                throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.booking.date.empty"), HttpStatus.BAD_REQUEST);
//            // validate booking date must after currentDate
//            Date currentDate = new Date();
//            if (currentDate.after(publishRequest.getBookingDate())) {
//                throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.booking.date.before.current.date"),
//                        HttpStatus.BAD_REQUEST);
//            }
//        }
//        // validate upload text
//        if (UploadDataType.FILE.equals(publishRequest.getUploadType())) {
//            if (StringUtils.isBlank(publishRequest.getUploadFileName())
//                    || StringUtils.isBlank(publishRequest.getUploadFilePath())) {
//                throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.file.upload.end.user.empty"),
//                        HttpStatus.BAD_REQUEST);
//            }
//        }
//
//    }

    private void validateAndSetDataPublish(PublishRequest publishRequest, Publish publish) {
        Integer campaignId = publishRequest.getCampaignId();
        /* 1. check valid campaign */
        Campaign campaign = validateAndGetCampaign(campaignId);

        /* 2. check valid contract */
        log.info("Find Contract with id: {}", campaign.getCustomerContractId());
        CustomerContract customerContract = customerContractRepository.findByIdAndValidYn(campaign.getCustomerContractId(), EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.contract.not.found"),
                        HttpStatus.BAD_REQUEST));
        // check validate customer
        log.info("Find Customer with id: {}", campaign.getCustomerId());
        Customer customer = customerRepository.findByIdAndApproveStatusCodeAndValidYn(customerContract.getCustomerId(), ApproveStatus.APPRV, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.customer.not.found"),
                        HttpStatus.BAD_REQUEST));
        if (CustomerType.CHANNEL.equals(customer.getCustomerTypeCode())) {
            // don't create manual publish for customer type CHANNEL
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.channel.publish.not.create"),
                    HttpStatus.BAD_REQUEST);
        }

        /* 3. check validate goods */
        Integer goodsId = publishRequest.getGoodsId();
        log.info("Check goods belong campaign with campaignId: {}, and goodsId: {}", campaignId, goodsId);
        boolean containGoods  = campaignGoodsRepository.existsByCampaignIdAndGoodsIdAndValidYn(campaignId, goodsId, EnumValidYn.Y);
        if (!containGoods) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.goods.id.not.belong.campaign"), HttpStatus.BAD_REQUEST);
        }
        log.info("Find Goods with id: {}", goodsId);
        Goods goods = goodsRepository.findByIdAndValidYn(goodsId, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.goods.not.found"),
                        HttpStatus.BAD_REQUEST));

        /* 4. check validate Supplier */
        log.info("Find Supplier with id: {}", goods.getSupplierId());
        Supplier supplier = supplierRepository.findByIdAndApproveStatusCodeAndValidYn(goods.getSupplierId(), ApproveStatus.APPRV, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.not.found"),
                        HttpStatus.BAD_REQUEST));

        publish.setGoods(goods);
        publish.setCustomerId(campaign.getCustomerId());
        publish.setSupplierId(supplier.getId());
        publish.setSellDiscountRate(customerContract.getSellDiscountRate());
        publish.setShowPopupYn(campaign.getShowPopupYn());
    }

    private Campaign validateAndGetCampaign(Integer campaignId) {
        log.info("Find Campaign with campaignId: {}", campaignId);
        Campaign campaign = campaignRepository.findByIdAndValidYn(campaignId, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.not.found"), HttpStatus.BAD_REQUEST));
        // Campaign must be approved
        if (!ApproveStatus.APPRV.equals(campaign.getApproveStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.has.not.been.approved"), HttpStatus.BAD_REQUEST);
        }
        // Campaign's endDate after current date
        if (campaign.getEndDate().before(new Date())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.date.invalid.with.current.date"), HttpStatus.BAD_REQUEST);
        }
        if (campaign.getStartDate().after(new Date())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.start.date.invalid.with.current.date"), HttpStatus.BAD_REQUEST);
        }
        return campaign;
    }

    private void savePublishApproveHistory(Integer id, ApproveStatus approveStatus, String rejectReason) {
        if (Objects.nonNull(approveStatus)) {
            log.info("Save PublishApproveHistory with publishId: {}", id);
            publishApproveHistoryRepository.save(PublishApproveHistory.builder()
                    .publishId(id)
                    .approveStatusCode(approveStatus)
                    .rejectReason(ApproveStatus.REJCT.equals(approveStatus) ? null : rejectReason)
                    .build());
        }
    }

    private void validateStatusPublishForUpdate(Publish publish) {
        if (Objects.nonNull(publish.getStatusCode())
                && !PublishStatusCode.WAIT_APPRV.getValue().equals(publish.getStatusCode())
                && !PublishStatusCode.CANCEL.getValue().equals(publish.getStatusCode())
                && !PublishStatusCode.REJECTED.getValue().equals(publish.getStatusCode())
                && !PublishStatusCode.CANCEL_APPRV.getValue().equals(publish.getStatusCode())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.publish.can.not.update"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validatePublishBeforeApprove(Publish publish) {
        PublishHandlingStrategy deliveryService = publishFactory.getStrategy(publish.getSmsType());
        // validate Campaign
        validateAndGetCampaign(publish.getCampaignId());

        // Only change status if publishStatus is WAIT_APPRV (Wait approve)
        if (!PublishStatusCode.WAIT_APPRV.getValue().equals(publish.getStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.approve.status.code.not.valid"),
                    HttpStatus.BAD_REQUEST);
        }

        // validate Booking date
//        if (EnumValidYn.Y.equals(publish.getBookingYn())) {
//            // validate booking date must after currentDate
//            Date currentDate = new Date();
//            if (currentDate.after(publish.getBookingDate())) {
//                throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.booking.date.before.current.date"), HttpStatus.BAD_REQUEST);
//            }
//        }
        validateBookingDate(publish.getBookingYn(), publish.getBookingDate());

        // Check the remaining External PIN
        int numberOfPinsRequired;
        if (deliveryService.isUsingUserInfoType(publish.getSmsType())) {
            numberOfPinsRequired = publish.getNumberOfVouchers();
        } else {
            Type listType = new TypeToken<ArrayList<EndUser>>(){}.getType();
            List<EndUser> endUsers = Constant.gson.fromJson(publish.getUploadText(), listType);
            numberOfPinsRequired = endUsers.size();
        }

        Goods goods = publish.getGoods();
        validateGoodBeforePublish(goods);
        Brand brand = brandRepository.findById(goods.getBrandId()).orElseThrow(
                () -> {
                    log.error("can not find brand by id: {}", goods.getBrandId());
                    throw new CustomCodeException(
                            MessageUtils.getMessage("evoucher.brand.not.found"),
                            HttpStatus.BAD_REQUEST
                    );
                }
        );
        validateBrandBeforePublish(brand);
        Date bookingDate = publish.getBookingYn() == EnumValidYn.Y ? publish.getBookingDate() : null;
        goodsService.validateExpiredDate(goods, bookingDate);

        validateAvailableVouchers(goods, numberOfPinsRequired, bookingDate);
    }

    private void validateGoodBeforePublish(Goods goods) {
        if (EnumValidYn.Y != goods.getValidYn()) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.good.is.invalid"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validateBrandBeforePublish(Brand brand) {
        if (EnumValidYn.Y != brand.getValidYn()) {
            log.error("brand is invalid");
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.brand.is.invalid"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void createPublishProcess(Integer id, Publish publish, List<EndUser> endUsers) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            if (publishFactory.getStrategy(publish.getSmsType()).isMetaDataPublishing()) {
                PublishRegMetaDataOnly payload = new PublishRegMetaDataOnly();
                payload.setPublishId(id);
                payload.setNumberOfVouchers(publish.getNumberOfVouchers());
                if (SMSType.DOWNLOAD == publish.getSmsType()) {
                    payload.setPhoneNumbers(endUsers.stream().map(EndUser::getUserMobileNum).collect(Collectors.toList()));
                }
                HttpEntity entity = new HttpEntity(payload, headers);
                log.info("Call publish-service meta-data only with payload: {}", payload);
                restTemplate.exchange(publishServiceUrl + "/publish/metadata-only", HttpMethod.POST, entity, Object.class);
            } else {
                PublishScheduleRegistration payload = PublishScheduleRegistration.builder()
                        .publishId(id)
                        .phoneNumbers(endUsers.stream().map(EndUser::getUserMobileNum).collect(Collectors.toList()))
                        .build();
                HttpEntity entity = new HttpEntity(payload, headers);
                log.info("Call publish-service with payload: {}", payload.toString());
                restTemplate.exchange(publishServiceUrl + "/publish", HttpMethod.POST, entity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            throw readPublishServiceError(e);
        } catch (Exception e) {
            log.error("Publish service error: {}", e.getMessage(), e);

            throw new CustomCodeException(
                MessageUtils.getMessage("evoucher.publish.create.process.error", "system error"),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void publishEmail(Publish publish, List<EndUserRequest> users) {
        try {
            log.debug("call publish service to publish email: {}", publish);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            EmailPublishRequest payload = EmailPublishRequest.builder()
                    .publishId(publish.getId().longValue())
                    .users(users)
                    .build();

            HttpEntity<EmailPublishRequest> entity = new HttpEntity<>(payload, headers);
            log.info("Call publish-service email with payload: {}", payload);
            restTemplate.exchange(publishServiceUrl + "/publish", HttpMethod.POST, entity, Object.class);
        } catch (HttpStatusCodeException e) {
            throw readPublishServiceError(e);
        } catch (Exception e) {
            log.error("Publish service error: {}", e.getMessage(), e);

            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.publish.create.process.error", "system error"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    private void validatePermissionAdminUser(Integer id, UserPrincipal userPrincipal) {
        log.info("Check user has permission approve status with campaignId: {}", id);
        if (!EnumRole.ROLE_ADMIN.toString().equals(userPrincipal.getAdminType())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
                    HttpStatus.FORBIDDEN);
        }
    }

    private OrderPinResponse getOrderPinResponse(Publish publish) {
        Integer publishId = publish.getId();
        String orderStatus = PublishStatusCode.APPROVED.getValue();

        Goods goods = publish.getGoods();
        log.info("Get brand with brandId: {}", goods.getBrandId());
        Optional<Brand> brand = brandRepository.findById(goods.getBrandId());

        log.info("Get publish detail with publishId: {}", publishId);
        Optional<PublishDetail> publishDetailOptional =
                publishDetailRepository.findFirstByPublishId(publishId);
        if (publishDetailOptional.isEmpty()) {
            log.info("Publish detail not found with publishId: {}", publishId);
            return OrderPinResponse.builder()
                    .orderId(publish.getId())
                    .status(orderStatus)
                    .goodsId(goods.getId())
                    .goodsName(goods.getGoodsName())
                    .goodsImgPath(goods.getGoodsImgPath())
                    .brandId(goods.getBrandId())
                    .brandName(brand.map(Brand::getBrandName).orElse(null))
                    .build();
        }
        PublishDetail publishDetail = publishDetailOptional.get();
        // Publish detail already exist => status = publishDetailStatus
        orderStatus = publishDetail.getPublishDetailStatusCode();

        log.info("Get evoucher for publishDetailId: {}", publishDetail.getId());
        Optional<EVoucher> eVoucherOptional =
                eVoucherRepository.findByPublishDetailId(publishDetail.getId());
        if (eVoucherOptional.isEmpty()) {
            log.info("EVoucher not found with publishDetailId: {}", publishDetail.getId());
            return OrderPinResponse.builder()
                    .orderId(publish.getId())
                    .publishDate(publish.getPublishDate())
                    .status(orderStatus)
                    .goodsId(goods.getId())
                    .goodsName(goods.getGoodsName())
                    .goodsImgPath(goods.getGoodsImgPath())
                    .brandId(goods.getBrandId())
                    .brandName(brand.map(Brand::getBrandName).orElse(null))
                    .build();
        }
        EVoucher eVoucher = eVoucherOptional.get();

        return OrderPinResponse.builder()
                .orderId(publish.getId())
                .pin((eVoucher.getExternalPinNo() == null || eVoucher.getExternalPinNo().isEmpty()) ?
                        eVoucher.getSerialNo() : eVoucher.getExternalPinNo())
                .shortLink(eVoucher.getShortLink())
                .createDate(eVoucher.getCreationDate())
                .publishDate(publish.getPublishDate())
                .status(orderStatus)
                .goodsId(goods.getId())
                .goodsName(goods.getGoodsName())
                .goodsImgPath(goods.getGoodsImgPath())
                .brandId(goods.getBrandId())
                .brandName(brand.map(Brand::getBrandName).orElse(null))
                .build();
    }

    private void cancelPublishProcess(List<Publish> publishes) {
        log.info("Cancel Pins for list publishIds: {}", publishes);
        try {
            List<PublishCancelRequest> payload = publishes.stream()
                    .map(
                            publish -> {
                                int noPinRequire = 0;
                                if (SystemType.EXTERNAL.equals(publish.getGoods().getSystem())) {
                                    if (SMSType.isVoucherCountType(publish.getSmsType())) {
                                        noPinRequire = publish.getNumberOfVouchers();
                                    } else {
                                        log.info("Map string to list endUser: {}", publish.getUploadType());
                                        Type listType = new TypeToken<ArrayList<EndUser>>(){}.getType();
                                        List<EndUser> endUsers = Constant.gson.fromJson(publish.getUploadText(), listType);

                                        noPinRequire = endUsers.size();
                                        if (EnumValidYn.N.equals(publish.getReceiverNoDuplicateAllowYn())) {
                                            List<EndUser> distinctEndUsers = new ArrayList<>(endUsers.stream()
                                                    .collect(
                                                            Collectors.toMap(
                                                                    EndUser::getUserMobileNum,
                                                                    endUser -> endUser,
                                                                    (existing, replacement) -> existing))
                                                    .values());
                                            noPinRequire = distinctEndUsers.size();
                                        }
                                    }
                                }

                                return PublishCancelRequest.builder()
                                        .publishId(publish.getId())
                                        .numbersCount(noPinRequire)
                                        .build();
                            })
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(payload)) {
                log.info("There are no Publish that need to change their PIN status");
                return;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(payload, headers);

            log.info("Call publish-service cancel publish with payload: {}", payload.toString());
            restTemplate.exchange(
                    publishServiceUrl + "/publish/cancel",
                    HttpMethod.PUT,
                    entity,
                    Void.class);
        } catch (HttpStatusCodeException e) {
            throw readPublishServiceError(e);
        }
    }

    private CustomCodeException readPublishServiceError(HttpStatusCodeException e) {
        log.error("Publish service error: {}", e.getMessage(), e);
        var error = Constant.gson.fromJson(e.getResponseBodyAsString(),
                PublishServiceResponseException.class);
        HttpStatus statusCode = HttpStatus.valueOf(error.getCode());
        return new CustomCodeException(
                MessageUtils.getMessage("evoucher.publish.create.process.error", error.getMessage()),
                statusCode);
    }

    public static void validateBookingDate(EnumValidYn isBooking, Date bookingDate) throws CustomCodeException {
        if (EnumValidYn.Y.equals(isBooking)) {
            // BookingDate not null
            if (Objects.isNull(bookingDate))
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.booking.date.empty"), HttpStatus.BAD_REQUEST);
            // validate booking date must after currentDate
            Date currentDate = new Date();
            if (currentDate.after(bookingDate)) {
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.booking.date.before.current.date"),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }

    public static void validateUploadData(UploadDataType type, String uploadFileName, String uploadFilePath) throws CustomCodeException {
        if (Objects.isNull(type)) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.empty.uploadType"), HttpStatus.BAD_REQUEST);
        }
        if (UploadDataType.FILE.equals(type)) {
            if (StringUtils.isBlank(uploadFileName)
                    || StringUtils.isBlank(uploadFilePath)) {
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.file.upload.end.user.empty"),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }

    public static void validateNumberOfVoucher(Integer number) throws CustomCodeException {
        if (Objects.isNull(number) || number <= 0) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.empty.numberOfVouchers"), HttpStatus.BAD_REQUEST);
        }
        if (number > MAX_DOWNLOAD_VOUCHERS_ALLOWED) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.max.numberOfVouchers", MAX_DOWNLOAD_VOUCHERS_ALLOWED), HttpStatus.BAD_REQUEST);
        }
    }

    public static void validateUserList(List<EndUserRequest> endUserList) throws CustomCodeException {
        if (Objects.isNull(endUserList) || endUserList.isEmpty()) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.empty.users"), HttpStatus.BAD_REQUEST);
        }
    }

    public static void validateNoDuplicateYn(EnumValidYn noDuplicate) throws CustomCodeException {
        if (Objects.isNull(noDuplicate)) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.empty.duplicateKey"), HttpStatus.BAD_REQUEST);
        }
    }

}

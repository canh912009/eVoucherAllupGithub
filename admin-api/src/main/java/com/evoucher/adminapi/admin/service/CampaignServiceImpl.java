package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.*;
import com.evoucher.adminapi.admin.dao.models.*;
import com.evoucher.adminapi.admin.mapper.CampaignMapper;
import com.evoucher.adminapi.admin.mapper.CustomerContractMapper;
import com.evoucher.adminapi.admin.mapper.PublishMapper;
import com.evoucher.adminapi.admin.service.models.*;
import com.evoucher.adminapi.auth.service.AdminService;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.CustomerRepository;
import com.evoucher.adminapi.cms.dao.GoodsRepository;
import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.Customer;
import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.dao.models.Supplier;
import com.evoucher.adminapi.cms.mapper.CustomerMapper;
import com.evoucher.adminapi.cms.mapper.GoodsMapper;
import com.evoucher.adminapi.cms.service.models.BrandDTO;
import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import com.evoucher.adminapi.cms.service.models.GoodsDTO;
import com.evoucher.adminapi.cms.service.models.SupplierDTO;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.*;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.DateUtils;
import com.evoucher.adminapi.common.utils.MessageUtils;
import com.evoucher.adminapi.good.service.GoodServiceFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignServiceImpl implements CampaignService {

    public static final String CAMPAIGN_NOT_FOUND = "evoucher.campaign.not.found";
    private final CampaignRepository campaignRepository;
    private final CampaignGoodsRepository campaignGoodsRepository;
    private final CustomerRepository customerRepository;
    private final CustomerContractRepository contractRepository;
    private final GoodsRepository goodsRepository;
    private final PublishRepository publishRepository;
    private final CampaignApproveHistoryRepository campaignApproveHistoryRepository;
    private final MessageTemplateService templateService;

    private final CampaignMapper campaignMapper;
    private final PublishMapper publishMapper;
    private final CustomerMapper customerMapper;
    private final CustomerContractMapper customerContractMapper;
    private final GoodsMapper goodsMapper;
    private final ObjectMapper objectMapper;
    private final AdminService adminService;

    @Override
    public CampaignDTO findById(Integer id) {
        log.info("jackson timezone: {}, system timezone: {}", objectMapper.getDeserializationConfig().getTimeZone().getID(), ZoneOffset.systemDefault());
        log.info("Find Campaign with campaignId: {}", id);
        Campaign campaign = campaignRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(CAMPAIGN_NOT_FOUND),
                        HttpStatus.BAD_REQUEST
                ));

        // validate permission
        adminService.validateCorpIdPermission(campaign.getCustomerId());

        log.info("Find Customer with customerId: {}", campaign.getCustomerId());
        CustomerDTO customer = customerRepository.findById(campaign.getCustomerId())
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.customer.not.found"),
                        HttpStatus.BAD_REQUEST));

        log.info("Find CustomerContract with contractId: {}", campaign.getCustomerContractId());
        CustomerContractDTO customerContract = contractRepository.findById(campaign.getCustomerContractId())
                .map(customerContractMapper::toContractDTO)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.contract.not.found"),
                        HttpStatus.BAD_REQUEST));

        log.info("Find list Goods information with campaignId: {}", id);
        List<Tuple> tupleList = campaignGoodsRepository.findGoodsSupplerBrandByCampaignIdAndValidYn(id, EnumValidYn.Y);
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

        log.info("Find list publish with campaignId: {}", id);
        List<Publish> publishes = publishRepository.findAllByCampaignId(id);
        List<PublishDTO> publishDTOS = publishes.stream().map(publish -> {
            PublishDTO publishDTO = publishMapper.toPublishDTO(publish);
            publishDTO.setGoods(GoodsDTO.builder()
                    .id(publish.getGoods().getId())
                    .goodsName(publish.getGoods().getGoodsName())
                    .build());
            return publishDTO;
        }).collect(Collectors.toList());

        CampaignDTO campaignDTO = campaignMapper.toCampaignDTO(campaign);
        campaignDTO.setCustomer(customer);
        campaignDTO.setCustomerContract(customerContract);
        campaignDTO.setListGoods(goodsDTOS);
        campaignDTO.setPublishes(publishDTOS);
        campaignDTO.setStatusCode(campaignDTO);
        return campaignDTO;
    }

    @Override
    @Transactional
    public CampaignDTO createCampaign(CampaignRequest campaignRequest) {
        log.info("Validate campaign input request");
        validateCampaignRequest(campaignRequest);

        String customerId = campaignRequest.getCustomerId();
        log.info("Find Customer with customerId: {}", customerId);
        Customer customer = customerRepository.findByIdAndValidYn(customerId, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.customer.not.found"),
                        HttpStatus.BAD_REQUEST));

        if (CustomerType.CHANNEL.equals(customer.getCustomerTypeCode())) {
            // Only create one campaign for customer type CHANNEL
            log.info("Check campaign already exist for customerId: {}", customerId);
            if (campaignRepository.existsByCustomerId(customerId)) {
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.channel.campaign.only.one.for.customer"),
                        HttpStatus.BAD_REQUEST);
            }

            if (!CollectionUtils.isEmpty(campaignRequest.getGoods())) {
                campaignRequest.getGoods()
                        .forEach(goods -> {
                            log.info("Find Goods with goodsId: {}", goods.getId());
                            Goods good = goodsRepository.findById(goods.getId()).orElse(new Goods());
                            if (!Objects.isNull(good.getSystem()) && GoodServiceFactory.PARENT_GOOD_SYSTEM.contains(good.getSystem())) {
                                throw new CustomCodeException(
                                        MessageUtils.getMessage("evoucher.campaign.goods.is.choice.goods", good.getSystem(), goods.getId()),
                                        HttpStatus.BAD_REQUEST);
                            }
                        });
            }
        }

        log.info("Validate Customer info and CustomerContract info before create Campaign");
        validateInformationBeforeCreateOrUpdateCampaign(campaignRequest, customer);

        log.info("Convert Campaign entity");
        Campaign campaign = campaignMapper.toCampaign(campaignRequest);
        campaign.setValidYn(EnumValidYn.Y);
        campaign.setApproveStatusInfo(campaignRequest.getApproveStatusCode());

        log.info("get message template by id: {}", campaignRequest.getMessageTemplateId());
        MessageTemplate messageTemplate = templateService.findById(campaignRequest.getMessageTemplateId());

        campaign.setMessageTemplate(messageTemplate);

        log.info("Save campaign");
        campaign = campaignRepository.save(campaign);

        // save CampaignApproveHistory if it's request approve
        saveCampaignApproveHistory(campaign.getId(), campaignRequest.getApproveStatusCode(), null);

        // save list campaign goods
        List<Goods> listGoods = saveListCampaignGoods(campaignRequest, campaign.getId());

        CampaignDTO campaignDTO = campaignMapper.toCampaignDTO(campaign, listGoods, null);
        campaignDTO.setStatusCode(campaignDTO);
        return campaignDTO;
    }

    @Override
    @Transactional
    public CampaignDTO updateCampaign(Integer id, CampaignRequest campaignRequest) {
        log.info("Validate campaign input request");
        validateCampaignRequest(campaignRequest);

        log.info("Find campaign old with campaignId: {}", id);
        Campaign campaignOld = campaignRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(CAMPAIGN_NOT_FOUND),
                        HttpStatus.BAD_REQUEST
                ));

        String customerId = campaignRequest.getCustomerId();
        log.info("Find Customer with customerId: {}", customerId);
        Customer customer = customerRepository.findByIdAndValidYn(customerId, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.customer.not.found"),
                        HttpStatus.BAD_REQUEST));

        // With customer type CHANNEL you can edit campaigns in any status
        if (!CustomerType.CHANNEL.equals(customer.getCustomerTypeCode())) {
            log.info("Check campaign status with campaignId: {}", id);
            validateStatusCampaignForUpdate(campaignOld);
        } else {
            // Only create one campaign for customer type CHANNEL
            log.info("Check campaign already exist for customerId: {}", customerId);
            if (!customerId.equals(campaignOld.getCustomerId())) {
                throw new CustomCodeException(
                        MessageUtils.getMessage("evoucher.channel.campaign.only.one.for.customer"),
                        HttpStatus.BAD_REQUEST);
            }
        }


        log.info("Validate Customer info and CustomerContract info before update Campaign");
        validateInformationBeforeCreateOrUpdateCampaign(campaignRequest, customer);


        log.info("Convert Campaign entity");
        Campaign campaign = campaignMapper.toCampaign(campaignRequest);
        campaign.setId(id);
        campaign.setValidYn(EnumValidYn.Y);

        if (CustomerType.CHANNEL.equals(customer.getCustomerTypeCode())
                && ApproveStatus.APPRV.equals(campaignOld.getApproveStatusCode())) {
            // Customer type is CHANNEL and client want update campaign status APPROVE
            // -> Need to clear all old status information
            campaignOld.setApproveStatusCode(null);
            campaignOld.setApproveRequestId(null);
            campaignOld.setApproveRequestDate(null);
            campaignOld.setApproveDate(null);
            campaignOld.setApproveStatusCode(null);
            campaignOld.setApproveId(null);
        }

        if (Objects.nonNull(campaignRequest.getApproveStatusCode())) {
            campaign.setApproveStatusInfo(campaignRequest.getApproveStatusCode());
        } else {
            campaign.setApproveStatusCode(campaignOld.getApproveStatusCode());
            campaign.setApproveRequestId(campaignOld.getApproveRequestId());
            campaign.setApproveRequestDate(campaignOld.getApproveRequestDate());
        }

        log.info("check message template is changed or not");
        if (campaignOld.getMessageTemplate() == null || campaignOld.getMessageTemplate().getId() != campaignRequest.getMessageTemplateId()) {
            log.info("message template is changed, get new message template");
            MessageTemplate messageTemplate = templateService.findById(campaignRequest.getMessageTemplateId());
            campaign.setMessageTemplate(messageTemplate);
        } else {
            campaign.setMessageTemplate(campaignOld.getMessageTemplate());
        }

        log.info("Save campaign with campaignId: {}", id);
        campaignRepository.save(campaign);

        // save CampaignApproveHistory if it's request approve
        saveCampaignApproveHistory(campaign.getId(), campaignRequest.getApproveStatusCode(), null);

        // delete list campaign goods relationship old
        List<CampaignGoods> campaignGoods = campaignGoodsRepository.findAllByCampaignIdAndValidYn(id, EnumValidYn.Y);
        if (!campaignGoods.isEmpty()) {
            campaignGoods.forEach(cg -> cg.setValidYn(EnumValidYn.N));
            campaignGoodsRepository.saveAll(campaignGoods);
        }

        // save list campaign goods relationship new
        List<Goods> listGoods = saveListCampaignGoods(campaignRequest, campaign.getId());

        CampaignDTO campaignDTO = campaignMapper.toCampaignDTO(campaign, listGoods, null);
        campaignDTO.setStatusCode(campaignDTO);
        return campaignDTO;
    }

    @Override
    @Transactional
    public Integer deleteCampaignById(Integer id) {
        log.info("Check campaign is exist with campaignId: {}", id);
        Campaign campaign = campaignRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage(CAMPAIGN_NOT_FOUND), HttpStatus.BAD_REQUEST));

        // Check campaign status can update
        validateStatusCampaignForUpdate(campaign);

        campaign.setValidYn(EnumValidYn.N);
        log.info("Update validYn of Campaign with campaignId: {}", id);
        campaignRepository.save(campaign);

        log.info("Find list CampaignGoods with campaignId: {}", id);
        List<CampaignGoods> campaignGoods = campaignGoodsRepository.findAllByCampaignIdAndValidYn(id, EnumValidYn.Y);
        if (!CollectionUtils.isEmpty(campaignGoods)) {
            campaignGoods.forEach(cg -> cg.setValidYn(EnumValidYn.N));
            log.info("Update validYn of list CampaignGoods with campaignId: {}", id);
            campaignGoodsRepository.saveAll(campaignGoods);
        }

        return id;
    }

    @Override
    public Page<SearchCampaignResponse> searchCampaign(FilterSearchAdmin filterSearchAdmin) {
        int page = ObjectUtils.isEmpty(filterSearchAdmin.getPage()) ? 0 : filterSearchAdmin.getPage() - 1; //pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchAdmin.getPageSize()) ? 10 : filterSearchAdmin.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        List<SearchCampaignResponse> campaignDTOS = campaignRepository.searchCampaign(filterSearchAdmin, pageable);
        long countCampaign = 0;
        if (!CollectionUtils.isEmpty(campaignDTOS)) {
            countCampaign = campaignRepository.countCampaign(filterSearchAdmin);
        }

        return new PageImpl<>(campaignDTOS, pageable, countCampaign);
    }

    @Override
    @Transactional
    public Integer updateStatusCampaign(Integer id, ApproveRequest approveRequest) {
        ApproveStatus approveStatus = approveRequest.getApproveStatusCode();
        log.info("Find campaign with campaignId: {}", id);
        Campaign campaign = campaignRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(CAMPAIGN_NOT_FOUND),
                        HttpStatus.BAD_REQUEST
                ));

        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        String adminType = user.getAdminType();
        EnumRole role = Enum.valueOf(EnumRole.class, adminType);
        if (EnumRole.ROLE_OPERATOR.equals(role)
                && !ApproveStatus.REQ.equals(approveStatus)
                && !ApproveStatus.CANCEL_REQ.equals(approveStatus)) {
            log.info("AdminId {} does not have permission!", user.getId());
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
                    HttpStatus.FORBIDDEN);
        }


        switch (approveStatus) {
            case REQ: {
                log.info("Find Customer with customerId: {}", campaign.getCustomerId());
                Customer customer = customerRepository.findByIdAndValidYn(campaign.getCustomerId(), EnumValidYn.Y)
                        .orElseThrow(() -> new CustomCodeException(
                                MessageUtils.getMessage("evoucher.customer.not.found"),
                                HttpStatus.BAD_REQUEST));

                // With customer type CHANNEL you can edit campaigns in any status
                if (!CustomerType.CHANNEL.equals(customer.getCustomerTypeCode())) {
                    log.info("Check campaign status with campaignId: {}", id);
                    validateStatusCampaignForUpdate(campaign);
                }

                campaign.setApproveStatusInfo(approveStatus);
                break;
            }
            case CANCEL_REQ:
            case REJCT:
            case APPRV: {
                // Only change status if campaignStatus is WAIT_APPRV (Wait approve)
                if (!ApproveStatus.REQ.equals(campaign.getApproveStatusCode())) {
                    throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.has.not.been.request"),
                            HttpStatus.BAD_REQUEST);
                }

                campaign.setApproveStatusInfo(approveStatus);
                break;
            }
            case CANCEL_APPRV: {
                // ADMIN can cancel_approve after campaign is approved but before publish is registered
                // 1. Check campaignStatus is APPROVED
                if (!ApproveStatus.APPRV.equals(campaign.getApproveStatusCode())) {
                    throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.has.not.been.approved"),
                            HttpStatus.BAD_REQUEST);
                }
                // 2. Check publish has not been created
                List<Publish> publishes = publishRepository.findAllByCampaignId(id);
                if (!publishes.isEmpty()) {
                    log.error("publish is created before");
                    throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.can.not.be.canceled"),
                            HttpStatus.BAD_REQUEST);
                }

                campaign.setApproveStatusInfo(approveStatus);
                break;
            }
            default:
                log.error("campaign status is not approved");
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.approveStatus.not.valid"),
                        HttpStatus.BAD_REQUEST);
        }

        log.info("Update status Campaign with id: {}", id);
        campaignRepository.save(campaign);

        // Create CampaignApproveHistory
        saveCampaignApproveHistory(campaign.getId(), approveStatus,
                ApproveStatus.REJCT.equals(approveStatus) ? approveRequest.getRejectReason() : null);

        return id;
    }

    private void validateCampaignRequest(CampaignRequest campaignRequest) {
        // validate approveStatus null or REQ
        if (Objects.nonNull(campaignRequest.getApproveStatusCode())
                && !ApproveStatus.REQ.equals(campaignRequest.getApproveStatusCode())) {
            log.error("campaign status is not request");
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.approveStatus.not.valid"),
                    HttpStatus.BAD_REQUEST);
        }

        Date startDate = campaignRequest.getStartDate();
        Date endDate = campaignRequest.getEndDate();
        // validate StartDate and EndDate of the campaign
        if (endDate.before(startDate)) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.end.date.before.start.date"),
                    HttpStatus.BAD_REQUEST);
        }

        if (!CollectionUtils.isEmpty(campaignRequest.getGoods())) {
            List<Goods> listGoods = campaignRequest.getGoods().stream()
                    .map(
                            goodsRequest -> goodsRepository.findByIdAndValidYn(goodsRequest.getId(), EnumValidYn.Y)
                                    .orElseThrow(() -> new CustomCodeException(
                                            MessageUtils.getMessage("evoucher.goods.not.found"),
                                            HttpStatus.BAD_REQUEST)))
                    .collect(Collectors.toList());
            //check if choice of
            int parentTypeCount = (int) listGoods.stream().filter(g -> GoodServiceFactory.PARENT_GOOD_SYSTEM.contains(g.getSystem())).count();

            boolean isAllParent = parentTypeCount == listGoods.size();

            boolean isDirectUse = parentTypeCount == 0;

            if (!isDirectUse && !isAllParent) {
                log.error("campaign has multiple type of goods");
                throw new CustomCodeException(
                        MessageUtils.getMessage("evoucher.campaign.goods.list.invalid"),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }

    private void validateInformationBeforeCreateOrUpdateCampaign(
            CampaignRequest campaignRequest,
            Customer customer) {
        // validate customer
        log.info("Check Customer has been approved with customerId: {}", customer.getId());
        if (!ApproveStatus.APPRV.equals(customer.getApproveStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.customer.not.approved"),
                    HttpStatus.BAD_REQUEST);
        }

        // validate customer contract
        Integer customerContractId = campaignRequest.getCustomerContractId();
        log.info("Find Contract with contractId: {}", customerContractId);
        CustomerContract customerContract = contractRepository.findByIdAndValidYn(customerContractId, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.contract.not.found"),
                        HttpStatus.BAD_REQUEST));
        log.info("Check Contract has been approved with contractId: {}", customerContractId);
        if (!ApproveStatus.APPRV.equals(customerContract.getApproveStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.contract.has.not.been.approved"),
                    HttpStatus.BAD_REQUEST);
        }

        // validate CustomerContract belong Customer
        if (!customer.getId().equals(customerContract.getCustomerId())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.contract.do.not.belong.customer"),
                    HttpStatus.BAD_REQUEST);
        }

        // validate StartDate and EndDate of the campaign are in the StartDate and EndDate of the contract
        Date startDateCampaign = campaignRequest.getStartDate();
        Date endDateCampaign = campaignRequest.getEndDate();
        Date startDateContract = customerContract.getStartDate();
        Date endDateContract = customerContract.getEndDate();
        if (startDateCampaign.before(startDateContract)
                || startDateCampaign.after(endDateContract)
                || endDateCampaign.before(startDateContract)
                || endDateCampaign.after(endDateContract)) {
            log.error("request period: {} - {}, {} - {}", startDateCampaign.getTime(), endDateCampaign.getTime(), startDateContract.getTime(), endDateContract.getTime());
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.date.invalid.with.contract.date"),
                    HttpStatus.BAD_REQUEST);
        }
    }

//    private void validatePermission(Campaign campaign) {
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
//                if (!adminCorpId.equals(campaign.getCustomerId())) {
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

    private void validateStatusCampaignForUpdate(Campaign campaign) {
        // check campaign is in editable state
        if (ApproveStatus.APPRV.equals(campaign.getApproveStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.has.been.approved"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private List<Goods> saveListCampaignGoods(CampaignRequest campaignRequest, Integer campaignId) {
        List<Goods> listGoods = new ArrayList<>();
        if (!CollectionUtils.isEmpty(campaignRequest.getGoods())) {
            log.info("Save list Campaign goods with campaignId: {}", campaignId);
            List<CampaignGoods> campaignGoods = campaignRequest.getGoods().stream()
                    .map(goods -> {
                        log.info("Find Goods with goodsId: {}", goods.getId());
                        Goods good = goodsRepository.findByIdAndValidYn(goods.getId(), EnumValidYn.Y)
                                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.goods.not.found"), HttpStatus.BAD_REQUEST));
                        listGoods.add(good);
                        return CampaignGoods.builder()
                                .goodsId(goods.getId())
                                .campaignId(campaignId)
                                .supplyDiscountRate(good.getSupplyDiscountRate())
                                .supplyDiscountAmount(good.getSupplyDiscountAmount())
                                .supplyCommissionRate(good.getSupplyCommissionRate())
                                .vatIncludeYn(good.getVatIncludeYn())
                                .settlementMethodCode(good.getSettlementMethodCode())
                                .validYn(EnumValidYn.Y)
                                .build();
                    }).collect(Collectors.toList());

            log.info("Save list CampaignGoods for Campaign with campaignId: {}", campaignId);
            campaignGoodsRepository.saveAll(campaignGoods);
        }
        return listGoods;
    }

    private void saveCampaignApproveHistory(Integer campaignId, ApproveStatus approveStatus, String rejectReason) {
        if (Objects.nonNull(approveStatus)) {
            log.info("Save CampaignApproveHistory with campaignId: {}", campaignId);
            campaignApproveHistoryRepository.save(CampaignApproveHistory.builder()
                    .campaignId(campaignId)
                    .approveStatusCode(approveStatus)
                    .rejectReason(rejectReason)
                    .build());
        }
    }
}

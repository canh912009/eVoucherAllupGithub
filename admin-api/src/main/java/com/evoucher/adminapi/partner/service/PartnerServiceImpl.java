package com.evoucher.adminapi.partner.service;

import com.evoucher.adminapi.admin.dao.CampaignGoodsRepository;
import com.evoucher.adminapi.admin.dao.CampaignRepository;
import com.evoucher.adminapi.partner.dao.ExternalPublishRepository;
import com.evoucher.adminapi.admin.dao.models.*;
import com.evoucher.adminapi.admin.service.PublishService;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.BrandRepository;
import com.evoucher.adminapi.cms.dao.CustomerRepository;
import com.evoucher.adminapi.cms.dao.GoodsRepository;
import com.evoucher.adminapi.cms.dao.SupplierRepository;
import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.Customer;
import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.dao.models.Supplier;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.CustomerType;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.MessageUtils;
import com.evoucher.adminapi.partner.dao.model.ExternalPublish;
import com.evoucher.adminapi.partner.service.model.request.ExternalPublishConvert;
import com.evoucher.adminapi.partner.service.model.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final CustomerRepository customerRepository;
    private final CampaignRepository campaignRepository;
    private final CampaignGoodsRepository campaignGoodsRepository;
    private final BrandRepository brandRepository;
    private final SupplierRepository supplierRepository;
    private final GoodsRepository goodsRepository;
    private final ExternalPublishRepository externalPublishRepository;

    private final PublishService publishService;


    @Override
    public List<BrandPartnerResponse> findAllBrand() {
        UserPrincipal userPrincipal = LoggedInUserContext.getLoggedInUser();
        String customerId = userPrincipal.getAdminCorpId();
        log.info("Find all Brand for customer: {}", customerId);

        log.info("Validate customer type is CHANNEL with customerId: {}", customerId);
        validateCustomerPartner(customerId);

        log.info("Get Campaign for customer: {}", customerId);
        Optional<Campaign> campaign = campaignRepository.findFirstByCustomerIdOrderByEndDateDesc(customerId);
        if (campaign.isEmpty()) {
            log.info("Campaign for customer: {} is empty", customerId);
            return new ArrayList<>();
        }

        Integer campaignId = campaign.get().getId();
        log.info("Get list CampaignGoods with campaignId: {}", campaignId);
        List<CampaignGoods> campaignGoods = campaignGoodsRepository.findAllByCampaignId(campaignId);
        if (campaignGoods.isEmpty()) {
            log.info("Campaign goods is empty with campaignId: {} for customerId: {}", campaignId, customerId);
            return new ArrayList<>();
        }

        Set<Integer> goodsIds = campaignGoods.stream()
                .map(CampaignGoods::getGoodsId)
                .collect(Collectors.toSet());
        log.info("Get list Brand with goodsIds: {}", goodsIds);
        List<Brand> brands = brandRepository.findAllByGoodsIdIn(goodsIds);

        return brands.stream()
                .map(brand -> BrandPartnerResponse.builder()
                        .id(brand.getId())
                        .brandName(brand.getBrandName())
                        .validYn(brand.getValidYn())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public BrandPartnerResponse findBrandByBrandId(String brandId) {
        var brandPartner = findAllBrand();
        List<String> brandIds = brandPartner.stream()
                .map(BrandPartnerResponse::getId)
                .collect(Collectors.toList());

        if (!brandIds.contains(brandId)) {
            log.info("Account does not have permission to access brand: {}", brandId);
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.channel.account.dont.permission.access.brand"),
                    HttpStatus.BAD_REQUEST);
        }

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.brand.not.found"),
                        HttpStatus.BAD_REQUEST));
        Supplier supplier = supplierRepository.findById(brand.getSupplierId())
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.not.found"),
                        HttpStatus.BAD_REQUEST));

        return BrandPartnerResponse.builder()
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .brandImagePath(brand.getBrandImagePath())
                .brandImageName(brand.getBrandImageName())
                .supplier(SupplierPartnerResponse.builder()
                        .id(supplier.getId())
                        .supplierName(supplier.getSupplierName())
                        .build())
                .build();
    }

    @Override
    public List<GoodsPartnerResponse> findAllGoods() {
        UserPrincipal userPrincipal = LoggedInUserContext.getLoggedInUser();
        String customerId = userPrincipal.getAdminCorpId();
        log.info("Find all Goods for customer: {}", customerId);

        log.info("Validate customer type is CHANNEL with customerId: {}", customerId);
        validateCustomerPartner(customerId);

        log.info("Get Campaign for customer: {}", customerId);
        Optional<Campaign> campaign = campaignRepository.findFirstByCustomerIdOrderByEndDateDesc(customerId);
        if (campaign.isEmpty()) {
            log.info("Campaign for customer: {} is empty", customerId);
            return new ArrayList<>();
        }

        Integer campaignId = campaign.get().getId();
        log.info("Get list CampaignGoods with campaignId: {}", campaignId);
        List<CampaignGoods> campaignGoods = campaignGoodsRepository.findAllByCampaignId(campaignId);
        if (campaignGoods.isEmpty()) {
            log.info("Campaign goods is empty with campaignId: {} for customerId: {}", campaignId, customerId);
            return new ArrayList<>();
        }

        Set<Integer> goodsIds = campaignGoods.stream()
                .map(CampaignGoods::getGoodsId)
                .collect(Collectors.toSet());
        log.info("Get list Goods with goodsIds: {}", goodsIds);
        List<Tuple> tupleList = goodsRepository.findGoodsSupplierBrandByIdIn(goodsIds);

        return tupleList.stream()
                .map(tuple -> {
                    Goods goods = tuple.get(0, Goods.class);
                    Supplier supplier = tuple.get(1, Supplier.class);
                    Brand brand = tuple.get(2, Brand.class);
                    return GoodsPartnerResponse.builder()
                            .id(goods.getId())
                            .goodsName(goods.getGoodsName())
                            .validYn(goods.getValidYn())
                            .brandName(ObjectUtils.isNotEmpty(brand) ? brand.getBrandName() : null)
                            .supplierName(ObjectUtils.isNotEmpty(supplier) ? supplier.getSupplierName() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public GoodsPartnerInfoResponse findGoodsByGoodsId(Integer goodsId) {
        var listGoodsPartner = findAllGoods();
        List<Integer> goodsIds = listGoodsPartner.stream()
                .map(GoodsPartnerResponse::getId)
                .collect(Collectors.toList());

        if (!goodsIds.contains(goodsId)) {
            log.info("Account does not have permission to access goods: {}", goodsId);
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.channel.account.dont.permission.access.goods"),
                    HttpStatus.BAD_REQUEST);
        }

        List<Tuple> tupleList = goodsRepository.findGoodsSupplierBrandByIdIn(List.of(goodsId));
        // Only 1 good is given out here otherwise it will be a system error
        Tuple tuple = tupleList.get(0);

        Goods goods = tuple.get(0, Goods.class);
        Supplier supplier = tuple.get(1, Supplier.class);
        Brand brand = tuple.get(2, Brand.class);

        return GoodsPartnerInfoResponse.builder()
                .id(goods.getId())
                .goodsName(goods.getGoodsName())
                .goodsDescription(goods.getGoodsDescription())
                .goodsType(goods.getGoodsType())
                .supplierGoodsId(goods.getSupplierGoodsId())
                .supplier(SupplierPartnerResponse.builder()
                        .id(ObjectUtils.isNotEmpty(supplier) ? supplier.getId() : null)
                        .supplierName(ObjectUtils.isNotEmpty(supplier) ? supplier.getSupplierName() : null)
                        .build())
                .brandPartnerResponse(BrandPartnerResponse.builder()
                        .id(ObjectUtils.isNotEmpty(brand) ? brand.getId() : null)
                        .brandName(ObjectUtils.isNotEmpty(brand) ? brand.getBrandName() : null)
                        .build())
                .periodType(goods.getPeriodType())
                .periodTerm(goods.getPeriodTerm())
                .periodExpireDate(goods.getPeriodExpireDate())
                .goodsImgName(goods.getGoodsImgName())
                .goodsImgPath(goods.getGoodsImgPath())
                .validYn(goods.getValidYn())
                .listPrice(goods.getListPrice())
                .sellPrice(goods.getSellPrice())
                .settlementMethodCode(goods.getSettlementMethodCode())
                .vatIncludeYn(goods.getVatIncludeYn())
                .startDate(goods.getStartDate())
                .endDate(goods.getEndDate())
                .categories(null)
                .build();
    }

    @Override
    public ExternalPublishResponse createExternalPublish(ExternalPublishConvert externalPublish) {
        String transactionId = externalPublish.getTransactionId();
        log.info("Create publish for transactionId: {}", transactionId);
        return publishService.createListPublishForCustomerChannel(externalPublish);
    }

    @Override
    public ExternalPublishResponse checkOrderProgressWithTransactionId(UUID transactionId) {
        UserPrincipal userPrincipal = LoggedInUserContext.getLoggedInUser();
        String customerId = userPrincipal.getAdminCorpId();

        log.info("Validate customer type is CHANNEL with customerId: {}", customerId);
        validateCustomerPartner(customerId);

        ExternalPublish externalPublish = externalPublishRepository
                .findById(transactionId.toString())
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.channel.transaction.id.not.found"),
                        HttpStatus.BAD_REQUEST));

        if (!customerId.equals(externalPublish.getCustomerId())) {
            log.info("External publish with transactionId: {} not belong customerId: {}",
                    transactionId,
                    customerId);
            return null;
        }

        return publishService.getExternalPublishWithTransactionId(transactionId.toString());
    }

    @Override
    public ExternalPublishOrderPinResponse checkOrderProgressWithOrderId(Integer orderId) {
        UserPrincipal userPrincipal = LoggedInUserContext.getLoggedInUser();
        String customerId = userPrincipal.getAdminCorpId();

        log.info("Validate customer type is CHANNEL with customerId: {}", customerId);
        validateCustomerPartner(customerId);

        return publishService.getExternalPublishWithOrderId(orderId, customerId);
    }

    @Override
    public void cancelExternalPublishByTransactionId(UUID transactionId) {
        UserPrincipal userPrincipal = LoggedInUserContext.getLoggedInUser();
        String customerId = userPrincipal.getAdminCorpId();

        log.info("Validate customer type is CHANNEL with customerId: {}", customerId);
        validateCustomerPartner(customerId);

        ExternalPublish externalPublish = externalPublishRepository
                .findById(transactionId.toString())
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.channel.transaction.id.not.found"),
                        HttpStatus.BAD_REQUEST));

        if (!customerId.equals(externalPublish.getCustomerId())) {
            log.info("External publish with transactionId: {} not belong customerId: {}",
                    transactionId,
                    customerId);
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.channel.transaction.id.not.belong.customer"),
                    HttpStatus.BAD_REQUEST);
        }

        log.info("Start Cancel Approve list publish with transactionId: {}", transactionId);
        publishService.cancelExternalPublishForCustomerChannel(transactionId);
    }

    @Override
    public void cancelOrderPinByOrderId(Integer orderId) {
        UserPrincipal userPrincipal = LoggedInUserContext.getLoggedInUser();
        String customerId = userPrincipal.getAdminCorpId();

        log.info("Validate customer type is CHANNEL with customerId: {}", customerId);
        validateCustomerPartner(customerId);

        log.info("Start Cancel External publish with orderId: {}", orderId);
        publishService.cancelExternalPublishForCustomerChannel(orderId, customerId);
    }

    private void validateCustomerPartner(String customerId) {
        log.info("Find Customer with customerId: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.customer.not.found"),
                        HttpStatus.BAD_REQUEST));

        if (!CustomerType.CHANNEL.equals(customer.getCustomerTypeCode())) {
            log.info("Customer does not belong to the channel customer type with customerId: {}", customerId);
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
                    HttpStatus.FORBIDDEN);
        }
    }
}

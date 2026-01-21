package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.admin.dao.SupplierContractRepository;
import com.evoucher.adminapi.admin.dao.models.SupplierContract;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.*;
import com.evoucher.adminapi.cms.dao.models.*;
import com.evoucher.adminapi.cms.mapper.BrandMapper;
import com.evoucher.adminapi.cms.mapper.GoodsMapper;
import com.evoucher.adminapi.cms.mapper.SupplierMapper;
import com.evoucher.adminapi.cms.service.models.*;
import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.*;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.Common;
import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.utils.DateUtils;
import com.evoucher.adminapi.common.utils.MessageUtils;
import com.evoucher.adminapi.good.service.GoodBasicService;
import com.evoucher.adminapi.good.service.GoodServiceFactory;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.evoucher.adminapi.common.utils.Common.getErrorMsgByCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {
    public static class ErrorCode{
        private ErrorCode(){}
        private static final int GOOD_EXPIRED = 10014;
        public static final int NOT_FOUND = 1092;
        public static final int BULK_CATEGORY_EMPTY = 1097;
    }

    static {
        Common.addMsg(ErrorCode.GOOD_EXPIRED, "evoucher.goods.expired");
        Common.addMsg(ErrorCode.NOT_FOUND, "evoucher.goods.not.found");
        Common.addMsg(ErrorCode.BULK_CATEGORY_EMPTY, "Bulk Category is empty");
    }

    private final GoodsRepository goodsRepository;

    private final CategoryRepository categoryRepository;

    private final CategoryGoodsRelRepository categoryGoodsRelRepository;

    private final SupplierMapper supplierMapper;
    private final BrandMapper brandMapper;
    private final GoodsMapper goodsMapper;


    private final StoreRepository storeRepository;

    private final BrandRepository brandRepository;

    private final SupplierRepository supplierRepository;

    private final SupplierContractRepository supplierContractRepository;

    private final GoodsChoiceRepository goodsChoiceRepository;
    private final GoodServiceFactory goodSerFactory;

    private final GoodBasicService basicService;

    @Scheduled(cron = "${job.sync-store.cron}")
    public void syncStoreForBrand() {
        this.syncBrandStore();
    }

    @Override
    @Transactional
    public GoodsDTO findById(Integer id) {
        log.info("Find Goods with goods id: {}", id);
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(getErrorMsgByCode(ErrorCode.NOT_FOUND)),
                        HttpStatus.BAD_REQUEST));

        // validate permission
        validatePermission(goods.getSupplierId(), goods.getBrandId());

        log.info("Find Supplier with supplierId: {}", goods.getSupplierId());
        SupplierDTO supplierDTO = supplierRepository.findById(goods.getSupplierId())
                .map(supplierMapper::toSupplierDTO)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.not.found"),
                        HttpStatus.BAD_REQUEST));

        log.info("Find Brand with brandId: {}", goods.getBrandId());
        BrandDTO brandDTO = brandRepository.findById(goods.getBrandId())
                .map(brandMapper::toBrandDTO)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.brand.not.found"),
                        HttpStatus.BAD_REQUEST));

        log.info("Find list Category with goods id: {}", id);
        List<Category> categories = categoryRepository.findByGoodIdAndValidYnIsYes(id);

        log.info("Find list Goods choice with goods id: {}", id);
        LinkedList<Goods> listGoodsChoice = goodsRepository.findGoodsChoiceById(id);

        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> storeIds = Constant.gson.fromJson(goods.getExceptStoreIds(), listType);
        List<Store> exceptStores = storeRepository.findAllByIdIn(storeIds);


        GoodsDTO goodsDTO = goodsMapper.toGoodsDTO(goods, categories, exceptStores);
        goodsDTO.setSupplier(supplierDTO);
        goodsDTO.setBrand(brandDTO);
        goodsDTO.setListGoodsChoice(goodsMapper.toListGoodsDTO(listGoodsChoice));

        return goodsDTO;
    }

    @Override
    @Transactional
    public GoodsDTO createGoods(GoodsRequest goodsRequest) {
        log.info("create good {}", goodsRequest);
        validateGoodsRequestCommon(goodsRequest);

        log.info("Validate permission create goods");
        validatePermission(goodsRequest.getSupplierId(), goodsRequest.getBrandId());

        log.info("Map values goods request to goods");
        Goods goods = new Goods(goodsRequest);

        // set store queryType
        goodSerFactory.getServiceBySystem(goodsRequest.getSystem())
                        .getStoreQueryService().setStoreTypeAndRequiredFields(goods, null);

        goodSerFactory.getServiceBySystem(goodsRequest.getSystem())
                        .mapAdditionInfo(goodsRequest, goods);

        log.info("Validate Goods request and update value for goods");
        validateGoodsInformationAndUpdateValue(goodsRequest, goods);

        log.info("validate goods Gift Pop if applicable");
        // validate integrated good
        if (GoodServiceFactory.INTEGRATED_GOOD.contains(goodsRequest.getSystem())) {
            validateExternalGood(goodsRequest);
        }

        //sync good(store, except store)
        if (GoodServiceFactory.isSyncType(goodsRequest.getSystem())) {
            goodSerFactory.getIntegratedSerByType(goodsRequest.getSystem())
                    .syncGood(goods, goodsRequest.getSupplierGoodsId());
        }

        goodSerFactory.getServiceBySystem(goods.getSystem())
                .setGoodToChildren(goods);
        // save Goods to database
        log.info("Save goods info");

        goods = basicService.save(goods);
        GoodsDTO goodsDTO = goodsMapper.toDTO(goods);

        // save categories relationship
        if (goodsRequest.getSystem() != SystemType.BULK) {
            List<CategoryDTO> categoryDTOS = goodsRequest.getCategories();
            List<CategoryGoodsRel> categoryGoodsRels = getCategoryGoodsList(categoryDTOS, goods.getId());
            log.info("Save list CategoryGoods for Goods with goodsId: {}", goods.getId());
            categoryGoodsRelRepository.saveAll(categoryGoodsRels);
            goodsDTO.setCategories(categoryDTOS);
        }

        // save list goods for choice voucher
        if (GoodsType.CH.equals(goods.getGoodsType())) {
            saveListGoodsChoice(goods.getId(), goodsRequest.getListGoodsChoice());
        }

        goodsDTO.setExceptStores(goodsRequest.getExceptStores());

        return goodsDTO;
    }

    @Override
    @Transactional
    public GoodsDTO updateGoods(Integer id, GoodsRequest goodsRequest) {
        log.info("update good {} to {}", id, goodsRequest);
        validateGoodsRequestCommon(goodsRequest);

        log.info("Find Goods by id {}", id);
        Goods goodsOld = goodsRepository.findById(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(getErrorMsgByCode(ErrorCode.NOT_FOUND)),
                        HttpStatus.BAD_REQUEST));

        log.info("Validate Goods request");
        validateGoodsRequestForUpdate(goodsRequest, goodsOld);

        goodSerFactory.getServiceBySystem(goodsRequest.getSystem())
                .updateDeletedChildren(goodsOld, goodsRequest);

        // save goods to database
        Goods goodsNew = new Goods(goodsRequest);

        goodSerFactory.getServiceBySystem(goodsRequest.getSystem())
                .mapAdditionInfo(goodsRequest, goodsNew);

        // set good store query type and set default value for store ids
//        goodSerFactory.getServiceBySystem(goodsRequest.getSystem())
//                .getStoreQueryService()
//                .setStoreTypeAndRequiredFields(goodsNew, goodsOld);
        goodsNew.setStoreQueryType(goodsOld.getStoreQueryType());

        log.info("Save update goods with goodsId: {}", id);
        goodsNew.setId(id);

        goodSerFactory.getServiceBySystem(goodsNew.getSystem())
                .setGoodToChildren(goodsNew);

        Goods goodsNews = goodsRepository.save(goodsNew);

        // Update categories relationship
        // 1. delete list category old
        log.info("Delete list categories relation ship for goodsId: {}", id);
        categoryGoodsRelRepository.deleteCategoryGoodsRelByGoodsId(id);

        // 2. save list category new
        List<CategoryDTO> categoryDTOS = goodsRequest.getCategories();
        List<CategoryGoodsRel> categoryGoodsRels = getCategoryGoodsList(categoryDTOS, goodsNews.getId());

        log.info("Save list CategoryGoods for Goods with goodsId: {}", goodsNews.getId());
        categoryGoodsRelRepository.saveAll(categoryGoodsRels);

        // save list goods for choice voucher
        if (GoodsType.CH.equals(goodsNews.getGoodsType())) {
            log.info("Find list goods choice for goodsId: {}", id);
            List<GoodsChoice> goodsChoicesOld = goodsChoiceRepository.findByParentGoodsIdAndValidYn(id, EnumValidYn.Y);
            log.info("Delete list Goods choice for goodsId: {}", id);
            goodsChoicesOld.forEach(o -> o.setValidYn(EnumValidYn.N));
            goodsChoiceRepository.saveAll(goodsChoicesOld);

            saveListGoodsChoice(id, goodsRequest.getListGoodsChoice());
        }

        GoodsDTO goodsDTO = goodsMapper.toDTO(goodsNews);
        goodsDTO.setCategories(categoryDTOS);
        goodsDTO.setExceptStores(goodsRequest.getExceptStores());
        return goodsDTO;
    }

    @Override
    public Integer delete(Integer id) {
        log.info("Check Goods is exist with goodsId: {}", id);
        Goods goods = goodsRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(getErrorMsgByCode(ErrorCode.NOT_FOUND)),
                        HttpStatus.BAD_REQUEST));

        // validate permission
        validatePermission(goods.getSupplierId(), goods.getBrandId());

        goods.setValidYn(EnumValidYn.N);
        log.info("Delete CategoryGoods with Goods id {}", id);
        categoryGoodsRelRepository.deleteCategoryGoodsRelByGoodsId(id);
        log.info("Update validYn of Goods with goodsId: {}", id);
        goodsRepository.save(goods);
        return id;
    }

    @Override
    public Page<? extends SearchGroupResponse> searchGoods(FilterSearchCms filterSearchCms) {
        int page = ObjectUtils.isEmpty(filterSearchCms.getPage()) ? 0 : filterSearchCms.getPage() - 1; //pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchCms.getPageSize()) ? 10 : filterSearchCms.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        log.info("search good: {}", filterSearchCms);
        Long countContractDTOS;
        List<SearchGroupResponse> goodsDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(filterSearchCms.getCategoryCode())) {
            countContractDTOS = goodsRepository.countGoodByCatBrandAndFilter(filterSearchCms, pageable);

            if (countContractDTOS > 0) {
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                goodsDTOS = Optional.ofNullable(goodsRepository.searchGoodByCatBrandAndFilter(filterSearchCms, pageable))
                        .orElse(new ArrayList<>()).stream().map(objs -> {
                            BulkGoodSearchResponse response = goodsMapper.toSearchResponse((Goods) objs[0]);
                            response.setCategoryIds(Constant.gson.fromJson((String) objs[1], type));
                            response.setBrandIds(Constant.gson.fromJson((String) objs[2], type));
                            return response;
                        }).collect(Collectors.toList());
            }
        } else {
            countContractDTOS = goodsRepository.countGoods(filterSearchCms, pageable, false);
            if (countContractDTOS > 0) {
                goodsDTOS = goodsRepository.searchGoods(filterSearchCms, pageable, false);
            }
        }
        return new PageImpl<>(goodsDTOS, pageable, countContractDTOS);
    }

    @Override
    public Page<SearchGroupResponse> searchGoodsIgnorePermissions(FilterSearchCms filterSearchCms) {
        int page = ObjectUtils.isEmpty(filterSearchCms.getPage()) ? 0 : filterSearchCms.getPage() - 1; //pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchCms.getPageSize()) ? 10 : filterSearchCms.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        List<SearchGroupResponse> goodsDTOS = goodsRepository.searchGoods(filterSearchCms, pageable, true);
        Long countContractDTOS = 0L;
        if (!goodsDTOS.isEmpty()) {
            countContractDTOS = goodsRepository.countGoods(filterSearchCms, pageable, true);
        }

        return new PageImpl<>(goodsDTOS, pageable, countContractDTOS);
    }


    private void validateGoodsRequestForUpdate(GoodsRequest goodsRequest, Goods goodsOld) {
        if (!goodsRequest.getSupplierContractId().equals(goodsOld.getSupplierContractId())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.contract.id.not.correct"),
                    HttpStatus.BAD_REQUEST);
        }
        if (!goodsRequest.getSupplierId().equals(goodsOld.getSupplierId())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.id.not.correct"),
                    HttpStatus.BAD_REQUEST);
        }
        if (!goodsRequest.getBrandId().equals(goodsOld.getBrandId())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.brand.id.not.correct"),
                    HttpStatus.BAD_REQUEST);
        }
        if (!goodsRequest.getSystem().equals(goodsOld.getSystem())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.system.type.not.correct"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validateGoodsRequestCommon(GoodsRequest goodsRequest) {
        Date startDate = goodsRequest.getStartDate();
        Date endDate = goodsRequest.getEndDate();
        // validate StartDate and EndDate of the goods
        if (endDate != null && endDate.before(startDate)) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.end.date.before.start.date"),
                    HttpStatus.BAD_REQUEST);
        }
        if (GoodServiceFactory.VALIDATING_PERIOD.contains(goodsRequest.getSystem())) {
            validatePeriod(goodsRequest);
        }

        if (GoodServiceFactory.PARENT_GOOD_SYSTEM.contains(goodsRequest.getSystem())
                || GoodServiceFactory.PARENT_GOOD_TYPE.contains(goodsRequest.getGoodsType())
        ) {
            GoodsType goodsTypeByGoodSystem =
                    goodSerFactory.getParentServiceBySystem(goodsRequest.getSystem())
                            .getGoodTypeBySystem();
            validateParentType(goodsRequest, goodsRequest.getSystem(), goodsTypeByGoodSystem);
            goodSerFactory.getParentServiceBySystem(goodsRequest.getSystem())
                    .validateChildren(goodsRequest);

        }


        // validate good by good type
        goodSerFactory.getGoodTypeServiceByType(goodsRequest.getGoodsType())
                .getUsingVoucherService().validateGoodByProductType(goodsRequest);

    }

    private static void validatePeriod(GoodsRequest goodsRequest) throws CustomCodeException{
        if (Objects.isNull(goodsRequest.getPeriodType())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.goods.period.type.not.null"),
                    HttpStatus.BAD_REQUEST);
        }
        if (PeriodType.FIXED_TERM.equals(goodsRequest.getPeriodType())
                && Objects.isNull(goodsRequest.getPeriodTerm())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.goods.term.day.not.null"),
                    HttpStatus.BAD_REQUEST);
        }
        if (PeriodType.FIXED_DT.equals(goodsRequest.getPeriodType())
                && Objects.isNull(goodsRequest.getPeriodExpireDate())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.goods.expire.day.not.null"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private static void validateParentType(GoodsRequest goodsRequest, SystemType systemType, GoodsType goodsType) throws CustomCodeException {
        if (!goodsType.equals(goodsRequest.getGoodsType())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.goods.choice.goods.type.invalid"),
                    HttpStatus.BAD_REQUEST);
        }
        if (!systemType.equals(goodsRequest.getSystem())) {
            throw new CustomCodeException(MessageUtils.getMessage(
                    "evoucher.goods.choice.goods.system.invalid", SystemType.CHOICE.toString()),
                    HttpStatus.BAD_REQUEST);
        }
    }


    public static void validateChildGood(GoodsDTO goodsDTO, Integer id) throws CustomCodeException {
        if (goodsDTO == null) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.goods.not.found.with.id", id),
                    HttpStatus.BAD_REQUEST);
        }
        if (goodsDTO.getValidYn() != EnumValidYn.Y) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.good.is.invalid", id),
                    HttpStatus.BAD_REQUEST);
        }
        if (!GoodServiceFactory.DIRECT_USAGE.contains(goodsDTO.getSystem())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.goods.child.goods.is.not.choice", id),
                    HttpStatus.BAD_REQUEST);
        }
    }
     private void validateGoodsInformationAndUpdateValue(GoodsRequest goodsRequest, Goods goods)
     throws CustomCodeException {
        //-- Check supplier
        log.info("Check supplier with supplierId: {}", goodsRequest.getSupplierId());
        Supplier supplier = supplierRepository.findByIdAndValidYn(goodsRequest.getSupplierId(), EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.supplier.not.found"),
                        HttpStatus.BAD_REQUEST));
        if (!supplier.getApproveStatusCode().equals(ApproveStatus.APPRV)) throw new CustomCodeException(
                MessageUtils.getMessage("evoucher.supplier.not.approved"),
                HttpStatus.BAD_REQUEST);

        //-- Check brand
        log.info("Check brand with brandId: {}", goodsRequest.getBrandId());
        Brand brand = brandRepository.findByIdAndValidYn(goodsRequest.getBrandId(), EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.brand.not.found"),
                        HttpStatus.BAD_REQUEST));
        if (!goodsRequest.getSupplierId().equals(brand.getSupplierId())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.brand.not.belong.supplier"),
                    HttpStatus.BAD_REQUEST);
        }
        if (!goodsRequest.getSystem().equals(brand.getSystem())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.goods.system.type.invalid"),
                    HttpStatus.BAD_REQUEST);
        }

        // check supplier contract
        log.info("Check supplier contract with contractId: {}", goodsRequest.getSupplierContractId());
        SupplierContract supplierContract = supplierContractRepository
                .findByIdAndValidYn(goodsRequest.getSupplierContractId(), EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.contract.not.found"),
                        HttpStatus.BAD_REQUEST));
        if (!goodsRequest.getSupplierId().equals(supplierContract.getSupplierId())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.contract.not.belong.supplier"),
                    HttpStatus.BAD_REQUEST);
        }
        if (supplierContract.getEndDate().before(new Date())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.contract.expired"),
                    HttpStatus.BAD_REQUEST);
        }

        // check category
        if (goodsRequest.getSystem() != SystemType.BULK) {
            List<CategoryDTO> categoryDTOS = goodsRequest.getCategories();
            categoryDTOS.forEach(category ->  {
                log.info("Find category by categoryCode: {}", category.getCategoryCode());
                categoryRepository.findByCategoryCodeAndValidYn(category.getCategoryCode(), EnumValidYn.Y)
                        .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.category.not.found"),
                                HttpStatus.BAD_REQUEST));
            });
        }

        // check list except store
        List<StoreDTO> exceptStoreDTOS = goodsRequest.getExceptStores();
        exceptStoreDTOS.forEach(storeDTO -> {
            log.info("Find store by storeId: {}", storeDTO.getId());
            Store store = storeRepository.findById(storeDTO.getId())
                    .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.store.not.found"),
                            HttpStatus.BAD_REQUEST));
            if (!goodsRequest.getBrandId().equals(store.getBrandId())) {
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.store.not.belong.brand"),
                        HttpStatus.BAD_REQUEST);
            }
        });

        // extra set value for goods
        goods.setSupplyDiscountRate(supplierContract.getSupplyDiscountRate());
    }

    private List<CategoryGoodsRel> getCategoryGoodsList(List<CategoryDTO> categoryDTOS, Integer goodsId) {
        return categoryDTOS.stream().map(categoryDTO -> CategoryGoodsRel.builder()
                .goodsId(goodsId)
                .categoryCode(categoryDTO.getCategoryCode())
                .build())
                .collect(Collectors.toList());
    }

    private void validatePermission(String supplierId, String brandId) {
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        String adminCorpId = user.getAdminCorpId();
        EnumRole role = Enum.valueOf(EnumRole.class, user.getAdminType());
        log.info("Validate permission with user info: {}", user);
        switch (role) {
            case ROLE_SUPPLIER:
                if (!adminCorpId.equals(supplierId)) {
                    throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
                            HttpStatus.BAD_REQUEST);
                }
                break;
            case ROLE_BRAND: {
                if (!adminCorpId.equals(brandId)) {
                    throw new  CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
                            HttpStatus.BAD_REQUEST);
                }
                break;
            }
            default:
                break;
        }
    }

    private void saveListGoodsChoice(Integer goodsId, List<GoodsDTO> goodsChoiceIds) {
        log.info("Save list Goods choice for goodsId: {}", goodsId);
        AtomicInteger displayIndex = new AtomicInteger(0);
        List<GoodsChoice> goodsChoiceList = goodsChoiceIds.stream()
                .map(goodsChild -> GoodsChoice.builder()
                        .parentGoodsId(goodsId)
                        .goodsId(goodsChild.getId())
                        .validYn(EnumValidYn.Y)
                        .displayIndex(displayIndex.incrementAndGet())
                        .build())
                .collect(Collectors.toList());

        log.info("Save list Goods choice: {}", goodsChoiceList);
        goodsChoiceRepository.saveAll(goodsChoiceList);
    }
    private void validateExternalGood(GoodsRequest goodsRequest) {
        Brand brand = brandRepository.findByIdAndValidYn(goodsRequest.getBrandId(), EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.brand.not.found"),
                        HttpStatus.BAD_REQUEST));

        goodSerFactory.getIntegratedSerByType(goodsRequest.getSystem())
                .validatePartnerGood(goodsRequest, brand.getBrandCode());
    }



    @Override
    public void validateExpiredDate(Goods goodsDTO, Date bookingDate) throws CustomCodeException {
        if (GoodServiceFactory.VALIDATING_PERIOD.contains(goodsDTO.getSystem())
                && goodsDTO.getPeriodType() == PeriodType.FIXED_DT) {
                Date expireDate =
                        bookingDate == null ?
                                DateUtils.getDateFromStringWithCommonFormat(goodsDTO.getPeriodExpireDate())
                                : bookingDate;
                if (new Date().after(expireDate)) {
                    log.error("good is expired: {}", expireDate);
                    throw new CustomCodeException(
                            ErrorCode.GOOD_EXPIRED,
                            MessageUtils.getMessage(getErrorMsgByCode(ErrorCode.GOOD_EXPIRED)),
                            HttpStatus.BAD_REQUEST);
                }
            }

    }

    @Override
    public void syncBrandStore() {
        for (SystemType systemType : SystemType.values()) {
            log.info("get service by type: {}", systemType);
            goodSerFactory.getServiceBySystem(systemType)
                    .syncStoreOfAllBrand();
        }
    }
}

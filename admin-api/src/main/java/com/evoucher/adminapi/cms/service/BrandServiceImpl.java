package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.auth.utils.AuthDataUtils;
import com.evoucher.adminapi.cms.dao.BrandRepository;
import com.evoucher.adminapi.cms.dao.GoodsRepository;
import com.evoucher.adminapi.cms.dao.StoreRepository;
import com.evoucher.adminapi.cms.dao.SupplierRepository;
import com.evoucher.adminapi.cms.dao.models.*;
import com.evoucher.adminapi.cms.mapper.BrandMapper;
import com.evoucher.adminapi.cms.mapper.SupplierMapper;
import com.evoucher.adminapi.cms.service.models.*;
import com.evoucher.adminapi.cms.service.models.request.BrandRequest;
import com.evoucher.adminapi.cms.utils.CmsConstant;
import com.evoucher.adminapi.cms.utils.CmsDataUtil;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.*;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.service.EntityService;
import com.evoucher.adminapi.good.service.*;
import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.utils.MessageUtils;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandServiceImpl extends EntityService<Brand, String, BrandDTO> implements BrandService {
    public static final Set<SystemType> EXTERNAL_BRAND = EnumSet.of(SystemType.GIFTPOP, SystemType.UR_BOX);
    private static final BrandMapper MAPPER = BrandMapper.INSTANT;
    public static final String EVOUCHER_BRAND_NOT_FOUND = "evoucher.brand.not.found";
    private final BrandRepository brandRepository;
    private final SupplierRepository supplierRepository;
    private final StoreRepository storeRepository;
    private final GoodsRepository goodsRepository;
    private final StoreService storeService;

    private final SupplierMapper supplierMapper;
    private final GoodServiceFactory goodSerFactory;

    private static final int BRAND_AUTH_CODE_LENGTH = 12;
    private static final int BRAND_ENCRYPTOR_BYTE_SIZE = 32;

    @Override
    public JpaRepository<Brand, String> getRepository() {
        return brandRepository;
    }

    @Override
    public String getEntityType() {
        return "brand";
    }

    @Override
    public EntityNotFoundException getNotFoundException(String id) {
        return new EntityNotFoundException(
                MessageUtils.getMessage(EVOUCHER_BRAND_NOT_FOUND));
    }

    @Override
    public BrandDTO findDtoById(String id) throws EntityNotFoundException, CustomCodeException {
        log.info("Find Brand by id: {}", id);
        Brand brand = findById(id);

        log.info("Find supplier by supplierId: {}", brand.getSupplierId());
        SupplierDTO supplier = supplierRepository.findById(brand.getSupplierId())
                .map(supplierMapper::toSupplierDTO)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.not.found"),
                        HttpStatus.BAD_REQUEST));

        log.info("Find list goods by brandId: {}", id);
        List<Goods> goods = goodsRepository.findAllByBrandId(brand.getId());

        log.info("Find list stores by brandId: {}", id);
        List<Store> stores = storeRepository.findAllByBrandId(brand.getId());

        BrandDTO brandDTO = MAPPER.toBrandDTO(brand, goods, stores);
        brandDTO.setSupplier(supplier);
        return brandDTO;
    }

    @Override
    public List<BrandDTO> findAllBySystemAndValid(SystemType systemType, EnumValidYn valid) {
        log.info("find all ");
        try {
            List<Brand> brands = brandRepository.findAllBySystemAndValidYn(systemType, valid);

            return brands.stream().map(this::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public Brand toEntity(BrandDTO dto) {
        return MAPPER.toBrand(dto);
    }

    @Override
    public BrandDTO toDto(Brand entity) {
        return MAPPER.toBrandDTO(entity);
    }

    @Override
    @Transactional
    public BrandDTO createBrand(BrandRequest brandRequest) {
        log.info("create brand: {}", brandRequest);
        // validate brand request
        validateBrandRequest(brandRequest);

        if(brandRequest.getSystem() == SystemType.INTERNAL) {
            Optional<Brand> brandOptional = brandRepository.findByAppId(brandRequest.getAppId());
            if (brandOptional.isPresent()) {
                throw new CustomCodeException(MessageUtils.getMessage("AppId already exists!"),
                        HttpStatus.BAD_REQUEST);
            }
        }
        String supplierId = brandRequest.getSupplierId();
        // validate create brand with role user
        validatePermissionCreateBrand(supplierId);

        // validate brand Gift Pop
        if (EXTERNAL_BRAND.contains(brandRequest.getSystem())) {
            //validate giftpop|urbox brand
            validateExternalBrand(brandRequest);
        }

        log.info("Create BrandId with supplierId: {}", supplierId);
        String brandId = createBrandId(supplierId);
        if (StringUtils.isEmpty(brandId)) throw new CustomCodeException(
                MessageUtils.getMessage("evoucher.brand.create.id.error"),
                HttpStatus.INTERNAL_SERVER_ERROR);

        log.info("Create brand with Id: {}", brandId);
        Brand brand = MAPPER.toBrand(brandRequest);
        // save to database
        brand.setId(brandId);

        log.info("Save brand to Database with Id: {}", brandId);
        // generate pos auth key and brand encryptor secret key
        generatePosKeyAndAuthCode(brand);
        brand = brandRepository.save(brand);

        // after save internal brand, sync store for brand
        if (GoodServiceFactory.isSyncType(brand.getSystem())) {
            goodSerFactory.getIntegratedSerByType(brandRequest.getSystem())
                    .syncBrand(brand, brandRequest.getBrandCode());
        }

        return MAPPER.toBrandDTO(brand);
    }

    @Override
    public BrandDTO updateBrand(String id, BrandRequest brandRequest) {
        log.info("update brand: {} to {}", id, brandRequest);
        Brand brandOld = brandRepository.findById(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_BRAND_NOT_FOUND),
                        HttpStatus.BAD_REQUEST
                ));

        if (!brandRequest.getSupplierId().equals(brandOld.getSupplierId()))
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.brand.incorrect.supplier.id"),
                    HttpStatus.BAD_REQUEST);

        if (!brandRequest.getSystem().equals(brandOld.getSystem()))
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.system.type.not.correct"),
                    HttpStatus.BAD_REQUEST);

        if (!StringUtils.isEmpty(brandRequest.getBrandCode())
                && !brandRequest.getBrandCode().equals(brandOld.getBrandCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.brand.incorrect.brand.code"),
                    HttpStatus.BAD_REQUEST);
        }

        validateBrandRequest(brandRequest);

        if(brandRequest.getSystem() == SystemType.INTERNAL) {
            Optional<Brand> brandOptional = brandRepository.findByAppId(brandRequest.getAppId());
            if (brandOptional.isPresent() && !brandOptional.get().getId().equals(id)) {
                throw new CustomCodeException(MessageUtils.getMessage("AppId already exists!"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        // convert to entity
        Brand brand = MAPPER.toBrand(brandRequest);

        // save to database
        brand.setId(id);
        MAPPER.keepOldProperties(brand, brandOld);

        log.info("Save brand to Database with id: {}", id);
        brand = brandRepository.save(brand);

        return MAPPER.toBrandDTO(brand);
    }

    @Override
    @Transactional
    public String deleteBrandById(String id) {
        log.info("Find brand with id: {}", id);
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_BRAND_NOT_FOUND),
                        HttpStatus.BAD_REQUEST
                ));

        brand.setValidYn(EnumValidYn.N);
        log.info("Delete brand with id: {}", id);
        brandRepository.save(brand);

        List<Store> stores = storeRepository.findAllByBrandIdAndValidYn(id, EnumValidYn.Y);
        if (!CollectionUtils.isEmpty(stores)) {
            stores.replaceAll(s -> {
                s.setValidYn(EnumValidYn.N);
                return s;
            });
            log.info("Delete all Store belong brandId: {}", id);
            storeRepository.saveAll(stores);
        }
        return id;
    }

    @Override
    public Page<? extends SearchBrandResponse> searchBrandDTO(FilterSearchCms filterSearchCms) {
        int page = ObjectUtils.isEmpty(filterSearchCms.getPage()) ? 0 : filterSearchCms.getPage() - 1; //pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchCms.getPageSize()) ? 10 : filterSearchCms.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);
        List<? extends SearchBrandResponse> searchBrandResponses = new ArrayList<>();
        log.info("Search Brand: {}", filterSearchCms);
        Long count;
        if (StringUtils.isNotBlank(filterSearchCms.getCategoryCode())) {
            count = brandRepository.countBrandByCategoryAndFilter(filterSearchCms, pageable);
            if (count > 0) {
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();
                searchBrandResponses = Optional.ofNullable(brandRepository.searchBrandByCategoryAndFilter(filterSearchCms, pageable))
                        .orElse(new ArrayList<>())
                        .stream().map(o -> {
                            BrandWithCategorySearchResponse response = MAPPER.toBrandWithCategory((Brand) o[0]);
                            response.setCategoryCode(Constant.gson.fromJson((String) o[1], type));
                            response.setSupplierName((String) o[2]);
                            return response;
                        }).collect(Collectors.toList());
            }
        } else {
            count = brandRepository.countBrand(filterSearchCms);
            if (count > 0) {
                searchBrandResponses = brandRepository.searchBrand(filterSearchCms, pageable);
            }
        }

        return new PageImpl<>(searchBrandResponses, pageable, count);
    }

    public void validateExistByIdIn(Collection<String> ids) throws EntityNotFoundException {
        List<String> existedIds = this.findAllByIdIn(ids).stream().map(Brand::getId).collect(Collectors.toList());
        ids.forEach(o -> {
            if (!existedIds.contains(o)) {
                throw this.getNotFoundException(o);
            }
        });
    }

    @Override
    public BulkBrandDTO toDto(BulkBrand entity) {
        return MAPPER.toDTO(entity);
    }


    private void validateBrandRequest(BrandRequest brandRequest) {
        // check supplier already exists and approved
        log.info("Find Supplier by supplierId: {}", brandRequest.getSupplierId());
        Supplier supplier = supplierRepository.findByIdAndValidYn(brandRequest.getSupplierId(), EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.not.found"),
                        HttpStatus.BAD_REQUEST));
        if (!ApproveStatus.APPRV.equals(supplier.getApproveStatusCode()))
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.not.approved"),
                    HttpStatus.BAD_REQUEST);

        if(brandRequest.getSystem() == SystemType.INTERNAL) {
            if (Objects.isNull(brandRequest.getAppId()) || brandRequest.getAppId().isEmpty()) {
                throw new CustomCodeException(MessageUtils.getMessage("App Id is empty!"),
                        HttpStatus.BAD_REQUEST);
            }
            if (Objects.isNull(brandRequest.getSerialNumberPrefix()) || brandRequest.getSerialNumberPrefix().isEmpty()) {
                throw new CustomCodeException(MessageUtils.getMessage("Serial number prefix is empty!"),
                        HttpStatus.BAD_REQUEST);
            }
            if (Objects.isNull(brandRequest.getSerialNumberTotalLength())) {
                throw new CustomCodeException("Serial number total length is empty!",
                        HttpStatus.BAD_REQUEST);
            }
            if (brandRequest.getSerialNumberTotalLength() - brandRequest.getSerialNumberPrefix().length() < 6) {
                throw new CustomCodeException("Serial number total length is too short",
                        HttpStatus.BAD_REQUEST);
            }
            if (brandRequest.getIpWhiteList() != null && !brandRequest.getIpWhiteList().isEmpty()) {
                Pattern ipPattern = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
                List.of(brandRequest.getIpWhiteList().split(",")).forEach(ip -> {
                    ip = ip.trim();
                    if (!ipPattern.matcher(ip).matches()) {
                        throw new CustomCodeException(String.format("Invalid IP address: %s", ip), HttpStatus.BAD_REQUEST);
                    }
                });
            }
        }
    }

    private String createBrandId(String supplierId) {
        if (Objects.isNull(supplierId)) return null;

        String result = supplierId + Constant.BRAND.BRAND_FIRST;
        log.info("Find list existing brandId of SupplierId: {}", supplierId);
        List<String> brandIds = brandRepository.findAllBrandIdByBrandIdOrderByIdASC(supplierId + CmsConstant.UNDERSCORE_SYMBOL + Constant.Common.REGEX_SEARCH_SYMBOL);

        String idPrefix = supplierId + CmsConstant.UNDERSCORE_SYMBOL;
        return CmsDataUtil.createId(brandIds, idPrefix, Constant.BRAND.BRAND_SIZE_MAX, result);
    }

    private void validatePermissionCreateBrand(String supplierId) {
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        log.info("User create brand: {}", user.toString());

        switch (EnumRole.valueOf(user.getAdminType())) {
            case ROLE_ADMIN:
            case ROLE_OPERATOR:
                break;
            case ROLE_SUPPLIER: {
                if (!supplierId.equals(user.getAdminCorpId())) {
                    throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
                            HttpStatus.UNAUTHORIZED);
                }
                break;
            }
            default:
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
                        HttpStatus.UNAUTHORIZED);
        }
    }

    private void validateExternalBrand(BrandRequest brandRequest) {
        String brandCode = brandRequest.getBrandCode();
        if (StringUtils.isBlank(brandCode)) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.brand.brand.code.empty"),
                    HttpStatus.BAD_REQUEST);
        }
        boolean isExistBrandOfGiftPop = brandRepository.existsByBrandCode(brandCode);
        if (isExistBrandOfGiftPop) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.gift.pop.brand.already.exist", brandCode),
                    HttpStatus.BAD_REQUEST);
        }

        goodSerFactory.getIntegratedSerByType(brandRequest.getSystem())
                .validatePartnerBrand(brandRequest.getSupplierId(), brandCode);


    }

    private static void generatePosKeyAndAuthCode(Brand brand) {
        brand.setAuthenticationKey(AuthDataUtils.generateRandomString(BRAND_AUTH_CODE_LENGTH));
        brand.setEncryptionKey(AuthDataUtils.generateRandomString(BRAND_ENCRYPTOR_BYTE_SIZE));
    }

}

package asia.castis.evoucher.api.service.generator.impl;

import asia.castis.evoucher.api.common.DateUtils;
import asia.castis.evoucher.api.common.enums.EnumValidYn;
import asia.castis.evoucher.api.dto.response.*;
import asia.castis.evoucher.api.entity.*;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.repository.BrandRepository;
import asia.castis.evoucher.api.repository.CategoryRepository;
import asia.castis.evoucher.api.repository.StoreRepository;
import asia.castis.evoucher.api.repository.SupplierRepository;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonGenerator {
    private final SupplierRepository supplierRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    public BrandResponse getBrandResponse(String brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ApplicationException(ResponseString.CAN_NOT_FIND_BRAND, ErrorCode.CAN_NOT_FIND_BRAND));
        return toBrandResponse(brand);
    }

    public SupplierResponse getSupplierResponse(String supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ApplicationException(ResponseString.CAN_NOT_FIND_SUPPLIER, ErrorCode.CAN_NOT_FIND_SUPPLIER));
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getSupplierName())
                .build();
    }

    public List<CategoryResponse> getCategoriesResponse(int goodsId) {
        List<Category> categories = categoryRepository.findByGoodIdAndValidYnIsYes(goodsId);
        return categories.stream().map(this::toCategoryResponse).collect(Collectors.toList());
    }

    public GoodsResponse toGoodsResponse(Goods goods) {
        GoodsResponse simpleGoodsResponse = toSimpleGoodsResponse(goods);
        simpleGoodsResponse.setBrand(getBrandResponse(goods.getBrandId()));
        return simpleGoodsResponse;
    }

    public GoodsResponse toSimpleGoodsResponse(Goods goods) {
        return GoodsResponse.builder()
                .id(goods.getId())
                .name(goods.getGoodsName())
                .listPrice(goods.getListPrice())
                .sellPrice(goods.getSellPrice())
                .usageCount(goods.getUsageCount())
                .isValid(EnumValidYn.Y.equals(goods.getValidYn()))
                .imagePath(goods.getGoodsImgPath())
                .imageName(goods.getGoodsImgName())
                .description(goods.getGoodsDescription())
                .system(Objects.nonNull(goods.getSystem()) ? goods.getSystem().name() : null)
                .type(Objects.nonNull(goods.getGoodsType()) ? goods.getGoodsType().name() : null)
                .startDate(DateUtils.toDateTimeString(goods.getStartDate()))
                .endDate(DateUtils.toDateTimeString(goods.getEndDate()))
                .build();
    }

    public CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getCategoryCode())
                .name(category.getCategoryName())
                .imagePath(category.getImagePath())
                .imageName(category.getImageName())
                .build();
    }

    public BrandResponse toBrandResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getBrandName())
                .description(brand.getDescription())
                .imgUrl(brand.getBrandImagePath())
                .logoUrl(brand.getBrandLogoPath())
                .logoNm(brand.getBrandLogoName())
                .system(Objects.nonNull(brand.getSystem()) ? brand.getSystem().name() : null)
                .isPosLink(EnumValidYn.Y.equals(brand.getIsPosLink()))
                .supplier(Objects.isNull(brand.getSupplierId()) ? null : getSupplierResponse(brand.getSupplierId()))
                .build();
    }

    public LimitedCountHistoryResponse toLcHistoryResponse(VoucherExchangeHistory exchangeHistory) {

        Store store = storeRepository.findById(exchangeHistory.getStoreId())
                .orElseThrow(() -> new ApplicationException(ResponseString.STORE_NOT_FOUND, ErrorCode.STORE_NOT_FOUND));
        return LimitedCountHistoryResponse.builder()
                .transactionDate(DateUtils.toDateTimeString(exchangeHistory.getTransactionDate()))
                .remainingCount(exchangeHistory.getUsageRemainingCount())
                .storeName(store.getStoreName())
                .build();
    }
}

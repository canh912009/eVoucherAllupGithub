package com.evoucher.adminapi.good.service.typed_service.system;

import com.evoucher.adminapi.cms.dao.models.BulkBrand;
import com.evoucher.adminapi.cms.dao.models.BulkCategory;
import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.mapper.GoodsMapper;
import com.evoucher.adminapi.cms.service.BrandService;
import com.evoucher.adminapi.cms.service.CategoryService;
import com.evoucher.adminapi.cms.service.models.BulkBrandDTO;
import com.evoucher.adminapi.cms.service.models.BulkCategoryDTO;
import com.evoucher.adminapi.cms.service.models.BulkGoodDTO;
import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.GoodsType;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.Common;
import com.evoucher.adminapi.good.service.GoodBasicService;
import com.evoucher.adminapi.good.service.GoodService;
import com.evoucher.adminapi.good.service.ParentGoodService;
import com.evoucher.adminapi.good.service.typed_store_service.NoneTypeStoreQueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.evoucher.adminapi.cms.service.GoodsServiceImpl.ErrorCode;

@Service("bulk")
@RequiredArgsConstructor
@Slf4j
public class BulkService implements ParentGoodService, GoodService {
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final GoodBasicService goodBasicService;
    @Getter
    private final NoneTypeStoreQueryService storeQueryService;
    private static final GoodsMapper goodMapper = GoodsMapper.INSTANCE;

    @Override
    public GoodsType getGoodTypeBySystem() {
        return GoodsType.BK;
    }

    @Override
    public void validateChildren(GoodsRequest request) throws CustomCodeException {
        // bulk category must be not empty and valid
        if (!CollectionUtils.isEmpty(request.getBulkCategories())) {
            List<String> categoryIdList = request.getBulkCategories()
                    .stream().map(BulkCategoryDTO::getCategoryCode)
                    .collect(Collectors.toList());

            categoryService.validateExistByIdIn(categoryIdList);
            List<String> brandIdList = request.getBulkCategories().stream()
                    .filter(c -> c.getBulkBrands() != null)
                    .flatMap(b -> b.getBulkBrands().stream())
                    .map(BulkBrandDTO::getBrandId)
                    .collect(Collectors.toList());

            brandService.validateExistByIdIn(brandIdList);

            List<Integer> goodIdList = request.getBulkCategories().stream()
                    .filter(c -> c.getBulkBrands() != null)
                    .flatMap(b -> b.getBulkBrands().stream())
                    .filter(b -> b.getBulkGoods() != null)
                    .flatMap(b -> b.getBulkGoods().stream())
                    .map(g -> g.getGoodsId().intValue())
                    .collect(Collectors.toList());

            goodBasicService.validateExistByIdIn(goodIdList);
        }
    }

    @Override
    public void mapAdditionInfo(GoodsRequest goodsRequest, Goods entity) throws CustomCodeException {
        if (CollectionUtils.isEmpty(goodsRequest.getBulkCategories())) {
            log.error("bulk categories could not be empty");
            throw new CustomCodeException(
                    Common.getErrorMsgByCode(ErrorCode.BULK_CATEGORY_EMPTY),
                    HttpStatus.BAD_REQUEST
            );
        }
        // map bulk category dto to category entity
        sortBulkCategory(goodsRequest);
        entity.setBulkCategories(categoryService.toListEntityBulkCategory(goodsRequest.getBulkCategories()));
    }

    @Override
    public void setGoodToChildren(Goods goods) {
        Optional.ofNullable(goods.getBulkCategories()).orElse(new ArrayList<>())
                .forEach(o -> {
                    o.setParentGoods(goods);
                    Optional.ofNullable(o.getBulkBrands()).orElse(new ArrayList<>())
                            .forEach(b -> {
                                b.setBulkCategory(o);
                                Optional.ofNullable(b.getBulkGoods()).orElse(new HashSet<>() {
                                        })
                                        .forEach(g -> g.setBulkBrand(b));
                            });
                });
    }

    @Override
    public void updateDeletedChildren(Goods oldGood, GoodsRequest newGoods) {
        if (newGoods.getBulkCategories() == null) newGoods.setBulkCategories(new LinkedList<>());
        List<Long> newCategoryId = newGoods.getBulkCategories().stream().map(BulkCategoryDTO::getId).filter(Objects::nonNull).collect(Collectors.toList());
        Map<Long, BulkCategory> deletedMap = oldGood.getBulkCategories().stream().filter(b-> !newCategoryId.contains(b.getId())).collect(Collectors.toMap(BulkCategory::getId, b -> b));
        Map<Long, BulkCategory> updatedMap = oldGood.getBulkCategories().stream().filter(b-> newCategoryId.contains(b.getId())).collect(Collectors.toMap(BulkCategory::getId, b -> b));


        newGoods.getBulkCategories().stream().filter(Objects::nonNull).forEach(b -> {
            if (updatedMap.containsKey(b.getId())) {
                mergeTwoBulkCategory(updatedMap.get(b.getId()), b);
            }
        });

        for (Map.Entry<Long, BulkCategory> entry : deletedMap.entrySet()) {
            BulkCategoryDTO dto = categoryService.toDto(entry.getValue());
            dto.setValidYn(EnumValidYn.N);
            dto.setBulkBrands(new LinkedList<>());
            mergeTwoBulkCategory(entry.getValue(), dto);
            newGoods.getBulkCategories().add(dto);
        }
    }

    @Override
    public void syncStoreOfAllBrand() throws CustomCodeException {
        log.info("trigger sync store for bulk brand");
    }

    private void mergeTwoBulkCategory(BulkCategory oldCategory, BulkCategoryDTO newCategory) {
        if (newCategory.getBulkBrands() == null) newCategory.setBulkBrands(new LinkedList<>());
        List<Long> newBrandIds = newCategory.getBulkBrands().stream().map(BulkBrandDTO::getId).filter(Objects::nonNull).collect(Collectors.toList());
        Map<Long, BulkBrand> deletedMap = oldCategory.getBulkBrands().stream().filter(b-> !newBrandIds.contains(b.getId())).collect(Collectors.toMap(BulkBrand::getId, b -> b));
        Map<Long, BulkBrand> updatedMap = oldCategory.getBulkBrands().stream().filter(b-> newBrandIds.contains(b.getId())).collect(Collectors.toMap(BulkBrand::getId, b -> b));


        newCategory.getBulkBrands().stream().filter(Objects::nonNull).forEach(b -> {
            if (updatedMap.containsKey(b.getId())) {
                mergeTwoBulkBrand(updatedMap.get(b.getId()), b);
            }
        });

        for (Map.Entry<Long, BulkBrand> entry : deletedMap.entrySet()) {
            BulkBrandDTO dto = brandService.toDto(entry.getValue());
            dto.setValidYn(EnumValidYn.N);
            dto.setBulkGoods(new LinkedHashSet<>());
            mergeTwoBulkBrand(entry.getValue(), dto);
            newCategory.getBulkBrands().add(dto);
        }
    }


    private void mergeTwoBulkBrand(BulkBrand oldBrand, BulkBrandDTO newBrand) {
        log.info("merge to good list: {}, {}", oldBrand.getBulkGoods(), newBrand.getBulkGoods());
        if (newBrand.getBulkGoods() == null) {
            newBrand.setBulkGoods(new LinkedHashSet<>());
        }
        List<Long> validGoods = newBrand.getBulkGoods().stream().map(BulkGoodDTO::getId).collect(Collectors.toList());
        oldBrand.getBulkGoods().stream().filter(g -> !validGoods.contains(g.getId())).forEach(g -> {
            BulkGoodDTO dto = goodMapper.toDTO(g);
            dto.setValidYn(EnumValidYn.N);
            newBrand.getBulkGoods().add(dto);
        });
    }

    private void sortBulkCategory(GoodsRequest request) {
        if (CollectionUtils.isEmpty(request.getBulkCategories())) return;
        AtomicInteger displayIndex = new AtomicInteger(1);
        request.getBulkCategories().stream()
                .filter( o -> o.getValidYn() == EnumValidYn.Y)
                .forEach(o -> {
                    log.info("cat - {}: {}", o.getId(), displayIndex.get());
                    o.setDisplayIndex(displayIndex.getAndIncrement());
                    sortBulkBrand(o);
                });
    }

    private void sortBulkBrand(BulkCategoryDTO bulkCategory) {
        if (CollectionUtils.isEmpty(bulkCategory.getBulkBrands())) return;
        AtomicInteger displayIndex = new AtomicInteger(1);
        bulkCategory.getBulkBrands().stream()
                .filter( o -> o.getValidYn() == EnumValidYn.Y)
                .forEach(o -> {
                    log.info("   brand - {}: {}", o.getId(), displayIndex.get());
                    o.setDisplayIndex(displayIndex.getAndIncrement());
                    sortBulkGood(o);
                });
    }

    private void sortBulkGood(BulkBrandDTO bulkBrand) {
        if (CollectionUtils.isEmpty(bulkBrand.getBulkGoods())) return;
        AtomicInteger displayIndex = new AtomicInteger(1);
        bulkBrand.getBulkGoods().stream()
                .filter( o -> o.getValidYn() == EnumValidYn.Y)
                .forEach(o -> {
                    log.info("        good - {}: {}", o.getId(), displayIndex.get());
                    o.setDisplayIndex(displayIndex.getAndIncrement());
                });
    }
}

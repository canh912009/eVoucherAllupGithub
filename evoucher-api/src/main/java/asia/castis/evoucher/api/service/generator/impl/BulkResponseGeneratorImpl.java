package asia.castis.evoucher.api.service.generator.impl;

import asia.castis.evoucher.api.dto.response.BulkBrandResponse;
import asia.castis.evoucher.api.dto.response.BulkCategoryResponse;
import asia.castis.evoucher.api.dto.response.BulkGoodsResponse;
import asia.castis.evoucher.api.dto.response.GoodsResponse;
import asia.castis.evoucher.api.entity.BulkBrand;
import asia.castis.evoucher.api.entity.BulkCategory;
import asia.castis.evoucher.api.entity.BulkGoods;
import asia.castis.evoucher.api.service.generator.BulkResponseGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BulkResponseGeneratorImpl implements BulkResponseGenerator {
    private final CommonGenerator commonGenerator;

    @Override
    public BulkCategoryResponse toBulkCategoryResponse(BulkCategory bulkCategory) {
        return BulkCategoryResponse.builder()
                .bulkCtgrId(bulkCategory.getBulkCtgrId())
                .goodsId(bulkCategory.getGoodsId())
                .category(commonGenerator.toCategoryResponse(bulkCategory.getCategory()))
                .displayIdx(bulkCategory.getDisplayIdx())
                .validYn(Objects.isNull(bulkCategory.getValidYn()) ? null : bulkCategory.getValidYn().name())
                .build();
    }

    @Override
    public BulkBrandResponse toBulkBrandResponse(BulkBrand bulkBrand) {
        return BulkBrandResponse.builder()
                .bulkBrandId(bulkBrand.getBulkBrandId())
                .bulkCtgrId(bulkBrand.getBulkCtgrId())
                .displayIdx(bulkBrand.getDisplayIdx())
                .validYn(Objects.isNull(bulkBrand.getValidYn()) ? null : bulkBrand.getValidYn().name())
                .brand(commonGenerator.toBrandResponse(bulkBrand.getBrand()))
                .build();
    }

    @Override
    public BulkGoodsResponse toBulkGoodsResponse(BulkGoods bulkGoods) {
        GoodsResponse goodResponse = commonGenerator.toSimpleGoodsResponse(bulkGoods.getGoods());


        return BulkGoodsResponse.builder()
                .bulkGoodsId(bulkGoods.getBulkGoodsId())
                .goods(goodResponse)
                .displayIdx(bulkGoods.getDisplayIdx())
                .validYn(Objects.isNull(bulkGoods.getValidYn()) ? null : bulkGoods.getValidYn().name())
                .build();
    }
}

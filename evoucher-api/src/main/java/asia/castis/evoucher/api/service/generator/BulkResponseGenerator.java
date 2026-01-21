package asia.castis.evoucher.api.service.generator;

import asia.castis.evoucher.api.dto.response.BulkBrandResponse;
import asia.castis.evoucher.api.dto.response.BulkCategoryResponse;
import asia.castis.evoucher.api.dto.response.BulkGoodsResponse;
import asia.castis.evoucher.api.entity.BulkBrand;
import asia.castis.evoucher.api.entity.BulkCategory;
import asia.castis.evoucher.api.entity.BulkGoods;

public interface BulkResponseGenerator {
    BulkCategoryResponse toBulkCategoryResponse(BulkCategory bulkCategory);

    BulkBrandResponse toBulkBrandResponse(BulkBrand bulkBrand);

    BulkGoodsResponse toBulkGoodsResponse(BulkGoods bulkGoods);
}

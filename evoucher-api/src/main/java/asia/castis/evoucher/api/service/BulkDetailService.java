package asia.castis.evoucher.api.service;


import asia.castis.evoucher.api.dto.request.bulk.BulkBrandRequest;
import asia.castis.evoucher.api.dto.request.bulk.BulkCategoryRequest;
import asia.castis.evoucher.api.dto.request.bulk.BulkGoodsRequest;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequest;
import asia.castis.evoucher.api.dto.response.BulkBrandResponse;
import asia.castis.evoucher.api.dto.response.BulkCategoryResponse;
import asia.castis.evoucher.api.dto.response.BulkGoodsResponse;
import asia.castis.evoucher.api.dto.response.PageResponse;

public interface BulkDetailService {
    PageResponse<BulkCategoryResponse> getBulkCategories(BulkCategoryRequest request);

    PageResponse<BulkBrandResponse> getBulkBrands(BulkBrandRequest request);

    PageResponse<BulkGoodsResponse> getBulkGoods(BulkGoodsRequest request);
}

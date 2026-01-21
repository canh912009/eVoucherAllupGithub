package asia.castis.evoucher.api.service;


import asia.castis.evoucher.api.dto.request.bulk.BulkBrandRequest;
import asia.castis.evoucher.api.dto.request.bulk.BulkCategoryRequest;
import asia.castis.evoucher.api.dto.request.bulk.BulkGoodsRequest;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequest;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV1;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV2;
import asia.castis.evoucher.api.dto.response.BulkBrandResponse;
import asia.castis.evoucher.api.dto.response.BulkCategoryResponse;
import asia.castis.evoucher.api.dto.response.BulkGoodsResponse;
import asia.castis.evoucher.api.dto.response.PageResponse;

public interface BulkVoucherService<T extends ChosenRequest> {
    void chooseProduct(T chosenRequest);
}

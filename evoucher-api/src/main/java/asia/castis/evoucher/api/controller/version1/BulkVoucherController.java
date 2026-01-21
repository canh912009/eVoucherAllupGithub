package asia.castis.evoucher.api.controller.version1;

import asia.castis.evoucher.api.dto.request.bulk.BulkBrandRequest;
import asia.castis.evoucher.api.dto.request.bulk.BulkCategoryRequest;
import asia.castis.evoucher.api.dto.request.bulk.BulkGoodsRequest;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV1;
import asia.castis.evoucher.api.dto.response.*;
import asia.castis.evoucher.api.service.BulkDetailService;
import asia.castis.evoucher.api.service.BulkVoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/bulk")
@RequiredArgsConstructor
public class BulkVoucherController {

    private final BulkVoucherService<ChosenRequestV1> bulkVoucherService;
    private final BulkDetailService bulkDetailService;

    @PostMapping("/choose")
    public void chooseProduct(@RequestBody ChosenRequestV1 chosenRequest) {
        bulkVoucherService.chooseProduct(chosenRequest);
    }

    @GetMapping("/categories")
    public ResponseData<PageResponse<BulkCategoryResponse>> getBulkCategories(@RequestParam int pageSize, @RequestParam int pageNum,
                                                                              @RequestParam Integer goodsId, @RequestParam(required = false) String name
    ) {
        BulkCategoryRequest request = BulkCategoryRequest.builder()
                .goodsId(goodsId)
                .name(name)
                .pageSize(pageSize)
                .pageNum(pageNum)
                .build();
        return ResponseData.ok(bulkDetailService.getBulkCategories(request));
    }

    @GetMapping("/brands")
    public ResponseData<PageResponse<BulkBrandResponse>> getBulkBrands(@RequestParam int pageSize, @RequestParam int pageNum,
                                                                       @RequestParam Long bulkCategoryId, @RequestParam(required = false) String name
    ) {
        BulkBrandRequest request = BulkBrandRequest.builder()
                .bulkCategoryId(bulkCategoryId)
                .name(name)
                .pageSize(pageSize)
                .pageNum(pageNum)
                .build();
        return ResponseData.ok(bulkDetailService.getBulkBrands(request));
    }

    @GetMapping("/goods")
    public ResponseData<PageResponse<BulkGoodsResponse>> getBulkGoods(@RequestParam int pageSize, @RequestParam int pageNum,
                                                                      @RequestParam Long bulkBrandId, @RequestParam(required = false) String name
    ) {
        BulkGoodsRequest request = BulkGoodsRequest.builder()
                .bulkBrandId(bulkBrandId)
                .name(name)
                .pageSize(pageSize)
                .pageNum(pageNum)
                .build();
        return ResponseData.ok(bulkDetailService.getBulkGoods(request));
    }
}
package asia.castis.evoucher.api.controller;

import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.dto.response.StoreResponse;
import asia.castis.evoucher.api.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    @Autowired
    private StoreService storeService;


    @GetMapping("/search")
    public ResponseData<List<StoreResponse>> searchStoreByGoodsId(@RequestParam Integer goodsId) {
        log.info("Get stores by goodsId {}", goodsId);
        return ResponseData.ok(storeService.searchStoreByGoodsId(goodsId));
    }

    @GetMapping("/findById/{storeId}")
    public ResponseData<StoreResponse> findById(@PathVariable String storeId) {
        return ResponseData.ok(storeService.findById(storeId));
    }
}

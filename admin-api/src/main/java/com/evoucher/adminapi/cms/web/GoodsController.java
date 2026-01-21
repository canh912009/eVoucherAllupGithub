package com.evoucher.adminapi.cms.web;

import com.evoucher.adminapi.cms.service.GoodsService;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.GoodsDTO;
import com.evoucher.adminapi.cms.service.models.SearchGroupResponse;
import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

    final GoodsService goodsService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Integer id) {
        GoodsDTO goodsDTO = goodsService.findById(id);
        return new ResponseEntity<>(new BaseResponse(goodsDTO), HttpStatus.OK);
    }


//    @DeleteMapping("/{id}")
//    public ResponseEntity<BaseResponse> deleteGoods(@PathVariable Integer id) {
//        Integer result = goodsService.delete(id);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }

    @PostMapping("")
    public ResponseEntity<BaseResponse> createGoods(@Valid @RequestBody GoodsRequest goodsRequest) {
        GoodsDTO goodsDTO = goodsService.createGoods(goodsRequest);
        return new ResponseEntity<>(new BaseResponse(goodsDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateGoods(@PathVariable Integer id, @Valid @RequestBody GoodsRequest goodsRequest) {
        GoodsDTO goodsDTO = goodsService.updateGoods(id, goodsRequest);
        return new ResponseEntity<>(new BaseResponse(goodsDTO), HttpStatus.OK);
    }

    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public ResponseEntity<BaseResponse> searchPublish(FilterSearchCms filterSearchCms,
                                                      @PathVariable(name = "page", required = false) Integer page,
                                                      @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<? extends SearchGroupResponse> result = goodsService.searchGoods(filterSearchCms);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = {"/search/ignore-permissions/{page}/{pageSize}"})
    public ResponseEntity<BaseResponse> searchGoodsIgnorePermissions(FilterSearchCms filterSearchCms,
                                                      @PathVariable(name = "page", required = false) Integer page,
                                                      @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<SearchGroupResponse> result = goodsService.searchGoodsIgnorePermissions(filterSearchCms);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(value = "/sync-stores")
    BaseResponse syncStore() {
        goodsService.syncBrandStore();
        return BaseResponse.ok(null);
    }
}

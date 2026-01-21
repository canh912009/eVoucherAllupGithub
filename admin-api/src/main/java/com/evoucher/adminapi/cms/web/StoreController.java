package com.evoucher.adminapi.cms.web;

import com.evoucher.adminapi.cms.service.StoreService;
import com.evoucher.adminapi.cms.service.models.StoreDTO;
import com.evoucher.adminapi.cms.service.models.request.StoreRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable String id) {
        StoreDTO storeDTO = storeService.findById(id);
        return new ResponseEntity<>(new BaseResponse(storeDTO), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createStore(@Valid @RequestBody StoreRequest storeRequest) {
        StoreDTO result = storeService.createStore(storeRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateStore(@PathVariable String id,
                                                    @Valid @RequestBody StoreRequest storeRequest) {
        StoreDTO result = storeService.updateStore(id, storeRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<BaseResponse> deleteStore(@PathVariable String id) {
//        String result = storeService.deleteStoreById(id);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }
}

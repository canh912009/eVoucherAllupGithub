package com.evoucher.adminapi.cms.web;

import com.evoucher.adminapi.admin.service.models.ApproveRequest;
import com.evoucher.adminapi.cms.service.SupplierService;
import com.evoucher.adminapi.cms.service.models.SupplierDTO;
import com.evoucher.adminapi.cms.service.models.request.SupplierRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable String id) {
        SupplierDTO supplierDTO = supplierService.findDtoById(id);
        return new ResponseEntity<>(new BaseResponse(supplierDTO), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createSupplier(@Valid @RequestBody SupplierRequest supplierDTO) {
        SupplierDTO result = supplierService.createSupplier(supplierDTO);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateSupplier(@PathVariable String id,
                                                       @Valid @RequestBody SupplierRequest supplierRequest) {
        SupplierDTO result = supplierService.updateSupplier(id, supplierRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse> updateStatusSupplier(
            @RequestBody ApproveRequest approveRequest,
            @PathVariable("id") String id) {
        String result = supplierService.updateStatusSupplier(id, approveRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<BaseResponse> deleteSupplier(@PathVariable String id) {
//        String result = supplierService.deleteSupplierById(id);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }
}

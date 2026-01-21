package com.evoucher.adminapi.admin.web;

import com.evoucher.adminapi.admin.service.SupplierContractService;
import com.evoucher.adminapi.admin.service.models.*;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supplier-contracts")
public class SupplierContractController {

    private final SupplierContractService supplierContractService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Integer id) {
        SupplierContractDTO supplierContractDTO = supplierContractService.findById(id);
        return new ResponseEntity<>(new BaseResponse(supplierContractDTO), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> createContract(@Valid @RequestBody SupplierContractRequest supplierContractRequest) {
        SupplierContractDTO result = supplierContractService.createContract(supplierContractRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BaseResponse> updatePublish(@PathVariable Integer id,
                                                      @Valid @RequestBody SupplierContractRequest supplierContractRequest) {
        SupplierContractDTO result = supplierContractService.updateContract(id, supplierContractRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<BaseResponse> approveContract(@PathVariable Integer id,
                                                             @Valid @RequestBody ApproveRequest approveRequest) {
        Integer result = supplierContractService.approveStatusContract(id, approveRequest);
        return new ResponseEntity<>(new BaseResponse(id), HttpStatus.OK);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<BaseResponse> deletePublish(@PathVariable Integer id) {
//        Integer result = supplierContractService.deleteContractById(id);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }

    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public ResponseEntity<BaseResponse> searchPublish(FilterSearchAdmin filterSearchAdmin,
                                                      @PathVariable(name = "page", required = false) Integer page,
                                                      @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<FilterSearchSupplierContract> result = supplierContractService.searchContract(filterSearchAdmin);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

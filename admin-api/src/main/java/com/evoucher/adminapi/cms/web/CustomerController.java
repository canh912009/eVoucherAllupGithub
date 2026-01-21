package com.evoucher.adminapi.cms.web;

import com.evoucher.adminapi.admin.service.models.ApproveRequest;
import com.evoucher.adminapi.cms.service.CustomerService;
import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.request.CustomerRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable String id) {
        CustomerDTO customerDTO = customerService.findById(id);
        BaseResponse resp = new BaseResponse(customerDTO);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


//    @DeleteMapping("/{id}")
//    public ResponseEntity<BaseResponse> deleteCustomer(@PathVariable String id) {
//        String result = customerService.delete(id);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }

    @PostMapping("")
    public ResponseEntity<BaseResponse> createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        CustomerDTO customerDTO = customerService.createCustomer(customerRequest);
        return new ResponseEntity<>(new BaseResponse(customerDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateCustomer(@PathVariable("id") String id, @Valid @RequestBody CustomerRequest customerRequest) {
        CustomerDTO result = customerService.updateCustomer(id, customerRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse> updateStatus(@RequestBody ApproveRequest approveRequest, @PathVariable("id") String id) {
        String result = customerService.updateStatusCustomer(id, approveRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public ResponseEntity<BaseResponse> searchPublish(FilterSearchCms filterSearchCms,
                                                      @PathVariable(name = "page", required = false) Integer page,
                                                      @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<CustomerDTO> result = customerService.searchCustomer(filterSearchCms);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

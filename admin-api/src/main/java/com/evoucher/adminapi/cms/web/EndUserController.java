package com.evoucher.adminapi.cms.web;

import com.evoucher.adminapi.admin.service.models.CampaignDTO;
import com.evoucher.adminapi.admin.service.models.FilterSearchAdmin;
import com.evoucher.adminapi.cms.service.EndUserService;
import com.evoucher.adminapi.cms.service.models.EndUserDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.request.EndUserRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class EndUserController {
    final EndUserService endUserService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable String id) {
        EndUserDTO endUserDTO = endUserService.findById(id);
        return new ResponseEntity<>(new BaseResponse(endUserDTO), HttpStatus.OK);
    }


//    @DeleteMapping("/{id}")
//    public ResponseEntity<BaseResponse> deleteEndUser(@PathVariable String id) {
//        String result = endUserService.deleteEndUserById(id);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }
//
//    @PostMapping("")
//    public ResponseEntity<BaseResponse> createEndUser(@Valid @RequestBody EndUserRequest endUserRequest) {
//        EndUserDTO result = endUserService.createEndUser(endUserRequest);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<BaseResponse> updateEndUser(@PathVariable String id,@Valid @RequestBody EndUserRequest endUserRequest) {
//        EndUserDTO result = endUserService.updateEndUser(id,endUserRequest);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }

    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public ResponseEntity<BaseResponse> searchCampaign(FilterSearchCms filterSearchCms,
                                                       @PathVariable(name = "page", required = false) Integer page,
                                                       @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<EndUserDTO> result = endUserService.search(filterSearchCms);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

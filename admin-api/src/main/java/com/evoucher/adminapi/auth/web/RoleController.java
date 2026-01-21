package com.evoucher.adminapi.auth.web;

import com.evoucher.adminapi.auth.service.RoleService;
import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.RoleDTO;
import com.evoucher.adminapi.auth.service.models.RoleRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;


    @GetMapping("/{roleCode}")
    public ResponseEntity<BaseResponse> findById(@PathVariable String roleCode) {
        RoleDTO role = roleService.findById(roleCode);
        return new ResponseEntity<>(new BaseResponse(role), HttpStatus.OK);
    }

//    @PostMapping()
//    public ResponseEntity<BaseResponse> createRole(@Valid @RequestBody RoleRequest roleRequest) {
//        RoleDTO result = roleService.createRole(roleRequest);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }
//
//    @PutMapping(value = "/{roleCode}")
//    public ResponseEntity<BaseResponse> updateRole(@PathVariable String roleCode,
//                                                      @Valid @RequestBody RoleRequest roleRequest) {
//        RoleDTO result = roleService.updateRole(roleCode, roleRequest);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{roleCode}")
//    public ResponseEntity<BaseResponse> deleteRole(@PathVariable String roleCode) {
//        String result = roleService.deleteRole(roleCode);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }

    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public ResponseEntity<BaseResponse> searchRole(FilterSearchAuth filterSearchAuth,
                                                      @PathVariable(name = "page", required = false) Integer page,
                                                      @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<RoleDTO> result = roleService.searchRole(filterSearchAuth);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

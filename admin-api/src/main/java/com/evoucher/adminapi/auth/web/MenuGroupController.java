package com.evoucher.adminapi.auth.web;

import com.evoucher.adminapi.auth.service.MenuGroupService;
import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.MenuGroupDTO;
import com.evoucher.adminapi.auth.service.models.MenuGroupRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/menu-groups")
@RequiredArgsConstructor
public class MenuGroupController {

    private final MenuGroupService menuGroupService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Integer id) {
        MenuGroupDTO menuGroupDTO = menuGroupService.findById(id);

        return new ResponseEntity<>(new BaseResponse(menuGroupDTO), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> createMenuGroup(@Valid @RequestBody MenuGroupRequest menuRequest) {
        MenuGroupDTO result = menuGroupService.createMenuGroup(menuRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BaseResponse> updateMenuGroup(@PathVariable Integer id,
                                                   @Valid @RequestBody MenuGroupRequest menuRequest) {
        MenuGroupDTO result = menuGroupService.editMenuGroup(id, menuRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteMenuGroup(@PathVariable Integer id) {
        Integer result = menuGroupService.deleteMenuGroup(id);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public ResponseEntity<BaseResponse> searchMenuGroup(FilterSearchAuth filterSearchAuth,
                                                      @PathVariable(name = "page", required = false) Integer page,
                                                      @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<MenuGroupDTO> result = menuGroupService.searchMenuGroup(filterSearchAuth);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

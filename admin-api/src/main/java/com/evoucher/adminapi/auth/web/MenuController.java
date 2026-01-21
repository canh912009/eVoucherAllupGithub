package com.evoucher.adminapi.auth.web;

import com.evoucher.adminapi.auth.service.MenuService;
import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.MenuDTO;
import com.evoucher.adminapi.auth.service.models.MenuRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Integer id) {
        MenuDTO menuDTO = menuService.findById(id);

        return new ResponseEntity<>(new BaseResponse(menuDTO), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> createMenu(@Valid @RequestBody MenuRequest menuRequest) {
        MenuDTO result = menuService.createMenu(menuRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BaseResponse> updateMenu(@PathVariable Integer id,
                                                   @Valid @RequestBody MenuRequest menuRequest) {
        MenuDTO result = menuService.editMenu(id, menuRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteMenu(@PathVariable Integer id) {
        Integer result = menuService.deleteMenu(id);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public ResponseEntity<BaseResponse> searchMenu(FilterSearchAuth filterSearchAuth,
                                                      @PathVariable(name = "page", required = false) Integer page,
                                                      @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<MenuDTO> result = menuService.searchMenu(filterSearchAuth);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

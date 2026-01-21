package com.evoucher.adminapi.auth.web;

import com.evoucher.adminapi.auth.service.CodeGroupService;
import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.CodeGroupDTO;
import com.evoucher.adminapi.auth.service.models.CodeGroupRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/code-groups")
@RequiredArgsConstructor
public class CodeGroupController {

    private final CodeGroupService codeGroupService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable String id) {
        CodeGroupDTO codeGroupDTO = codeGroupService.findById(id);

        return new ResponseEntity<>(new BaseResponse(codeGroupDTO), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createCodeGroup(@Valid @RequestBody CodeGroupRequest codeRequest) {
        CodeGroupDTO result = codeGroupService.createCodeGroup(codeRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BaseResponse> updateCodeGroup(@PathVariable String id,
                                                   @Valid @RequestBody CodeGroupRequest codeRequest) {
        CodeGroupDTO result = codeGroupService.editCodeGroup(id, codeRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteCodeGroup(@PathVariable String id) {
        String result = codeGroupService.deleteCodeGroup(id);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public ResponseEntity<BaseResponse> searchCodeGroup(FilterSearchAuth filterSearchAuth,
                                                      @PathVariable(name = "page", required = false) Integer page,
                                                      @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<CodeGroupDTO> result = codeGroupService.searchCodeGroup(filterSearchAuth);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

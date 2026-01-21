package com.evoucher.adminapi.auth.web;

import com.evoucher.adminapi.auth.service.CodeService;
import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.CodeDTO;
import com.evoucher.adminapi.auth.service.models.CodeRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/codes")
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    @GetMapping("/{codeGroupId}/{codeId}")
    public ResponseEntity<BaseResponse> findById(
            @PathVariable String codeGroupId,
            @PathVariable String codeId) {
        CodeDTO codeDTO = codeService.findById(codeId, codeGroupId);

        return new ResponseEntity<>(new BaseResponse(codeDTO), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> createCode(@Valid @RequestBody CodeRequest codeRequest) {
        CodeDTO result = codeService.createCode(codeRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PutMapping(value = "/{codeGroupId}/{codeId}")
    public ResponseEntity<BaseResponse> updateCode(
            @PathVariable String codeId,
            @PathVariable String codeGroupId,
            @Valid @RequestBody CodeRequest codeRequest) {
        CodeDTO result = codeService.editCode(codeId, codeGroupId, codeRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @DeleteMapping("/{codeGroupId}/{codeId}")
    public ResponseEntity<BaseResponse> deleteCode(
            @PathVariable String codeId,
            @PathVariable String codeGroupId) {
        String result = codeService.deleteCode(codeId, codeGroupId);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public ResponseEntity<BaseResponse> searchCode(FilterSearchAuth filterSearchAuth,
                                                      @PathVariable(name = "page", required = false) Integer page,
                                                      @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<CodeDTO> result = codeService.searchCode(filterSearchAuth);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

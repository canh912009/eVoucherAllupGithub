package com.evoucher.adminapi.cms.web;

import com.evoucher.adminapi.cms.service.ExternalPinUploadService;
import com.evoucher.adminapi.cms.service.models.ExternalPinUploadDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.request.ExternalPinUploadRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external-pin-upload")
public class ExternalPinUploadController {

    private final ExternalPinUploadService service;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Integer id) {
        ExternalPinUploadDTO result = service.findExternalPinUploadById(id);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createBrandDTO(
            @Valid @RequestBody ExternalPinUploadRequest externalPinUploadRequest) {
        ExternalPinUploadDTO result = service.createExternalPinUpload(externalPinUploadRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @GetMapping(value = {"/search/{page}/{pageSize}"})
    public ResponseEntity<BaseResponse> searchPublish(FilterSearchCms filterSearchCms,
                                                      @PathVariable(name = "page", required = false) Integer page,
                                                      @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<ExternalPinUploadDTO> result = service.searchExternalPinUpload(filterSearchCms);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

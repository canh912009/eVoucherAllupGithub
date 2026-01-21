package com.evoucher.externalserviceapi.api;

import com.evoucher.externalserviceapi.common.exception.ClientNotLoggedInException;
import com.evoucher.externalserviceapi.service.model.request.ExternalPinUploadRequest;
import com.evoucher.externalserviceapi.service.remote.ExternalPinRemoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("external-pin-upload")
public class ExternalPinController {

    private final ExternalPinRemoteService externalPinRemoteService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) throws ClientNotLoggedInException {
        log.info("Find External PIN upload by id: {}", id);
        Object result = externalPinRemoteService.findExternalPinUploadById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createBrandDTO(
            @Valid @RequestBody ExternalPinUploadRequest externalPinUploadRequest)
            throws ClientNotLoggedInException {
        log.info("Create External PIN upload with request: {}", externalPinUploadRequest);
        Object result = externalPinRemoteService.createExternalPinUpload(externalPinUploadRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = {"/search/{page}/{pageSize}"})
    public ResponseEntity<Object> searchPublish(@PathVariable(name = "page") Integer page,
                                                @PathVariable(name = "pageSize") Integer pageSize,
                                                @RequestParam(name = "goodsId") Integer goodsId)
            throws ClientNotLoggedInException {
        log.info("Search list External PIN with goodsId: {}", goodsId);
        Object result = externalPinRemoteService.searchExternalPinUpload(page, pageSize, goodsId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

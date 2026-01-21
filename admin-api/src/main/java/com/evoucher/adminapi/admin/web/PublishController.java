package com.evoucher.adminapi.admin.web;

import com.evoucher.adminapi.admin.service.PublishService;
import com.evoucher.adminapi.admin.service.models.*;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/publishes")
public class PublishController {

    private final PublishService publishService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Integer id) {
        PublishDTO publishDTO = publishService.findById(id);
        return new ResponseEntity<>(new BaseResponse(publishDTO), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createPublish(@Valid @RequestBody PublishRequest publishRequest) {
        log.info("Start create publish");
        PublishDTO result = publishService.createPublish(publishRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BaseResponse> updatePublish(@PathVariable Integer id,
                                                       @Valid @RequestBody PublishRequest publishRequest) {
        log.info("Start update publish");
        PublishDTO result = publishService.updatePublish(id, publishRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<BaseResponse> deletePublish(@PathVariable Integer id) {
//        Integer result = publishService.deletePublishById(id);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse> updateStatusPublish(
            @PathVariable Integer id,
            @RequestBody ApproveRequest approveRequest) {
        log.info("Update status Publish with publishId: {} and ApproveRequest: {}", id, approveRequest.toString());
        Integer result = publishService.updateStatusPublish(id, approveRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public ResponseEntity<BaseResponse> searchPublish(FilterSearchAdmin filterSearchAdmin,
                                                       @PathVariable(name = "page", required = false) Integer page,
                                                       @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<SearchPublishResponse> result = publishService.searchPublish(filterSearchAdmin);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

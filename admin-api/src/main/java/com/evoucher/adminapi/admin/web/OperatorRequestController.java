package com.evoucher.adminapi.admin.web;

import com.evoucher.adminapi.admin.service.OperatorLogicalService;
import com.evoucher.adminapi.admin.service.OperatorRequestBasicService;
import com.evoucher.adminapi.admin.service.models.FilterSearchAdmin;
import com.evoucher.adminapi.admin.service.models.OperatorRequestDto;
import com.evoucher.adminapi.admin.service.models.SearchPublishResponse;
import com.evoucher.adminapi.admin.service.models.search_response.OperatorSearchRes;
import com.evoucher.adminapi.admin.validator_group.ApprovingGroup;
import com.evoucher.adminapi.admin.validator_group.CreatingGroup;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/operator-request")
public class OperatorRequestController {
    private final OperatorLogicalService service;

    @PostMapping
    public BaseResponse createNew(@RequestBody OperatorRequestDto request) {
        return service.createNew(request);
    }
    @GetMapping("/ev/{ev}")
    public BaseResponse findByEv(@PathVariable String ev) {
        return service.findRequestByEv(ev);
    }
    @PatchMapping
    public BaseResponse approve(@RequestBody @Validated(ApprovingGroup.class) OperatorRequestDto approveRequest) {
        return service.approveRequest(approveRequest);
    }

    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public BaseResponse searchPublish(FilterSearchAdmin filterSearchAdmin,
                                                      @PathVariable(name = "page", required = false) Integer page,
                                                      @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        return service.searchOperatorRequest(filterSearchAdmin, pageSize, page);
    }
    
}

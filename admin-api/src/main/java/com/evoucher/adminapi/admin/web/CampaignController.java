package com.evoucher.adminapi.admin.web;

import com.evoucher.adminapi.admin.service.models.*;
import com.evoucher.adminapi.common.message.BaseResponse;
import com.evoucher.adminapi.admin.service.CampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
@Slf4j
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Integer id) {
        CampaignDTO campaignDTO = campaignService.findById(id);
        return new ResponseEntity<>(new BaseResponse(campaignDTO), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createCampaign(@Valid @RequestBody CampaignRequest campaignRequest) {
        CampaignDTO campaignDTO = campaignService.createCampaign(campaignRequest);
        return new ResponseEntity<>(new BaseResponse(campaignDTO), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BaseResponse> updateCampaign(@PathVariable Integer id,
                                                       @Valid @RequestBody CampaignRequest campaignRequest) {
        CampaignDTO result = campaignService.updateCampaign(id, campaignRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<BaseResponse> deleteCampaign(@PathVariable Integer id) {
//        Integer result = campaignService.deleteCampaignById(id);
//        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
//    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse> updateStatusCampaign(
            @PathVariable Integer id,
            @RequestBody ApproveRequest approveRequest) {
        log.info("Update status Campaign with campaignId: {} and ApproveRequest: {}", id, approveRequest.toString());
        Integer result = campaignService.updateStatusCampaign(id, approveRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @GetMapping(value = {"/search","/search/{page}/{pageSize}", })
    public ResponseEntity<BaseResponse> searchCampaign(FilterSearchAdmin filterSearchAdmin,
                                                       @PathVariable(name = "page", required = false) Integer page,
                                                       @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<SearchCampaignResponse> result = campaignService.searchCampaign(filterSearchAdmin);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

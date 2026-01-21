package com.castis.publishservice.client;

import com.castis.publishservice.config.ClientConfiguration;
import com.castis.publishservice.dto.request.CreateChoiceItemRequest;
import com.castis.publishservice.dto.request.EvoucherRequest;
import com.castis.publishservice.dto.request.VoucherHandoverBERequest;
import com.castis.publishservice.dto.response.BaseResponse;
import com.castis.publishservice.dto.response.HandoverResponse;
import com.castis.publishservice.dto.response.VoucherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "jplaceholder", url = "${e-voucher.url.service-be}", configuration = ClientConfiguration.class)
public interface BeServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "/create", produces = "application/json")
    VoucherResponse createEvouchers(@RequestBody EvoucherRequest request);

    @RequestMapping(method = RequestMethod.POST, value = "/create/handover")
    HandoverResponse createNewVoucherForHandOver(@RequestBody VoucherHandoverBERequest request);

    @PostMapping("/revert")
    void revertVoucher(List<String> evs);

    @PostMapping("/create/choice-voucher")
    BaseResponse createChoiceItem(@RequestBody CreateChoiceItemRequest request);
}

package asia.castis.evoucher.api.controller;

import asia.castis.evoucher.api.dto.request.vnpt.VnptPurchaseRequest;
import asia.castis.evoucher.api.service.VnptVoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/vnpt")
@RequiredArgsConstructor
public class VnptVoucherController {

    private final VnptVoucherService vnptVoucherService;

    @PostMapping("/purchase")
    public void purchase(@RequestBody VnptPurchaseRequest purchaseRequest) {
        vnptVoucherService.purchase(purchaseRequest);
    }
}
package asia.castis.evoucher.api.controller.version2;

import asia.castis.evoucher.api.dto.request.vnpt.VnptPurchaseRequestV2;
import asia.castis.evoucher.api.service.version2.VnptVoucherServiceV2;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static asia.castis.evoucher.api.common.Constant.gson;

@Slf4j
@RestController
@RequestMapping("/v2/vnpt")
@RequiredArgsConstructor
public class VnptVoucherControllerV2 {

    private final VnptVoucherServiceV2 vnptVoucherService;

    @PostMapping("/purchase")
    public void purchase(@RequestBody VnptPurchaseRequestV2 purchaseRequest) {
        log.info("Purchase called with purchaseRequest: {}", gson.toJson(purchaseRequest));
        vnptVoucherService.purchase(purchaseRequest);
    }
}
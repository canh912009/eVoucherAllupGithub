package asia.castis.web_hook.controller;

import asia.castis.web_hook.bean.dto.request.GiftPopRequest;
import asia.castis.web_hook.bean.dto.request.WataneHeaders;
import asia.castis.web_hook.bean.dto.request.WataneRequest;
import asia.castis.web_hook.bean.dto.response.BaseResponse;
import asia.castis.web_hook.bean.dto.response.WataneResponse;
import asia.castis.web_hook.exception.defined.RelatedServiceHandlingException;
import asia.castis.web_hook.serivce.VoucherStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class VoucherStatusController {
    private final VoucherStatusService service;
    @PostMapping("/giftpop")
    public BaseResponse processUsingVoucher(@RequestBody GiftPopRequest requestBody, HttpServletRequest request) throws RelatedServiceHandlingException {
        return service.processUsingGiftPopVoucher(requestBody, request.getRequestURI());
    }

    @PutMapping("/ur_box")
    public BaseResponse processUsingVoucher(@RequestParam List<String> vouchers) {
        return service.updateUrBoxVouchersStatus(vouchers);
    }

    @PostMapping("/ur_box")
    public void processUrBoxUsingVoucher() {
        service.updateVoucherStatus();
    }

    @PostMapping("/watane")
    public ResponseEntity<WataneResponse> processWataneVoucher(
            @RequestBody WataneRequest requestBody,
            @RequestHeader Map<String, String> headers,
            HttpServletRequest request) {

        WataneHeaders wataneHeaders = new WataneHeaders(
                headers.get("username"),
                headers.get("credential"),
                headers.get("signature")
        );

        return service.processWataneRequest(requestBody, wataneHeaders, request.getRequestURI());
    }
}
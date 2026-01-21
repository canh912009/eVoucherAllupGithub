package asia.castis.evoucher.api.controller;

import asia.castis.evoucher.api.common.Encryption;
import asia.castis.evoucher.api.dto.request.PreCheckRequest;
import asia.castis.evoucher.api.dto.response.PreCheckResponse;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.service.PreCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static asia.castis.evoucher.api.common.Constant.gson;

@RestController
@CrossOrigin("*")
@RequestMapping("/preCheck")
@RequiredArgsConstructor
@Slf4j
public class PreCheckController {
    private final PreCheckService preCheckService;

    /***
     * Pre-check voucher
     * <pre>
     * input:
     * {
     *     "ev": "33a479a9-3826-447b-95e2-1c434ba2ba48"
     * }
     * </pre>
     *
     * <pre>
     * output:
     * {
     *     "code": 200,
     *     "message": "OK",
     *     "data": {
     *         "version": "version_1",
     *         "activated": true,
     *         "phoneNumber": "0000000000",
     *         "otpRequired": false
     *     }
     * }
     * </pre>
     */
    @PostMapping("")
    public ResponseData<PreCheckResponse> preCheck(@Valid @RequestBody @NotNull PreCheckRequest request) {
        log.info("PreCheck request shortLink key={}", request.getVoucherId());
        PreCheckResponse preCheckResponse = preCheckService.voucherPreCheck(request.getVoucherId());
        log.info("PreCheck response for {}={}", request, preCheckResponse);
        return ResponseData.ok(preCheckResponse);
    }
}

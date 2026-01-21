package asia.castis.evoucher.api.controller.version2;

import asia.castis.evoucher.api.dto.request.choose.ChoiceChosenRequestV1;
import asia.castis.evoucher.api.dto.request.choose.ChoiceChosenRequestV2;
import asia.castis.evoucher.api.dto.response.*;
import asia.castis.evoucher.api.service.BulkVoucherService;
import asia.castis.evoucher.api.service.ChoiceVoucherService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static asia.castis.evoucher.api.common.Constant.gson;

@Slf4j
@RestController
@RequestMapping("/v2/choice")
@RequiredArgsConstructor
public class ChoiceVoucherControllerV2 {

    private final ChoiceVoucherService<ChoiceChosenRequestV2> choiceVoucherService;

    @PostMapping("/choose")
    public ResponseData<String> chooseChoiceVoucher(@RequestBody ChoiceChosenRequestV2 choiceRequest) {
        log.info("ChoiceVoucherControllerV2.chooseChoiceVoucher called with choiceRequest: {}", gson.toJson(choiceRequest));
        choiceVoucherService.chooseChoiceVoucher(choiceRequest);
        return ResponseData.ok();
    }
}
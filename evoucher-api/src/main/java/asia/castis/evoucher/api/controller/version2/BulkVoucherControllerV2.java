package asia.castis.evoucher.api.controller.version2;

import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV2;
import asia.castis.evoucher.api.service.BulkVoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static asia.castis.evoucher.api.common.Constant.gson;

@Slf4j
@RestController
@RequestMapping("/v2/bulk")
@RequiredArgsConstructor
public class BulkVoucherControllerV2 {

    private final BulkVoucherService<ChosenRequestV2> bulkVoucherService;

    @PostMapping("/choose")
    public void chooseProduct(@Valid @RequestBody ChosenRequestV2 chosenRequest) {
        log.info("BulkVoucherControllerV2.chooseProduct called with chosenRequest: {}", gson.toJson(chosenRequest));
        bulkVoucherService.chooseProduct(chosenRequest);
    }
}
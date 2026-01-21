package asia.castis.evoucher.api.controller;

import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.dto.response.VoucherResponseWrapper;
import asia.castis.evoucher.api.service.WebViewerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/viewer")
@RequiredArgsConstructor
public class ViewerController {
    private final WebViewerService webViewerService;

    @GetMapping("/{id}")
    public ResponseData<VoucherResponseWrapper> getVoucherInfo(@PathVariable String id) {
        return ResponseData.ok(webViewerService.getVoucher(id));
    }
}

package asia.castis.evoucher.api.controller;

import asia.castis.evoucher.api.dto.response.PublishDetails;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.service.PublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/publish")
@RequiredArgsConstructor
public class PublishController {
    private final PublishService publishService;

    @GetMapping("/publishDetails")
    public ResponseData<PublishDetails> publishDetails(@RequestParam(value = "activationKey") String activationKey) {
        PublishDetails response = publishService.publishByActivationKey(activationKey);
        return ResponseData.ok(response);
    }
}

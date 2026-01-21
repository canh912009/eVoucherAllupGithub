package asia.castis.evoucherservicefe.controller;

import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.RequestFromBE;
import asia.castis.evoucherservicefe.common.dto.response.ResponseData;
import asia.castis.evoucherservicefe.exceptions.*;
import asia.castis.evoucherservicefe.publishrequest.dto.ChosenRequest;
import asia.castis.evoucherservicefe.publishrequest.service.PublishRequestService;
import asia.castis.evoucherservicefe.voucherhandler.dto.ActivateRequest;
import asia.castis.evoucherservicefe.voucherhandler.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voucher")
public class VoucherController {
    private final VoucherService service;
    private final PublishRequestService publishRequestService;


    @PostMapping("/choose-choice")
    public ResponseData<List<String>> chooseChoiceItem(@RequestBody ChosenRequest choiceRequest) {
        return service.chooseChoiceItem(choiceRequest);
    }

    @PostMapping("/activate")
    public ResponseEntity<ResponseData<String>> activateVoucher(@RequestBody @Valid ActivateRequest activateRequest) throws Exception {
        service.activateVoucher(activateRequest);
        return ResponseEntity.ok(ResponseData.ok());
    }

    @PostMapping("/publish")
    public ResponseData<String> sendMail(@RequestBody RequestFromBE requestFromBE) throws CreateMessageException, InvalidException,
            NotFoundException, IOException, RetryJobException, DecryptException, SendMessageToQueueException {
        publishRequestService.incomingPublishHandling(requestFromBE, new Date());
        return ResponseData.ok();
    }


}

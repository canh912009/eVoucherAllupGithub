package com.evoucher.evoucherbe.api;

import com.evoucher.evoucherbe.dto.EVoucherHistoryProcess;
import com.evoucher.evoucherbe.dto.EVoucherTransferProcess;
import com.evoucher.evoucherbe.dto.VoucherDisableProcessResponse;
import com.evoucher.evoucherbe.dto.VoucherJobResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voucher-trigger")
@RequiredArgsConstructor
public class EVoucherTriggerController {

    private final RabbitTemplate rabbitTemplate;

    @Value("${queues.used_voucher.queue}")
    private String queueUsedVoucher;

    @Value("${queues.receive_voucher.queue}")
    private String queueReceiveVoucher;
    @Value("${queues.activate_voucher.queue}")
    private String queueActivateVoucher;

    @Value("${queues.update_voucher.queue}")
    private String queueUpdateVoucher;

    @Value("${queues.disable_voucher_result.queue}")
    private String queueDisableVoucherResult;

    @PostMapping("/used-voucher")
    public ResponseEntity<Object> usedVoucher(@RequestBody EVoucherHistoryProcess eVoucherHistoryProcess) {
        rabbitTemplate.convertAndSend(queueUsedVoucher, eVoucherHistoryProcess);
        return ResponseEntity.ok("");
    }

    @PostMapping("/transfer-voucher")
    public ResponseEntity<Object> transferVoucher(@RequestBody EVoucherTransferProcess voucherTransferProcess) {
        rabbitTemplate.convertAndSend(queueReceiveVoucher, voucherTransferProcess);
        return ResponseEntity.ok("");
    }

    @PostMapping("/activate-voucher")
    public ResponseEntity<Object> activateVoucher(@RequestBody EVoucherTransferProcess voucherTransferProcess) {
        rabbitTemplate.convertAndSend(queueActivateVoucher, voucherTransferProcess);
        return ResponseEntity.ok("");
    }

    @PostMapping("/update-voucher")
    public ResponseEntity<Object> updateVoucher(@RequestBody VoucherJobResultDTO voucherJobResultDTO) {
        rabbitTemplate.convertAndSend(queueUpdateVoucher, voucherJobResultDTO);
        return ResponseEntity.ok("");
    }

    @PostMapping("/disable-voucher-result")
    public ResponseEntity<Object> disableVoucherResult(
            @RequestBody VoucherDisableProcessResponse response) {
        rabbitTemplate.convertAndSend(queueDisableVoucherResult, response);
        return ResponseEntity.ok("");
    }
}

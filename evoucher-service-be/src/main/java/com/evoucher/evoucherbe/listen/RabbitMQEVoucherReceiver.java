package com.evoucher.evoucherbe.listen;

import com.evoucher.evoucherbe.dto.*;
import com.evoucher.evoucherbe.service.EVoucherProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQEVoucherReceiver {

    private final EVoucherProcessService eVoucherProcessService;

    /**
     * Receive message voucher EXPIRE and Update voucher EXPIRE
     * @param voucherJobResultDTO
     */
    @RabbitListener(queues = {"${queues.update_voucher.queue}"})
    public void listenOnQueueUpdateVoucher(VoucherJobResultDTO voucherJobResultDTO) {
        log.info("Processing update voucher: {}", voucherJobResultDTO);
        eVoucherProcessService.processUpdateVoucher(voucherJobResultDTO);
    }
}

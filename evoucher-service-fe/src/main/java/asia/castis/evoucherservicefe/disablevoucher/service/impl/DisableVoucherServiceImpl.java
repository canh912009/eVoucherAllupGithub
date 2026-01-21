package asia.castis.evoucherservicefe.disablevoucher.service.impl;

import asia.castis.evoucherservicefe.common.enums.EnumDisableVoucherResult;
import asia.castis.evoucherservicefe.common.enums.EnumVoucherStatus;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.common.service.EsVoucherService;
import asia.castis.evoucherservicefe.disablevoucher.dto.VoucherDisableProcessRequest;
import asia.castis.evoucherservicefe.disablevoucher.dto.VoucherDisableProcessResponse;
import asia.castis.evoucherservicefe.disablevoucher.sennder.DisableResultSender;
import asia.castis.evoucherservicefe.disablevoucher.service.DisableVoucherService;
import asia.castis.evoucherservicefe.exceptions.InvalidException;
import asia.castis.evoucherservicefe.exceptions.NotFoundException;
import asia.castis.evoucherservicefe.exceptions.SendMessageToQueueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class DisableVoucherServiceImpl implements DisableVoucherService {

    EsVoucherService esVoucherService;
    DisableResultSender sender;

    @Autowired
    public DisableVoucherServiceImpl(EsVoucherService esVoucherService, DisableResultSender sender) {
        this.esVoucherService = esVoucherService;
        this.sender = sender;
    }

    @Override
    public void disableVoucher(VoucherDisableProcessRequest payload, LocalDateTime currentDate) {
        // 0. Validate voucher
        boolean isValid = true;
        EnumVoucherStatus prevStatus = null;
        VoucherModel voucher;
        try {
            voucher = validateRequestAndRetrieveVoucher(payload);
            prevStatus = voucher.getVoucherStatus();
            // 1. Disable current voucher
            esVoucherService.updateVoucherStatus(payload.getEv(), EnumVoucherStatus.DISABLED);
            log.info("Voucher is disabled successfully, ev={}", payload.getEv());
        } catch (NotFoundException | InvalidException e) {
            isValid = false;
            log.error(e.getMessage());
        } catch (Exception e) {
            isValid = false;
            log.error(e.getMessage(), e);
        } finally {
            // 2. Send voucher disable result
            VoucherDisableProcessResponse response = new VoucherDisableProcessResponse();
            response.setEv(payload.getEv());
            response.setVoucherDisableHistoryId(payload.getVoucherDisableHistoryId());
            response.setFrontEndPreviousStatusCode(prevStatus);
            if (isValid) {
                // 2.1. Send success result
                response.setFrontEndUpdateResult(EnumDisableVoucherResult.SUCCESS);
            } else {
                // 2.2. Send error result
                response.setFrontEndUpdateResult(EnumDisableVoucherResult.FAILED);
            }
            try {
                sender.sendDisableVoucherResult(response);
            } catch (SendMessageToQueueException e) {
                log.error("Error while sending response to queue", e);
            }
        }
    }

    private VoucherModel validateRequestAndRetrieveVoucher(VoucherDisableProcessRequest req) throws NotFoundException, InvalidException {
        if (req.getEv() == null || req.getEv().isEmpty()) {
           throw new InvalidException("Voucher ev is null or empty");
        }
        if (req.getVoucherDisableHistoryId() == null || req.getVoucherDisableHistoryId() <= 0) {
            throw new InvalidException(String.format("Voucher disable history is null or invalid, id=%d", req.getVoucherDisableHistoryId()));
        }
        // If voucher not found -> error
        VoucherModel voucher;
        try {
            voucher = esVoucherService.findById(req.getEv());
        } catch (NotFoundException notFoundException) {
            throw new NotFoundException(String.format("%s, ev=%s", notFoundException.getMessage(), req), notFoundException);
        }
        // If voucher status == DISABLED || EXPIRE || USED -> error
        if (voucher.getVoucherStatus() == null
                || EnumVoucherStatus.DISABLED == voucher.getVoucherStatus()
                || EnumVoucherStatus.EXPIRE == voucher.getVoucherStatus()
                || EnumVoucherStatus.USED == voucher.getVoucherStatus()
        ) {
            throw new InvalidException(String.format("Voucher status is invalid, ev=%s, status=%s", req.getEv(), voucher.getVoucherStatus()));
        }
        // Else OK
        return voucher;
    }
}

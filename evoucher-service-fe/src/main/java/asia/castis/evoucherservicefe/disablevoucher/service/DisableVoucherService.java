package asia.castis.evoucherservicefe.disablevoucher.service;

import asia.castis.evoucherservicefe.disablevoucher.dto.VoucherDisableProcessRequest;

import java.time.LocalDateTime;

public interface DisableVoucherService {
    void disableVoucher(VoucherDisableProcessRequest payload, LocalDateTime currentDate);
}

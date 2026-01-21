package asia.castis.evoucher.api.service;

import asia.castis.evoucher.api.dto.response.PreCheckResponse;

public interface PreCheckService {
    PreCheckResponse voucherPreCheck(String ev);
}

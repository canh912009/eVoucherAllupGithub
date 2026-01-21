package asia.castis.evoucher.api.service;


import asia.castis.evoucher.api.dto.response.LimitedCountHistoryResponse;

import java.util.List;

public interface LimitedCountVoucherService {
    List<LimitedCountHistoryResponse> getHistory(String ev);
}

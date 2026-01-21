package asia.castis.evoucher.api.service.generator;

import asia.castis.evoucher.api.dto.response.vnpt.VnptResponse;

public interface VnptResponseGenerator {

    VnptResponse getPurchasedVnptResponse(String ev, Integer goodsId);

    VnptResponse getCleanVnptResponse(String ev, Integer goodsId);
}

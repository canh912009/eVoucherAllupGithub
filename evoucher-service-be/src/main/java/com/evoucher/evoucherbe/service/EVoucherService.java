package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.EnumAssetType;
import com.evoucher.evoucherbe.common.enums.PeriodType;
import com.evoucher.evoucherbe.service.request.*;

import java.util.Date;
import java.util.List;

public interface EVoucherService {

    List<String> generateEachVoucher(PublishRequest publishRequest);

    void revertListVoucher(List<String> evs);

    String voucherHandover(VoucherHandoverRequest voucherHandoverRequest);

    List<String> processCreateChoiceVoucher(VoucherChoiceRequest voucherChoiceRequest);
    List<String> processCreateChildVouchersV2(ChildVoucherRequest request);

    void disableVoucher(VoucherDisableRequest request);

    Object getLatestData(EnumAssetType type, String id);
    Date getGoodEndDate(PeriodType type, Integer term, String periodDate);
}

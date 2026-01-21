package com.evoucher.partner.service.mapper;

import com.evoucher.partner.service.bean.request.VnptQueryPaymentCdvRequest;
import com.evoucher.partner.service.vnpt.bean.request.PaymentCdvRequestQuery;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VnptTopUpMapper {
    VnptTopUpMapper INSTANCE = Mappers.getMapper(VnptTopUpMapper.class);

    PaymentCdvRequestQuery toVnptPaymentRequest(VnptQueryPaymentCdvRequest request);
}

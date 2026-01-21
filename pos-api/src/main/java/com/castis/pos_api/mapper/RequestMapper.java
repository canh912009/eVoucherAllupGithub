package com.castis.pos_api.mapper;

import com.castis.pos_api.dto.PosTransactionDto;
import com.castis.pos_api.dto.VoucherDto;
import com.castis.pos_api.dto.request.CancelRequest;
import com.castis.pos_api.dto.request.ConfirmRequest;
import com.castis.pos_api.dto.request.POSRequest;
import com.castis.pos_api.dto.request.third_party.ServiceBeUsingVoucherReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(target = "paymentAmount", source = "requestAmount")
    @Mapping(target = "otpInputType", source = "numInputType")
    ConfirmRequest toConfirmRequest(POSRequest posRequest);

    CancelRequest toCancelRequest(POSRequest posRequest);
    @Mapping(target = "voucherId", source = "transaction.ev")
    @Mapping(target = "exchangeAmount", source = "transaction.prepaidAmount")
    ServiceBeUsingVoucherReq toServiceBeReqForCancel(PosTransactionDto transaction, VoucherDto voucher);
}

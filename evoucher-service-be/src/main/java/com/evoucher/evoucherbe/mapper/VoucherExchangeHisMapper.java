package com.evoucher.evoucherbe.mapper;

import com.evoucher.evoucherbe.dto.*;
import com.evoucher.evoucherbe.entity.VoucherExchangeHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VoucherExchangeHisMapper {
    VoucherExchangeHistory toEntity(VoucherExchangeHistoryDto dto);
    VoucherExchangeHistoryDto toDto(VoucherExchangeHistory entity);
    @Mapping(target = "transactionDate", source = "exchangeRequest.transactionDate")
    @Mapping(target = "storeId", source = "exchangeRequest.storeId")
    @Mapping(target = "ev", source = "exchangeRequest.voucherId")
    @Mapping(target = "voucherTypeCode", source = "voucher.voucherTypeCode")
    @Mapping(target = "goodsId", source = "good.id")
    @Mapping(target = "goodsName", source = "good.goodsName")
    @Mapping(target = "listPrice", source = "good.listPrice")
    @Mapping(target = "discountRate", source = "good.supplyDiscountRate")
    @Mapping(target = "discountAmount", source = "good.supplyDiscountAmount")
    @Mapping(target = "exchangeAmount", source = "exchangeRequest.exchangeAmount")
    @Mapping(target = "userMobileNumber", source = "voucher.userMobileNumber")
    @Mapping(target = "staffMobileNumber", source = "exchangeRequest.staffMobileNumber")
    VoucherExchangeHistoryDto createExchangeHistory(VoucherExchangeReq exchangeRequest, GoodDto good, VoucherDto voucher);
}

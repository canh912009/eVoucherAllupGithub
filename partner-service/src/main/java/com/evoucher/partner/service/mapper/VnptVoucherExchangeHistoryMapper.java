package com.evoucher.partner.service.mapper;

import com.evoucher.partner.service.bean.dtos.VnptVoucherExchangeHistoryDto;
import com.evoucher.partner.service.bean.entity.VnptVoucherExchangeHistory;
import com.evoucher.partner.service.bean.enum_type.VnptExchangeType;
import com.evoucher.partner.service.bean.request.VnptTopUpRequest;
import com.evoucher.partner.service.vnpt.bean.request.DownloadSoftPinRequestQuery;
import com.evoucher.partner.service.vnpt.bean.request.TopUpRequestQuery;
import com.evoucher.partner.service.vnpt.bean.request.VnptQueryBaseRequest;
import com.evoucher.partner.service.vnpt.bean.response.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.TargetType;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VnptVoucherExchangeHistoryMapper {
    VnptVoucherExchangeHistoryMapper INSTANCE = Mappers.getMapper(VnptVoucherExchangeHistoryMapper.class);

    VnptVoucherExchangeHistory toEntity(VnptVoucherExchangeHistoryDto dto);
    VnptVoucherExchangeHistoryDto toDto(VnptVoucherExchangeHistory entity);
    @Mapping(target = "ev", source = "ev")
    @Mapping(target = "exchangeType", source = "exchangeType")
    @Mapping(target = "vnptRequestId", source = "queryRequest.requestId")
    @Mapping(target = "providerCode", source = "queryRequest.provider")
    VnptVoucherExchangeHistoryDto toExchangeHistory(VnptQueryBaseRequest queryRequest, VnptExchangeType exchangeType, String ev);

    @Mapping(target = "cardSerial", source = "card.serial")
    @Mapping(target = "cardPin", source = "card.pin")
    @Mapping(target = "expireDate", source = "card.expire")
    @Mapping(target = "faceValue", source = "card.amount")
    void updateCardInfo(@MappingTarget VnptVoucherExchangeHistoryDto history, Card card);

    @Mapping(target = "targetPhone", source = "target")
    @Mapping(target = "faceValue", source = "amount")
    void updateCardInfo(@MappingTarget VnptVoucherExchangeHistoryDto history, TopUpRequestQuery topup);
}

package com.evoucher.adminapi.cms.mapper;

import com.evoucher.adminapi.cms.dao.models.ExternalPin;
import com.evoucher.adminapi.cms.service.models.vnptEPay.response.VnptCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VnptResponseMapper {
    VnptResponseMapper INSTANCE = Mappers.getMapper(VnptResponseMapper.class);
    @Mapping(target = "expireTime", source = "expire")
    @Mapping(target = "externalPinNo", source = "pin")
    ExternalPin fromVnptCard(VnptCard vnptCard);
}

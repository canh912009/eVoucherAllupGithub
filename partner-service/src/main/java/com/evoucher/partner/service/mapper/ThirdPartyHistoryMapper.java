package com.evoucher.partner.service.mapper;

import com.evoucher.partner.service.bean.dtos.ThirdPartyHistoryDto;
import com.evoucher.partner.service.bean.entity.ThirdPartyHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ThirdPartyHistoryMapper {
    ThirdPartyHistoryMapper INSTANCE = Mappers.getMapper(ThirdPartyHistoryMapper.class);

    ThirdPartyHistoryDto toDto(ThirdPartyHistory entity);
    ThirdPartyHistory toEntity(ThirdPartyHistoryDto dto);
}

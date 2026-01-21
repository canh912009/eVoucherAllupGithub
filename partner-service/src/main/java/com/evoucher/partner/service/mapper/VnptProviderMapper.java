package com.evoucher.partner.service.mapper;

import com.evoucher.partner.service.bean.dtos.VnptProviderDto;
import com.evoucher.partner.service.bean.entity.VnptProvider;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface VnptProviderMapper {
    VnptProviderMapper INSTANCE = Mappers.getMapper(VnptProviderMapper.class);

    VnptProviderDto toDto(VnptProvider entity);
    VnptProvider toEntity(VnptProviderDto dto);
}

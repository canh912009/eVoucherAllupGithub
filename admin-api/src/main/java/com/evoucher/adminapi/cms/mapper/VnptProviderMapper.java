package com.evoucher.adminapi.cms.mapper;

import com.evoucher.adminapi.cms.dao.models.VnptProvider;
import com.evoucher.adminapi.cms.service.models.VnptProviderDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface VnptProviderMapper {
    VnptProviderMapper INSTANCE = Mappers.getMapper(VnptProviderMapper.class);

    VnptProviderDto toDto(VnptProvider entity);
    VnptProvider toEntity(VnptProviderDto dto);
}

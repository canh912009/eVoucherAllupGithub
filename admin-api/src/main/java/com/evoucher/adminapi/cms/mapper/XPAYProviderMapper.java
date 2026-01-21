package com.evoucher.adminapi.cms.mapper;
import com.evoucher.adminapi.cms.dao.models.XPAYProvider;
import com.evoucher.adminapi.cms.service.models.XPAYProviderDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface XPAYProviderMapper {
    XPAYProviderMapper INSTANCE = Mappers.getMapper(XPAYProviderMapper.class);

    XPAYProviderDto toDto(XPAYProvider entity);
    XPAYProvider toEntity(XPAYProviderDto dto);
}

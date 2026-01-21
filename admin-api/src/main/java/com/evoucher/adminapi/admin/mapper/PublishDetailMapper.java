package com.evoucher.adminapi.admin.mapper;

import com.evoucher.adminapi.admin.dao.models.PublishDetail;
import com.evoucher.adminapi.admin.service.models.PublishDetailDto;
import org.mapstruct.Mapper;

@Mapper
public interface PublishDetailMapper {
    PublishDetail dtoToEntity(PublishDetailDto dto);
    PublishDetailDto entityToDto(PublishDetail entity);
}

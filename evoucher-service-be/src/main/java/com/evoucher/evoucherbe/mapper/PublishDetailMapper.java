package com.evoucher.evoucherbe.mapper;

import com.evoucher.evoucherbe.dto.PublishDetailDto;
import com.evoucher.evoucherbe.entity.PublishDetail;
import org.mapstruct.Mapper;

@Mapper
public interface PublishDetailMapper {
    PublishDetail dtoToEntity(PublishDetailDto dto);
    PublishDetailDto entityToDto(PublishDetail entity);
}

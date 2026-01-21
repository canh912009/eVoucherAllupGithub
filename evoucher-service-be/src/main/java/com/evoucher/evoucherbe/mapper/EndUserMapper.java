package com.evoucher.evoucherbe.mapper;

import com.evoucher.evoucherbe.dto.EndUserDto;
import com.evoucher.evoucherbe.entity.EndUser;
import com.evoucher.evoucherbe.service.request.EndUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EndUserMapper {

    EndUserMapper INSTANCE = Mappers.getMapper(EndUserMapper.class);

    EndUser toEntity(EndUserRequest endUserRequest);
    EndUserDto mapEntityToDto(EndUser entity);
    EndUser mapDtoToEntity(EndUserDto dto);
}
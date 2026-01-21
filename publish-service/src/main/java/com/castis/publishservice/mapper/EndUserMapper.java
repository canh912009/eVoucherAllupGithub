package com.castis.publishservice.mapper;

import com.castis.publishservice.dto.EndUserDto;
import com.castis.publishservice.entity.EndUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EndUserMapper {
    EndUserMapper INSTANCE = Mappers.getMapper(EndUserMapper.class);
    EndUser dtoToEntity(EndUserDto dto);
    EndUserDto entityToDto(EndUser entity);
}

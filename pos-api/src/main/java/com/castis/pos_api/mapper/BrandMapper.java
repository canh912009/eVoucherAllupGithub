package com.castis.pos_api.mapper;

import com.castis.pos_api.dto.BrandDto;
import com.castis.pos_api.dto.response.BrandResponse;
import com.castis.pos_api.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);
    BrandDto toDto(Brand entity);
    Brand toEntity(BrandDto dto);
    @Mapping(target = "brandId", source = "id")
    BrandResponse toResponse(BrandDto dto);
}

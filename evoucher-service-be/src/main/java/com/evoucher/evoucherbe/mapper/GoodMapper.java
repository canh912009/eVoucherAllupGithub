package com.evoucher.evoucherbe.mapper;

import com.evoucher.evoucherbe.dto.GoodDto;
import com.evoucher.evoucherbe.entity.Goods;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GoodMapper {
    GoodMapper INSTANCE = Mappers.getMapper(GoodMapper.class);
    GoodDto toDto(Goods entity);
    Goods toEntity(GoodDto dto);
}

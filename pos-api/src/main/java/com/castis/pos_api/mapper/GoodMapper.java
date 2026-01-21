package com.castis.pos_api.mapper;

import com.castis.pos_api.dto.GoodsDto;
import com.castis.pos_api.dto.response.GoodResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {BrandMapper.class})
public interface GoodMapper {
    GoodMapper INSTANCE = Mappers.getMapper(GoodMapper.class);
    @Mapping(target = "salesPrice", source = "sellPrice")
    @Mapping(target = "goodsImgUrl", source = "goodsImgPath")
    @Mapping(target = "supplier.supplierId", source = "supplier.id")
    GoodResponse toResponse(GoodsDto dto);
}

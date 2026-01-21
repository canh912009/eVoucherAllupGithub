package com.evoucher.adminapi.cms.mapper;

import com.evoucher.adminapi.cms.dao.models.CategoryGoodsRel;
import com.evoucher.adminapi.cms.service.models.CategoryGoodsRelDTO;
import org.mapstruct.Builder;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface CategoryGoodsRelMapper {

    CategoryGoodsRelMapper INSTANCE = Mappers.getMapper(CategoryGoodsRelMapper.class);

    @InheritConfiguration
    CategoryGoodsRel toEntity(CategoryGoodsRelDTO categoryGoodsRelDTO);

    @InheritConfiguration
    CategoryGoodsRelDTO toDTO(CategoryGoodsRel categoryGoodsRel);

    @InheritConfiguration
    List<CategoryGoodsRelDTO> toListDTO(List<CategoryGoodsRel> categoryGoodsRels);

    @InheritConfiguration
    List<CategoryGoodsRel> toListEntity(List<CategoryGoodsRelDTO> categoryGoodsRelDTOS);
}

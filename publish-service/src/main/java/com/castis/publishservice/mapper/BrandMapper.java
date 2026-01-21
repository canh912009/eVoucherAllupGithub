package com.castis.publishservice.mapper;

import com.castis.publishservice.dto.BrandDTO;
import com.castis.publishservice.dto.queue.BrandRequest;
import com.castis.publishservice.entity.Brand;
import com.castis.publishservice.utils.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {Utils.class}, uses = SupplierMapper.class)
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);
    @Mapping(target = "name", source = "brandName")
    @Mapping(target = "imgUrl", source = "brandImagePath")
    @Mapping(target = "posLink", expression = "java(Utils.mappingYesNo(entity.getIsPosLink()))")
    BrandRequest toRequest(Brand entity);

    @Mapping(target = "updateId", source = "updtId")
    @Mapping(target = "updateDate", source = "updtDt")
    BrandDTO toDTO(Brand entity);
}

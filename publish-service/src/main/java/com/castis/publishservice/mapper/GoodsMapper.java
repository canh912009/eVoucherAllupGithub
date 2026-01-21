package com.castis.publishservice.mapper;

import com.castis.publishservice.dto.GoodsDTO;
import com.castis.publishservice.dto.queue.GoodsRequest;
import com.castis.publishservice.entity.Goods;
import com.castis.publishservice.utils.Constants;
import com.castis.publishservice.utils.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {Utils.class}, uses = {CategoryMapper.class})
public interface GoodsMapper {
    public static final GoodsMapper INSTANCE = Mappers.getMapper(GoodsMapper.class);
    @Mapping(target = "name", source = "goodsName")
    @Mapping(target = "imagePath", source = "goodsImgPath")
    @Mapping(target = "imageName", source = "goodsImgName")
    @Mapping(target = "description", source = "goodsDescription")
    @Mapping(target = "supplyVatInclude", expression = "java(Utils.mappingYesNo(entity.getVatIncludeYn()))")
    @Mapping(target = "valid", expression = "java(Utils.mappingYesNo(entity.getValidYn()))")
    @Mapping(target = "startDate", dateFormat = Constants.fullDateTimeFormat)
    @Mapping(target = "endDate", dateFormat = Constants.fullDateTimeFormat)
    GoodsRequest toRequest(Goods entity);

    GoodsDTO toDTO(Goods entity);
}

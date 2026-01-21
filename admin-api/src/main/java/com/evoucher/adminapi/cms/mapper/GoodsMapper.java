package com.evoucher.adminapi.cms.mapper;

import com.evoucher.adminapi.cms.dao.models.*;
import com.evoucher.adminapi.cms.service.models.*;
import com.evoucher.adminapi.cms.service.models.vnptEPay.response.GiftSearchWithDateDTO;
import com.evoucher.adminapi.cms.service.models.vnptEPay.response.GiftSearchWithoutDateDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.LinkedList;
import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface GoodsMapper {

    GoodsMapper INSTANCE = Mappers.getMapper(GoodsMapper.class);

    Goods toEntity(GoodsDTO goodsDTO);

    GoodsDTO toDTO(Goods goods);

    @Named("toDtoWithoutChildrenInfo")
    @Mapping(target = "listGoodsChoice", ignore = true)
    @Mapping(target = "bulkCategories", ignore = true)
    @Mapping(target = "vnptGoods", ignore = true)
    GoodsDTO toDtoWithoutChildrenInfo(Goods goods);

    @Mapping(source = "categories", target = "categories")
    @Mapping(source = "exceptStores", target = "exceptStores")
    GoodsDTO toGoodsDTO(Goods goods, List<Category> categories, List<Store> exceptStores);

//    @Mapping(source = "categories", target = "categories")
//    @Mapping(source = "exceptStores", target = "exceptStores")
//    void updateDTO(List<Category> categories, List<Store> exceptStores, @MappingTarget GoodsDTO goodsDTO);

    LinkedList<GoodsDTO> toListGoodsDTO(LinkedList<Goods> listGoods);
    List<GiftSearchWithoutDateDTO> toGiftSearchWithoutDate(List<GiftSearchWithDateDTO> o);
    BulkGoodSearchResponse toSearchResponse(Goods entity);
    BulkGoodDTO toDTO(BulkGoods entity);
    BulkGoods toEntity(BulkGoodDTO dTO);

    VnptGoodDto toDTO(VnptGood entity);
    VnptGood toEntity(VnptGood dto);

    XpayGoodDto toDTO(XpayGood entity);
    XpayGood toEntity(XpayGoodDto dto);

    List<VnptGoodDto> toDtoList(List<VnptGood> entities);
    List<VnptGood> toEntityList(List<VnptGoodDto> dtoList);

    List<XpayGoodDto> toXpayGoodDtoList(List<XpayGood> entities);
    List<XpayGood> toXpayGoodEntityList(List<XpayGoodDto> dtoList);

}

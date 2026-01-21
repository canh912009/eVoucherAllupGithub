package com.evoucher.adminapi.cms.mapper;

import com.evoucher.adminapi.cms.dao.models.Store;
import com.evoucher.adminapi.cms.service.models.StoreDTO;
import com.evoucher.adminapi.cms.service.models.request.StoreRequest;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.good.service.model.gift_pop.GiftpopStoreResponse;
import com.evoucher.adminapi.good.service.model.ur_box.UrBoxGood;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface StoreMapper {

    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    StoreDTO toStoreDTO(Store store);

    Store toStore(StoreDTO storeDTO);

    @Mapping(source = "storeId", target = "id")
    Store toStore(StoreRequest storeRequest);

    @Mapping(source = "brandId", target = "brandId")
    @Mapping(source = "storeId", target = "storeId")
    @Mapping(source = "office.address", target = "fullAddress")
    @Mapping(source = "office.code", target = "mapCode")
    @Mapping(source = "office.brandTitle", target = "storeName")
    @Mapping(source = "office.brandImgSrc", target = "storeImagePath")
    @Mapping(source = "office.id", target = "storeCode")
    @Mapping(source = "valid", target = "validYn")
    @Mapping(source = "office.phone", target = "telephoneNumber")
    @Mapping(source = "office.titleCity", target = "region")
    StoreRequest toRequestDTO(UrBoxGood.Office office, String storeId, String brandId, EnumValidYn valid);
    @Mapping(source = "brandId", target = "brandId")
    @Mapping(source = "storeId", target = "storeId")
    @Mapping(source = "office.storeAddr", target = "fullAddress")
    @Mapping(source = "office.storeName", target = "storeName")
    @Mapping(source = "office.storeCode", target = "storeCode")
    @Mapping(source = "valid", target = "validYn")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "office.storeTel", target = "telephoneNumber")
    StoreRequest toRequestDTO(GiftpopStoreResponse.Store office, String brandId, String storeId, EnumValidYn valid, String latitude, String longitude);
}

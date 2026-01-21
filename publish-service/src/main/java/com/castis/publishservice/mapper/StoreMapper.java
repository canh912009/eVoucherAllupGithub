package com.castis.publishservice.mapper;

import com.castis.publishservice.dto.StoreDTO;
import com.castis.publishservice.dto.queue.StoreQueueRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StoreMapper {
    static final StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);
    @Mapping(target = "storeId", source = "id")
    @Mapping(target = "validYN", source = "validYn")
    @Mapping(target = "registerDate", source = "regDt")
    @Mapping(target = "registerId", source = "regId")
    @Mapping(target = "updateDate", source = "updtDt")
    @Mapping(target = "tel", source = "telephoneNumber")
    @Mapping(target = "updateId", source = "updtId")
    StoreQueueRequest toQueueRequest(StoreDTO dto);
}

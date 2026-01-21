package com.castis.publishservice.mapper;

import com.castis.publishservice.dto.StoreDTO;
import com.castis.publishservice.dto.queue.UpdateStoreQueueRequest;
import com.castis.publishservice.dto.request.DataSynchronizeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = StoreMapper.class)
public interface DataSynchronizeMapper {
    static final DataSynchronizeMapper INSTANCE = Mappers.getMapper(DataSynchronizeMapper.class);
    @Mapping(target = "store", source = "payload")
    UpdateStoreQueueRequest toQueueRequest(DataSynchronizeRequest<StoreDTO> dto);
}

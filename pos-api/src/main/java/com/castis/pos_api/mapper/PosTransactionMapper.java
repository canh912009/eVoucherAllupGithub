package com.castis.pos_api.mapper;

import com.castis.pos_api.dto.PosTransactionDto;
import com.castis.pos_api.dto.request.FinalizeSingleRequest;
import com.castis.pos_api.dto.request.FinalizeListRequest;
import com.castis.pos_api.dto.request.ValidatingRequest;
import com.castis.pos_api.entity.PosTransaction;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PosTransactionMapper {
    PosTransactionMapper INSTANCE = Mappers.getMapper(PosTransactionMapper.class);
    PosTransactionDto toDto(PosTransaction entity);
    PosTransaction toEntity(PosTransactionDto dto);
    PosTransactionDto fromValidatingRequest(ValidatingRequest request);
    PosTransactionDto fromFinalizePrePaidRequest(FinalizeSingleRequest request);
    PosTransactionDto fromFinalizeSingleItemRequest(FinalizeListRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "prepaidAmount", source = "prepaidAmount")
    void updateByFinalizeRequest(@MappingTarget PosTransactionDto transaction, FinalizeSingleRequest request);
}

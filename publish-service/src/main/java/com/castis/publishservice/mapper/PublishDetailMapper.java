package com.castis.publishservice.mapper;

import com.castis.publishservice.dto.PublishDetailDTO;
import com.castis.publishservice.entity.PublishDetail;
import com.castis.publishservice.utils.Constants;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {Constants.class})
public interface PublishDetailMapper {
    PublishDetailMapper INSTANCE = Mappers.getMapper(PublishDetailMapper.class);
    PublishDetail toEntity(PublishDetailDTO dto);
    PublishDetailDTO toDTO(PublishDetail entity);
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "publishId", source = "publishId")
    @Mapping(target = "receiverMobileNo", source = "receiverMobileNo")
    @Mapping(target = "extPinId", source = "extPinId")
    @Mapping(target = "smsType", source = "smsType")
    void transferVoucher(@MappingTarget PublishDetailDTO target, PublishDetailDTO origin);
}

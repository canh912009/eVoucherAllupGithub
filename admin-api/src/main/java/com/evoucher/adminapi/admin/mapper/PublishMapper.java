package com.evoucher.adminapi.admin.mapper;

import com.evoucher.adminapi.admin.dao.models.Publish;
import com.evoucher.adminapi.admin.service.models.PublishDTO;
import com.evoucher.adminapi.admin.service.models.PublishRequest;
import com.evoucher.adminapi.cms.mapper.CategoryMapper;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true), uses = CategoryMapper.class)
public interface PublishMapper {

    PublishMapper INSTANT = Mappers.getMapper(PublishMapper.class);

    Publish toPublish(PublishRequest publishRequest);

    Publish toPublish(PublishDTO publishDTO);

    PublishDTO toPublishDTO(Publish publish);

    Publish copyPublish(Publish publish);

    @Named("toOperatorReqInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "publishName", target = "publishName")
    @Mapping(source = "customerId", target = "customerId")
    PublishDTO toOperatorReqInfo(Publish entity);
}

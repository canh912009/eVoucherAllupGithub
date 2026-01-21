package com.evoucher.adminapi.admin.mapper;

import com.evoucher.adminapi.admin.dao.models.MessageTemplate;
import com.evoucher.adminapi.admin.service.models.MessageTemplateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageTemplateMapper {
    MessageTemplateMapper INSTANCE = Mappers.getMapper(MessageTemplateMapper.class);
    MessageTemplateDTO toDTO(MessageTemplate entity);
}

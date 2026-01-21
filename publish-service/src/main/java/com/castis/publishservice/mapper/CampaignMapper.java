package com.castis.publishservice.mapper;

import com.castis.publishservice.dto.queue.CampaignRequest;
import com.castis.publishservice.dto.queue.SupplierRequest;
import com.castis.publishservice.entity.Campaign;
import com.castis.publishservice.entity.Supplier;
import com.castis.publishservice.service.MessageTemplateService;
import com.castis.publishservice.utils.Constants;
import com.castis.publishservice.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {Utils.class, MessageTemplateService.class})
public interface CampaignMapper {
    CampaignMapper INSTANCE = Mappers.getMapper(CampaignMapper.class);
    @Mapping(target = "name", source = "campaignName")
    @Mapping(target = "startDate", dateFormat = Constants.fullDateTimeFormat)
    @Mapping(target = "endDate", dateFormat = Constants.fullDateTimeFormat)
    @Mapping(target = "messageTemplate", expression = "java(MessageTemplateService.toRequest(entity.getMessageTemplate()))")
    CampaignRequest toRequest(Campaign entity) throws JsonProcessingException;
}

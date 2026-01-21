package com.evoucher.adminapi.admin.mapper;

import com.evoucher.adminapi.admin.dao.models.Campaign;
import com.evoucher.adminapi.admin.dao.models.Publish;
import com.evoucher.adminapi.admin.service.models.CampaignDTO;
import com.evoucher.adminapi.admin.service.models.CampaignRequest;
import com.evoucher.adminapi.cms.dao.models.Goods;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true), uses = {MessageTemplateMapper.class})
public interface CampaignMapper {

    CampaignMapper INSTANT = Mappers.getMapper(CampaignMapper.class);

    CampaignDTO toCampaignDTO(Campaign campaign);

    @Mappings({
            @Mapping(source = "listGoods" ,target = "listGoods"),
            @Mapping(source = "publishes" ,target = "publishes"),
    })
    CampaignDTO toCampaignDTO(Campaign campaign, List<Goods> listGoods, List<Publish> publishes);

    Campaign toCampaign(CampaignDTO campaignDTO);

    Campaign toCampaign(CampaignRequest campaignRequest);
}

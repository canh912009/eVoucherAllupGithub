package com.evoucher.adminapi.admin.mapper;

import com.evoucher.adminapi.admin.dao.models.CampaignGoods;
import com.evoucher.adminapi.admin.service.models.CampaignGoodsDTO;
import com.evoucher.adminapi.admin.service.models.CampaignGoodsRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface CampaignGoodsMapper {

    CampaignGoodsMapper INSTANT = Mappers.getMapper(CampaignGoodsMapper.class);

    CampaignGoods toCampaignGoods(CampaignGoodsDTO campaignGoodsDTO);

    List<CampaignGoods> toListCampaignGoods(List<CampaignGoodsDTO> campaignGoodsDTOS);

    List<CampaignGoods> toListCampaignGoodsFromCampaignGoodsRequest(List<CampaignGoodsRequest> campaignGoodsRequests);

    CampaignGoodsDTO campaignGoodsDTO(CampaignGoods campaignGoods);
}

package com.evoucher.evoucherbe.mapper;

import com.evoucher.evoucherbe.dto.CampaignDto;
import com.evoucher.evoucherbe.entity.Campaign;
import org.mapstruct.Mapper;

@Mapper
public interface CampaignMapper {
    Campaign toEntity(CampaignDto dto);
    CampaignDto toDto(Campaign entity);
}

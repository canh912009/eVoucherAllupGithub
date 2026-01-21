package com.evoucher.evoucherbe.mapper;

import com.evoucher.evoucherbe.dto.PublishDto;
import com.evoucher.evoucherbe.entity.Publish;
import org.mapstruct.Mapper;

@Mapper(uses = {CampaignMapper.class, GoodMapper.class})
public interface PublishMapper {
    PublishDto toDto(Publish entity);
    Publish toEntity(PublishDto dto);
}

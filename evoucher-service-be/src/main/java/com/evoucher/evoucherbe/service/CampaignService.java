package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.dto.CampaignDto;
import com.evoucher.evoucherbe.entity.Campaign;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.CampaignMapper;
import com.evoucher.evoucherbe.repository.CampaignRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignService extends EntityService<Campaign, Integer, CampaignDto> {
    @Getter
    private final CampaignRepository repository;
    private final CampaignMapper mapper;


    @Override
    public String getEntityType() {
        return "campaign";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Integer id) {
        return new EntityNotFoundException("Campaign not found.");
    }

    @Override
    public Campaign toEntity(CampaignDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public CampaignDto toDto(Campaign entity) {
        return mapper.toDto(entity);
    }
}

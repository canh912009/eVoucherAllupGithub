package com.evoucher.partner.service.service;


import com.evoucher.partner.service.bean.dtos.ThirdPartyHistoryDto;
import com.evoucher.partner.service.bean.entity.ThirdPartyHistory;
import com.evoucher.partner.service.bean.enum_type.SystemType;
import com.evoucher.partner.service.bean.enum_type.ThirdRequestType;
import com.evoucher.partner.service.exception.define_exception.CustomCodeException;
import com.evoucher.partner.service.mapper.ThirdPartyHistoryMapper;
import com.evoucher.partner.service.repository.ThirdPartyHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThirdPartyHistoryService extends BasicService<ThirdPartyHistory, Long, ThirdPartyHistoryDto> {

    private static final ThirdPartyHistoryMapper MAPPER = ThirdPartyHistoryMapper.INSTANCE;
    private final ThirdPartyHistoryRepository repository;

    public ThirdPartyHistoryDto makeNewOutBound(String url, SystemType systemType) {
        return ThirdPartyHistoryDto.builder()
                .requestType(ThirdRequestType.OUTBOUND)
                .requestUrl(url)
                .system(systemType != null ? systemType.name() : null)
                .requestTime(new Date())
                .build();
    }

    @Override
    public JpaRepository<ThirdPartyHistory, Long> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "third party api calling history";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Long id) {
        return new EntityNotFoundException("can not find third party api calling history by id: " + id);
    }

    @Override
    public ThirdPartyHistory toEntity(ThirdPartyHistoryDto dto) {
        return MAPPER.toEntity(dto);
    }

    @Override
    public ThirdPartyHistoryDto toDto(ThirdPartyHistory entity) {
        return MAPPER.toDto(entity);
    }
    public ThirdPartyHistoryDto save(ThirdPartyHistoryDto dto) throws CustomCodeException {
        log.info("save history: {}", dto);
        ThirdPartyHistory entity = toEntity(dto);
        entity = this.save(entity);
        return toDto(entity);
    }
}

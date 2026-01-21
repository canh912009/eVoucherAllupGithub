package com.castis.publishservice.service;

import com.castis.publishservice.entity.ThirdPartyCallAPIHistory;
import com.castis.publishservice.repository.ThirdPartyCallAPIHistoryRepository;
import com.castis.publishservice.utils.enum_template.SystemType;
import com.castis.publishservice.utils.enum_template.ThirdRequestType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThirdPartyCallAPIHistoryService {

    private final ThirdPartyCallAPIHistoryRepository repository;

    public ThirdPartyCallAPIHistory saveThirdPartyCallAPIHistory(
            ThirdPartyCallAPIHistory thirdPartyCallAPIHistory) {
        log.info("Save log call api");
        try {
            return repository.save(thirdPartyCallAPIHistory);
        } catch (Exception e) {
            log.info("Error save ThirdPartyCallAPIHistory: {}", e.getMessage(), e);
            return null;
        }
    }

    public ThirdPartyCallAPIHistory makeNewOutBound(String url, SystemType systemType) {
        return ThirdPartyCallAPIHistory.builder()
                .requestType(ThirdRequestType.OUTBOUND)
                .requestUrl(url)
                .system(systemType != null ? systemType.name() : null)
                .requestTime(new Date())
                .build();
    }
}

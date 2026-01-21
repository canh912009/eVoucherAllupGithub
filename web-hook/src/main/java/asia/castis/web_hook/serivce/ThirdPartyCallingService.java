package asia.castis.web_hook.serivce;

import asia.castis.web_hook.bean.entity.ThirdPartyCallingHistory;
import asia.castis.web_hook.common.RequestType;
import asia.castis.web_hook.repository.ThirdPartyCallingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThirdPartyCallingService {

    private final ThirdPartyCallingRepository repository;

    public ThirdPartyCallingHistory save(
            ThirdPartyCallingHistory thirdPartyCallingHistory) {
        log.info("Save log call api");
        try {
            return repository.save(thirdPartyCallingHistory);
        } catch (Exception e) {
            log.info("Error save ThirdPartyCallAPIHistory: {}", e.getMessage(), e);
            return null;
        }
    }

    public ThirdPartyCallingHistory.ThirdPartyCallingHistoryBuilder makeNewHistory(RequestType requestType) {
        return ThirdPartyCallingHistory.builder().requestType(requestType)
                .requestTime(new Date())
                ;
    }
}

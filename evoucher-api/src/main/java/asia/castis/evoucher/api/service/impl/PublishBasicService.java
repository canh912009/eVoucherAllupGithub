package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.entity.Publish;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.repository.PublishRepository;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublishBasicService {
    private final PublishRepository repository;
    public Publish findById(Integer id) throws ApplicationException {
        log.info("find publish by id: {}", id);
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new ApplicationException(ResponseString.CAN_NOT_FIND_PUBLISH, ErrorCode.CAN_NOT_FIND_PUBLISH));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(ResponseString.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR);
        }
    }
}

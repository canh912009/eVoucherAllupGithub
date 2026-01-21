package asia.castis.evoucher.push.service;

import asia.castis.evoucher.push.model.CheckPublish;
import asia.castis.evoucher.push.model.PublishModel;
import asia.castis.evoucher.push.repositories.CheckPublishRepository;
import asia.castis.evoucher.push.repositories.PublishRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CheckPublishService {
    private final CheckPublishRepository repository;
    @Autowired
    public CheckPublishService(CheckPublishRepository repository) {
        this.repository = repository;
    }
    public void deleteALl() {
        repository.deleteAll();
    }
    public CheckPublish save(final CheckPublish message) {
        return repository.save(message);
    }

    public CheckPublish findByPublishId(long publishId) {
        log.info("findByPublishId {}", publishId);
        return repository.findByPublishId(publishId).orElse(null);
    }

    public List<CheckPublish> getListCheckPublishNotDone() {
        return repository.findByDone(false).orElse(null);
    }
}

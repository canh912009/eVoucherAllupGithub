package asia.castis.evoucher.push.service;

import asia.castis.evoucher.push.model.PublishModel;
import asia.castis.evoucher.push.repositories.PublishRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PublishService {
    private final PublishRepository repository;
    @Autowired
    public PublishService(PublishRepository repository) {
        this.repository = repository;
    }
    public void deleteALl() {
        repository.deleteAll();
    }
    public PublishModel save(final PublishModel message) {
        return repository.save(message);
    }
    public PublishModel findById(Long id) {
        log.info("findById {}", id);
        return repository.findById(id).orElse(null);
    }
    /*
    public PublishModel updateMessageStatus(final PublishModel message) {
        // find message with PublishModel Id
        // if exist => save -> it will automatice update

        return null;
    }

     */
    // get list PAMesage by publishScheduleId
    // get list PAMesage by campaignId
    // get list PAMesage by publishScheduleId and messageType...
    // sending back static of list PublishModel which sending by (1)
    /*
    public List<PublishModel> findByPublishDetailId(final int publishDetailId) {
        return repository.findByPublishDetailId(publishDetailId).orElse(null);
    }

    public void deleteByPublishDetailId(final int publishDetailId) {
        repository.deleteByPublishDetailId(publishDetailId);
    }

     */
    //

}

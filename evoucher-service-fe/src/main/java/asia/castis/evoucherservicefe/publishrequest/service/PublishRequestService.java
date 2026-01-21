package asia.castis.evoucherservicefe.publishrequest.service;

import asia.castis.evoucherservicefe.exceptions.*;
import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.RequestFromBE;

import java.io.IOException;
import java.util.Date;

public interface PublishRequestService {

    void incomingPublishHandling(RequestFromBE incomingPublish, Date startProcessingTime)
            throws RetryJobException, InvalidException, CreateMessageException, DecryptException, NotFoundException, IOException, SendMessageToQueueException;
}

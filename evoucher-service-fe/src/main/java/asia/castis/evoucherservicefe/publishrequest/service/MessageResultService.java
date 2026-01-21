package asia.castis.evoucherservicefe.publishrequest.service;

import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.RequestFromBE;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.PublishMessage;
import asia.castis.evoucherservicefe.common.enums.EnumPublishStatus;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.common.model.publish.PublishModel;
import asia.castis.evoucherservicefe.exceptions.SendMessageToQueueException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MessageResultService {
    void sendAllFailMessages(RequestFromBE incomingPublish, Date startProcessingTime);

    void sendStartGenMessage(RequestFromBE request, List<VoucherModel> parseVouchers,
                             Date startProcessingTime, EnumPublishStatus publishStatus) throws SendMessageToQueueException;

    void sendFinishResult(PublishModel publishModel, List<VoucherModel> parseVouchers,
                          Date startProcessingTime) throws SendMessageToQueueException;

    void sendEndGenResult(RequestFromBE incomingPublish, List<VoucherModel> parsedVouchers, Date startProcessingTime, Map<VoucherModel, PublishMessage> publishMessageMap, PublishModel processingPublishModel) throws SendMessageToQueueException;

    void sendEmailResult(RequestFromBE request, Map<VoucherModel, String> emailSendingResult, Date startProcessingTime) throws SendMessageToQueueException;
}

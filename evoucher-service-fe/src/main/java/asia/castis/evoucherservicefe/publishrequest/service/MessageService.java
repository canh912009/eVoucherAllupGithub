package asia.castis.evoucherservicefe.publishrequest.service;

import asia.castis.evoucherservicefe.common.dto.messagetemplate.MessageTemplate;
import asia.castis.evoucherservicefe.exceptions.CreateMessageException;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.PublishMessage;
import asia.castis.evoucherservicefe.common.model.publish.PublishModel;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.exceptions.DecryptException;
import asia.castis.evoucherservicefe.exceptions.InvalidException;

public interface MessageService {
    PublishMessage createMessage(MessageTemplate messageTemplate, VoucherModel voucher, PublishModel publish) throws CreateMessageException;
}

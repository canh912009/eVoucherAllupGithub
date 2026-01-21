package asia.castis.evoucherservicefe.common.service;

import asia.castis.evoucherservicefe.common.dto.messagetemplate.MessageTemplate;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.TemplateData;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.common.model.publish.PublishModel;
import asia.castis.evoucherservicefe.exceptions.DecryptException;
import asia.castis.evoucherservicefe.exceptions.InvalidException;

public interface MsgTemplateService {
    String decrypt(String input) throws DecryptException, InvalidException;

    void setTemplateValues(MessageTemplate msgTemplate, VoucherModel voucher, PublishModel publish) throws InvalidException, DecryptException;
}

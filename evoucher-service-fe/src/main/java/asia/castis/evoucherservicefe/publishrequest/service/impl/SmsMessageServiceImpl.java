package asia.castis.evoucherservicefe.publishrequest.service.impl;

import asia.castis.evoucherservicefe.common.dto.messagetemplate.MessageTemplate;
import asia.castis.evoucherservicefe.common.dto.messagetemplate.MsgTemplateData;
import asia.castis.evoucherservicefe.common.enums.EnumPublishType;
import asia.castis.evoucherservicefe.common.service.MsgTemplateService;
import asia.castis.evoucherservicefe.common.utils.JsonMapper;
import asia.castis.evoucherservicefe.exceptions.CreateMessageException;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.PublishMessage;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.TemplateData;
import asia.castis.evoucherservicefe.common.model.publish.PublishModel;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.publishrequest.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("smsMessageService")
public class SmsMessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(SmsMessageServiceImpl.class);

    @Value("${message.path.template.en}")
    private String messageTemplateFileNameEn;
    @Value("${message.transfer.path.template.en}")
    private String transferMessageTemplateFileNameEn;
    private final MsgTemplateService msgTemplateService;

    @Autowired
    public SmsMessageServiceImpl(MsgTemplateService msgTemplateService) {
        this.msgTemplateService = msgTemplateService;
    }

    /**
     * SMS only generate message. No template set
     */
    @Override
    public PublishMessage createMessage(MessageTemplate messageTemplate, VoucherModel voucher, PublishModel request) throws CreateMessageException {
        try {
            //2. Generate template data object
            msgTemplateService.setTemplateValues(messageTemplate, voucher, request);
            //3. Fill in all placeholders
            String resultVoucherMsg = combineTemplateWithParams(messageTemplate);
            //4. Make publish message
            PublishMessage publishMessage = makePublishMessage(voucher, resultVoucherMsg);
            logger.info("Create message successfully voucher={}, request={}", voucher.getId(), request.getId());
            return publishMessage;
        } catch (Exception e) {
            throw new CreateMessageException(
                    String.format("Exception while creating message, voucherId=%s, msg=%s",
                            voucher.getId(), e.getMessage()), e);
        }
    }

    private String combineTemplateWithParams(MessageTemplate messageTemplate) {
        String result = messageTemplate.getTemplate();
        for (MsgTemplateData msgTemplateData : messageTemplate.getData()) {
            if (msgTemplateData.getValue() == null) {
                continue;
            }
            result = result.replace("{" + msgTemplateData.getKey() + "}", msgTemplateData.getValue());
        }
        return result;
    }

    private PublishMessage makePublishMessage(VoucherModel voucher, String resultVoucherMsg) {
        PublishMessage publishMessage = new PublishMessage();
        publishMessage.setPublishDetailId(voucher.getPublishDetailId());
        publishMessage.setMessage(resultVoucherMsg);
        publishMessage.setReceiverMobileNumber(voucher.getUserMobileNumber());
        return publishMessage;
    }

    private MessageTemplate getTemplateMessage(EnumPublishType type) throws IOException {
        String templateFilePath;
        if (type == EnumPublishType.TRANSFER) {
            templateFilePath = transferMessageTemplateFileNameEn;
        } else {
            templateFilePath = messageTemplateFileNameEn;
        }
        logger.info("Using template {}", templateFilePath);
        return JsonMapper.getInstance().readValue(new File(templateFilePath), MessageTemplate.class);
    }

    private String combineTemplateWithParams(String templateStr, TemplateData templateData) {
        List<String> params = new ArrayList<>();
        params.add(templateData.getCustomerName());
        params.add(templateData.getCta1());
        params.add(templateData.getMessage());
        params.add(templateData.getSender());
        params.add(templateData.getProductName());
        params.add(templateData.getExpireDate());
        params.add(templateData.getCta2());
        if (templateData.getTransferMessage() != null && !templateData.getTransferMessage().isEmpty()) {
            params.add(templateData.getTransferMessage());
        } else {
            params.add("");
        }
        return String.format(templateStr, params.toArray());
    }
}

package asia.castis.evoucherservicefe.common.service.impl;

import asia.castis.evoucherservicefe.common.dto.messagetemplate.EnumTemplateKey;
import asia.castis.evoucherservicefe.common.dto.messagetemplate.MessageTemplate;
import asia.castis.evoucherservicefe.common.dto.messagetemplate.EnumTemplateType;
import asia.castis.evoucherservicefe.common.dto.messagetemplate.MsgTemplateData;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.common.model.publish.PublishModel;
import asia.castis.evoucherservicefe.common.service.MsgTemplateService;
import asia.castis.evoucherservicefe.common.utils.DateUtils;
import asia.castis.evoucherservicefe.exceptions.DecryptException;
import asia.castis.evoucherservicefe.exceptions.InvalidException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;


@Service
@Slf4j
public class MsgTemplateServiceImpl implements MsgTemplateService {
    @Value("${system.decrypt.vector:TotalRandoVector}")
    private String initVector;
    @Value("${system.decrypt.key:XaYbCz3579CzXaYb0246813579aBcDeF}")
    private String key;

    @Value("${customer.name.default.vi:Khách hàng}")
    private String customerNameDefaultVi;
    @Value("${customer.name.default.en:Customer}")
    private String customerNameDefaultEn;

    @Override
    public String decrypt(String input) throws InvalidException {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidException("Null or empty input");
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(input));
            return new String(original);
        } catch (Exception ex) {
            log.warn("Error decrypting message, use default value = {}, msg = {}", input, ex.getMessage());
            return input;
        }
    }

    @Override
    public void setTemplateValues(MessageTemplate msgTemplate, VoucherModel voucher, PublishModel publish) throws InvalidException, DecryptException {
        for (MsgTemplateData msgTemplateData : msgTemplate.getData()) {
            if (msgTemplateData == null || msgTemplateData.getType() == EnumTemplateType.DISABLED) {
                continue;
            }
            if (msgTemplateData.getType() == EnumTemplateType.FIXED) {
                msgTemplateData.setValue(msgTemplateData.getDefaultValue());
            }
            if (msgTemplateData.getType() == EnumTemplateType.CUSTOM) {
                msgTemplateData.setValue(getTemplateValueByKey(msgTemplate.getLanguage(), msgTemplateData, voucher, publish));
            }
        }
    }

    private String getTemplateValueByKey(String language, MsgTemplateData templateData, VoucherModel voucher, PublishModel publish)
            throws InvalidException {
        if (templateData.getKey() == EnumTemplateKey.customerName) {
            String customerName = voucher.getUserName();
            if (Objects.isNull(customerName) || customerName.isEmpty()) {
                if (language.equalsIgnoreCase("EN")) {
                    customerName = customerNameDefaultEn;
                } else {
                    customerName = customerNameDefaultVi;
                }
            }
            return getTemplateValue(templateData.isEncrypted(), templateData.getMaxLength(), customerName);
        } else if (templateData.getKey() == EnumTemplateKey.sender) {
            return getTemplateValue(templateData.isEncrypted(), templateData.getMaxLength(), publish.getSenderName());
        } else if (templateData.getKey() == EnumTemplateKey.message) {
            return getTemplateValue(templateData.isEncrypted(), templateData.getMaxLength(), publish.getMessageContent());
        } else if (templateData.getKey() == EnumTemplateKey.shortLink) {
            return getTemplateValue(templateData.isEncrypted(), templateData.getMaxLength(), voucher.getShortLink());
        } else if (templateData.getKey() == EnumTemplateKey.productName) {
            return getTemplateValue(templateData.isEncrypted(), templateData.getMaxLength(), voucher.getGoods().getName());
        } else if (templateData.getKey() == EnumTemplateKey.expireDate) {
            return getTemplateValue(templateData.isEncrypted(), templateData.getMaxLength(), DateUtils.toDateTimeString(voucher.getExpireDate()));
        } else if (templateData.getKey() == EnumTemplateKey.cta2) {
            return getTemplateValue(templateData.isEncrypted(), templateData.getMaxLength(), publish.getMessageCallingNumber());
        } else if (templateData.getKey() == EnumTemplateKey.transferMessage) {
            return getTemplateValue(templateData.isEncrypted(), templateData.getMaxLength(), voucher.getTransferMessage());
        } else if (templateData.getKey() == EnumTemplateKey.OTP) {
            return getTemplateValue(templateData.isEncrypted(), templateData.getMaxLength(), voucher.getChoiceToken());
        }
        log.warn("Can not find value by key");
        return " ";
    }

    private  String getTemplateValue(boolean encrypted, int maxLength, String value) throws InvalidException {
        if (value == null) {
            return "";
        }
        String result = encrypted ? decrypt(value) : value;
        if (maxLength < result.length()) {
            throw new InvalidException (String.format("Value=%s is longer than max length allowed=%d", value, maxLength));
        }
        return result;
    }

}

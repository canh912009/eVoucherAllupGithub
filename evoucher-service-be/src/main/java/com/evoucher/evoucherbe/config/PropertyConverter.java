package com.evoucher.evoucherbe.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class PropertyConverter implements AttributeConverter<String, String> {

    private final Logger LOGGER = LoggerFactory.getLogger(PropertyConverter.class);

    @Value("${spring.convertor.key}")
    @Setter
    private String key;

    private String initVector = "TotalRandoVector";

    public String encrypt(String input) {
        return convertToDatabaseColumn(input);
    }
    public String decrypt(String input) {
        return convertToEntityAttribute(input);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (!StringUtils.isBlank(attribute)) {
            try {
                IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

                byte[] encrypted = cipher.doFinal(attribute.getBytes());

                return Base64.encodeBase64String(encrypted);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        return null;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (!StringUtils.isBlank(dbData)) {
            dbData = dbData.replace("\r", "");
            dbData = dbData.replace("\n", "");
            try {
                IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

                byte[] original = cipher.doFinal(Base64.decodeBase64(dbData));
                return new String(original);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        return null;
    }

}

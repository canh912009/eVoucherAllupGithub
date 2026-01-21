package com.castis.publishservice.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;

@Component
@Slf4j
public class PropertyConverter implements AttributeConverter<String, String> {

    @Value("${spring.convertor.key}")
    private String key;

    private String initVector = "TotalRandoVector";

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
                log.error(ex.getMessage(), ex);
            }
        }
        return null;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (!StringUtils.isBlank(dbData)) {
            try {
                IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

                byte[] original = cipher.doFinal(Base64.decodeBase64(dbData));
                return new String(original);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return null;
    }
}

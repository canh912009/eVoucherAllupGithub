package com.evoucher.adminapi.common.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class PropertyConverter implements AttributeConverter<String, String> {


  @Value("${spring.convertor.key}")
  @Setter
  private String key;

  private final String initVector = "TotalRandoVector";

  @Override
  public String convertToDatabaseColumn(String attribute) {
    if (!StringUtils.isBlank(attribute)) {
      try {
        byte[] encrypted = encryptToByte(attribute);

        return Base64.encodeBase64String(encrypted);
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return null;
  }

  public byte[] encryptToByte(String attribute) throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {
    IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
    SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

    return cipher.doFinal(attribute.getBytes());
  }
  public String convertToDatabaseColumnForSearch(String attribute) {
    if (!StringUtils.isBlank(attribute)) {
      try {
        byte[] encrypted = encryptToByte(attribute);

        return Base64.encodeBase64String(encrypted, false);
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
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

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

  public static void main(String[] args) {
    PropertyConverter propertyConverter = new PropertyConverter();
    propertyConverter.setKey("XaYbCz3579CzXaYb0246813579aBcDeF");

    System.out.println(propertyConverter.convertToEntityAttribute("gQS9Pe5iRePxjieaxsmauw=="));
  }
}

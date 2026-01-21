package com.castis.publishservice.service;

import com.castis.publishservice.exception.defineException.BadRequestException;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Service
@Slf4j
public class DataService {
    @Value("${system.decrypt.vector:TotalRandoVector}")
    private String initVector;
    @Value("${system.decrypt.key:XaYbCz3579CzXaYb0246813579aBcDeF}")
    private String key;

    public String decrypt(String input) throws ServerRuntimeException {
        if (input == null || input.trim().isEmpty()) {
            throw new BadRequestException("Null or empty input");
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(input));
            return new String(original);
        } catch (Exception ex) {
            log.error("exception when decrypt data = {}", ex.getMessage());
            throw new ServerRuntimeException(ex.getMessage(), ex);
        }
    }

    public String encrypt(String input) throws ServerRuntimeException {
        if (StringUtils.isBlank(input)) {
            throw new BadRequestException("Null or empty input");
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(input.getBytes());

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            log.error("exception when encrypt data = {}", ex.getMessage());
            throw new ServerRuntimeException(ex.getMessage(), ex);
        }
    }
}

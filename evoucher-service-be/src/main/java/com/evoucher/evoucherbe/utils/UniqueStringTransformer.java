package com.evoucher.evoucherbe.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Base64;

@Slf4j
@Service
public class UniqueStringTransformer {

    public static final int NEW_STRING_SIZE = 6;

    public static String transform(String originalString) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Generate the hash value
            byte[] hashBytes = digest.digest(originalString.getBytes());

            // Convert the hash bytes to a string representation
            String hashedString = Base64.getEncoder().encodeToString(hashBytes);

            // Take the first 6 characters to ensure the output string length is 6
            return hashedString.substring(0, NEW_STRING_SIZE);
        } catch (Exception e) {
            log.error("can not transform, fallback to Apache random. msg={}", e.getMessage());
            return RandomStringUtils.randomAlphanumeric(NEW_STRING_SIZE);
        }
    }
}
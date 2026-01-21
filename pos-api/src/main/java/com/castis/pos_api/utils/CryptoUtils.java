package com.castis.pos_api.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class CryptoUtils {
    private static final String ALGORITHMS = "AES/ECB/PKCS5Padding";

    public static void main(String args[]) throws Exception {
        String encryptedPin = encrypt("1234567890761754", "4O71yov9m1VwhXs97XJ1xJjtyblCrPmG");
//        String encryptedPassword = encrypt("ycnIaDGS", "dfEv6tMV23n6aHsnRQhL0qHFywnHDnC40ychnYS3za0=");
//        String encryptedPhone = encrypt("0372594810", "dfEv6tMV23n6aHsnRQhL0qHFywnHDnC40ychnYS3za0=");

        System.out.println(encryptedPin);
//        System.out.println(encryptedPassword);
//        System.out.println(encryptedPhone);
    }

    /**
     * @param input     need to be encrypted
     * @param secretKey secret Key
     * @throws Exception
     */

    public static String encrypt(String input, String secretKey) throws Exception {
        if (input == null || input.isEmpty()) return "";

        Key key = generateKey(secretKey);
        Cipher c = Cipher.getInstance(ALGORITHMS);
        c.init(Cipher.ENCRYPT_MODE, key);

        byte[] encValue = c.doFinal(input.getBytes());
        byte[] encryptedByteValue = Base64.getEncoder().encode(encValue);

        return new String(encryptedByteValue);
    }

    /**
     * @param input     need to be encrypted
     * @param secretKey secret Key
     */
    public static String decrypt(String input, String secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Key key = generateKey(secretKey);
        Cipher c = Cipher.getInstance(ALGORITHMS);
        c.init(Cipher.DECRYPT_MODE, key);

        byte[] value = input.getBytes();
        byte[] encryptedValue = Base64.getDecoder().decode(value);
        byte[] decodedValue = c.doFinal(encryptedValue);

        return new String(decodedValue);
    }

    private static Key generateKey(String secretKey) {
        byte[] decodedKey = secretKey.getBytes();
        return new SecretKeySpec(decodedKey, "AES");
    }
}

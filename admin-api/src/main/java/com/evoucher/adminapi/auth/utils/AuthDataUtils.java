package com.evoucher.adminapi.auth.utils;


import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AuthDataUtils {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static SecureRandom random = new SecureRandom();


    public static String generateRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            sb.append(rndChar);
        }

        return sb.toString();
    }

    public static String generateRandomStringByByteLength(int byteSize) {
        SecureRandom random=new SecureRandom();
        byte[] data = new byte[byteSize];
        random.nextBytes(data);
        return Base64.getEncoder().encodeToString(data);
    }

    public static void main(String[] args) {
//        System.out.println(generateRandomString(12));
//        String byByteSize = generateRandomStringByByteLength(32);
//        System.out.println(byByteSize);
//        System.out.println(byByteSize.getBytes().length);
    }
}
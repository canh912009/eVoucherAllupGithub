package com.evoucher.evoucherbe.utils;


import java.security.SecureRandom;
import java.text.DecimalFormat;

public class DataUtils {
    private DataUtils() {

    }

    public static String getToken() {
        SecureRandom random = new SecureRandom();
        String inputString = "0123456789";
        int length = 6;

        StringBuilder randomStringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(inputString.length());
            char randomChar = inputString.charAt(index);

            randomStringBuilder.append(randomChar);
        }

        return randomStringBuilder.toString();
    }

    public static double roundingNumber(double num) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String roundedResultString = decimalFormat.format(num);
        return Double.parseDouble(roundedResultString);
    }
}

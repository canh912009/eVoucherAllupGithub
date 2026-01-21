package asia.castis.evoucher.api.common;

import java.util.Random;

public class StringUtils {
    public static String generateRandom(int length) {
        String aToZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
        Random rand = new Random();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randIndex = rand.nextInt(aToZ.length());
            res.append(aToZ.charAt(randIndex));
        }
        return res.toString();
    }
}

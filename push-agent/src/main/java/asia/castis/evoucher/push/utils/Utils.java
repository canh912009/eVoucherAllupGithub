package asia.castis.evoucher.push.utils;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.Base64;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

@Slf4j
public class Utils {

    private static String initVector = "TotalRandoVector";
    /*
    @Value("${DATABASE_CONVERTOR_KEY:XaYbCz3579CzXaYb0246813579aBcDeF}")
    private static String key;
    */
    private static String key = "XaYbCz3579CzXaYb0246813579aBcDeF";
    /*
    public static String cipherDecrypt(String dbData) {
        if (!io.micrometer.core.instrument.util.StringUtils.isBlank(dbData)) {
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
    public static String cipherEncrypt(String attribute) {
        if (!StringUtils.isBlank(attribute)) {
            try {
                IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

                byte[] encrypted = cipher.doFinal(attribute.toUpperCase().getBytes());

                return Base64.encodeBase64String(encrypted);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return null;
    }

     */
    public static String endCodeUnicodeMessage(String message) {
        String resultMessage;
        try {
            resultMessage = Base64.encodeBase64String(message.getBytes("UTF-8"));
        }catch (UnsupportedEncodingException unsupportedEncodingException) {
            resultMessage = Base64.encodeBase64String(message.getBytes());
        } catch (Exception e) {
            resultMessage = Base64.encodeBase64String(message.getBytes());
        }
        return resultMessage;
    }

}

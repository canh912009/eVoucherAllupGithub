package asia.castis.evoucherservicefe.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Component
public class CryptoUtils {
    @Value("${system.decrypt.vector:TotalRandoVector}")
    private String initVector;
    @Value("${system.decrypt.key:XaYbCz3579CzXaYb0246813579aBcDeF}")
    private String secretKey;

    public String encrypt(String input) throws Exception {
        if (!input.isEmpty()) {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(input.getBytes());
            String finalString = Base64.encodeBase64String(encrypted);
            log.info("Encrypt: {}->{}", input, finalString);
            return finalString;
        }
        return null;
    }

    public String decrypt(String input) throws Exception {
        if (!input.isEmpty()) {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(input));
            String finalString = new String(original);
            log.info("Decrypt: {}->{}", input, finalString);
            return finalString;
        }
        return null;
    }
}

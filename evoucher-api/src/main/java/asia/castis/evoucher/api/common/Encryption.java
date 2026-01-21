package asia.castis.evoucher.api.common;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Configuration
@Service
public class Encryption {
    private final Logger logger = LoggerFactory.getLogger(Encryption.class);
    private String initVector = "TotalRandoVector";
    @Value("${encryption.key}")
    private String key;

    public String encryptData(String value) {
        if (value != null && !value.equals("")) {
            try {
                IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

                byte[] encrypted = cipher.doFinal(value.toUpperCase().getBytes());

                return Base64.encodeBase64String(encrypted);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return null;
    }

    public String decryptData(String value) {
        if (Objects.nonNull(value) && !value.isEmpty()) {
            try {
                IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

                byte[] original = cipher.doFinal(Base64.decodeBase64(value));
                return new String(original);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        logger.warn("Decryption failed: null or empty value");
        return null;
    }
}

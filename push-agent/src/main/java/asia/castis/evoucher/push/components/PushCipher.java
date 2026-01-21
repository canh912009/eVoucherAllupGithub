package asia.castis.evoucher.push.components;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
/*
 * Use to encrypt and decrypt string
 * Use for sensitive infomation
 */
public class PushCipher {
    private String initVector;

    private String key;
    public PushCipher(String initVector, String key) {
       this.initVector = initVector;
       this.key = key;
    }
    public String cipherDecrypt(String dbData) {
        //log.info("cipherDecrypt: attribute args:{}", dbData);
        if (!io.micrometer.core.instrument.util.StringUtils.isBlank(dbData)) {
            try {
                IvParameterSpec iv = new IvParameterSpec(this.getInitVector().getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec(this.getKey().getBytes("UTF-8"), "AES");

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
    public String cipherEncrypt(String attribute) {
        //log.info("cipherEncrypt attribute args:{}", attribute);
        if (!StringUtils.isBlank(attribute)) {
            try {
                IvParameterSpec iv = new IvParameterSpec(this.getInitVector().getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec(this.getKey().getBytes("UTF-8"), "AES");

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


    public String getInitVector() {
        return this.initVector;
    }

    public void setInitVector(String initVector) {
        this.initVector = initVector;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}

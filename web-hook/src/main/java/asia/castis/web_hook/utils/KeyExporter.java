package asia.castis.web_hook.utils;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Base64;

@Slf4j
public class KeyExporter {

    public static void exportPublicKeyFromKeyStore(String keyStorePath, String keyStorePassword, String alias, String outputPath) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(keyStorePath)) {
            keyStore.load(fis, keyStorePassword.toCharArray());
        }

        Certificate cert = keyStore.getCertificate(alias);
        if (cert == null) {
            throw new RuntimeException("Certificate not found for alias: " + alias);
        }

        PublicKey publicKey = cert.getPublicKey();

        try (Writer writer = new FileWriter(outputPath)) {
            writer.write("-----BEGIN PUBLIC KEY-----\n");
            writer.write(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(publicKey.getEncoded()));
            writer.write("\n-----END PUBLIC KEY-----\n");
        }

        log.info("Public key has been written to: {}", outputPath);
    }
}

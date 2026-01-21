package asia.castis.web_hook.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.security.cert.Certificate;

@Slf4j
public class CryptoUtils {

    public static void main(String[] args) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("code", "WATANE2023456789");
        jsonMap.put("serial_number", "SN-9876543");
        jsonMap.put("status", 1);
        jsonMap.put("redeemed_at", "2025-04-21T10:30:45+07:00");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(jsonMap);

        // Tạo cặp khóa RSA 2048 để test (bạn nên thay bằng khóa thật đã cấp)
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Ký nội dung JSON
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(privateKey);
        rsa.update(json.getBytes("UTF-8"));
        byte[] signedBytes = rsa.sign();

        // Chữ ký dạng Base64
        String signatureBase64 = Base64.getEncoder().encodeToString(signedBytes);
        System.out.println("✅ Signature (Base64, length=" + signatureBase64.length() + "):");
        System.out.println(signatureBase64);

        // Ví dụ xác thực lại chữ ký
        Signature verifier = Signature.getInstance("SHA1withRSA");
        verifier.initVerify(publicKey);
        verifier.update(json.getBytes("UTF-8"));
        boolean isValid = verifier.verify(Base64.getDecoder().decode(signatureBase64));
        System.out.println("✅ Signature valid: " + isValid);
    }

    public static PrivateKey loadPrivateKey(String keyStorePass, String path, String alias, String keyPass) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        char[] password = keyStorePass.toCharArray();

        try (FileInputStream fis = new FileInputStream(path)) {
            keyStore.load(fis, password);
        }

        return (PrivateKey) keyStore.getKey(alias, keyPass.toCharArray());
    }

    public static PrivateKey loadPrivateKey(String path) {
        log.info("Loading private key from: {}", path);
        try {
            String key = Files.readString(Paths.get(path), StandardCharsets.UTF_8);

            key = key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key from file system: " + e.getMessage(), e);
        }
    }

    public static void exportPublicKeyFromKeyStore(String keyStorePath, String keyStorePassword, String alias, String outputPath) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(keyStorePath)) {
            keyStore.load(fis, keyStorePassword.toCharArray());
        }

        // Lấy certificate chứa public key
        Certificate cert = keyStore.getCertificate(alias);
        if (cert == null) {
            throw new RuntimeException("Không tìm thấy certificate với alias: " + alias);
        }

        PublicKey publicKey = cert.getPublicKey();

        // Ghi public key ra file (PEM format)
        try (Writer writer = new FileWriter(outputPath)) {
            writer.write("-----BEGIN PUBLIC KEY-----\n");
            writer.write(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(publicKey.getEncoded()));
            writer.write("\n-----END PUBLIC KEY-----\n");
        }

        System.out.println("Public key đã được ghi vào: " + outputPath);
    }


    public static PublicKey loadPublicKey(String path) {
        log.info("Loading public key from: {}", path);
        try {
            String key = Files.readString(Paths.get(path), StandardCharsets.UTF_8);

            key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key from file system: " + e.getMessage(), e);
        }
    }

    public static boolean verifyRsaSha1Signature(PublicKey publicKey, String originalData, String base64Signature) {
        try {
            if (base64Signature == null || base64Signature.trim().isEmpty()) {
                log.warn("Empty signature provided");
                return false;
            }

            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);
            signature.update(originalData.getBytes(StandardCharsets.UTF_8));

            byte[] decodedSignature;
            try {
                decodedSignature = Base64.getDecoder().decode(base64Signature);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid Base64 signature format: {}", base64Signature);
                return false;
            }

            return signature.verify(decodedSignature);
        } catch (Exception e) {
            log.error("Error verifying RSA SHA1 signature: {}", e.getMessage(), e);
            throw new RuntimeException("Error verifying RSA SHA1 signature: " + e.getMessage(), e);
        }
    }

    public static String signRsaSha1(PrivateKey privateKey, String originalData) {
        try {
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
            log.info("Key size: " + rsaPrivateKey.getModulus().bitLength());

            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKey);
            signature.update(originalData.getBytes(StandardCharsets.UTF_8));

            byte[] signedBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signedBytes);
        } catch (Exception e) {
            log.error("Error signing data with RSA SHA1: {}", e.getMessage(), e);
            throw new RuntimeException("Error signing data with RSA SHA1: " + e.getMessage(), e);
        }
    }

}

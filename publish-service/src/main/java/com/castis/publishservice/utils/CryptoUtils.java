package com.castis.publishservice.utils;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CryptoUtils {
    public static PublicKey publicKey;
    public static PrivateKey privateKey;
    public static Certificate certificate;
    private static String keyPath = "/Users/daont/tools/java_jdk/jdk-11.0.23.jdk/Contents/Home/bin/urboxStore.p12";
    public static void main(String[] args) throws Exception {
        privateKey = loadPrivateKey("castis", keyPath, "urboxsignkey", "castis");
        String sign = sign("{\"app_id\":500000282,\"app_secret\":\"f8876e0e88b69b1aa1b4411c71931adf\",\"campaign_code\":\"UG723322\",\"dataBuy\":[{\"priceId\":\"5074\",\"quantity\":5,\"amount\":null}],\"isSendSms\":0,\"site_user_id\":\"AQUA_E_VOUCHER\",\"transaction_id\":\"e6475468-3f42-4540-bd54-9e76f088a2a8\"}", privateKey);
        System.out.println(sign);
        System.out.println("\n");
//        System.out.println(getPublicKeyAsString(publicKey));

        File file = new File("/Users/daont/tools/java_jdk/jdk-11.0.23.jdk/Contents/Home/bin/pubkey.pem");
        String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());

        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        PublicKey publicKey1 =  keyFactory.generatePublic(keySpec);
        System.out.println(verify("hello", sign, publicKey1));
//        System.out.println(Base64.getDecoder().);
    }
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Độ dài khóa 2048 bits
        return keyPairGenerator.generateKeyPair();
    }

    // Phương thức tạo chữ ký
    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }

    public static String signSha1(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA1withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }

    public static boolean verify(String cipherText, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        byte[] bytes = Base64.getDecoder().decode(cipherText.getBytes(UTF_8));
        publicSignature.update(bytes);
        return publicSignature.verify(bytes);
    }

    public static boolean verify(String data, String signature, Certificate certificate) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(certificate);
        publicSignature.update(data.getBytes(UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }

    public static boolean verify(String data, String signature, PublicKey certificate) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(certificate);
        publicSignature.update(data.getBytes(UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }

    public static PrivateKey loadPrivateKey(String keyStorePass, String path, String alias, String keyPass) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        char[] password = keyStorePass.toCharArray(); // Mật khẩu cho KeyStore

        // Tải KeyStore từ tệp tin (nếu tồn tại)
        try (FileInputStream fis = new FileInputStream(path)) {
            keyStore.load(fis, password);
        }

        // Tạo một cặp khóa RSA
        return (PrivateKey) keyStore.getKey(alias, keyPass.toCharArray());
    }

    public static Certificate getCertificate(String certificatePath)
            throws Exception {
        CertificateFactory certificateFactory = CertificateFactory
                .getInstance("X.509");
        FileInputStream in = new FileInputStream(certificatePath);

        Certificate certificate = certificateFactory
                .generateCertificate(in);
        in.close();

        return certificate;
    }
    private static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    private static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public static String decrypt(String encryptedMessage) throws Exception {
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("SHA256withRSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
        return new String(decryptedMessage, "UTF8");
    }



}

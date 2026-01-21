package com.evoucher.adminapi.common.utils;

import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CryptoUtils {
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Độ dài khóa 2048 bits
        return keyPairGenerator.generateKeyPair();
    }

    // Phương thức tạo chữ ký
    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA1withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }

    public static boolean verify(String cipherText, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA1withRSA");
        publicSignature.initVerify(publicKey);
        byte[] bytes = Base64.getDecoder().decode(cipherText.getBytes(UTF_8));
        publicSignature.update(bytes);
        return publicSignature.verify(bytes);
    }

    public static boolean verify(String data, String signature, Certificate certificate) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA1withRSA");
        publicSignature.initVerify(certificate);
        publicSignature.update(data.getBytes(UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }

    public static boolean verify(String data, String signature, PublicKey certificate) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA1withRSA");
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

}

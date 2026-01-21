package com.evoucher.partner.service.config;

import com.evoucher.partner.service.exception.CryptoException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.websocket.DecodeException;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class CryptoUtils {
    public static PublicKey publicKey;
    public static PrivateKey privateKey;
    public static Certificate certificate;
    private static final String ALGORITHMS = "SHA1withRSA";
    private static final String TRI_DEC_ALGORITHMS = "DESede";
    private static final String MD5_ALGORITHMS = "MD5";
    public static void main(String[] args) throws Exception {
//        privateKey = loadPrivateKey("castis", "/Users/daont/tools/java_jdk/jdk-11.0.23.jdk/Contents/Home/bin/urboxStore.p12", "urboxsignkey", "castis");
////        certificate = getCertificate("/Users/daont/tools/java_jdk/jdk-11.0.23.jdk/Contents/Home/bin/pubkey.pem");
////        String sign = sign("{\"app_id\":500000282,\"app_secret\":\"f8876e0e88b69b1aa1b4411c71931adf\",\"campaign_code\":\"UG723322\",\"dataBuy\":[{\"priceId\":\"5074\",\"quantity\":5,\"amount\":null}],\"isSendSms\":0,\"site_user_id\":\"AQUA_E_VOUCHER\",\"transaction_id\":\"e6475468-3f42-4540-bd54-9e76f088a2a8\"}", privateKey);
//        String sign = sign("hello", privateKey);
//        System.out.println(sign);
//        System.out.println("\n");
////        System.out.println(getPublicKeyAsString(publicKey));
//
//        File file = new File("/Users/daont/tools/java_jdk/jdk-11.0.23.jdk/Contents/Home/bin/pubkey.pem");
//        String key = Files.readString(file.toPath(), Charset.defaultCharset());
//
//        String publicKeyPEM = key
//                .replace("-----BEGIN PUBLIC KEY-----", "")
//                .replaceAll(System.lineSeparator(), "")
//                .replace("-----END PUBLIC KEY-----", "");
//
//        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
//
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
//        PublicKey publicKey1 =  keyFactory.generatePublic(keySpec);
//        System.out.println(verify("hello", sign, publicKey1));
//        System.out.println(Base64.getDecoder().);
        String encrypted = triDescEncrypt("hello", "12345");
        log.info("encrypted: {}", encrypted);

        log.info("decrypted: {}", triDescDecrypt(encrypted, "12345"));
    }
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Độ dài khóa 2048 bits
        return keyPairGenerator.generateKeyPair();
    }

    // Phương thức tạo chữ ký
    public static String sign(String plainText, PrivateKey privateKey) throws CryptoException {
        try {
            Signature privateSignature = Signature.getInstance(ALGORITHMS);
            privateSignature.initSign(privateKey);
            privateSignature.update(plainText.getBytes(UTF_8));

            byte[] signature = privateSignature.sign();

            return Base64.getEncoder().encodeToString(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new CryptoException(e.getMessage());
        }
    }

    public static boolean verify(String cipherText, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance(ALGORITHMS);
        publicSignature.initVerify(publicKey);
        byte[] bytes = Base64.getDecoder().decode(cipherText.getBytes(UTF_8));
        publicSignature.update(bytes);
        return publicSignature.verify(bytes);
    }

    public static boolean verify(String data, String signature, Certificate certificate) throws Exception {
        Signature publicSignature = Signature.getInstance(ALGORITHMS);
        publicSignature.initVerify(certificate);
        publicSignature.update(data.getBytes(UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }

    public static boolean verify(String data, String signature, PublicKey certificate) throws Exception {
        Signature publicSignature = Signature.getInstance(ALGORITHMS);
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

    public static PrivateKey loadPrivateKey(String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
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
    private static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public static String decrypt(String encryptedMessage) throws DecodeException {
        try {
            byte[] encryptedBytes = decode(encryptedMessage);
            Cipher cipher = Cipher.getInstance(ALGORITHMS);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
            return new String(decryptedMessage, UTF_8);
        } catch (NoSuchPaddingException| NoSuchAlgorithmException| InvalidKeyException| IllegalBlockSizeException| BadPaddingException e) {
            throw new CryptoException(e.getMessage());
        }
    }

    public static String triDescEncrypt(String plainText, String key) {
        try {
            byte[] arrayBytes = getValidKey(key);
            KeySpec ks = new DESedeKeySpec(arrayBytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(TRI_DEC_ALGORITHMS);
            Cipher cipher = Cipher.getInstance(TRI_DEC_ALGORITHMS);
            SecretKey seckey = skf.generateSecret(ks);
            //
            cipher.init(Cipher.ENCRYPT_MODE, seckey);
            byte[] plainByte = plainText.getBytes(UTF_8);
            byte[] encryptedByte = cipher.doFinal(plainByte);
            return Base64.getEncoder().encodeToString(encryptedByte);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CryptoException(e.getMessage());
        }
    }

    public static String triDescDecrypt(String encryptData, String key) throws CryptoException {
        try {
            byte[] arrayBytes = getValidKey(key);
            KeySpec ks = new DESedeKeySpec(arrayBytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(TRI_DEC_ALGORITHMS);
            Cipher cipher = Cipher.getInstance(TRI_DEC_ALGORITHMS);
            SecretKey seckey = skf.generateSecret(ks);
            //
            cipher.init(Cipher.DECRYPT_MODE, seckey);
            byte[] encryptByte = Base64.getDecoder().decode(encryptData);
            byte[] plainByte = cipher.doFinal(encryptByte);
            return new String(plainByte);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CryptoException(e.getMessage());
        }
    }
    private static byte[] getValidKey(String key) throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        md.update(key.getBytes(StandardCharsets.ISO_8859_1), 0, key.length());
        byte[] md5hash = md.digest();
        String hashed =  convertToHex(md5hash);
        return hashed.substring(0, 24).getBytes();
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (byte datum : data) {
            int halfbyte = (datum >>> 4) & 0x0F;
            int twoHalf = 0;
            do {
                if (halfbyte <= 9)
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = datum & 0x0F;
            } while (twoHalf++ < 1);
        }
        return buf.toString();
    }

}

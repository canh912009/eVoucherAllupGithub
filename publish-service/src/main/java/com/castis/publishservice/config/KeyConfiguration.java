package com.castis.publishservice.config;

import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class KeyConfiguration {

  	@Value("${rsa.evoucher.private-key}")
  	private String evoucherPrivateKey;

  	@Value("${rsa.evoucher.public-key}")
  	private String evoucherPublishKey;

  	@Value("${rsa.gift-pop.public-key}")
  	private String giftPopPublicKey;

  @Bean
  public RSAKey rsaEVoucherKey() throws GeneralSecurityException {
    java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");

    RSAPrivateKey privateKey =
        (RSAPrivateKey)
            keyFactory.generatePrivate(
                new PKCS8EncodedKeySpec(
                    Base64.decodeBase64(evoucherPrivateKey)));

    RSAPublicKey publicKey =
        (RSAPublicKey)
            keyFactory.generatePublic(
                new X509EncodedKeySpec(Base64.decodeBase64(evoucherPublishKey)));

    return new RSAKey.Builder(publicKey).privateKey(privateKey).build();
  }

  @Bean
  public RSASSAVerifier RSASSAVerifierGiftPop() throws GeneralSecurityException {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec keySpec =
            new X509EncodedKeySpec(Base64.decodeBase64(giftPopPublicKey));

    return new RSASSAVerifier((RSAPublicKey) keyFactory.generatePublic(keySpec));
  }
}

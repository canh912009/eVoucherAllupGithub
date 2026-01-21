package com.castis.publishservice.service.external_pin;

import com.castis.publishservice.dto.request.ur_box.UrBoxSignData;
import com.castis.publishservice.exception.defineException.SignatureGeneratingException;
import com.castis.publishservice.utils.CryptoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignatureService {
    private final ObjectMapper objectMapper;

    @Value("${ur-box.key-store.path}")
    private String urBoxKeyStorePath;
    @Value("${ur-box.key-store.password}")
    private String urBoxKeyStorePassword;
    @Value("${ur-box.key-store.alias}")
    private String urBoxKeyAlias;
    @Value("${ur-box.key-store.key.password}")
    private String urBoxKeyPassword;
    private PrivateKey urBoxPrivateKey;

    @PostConstruct
    private void loadKeys() throws Exception {
        urBoxPrivateKey = CryptoUtils.loadPrivateKey(urBoxKeyStorePassword, urBoxKeyStorePath, urBoxKeyAlias, urBoxKeyPassword);
    }

    public String sign(UrBoxSignData signData) {
        try {
            log.info("make ur box sign");
            log.info("{}", signData);
            String plainText = objectMapper.writeValueAsString(signData);
            log.info("plain text: {}", plainText);
            return CryptoUtils.sign(plainText, urBoxPrivateKey);
        }catch (SignatureGeneratingException e) {
            throw e;
        } catch (Exception e) {
            log.error("exception when generate ur box digital signature");
            log.error(e.getMessage(), e);
            throw new SignatureGeneratingException(e.getMessage());
        }
    }

}

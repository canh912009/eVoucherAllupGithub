package com.castis.publishservice.service.external_pin.watane;

import com.castis.publishservice.exception.defineException.CustomCodeException;
import com.castis.publishservice.utils.CryptoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;

@Service
@Slf4j
@RequiredArgsConstructor
public class WataneSignatureService {

    @Value("${watane.key-store.path}")
    private String wataneKeyStorePath;
    @Value("${watane.key-store.password}")
    private String wataneKeyStorePassword;
    @Value("${watane.key-store.alias}")
    private String wataneKeyAlias;
    @Value("${watane.key-store.key.password}")
    private String wataneKeyPassword;
    private PrivateKey watanePrivateKey;

    @PostConstruct
    private void loadKeys() throws Exception {
        watanePrivateKey = CryptoUtils.loadPrivateKey(wataneKeyStorePassword, wataneKeyStorePath, wataneKeyAlias, wataneKeyPassword);
    }

    public String sign(String signData) {
        try {
            return CryptoUtils.signSha1(signData, watanePrivateKey);
        } catch (Exception e) {
            throw new CustomCodeException(
                    WataneErrorCode.ERROR_WHILE_CREATING_REQUEST.getMessage(),
                    WataneErrorCode.ERROR_WHILE_CREATING_REQUEST.getAquaCode(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

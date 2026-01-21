package asia.castis.web_hook.serivce.external_sys;

import asia.castis.web_hook.bean.dto.request.WataneHeaders;
import asia.castis.web_hook.bean.dto.request.WataneRequest;
import asia.castis.web_hook.bean.dto.response.WataneResponse;
import asia.castis.web_hook.exception.InvalidCredentialException;
import asia.castis.web_hook.exception.InvalidSignatureException;
import asia.castis.web_hook.exception.defined.BadRequestException;
import asia.castis.web_hook.utils.Common;
import asia.castis.web_hook.utils.CryptoUtils;
import asia.castis.web_hook.utils.KeyExporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class WataneService {

    @Value("${watane.key-store.path}")
    private String wataneKeyStorePath;

    @Value("${watane.key-store.password}")
    private String wataneKeyStorePassword;

    @Value("${watane.key-store.alias}")
    private String wataneKeyAlias;

    @Value("${watane.key-store.key.password}")
    private String wataneKeyPassword;

    @Value("${watane.ipn.username}")
    @Getter
    private String username;

    @Value("${watane.ipn.credential}")
    @Getter
    private String credential;

    @Value("${watane.ipn.public-key-path}")
    private String watanePublicKeyPath;

    private PrivateKey aquaPrivatekey;
    private PublicKey watanePublicKey;

    @PostConstruct
    public void init() {
        try {
            this.aquaPrivatekey = CryptoUtils.loadPrivateKey(wataneKeyStorePassword, wataneKeyStorePath, wataneKeyAlias, wataneKeyPassword);
            this.watanePublicKey = CryptoUtils.loadPublicKey(watanePublicKeyPath);

//            testSignature();
//            String aquaPublicKeyPath = "E:\\Projects\\E-Voucher\\Projects\\web-hook\\src\\main\\resources\\keys\\castis_watane_1024_public_key.pem";
//            KeyExporter.exportPublicKeyFromKeyStore(wataneKeyStorePath, wataneKeyStorePassword, wataneKeyAlias, aquaPublicKeyPath);

            log.info("WataneService initialized");
        } catch (Exception e) {
            log.error("Error initializing WataneService: {}", e.getMessage(), e);
        }
    }

    private void testSignature() {
        try {
            WataneRequest request = new WataneRequest();
            request.setVoucherSerial("0000246388");
            request.setVoucherStatus(1);

            String rawDate = "22/04/2025 10:30:45";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            LocalDateTime localDateTime = LocalDateTime.parse(rawDate, formatter);
            Instant instant = localDateTime.atZone(ZoneOffset.UTC).toInstant();

            Date redeemedDate = Date.from(instant);
            request.setRedeemedAt(redeemedDate);

            String json = Common.toJsonBody(request);

            // ~Test: Set Watane private key is same as Aqua private key
            PrivateKey watanePrivatekey = aquaPrivatekey;
            String signature = CryptoUtils.signRsaSha1(watanePrivatekey, json);

            log.info("🔐 Generated Test Signature (Base64): {}", signature);
        } catch (Exception e) {
            log.error("Error generating test signature: {}", e.getMessage(), e);
        }
    }

    public void validate(WataneHeaders headers, WataneRequest request) {
        log.info("Validating headers from Watane: {}", headers);
        log.info("Validating request from Watane: {}", request);

        // 1. Validate username and credential
        if (!username.equals(headers.getUsername()) || !credential.equals(headers.getCredential())) {
            log.error("Invalid username or credential");
            throw new InvalidCredentialException("Invalid username or credential");
        }

        // 2. Validate digital signature
        String rawRequestJson = Common.toJsonBody(request);
        log.info("Raw request JSON used for signature verification: {}", rawRequestJson);

        boolean isValid = CryptoUtils.verifyRsaSha1Signature(watanePublicKey, rawRequestJson, headers.getSignature());

        if (!isValid) {
            log.error("Invalid digital signature");
            throw new InvalidSignatureException("Invalid digital signature");
        }

        // 3. Validate request status
        if (request.getVoucherStatus() != 1) {
            log.error("Invalid status: {}", request.getVoucherStatus());
            throw new BadRequestException("Invalid status: must be 1");
        }
    }

    public String sign(WataneResponse response) {
        try {
            log.info("Signing response: {}", response);

            String json = Common.toJsonBody(response);
            String signature = CryptoUtils.signRsaSha1(aquaPrivatekey, json);

            log.info("Response signed successfully.");
            return signature;
        } catch (Exception e) {
            log.error("Failed to sign response: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to sign response");
        }
    }
}

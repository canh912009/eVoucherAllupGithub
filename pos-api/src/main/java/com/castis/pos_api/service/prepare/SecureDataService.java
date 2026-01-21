package com.castis.pos_api.service.prepare;

import com.castis.pos_api.dto.request.*;
import com.castis.pos_api.dto.response.VoucherResponse;
import com.castis.pos_api.exception.ApplicationException;
import com.castis.pos_api.utils.CryptoUtils;
import com.castis.pos_api.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class SecureDataService {
    private SecureDataService() {
    }

    public static void decryptData(ValidatingRequest request, String secret) {
        try {
            log.info("start decrypt validating request: {}", request);
            String decryptedKey = CryptoUtils.decrypt(request.getKey(), secret);
            String decryptedPassword = "";
            if (request.getPassword() != null && request.getPassword().isEmpty()) {
                decryptedPassword = CryptoUtils.decrypt(request.getPassword(), secret);
            }
            String decryptedPhoneNum = "";
            if (request.getUserPhoneNo() != null && request.getUserPhoneNo().isEmpty()) {
                decryptedPhoneNum = CryptoUtils.decrypt(request.getUserPhoneNo(), secret);
            }
            log.info("decrypt request successfully: {key:{}, pwd:{}, phone:{}}", decryptedKey, decryptedPassword, decryptedPhoneNum);

            request.setPassword(decryptedPassword);
            request.setKey(decryptedKey);
            request.setUserPhoneNo(decryptedPhoneNum);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E6002_ENCRYPTION_ERROR.getCode(), e.getMessage());
        }
    }

    public static void decryptData(FinalizeSingleRequest request, String secret) {
        try {
            log.info("start decrypt prepaid request: {}", request);
            String decryptedKey = CryptoUtils.decrypt(request.getKey(), secret);
            String decryptedPassword = "";
            if (request.getPassword() != null && request.getPassword().isEmpty()) {
                decryptedPassword = CryptoUtils.decrypt(request.getPassword(), secret);
            }
            log.info("decrypt request successfully: {key:{}, pwd:{}}}", decryptedKey, decryptedPassword);

            request.setKey(decryptedKey);
            request.setPassword(decryptedPassword);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E6002_ENCRYPTION_ERROR.getCode(), e.getMessage());
        }
    }

    public static void decryptData(FinalizeListRequest request, String secret) {
        try {
            log.info("start decrypt Single item request: {}", request);
            List<SingleItem> decryptedItems = new ArrayList<>();
            for (SingleItem item : request.getData()) {
                SingleItem newItem = new SingleItem();
                String decryptedKey = CryptoUtils.decrypt(item.getKey(), secret);
                String decryptedPassword = "";
                if (item.getPassword() != null && item.getPassword().isEmpty()) {
                    decryptedPassword = CryptoUtils.decrypt(item.getPassword(), secret);
                }
                log.info("decrypt request successfully: {key:{}, pwd:{}}}", decryptedKey, decryptedPassword);

                newItem.setKey(decryptedKey);
                newItem.setPassword(decryptedPassword);
                newItem.setKeyType(item.getKeyType());

                decryptedItems.add(newItem);
            }
            request.setData(decryptedItems);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E6002_ENCRYPTION_ERROR.getCode(), e.getMessage());
        }
    }

    public static void decryptData(HavingTransactionId request, String secret) {
        try {
            log.info("start decrypt finalizing request: {}", request);
            String decryptedTransactionId = CryptoUtils.decrypt(request.getTransactionId(), secret);
            log.info("decrypt request successfully");

            request.setTransactionId(decryptedTransactionId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E6002_ENCRYPTION_ERROR.getCode(), e.getMessage());
        }
    }

    public static void encryptData(VoucherResponse voucher, String secret) throws ApplicationException {
        try {
            log.info("encrypt response data");
            String encryptedEv = CryptoUtils.encrypt(voucher.getEv(), secret);
            String encryptedTransactionId = CryptoUtils.encrypt(voucher.getTransactionId(), secret);
            String encryptedPhoneNo = CryptoUtils.encrypt(voucher.getUserPhoneNo(), secret);

            voucher.setEv(encryptedEv);
            voucher.setTransactionId(encryptedTransactionId);
            voucher.setUserPhoneNo(encryptedPhoneNo);
            log.info("ev, transaction id, user phone no were encrypted");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E6002_ENCRYPTION_ERROR.getCode(), e.getMessage());
        }
    }

    public static void encryptData(TransactionIdOnlyResponse response, String secret) throws ApplicationException {
        try {
            log.info("encrypt transaction only response");
            String encryptedTransactionId = CryptoUtils.encrypt(response.getTransactionId(), secret);
            response.setTransactionId(encryptedTransactionId);
            log.info("success encryption");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E6002_ENCRYPTION_ERROR.getCode(), e.getMessage());
        }
    }
}

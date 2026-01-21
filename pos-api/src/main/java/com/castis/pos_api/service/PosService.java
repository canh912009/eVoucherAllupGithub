package com.castis.pos_api.service;

import com.castis.pos_api.clients.ServiceBeClient;
import com.castis.pos_api.dto.PosTransactionDto;
import com.castis.pos_api.dto.VoucherDto;
import com.castis.pos_api.dto.request.*;
import com.castis.pos_api.dto.request.third_party.ServiceBeUsingVoucherReq;
import com.castis.pos_api.dto.response.ResponseData;
import com.castis.pos_api.dto.response.VoucherResponse;
import com.castis.pos_api.dto.response.third_party.ServiceBeBaseResponse;
import com.castis.pos_api.entity.Brand;
import com.castis.pos_api.entity.Voucher;
import com.castis.pos_api.enum_constant.EnumValidYn;
import com.castis.pos_api.enum_constant.PosKeyType;
import com.castis.pos_api.enum_constant.VoucherTypeCode;
import com.castis.pos_api.exception.ApplicationException;
import com.castis.pos_api.mapper.RequestMapper;
import com.castis.pos_api.mapper.VoucherMapper;
import com.castis.pos_api.repositories.BrandRepository;
import com.castis.pos_api.repositories.VoucherRepository;
import com.castis.pos_api.service.basic.PosTransactionBasicService;
import com.castis.pos_api.service.basic.VoucherBasicService;
import com.castis.pos_api.service.prepare.ServiceBeBeanResolve;
import com.castis.pos_api.service.validator.BrandValidator;
import com.castis.pos_api.service.validator.VoucherValidator;
import com.castis.pos_api.utils.CustomResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.castis.pos_api.service.prepare.SecureDataService.decryptData;
import static com.castis.pos_api.service.prepare.SecureDataService.encryptData;

@Service
@RequiredArgsConstructor
@Slf4j
public class PosService {
    private static final RequestMapper requestMapper = RequestMapper.INSTANCE;
    private static final VoucherMapper voucherMapper = VoucherMapper.INSTANCE;

    private final ObjectMapper objectMapper;
    private final BrandRepository brandRepository;
    private final VoucherBasicService voucherService;
    private final VoucherRepository voucherRepository;
    private final PosTransactionBasicService transactionService;
    private final ServiceBeClient serviceBeClient;
    private final BrandValidator brandValidator;

    public ResponseData<VoucherResponse> getVoucherDetail(String appId, @Valid ValidatingRequest request) {
        log.info("validate: {}", request);

        PosTransactionDto transaction = transactionService.makeNewByValidatingRequest(request, appId);
        try {
            Brand brand = getBrand(appId);

            // decrypt encrypted data
            decryptData(request, brand.getEncryptionKey());

            // Validate
            VoucherDto voucher = getVoucherDto(request.getKeyType(), request.getKey(), request.getPassword());
            brandValidator.validateBrandRelatedReturnBrandId(brand, request.getBrandId(), request.getStoreId(), voucher.getGoods().getId());

            VoucherResponse result = voucherMapper.toResponse(voucher);
            transaction.setEv(voucher.getId());
            result.setTransactionId(transaction.getTransactionId());
            result.setVoucherType(voucher.getVoucherTypeCd().ordinal());

            encryptData(result, brand.getEncryptionKey());
            transaction.success(objectMapper.writeValueAsString(result));
            return new ResponseData<>(result);

        } catch (ApplicationException e) {
            transaction.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            transaction.error(e.getMessage());
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        } finally {
            transactionService.saveDto(transaction);
        }
    }

    private @NotNull VoucherDto getVoucherDto(Integer keyType, String key, String password) {
        List<VoucherDto> vouchers = new ArrayList<>();
        if (PosKeyType.PIN.getCode() == keyType) {
            Voucher voucher = voucherRepository.findById(key)
                    .orElseThrow(() -> new ApplicationException(CustomResponse.E4404_NOT_FOUND.getCode(), "Voucher not found"));
            vouchers.add(voucherMapper.toDto(voucher));
        } else if (PosKeyType.SERIAL_NO.getCode() == keyType) {
            vouchers = voucherService.findUsableVoucherBySerialNumber(key);
        } else {
            throw new ApplicationException(CustomResponse.E4101_INVALID_REQUEST.getCode(), "Invalid key type");
        }
        // get only one valid voucher
        VoucherDto voucher = VoucherValidator.getUniqueUsableVoucher(vouchers);

        if (Objects.nonNull(voucher.getExternalPinPassword()) && !voucher.getExternalPinPassword().isEmpty()) {
            VoucherValidator.validateVoucherPassword(voucher, password);
        }
        VoucherValidator.validateVoucherType(voucher);
        return voucher;
    }

    private @NotNull Brand getBrand(String appId) {
        return brandRepository.findByAppIdAndValidYn(appId, EnumValidYn.Y)
                .orElseThrow(() -> new ApplicationException(CustomResponse.E4404_NOT_FOUND.getCode(), "Brand not found"));
    }

    public ResponseData<TransactionIdOnlyResponse> finalizePrePaid(String appId, @NotNull FinalizeSingleRequest request) {
        PosTransactionDto transaction = transactionService.makeNewByFinalizePrepaid(request, appId);
        try {
            log.info("Finalize single request: {}", objectMapper.writeValueAsString(request));

            Brand brand = getBrand(appId);

            // decrypt encrypted data
            decryptData(request, brand.getEncryptionKey());

            TransactionIdOnlyResponse response = new TransactionIdOnlyResponse(transaction.getTransactionId());

            // find voucher by saved ev
            VoucherDto voucher = getVoucherDto(request.getKeyType(), request.getKey(), request.getPassword());

            if (!voucher.getVoucherTypeCd().equals(VoucherTypeCode.PP)) {
                throw new ApplicationException(CustomResponse.E4101_INVALID_REQUEST.getCode(), "Only allow prepaid voucher");
            }

            log.info("validate voucher balance");
            VoucherValidator.validateBalance(voucher, request.getPrepaidAmount());

            log.info("create request body to service be for using voucher");
            ServiceBeUsingVoucherReq beRequest = ServiceBeUsingVoucherReq.builder()
                    .voucherId(voucher.getId())
                    .exchangeAmount(request.getPrepaidAmount())
                    .storeId(request.getStoreId())
                    .transactionDate(new Date()).build();

            ServiceBeBeanResolve.setDefaultValueForExchange(beRequest);

            ServiceBeBaseResponse beBaseResponse = serviceBeClient.requestUsingVoucher(beRequest);

            postRequestServiceBeHandle(beBaseResponse, transaction, brand.getEncryptionKey(), response);
            if (beBaseResponse.isOk()) {
                return new ResponseData<>(response);
            }
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error(e.getMessage());
            transaction.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            transaction.error(e.getMessage());
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        } finally {
            transactionService.saveDto(transaction);
        }
    }

    public ResponseData<TransactionIdOnlyResponse> finalizeListOfItems(String appId, @NotNull FinalizeListRequest request) {
        PosTransactionDto transaction = transactionService.makeNewByFinalizeSingleItem(request, appId);
        try {
            log.info("Finalize list of items: {}", objectMapper.writeValueAsString(request));
            TransactionIdOnlyResponse response = new TransactionIdOnlyResponse(transaction.getTransactionId());
            Brand brand = getBrand(appId);
            // decrypt encrypted data
            decryptData(request, brand.getEncryptionKey());
            List<ServiceBeUsingVoucherReq> beRequests = new ArrayList<>();
            String uniqueBrandId = null;

            for(SingleItem item : request.getData()) {
                // find voucher by saved ev
                VoucherDto voucher = getVoucherDto(item.getKeyType(), item.getKey(), item.getPassword());
                String brandId = brandValidator.validateBrandRelatedReturnBrandId(brand, request.getBrandId(), request.getStoreId(), voucher.getGoods().getId());
                if (Objects.isNull(uniqueBrandId)) {
                    uniqueBrandId = brandId;
                } else if (!uniqueBrandId.equals(brandId)) {
                    throw new ApplicationException(CustomResponse.E4101_INVALID_REQUEST.getCode(), "All vouchers must be related to the same brand");
                }
                if (!voucher.getVoucherTypeCd().equals(VoucherTypeCode.SI)) {
                    throw new ApplicationException(CustomResponse.E4101_INVALID_REQUEST.getCode(), "Only allow single item voucher type");
                }
                ServiceBeUsingVoucherReq beRequest = ServiceBeUsingVoucherReq.builder()
                        .voucherId(voucher.getId())
                        .storeId(request.getStoreId())
                        .exchangeAmount(0.0)
                        .transactionDate(new Date()).build();
                beRequests.add(beRequest);
            }

            ServiceBeBaseResponse beBaseResponse = serviceBeClient.requestUsingVoucherList(beRequests);
            log.info("post request service be handle: {}", objectMapper.writeValueAsString(beBaseResponse));
            postRequestServiceBeHandle(beBaseResponse, transaction, brand.getEncryptionKey(), response);
            if (beBaseResponse.isOk()) {
                return new ResponseData<>(response);
            }
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error(e.getMessage());
            transaction.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            transaction.error(e.getMessage());
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        } finally {
            transactionService.saveDto(transaction);
        }
    }
//    public ResponseData<TransactionIdOnlyResponse>
//    cancelExchange(@NotNull String authCode, @NotNull CancelRequest request) {
//        BrandDto brand = brandService.findDtoById(request.getBrandId());
//
//        // validate auth code
//        BrandValidator.validateAuthCode(brand, authCode);
//
//        // decrypt encrypted data
//        decryptData(request, brand.getPosEncryptorKey());
//
//        PosTransactionDto transaction = transactionService.findDtoById(request.getTransactionId());
//
//        try {
//            transactionService.update(transaction, request);
//            TransactionIdOnlyResponse response = new TransactionIdOnlyResponse(request.getTransactionId());
//
//            // find voucher by saved ev
//            VoucherDto voucher = voucherService.findDtoById(transaction.getEv());
//
//            log.info("create request body to service be for using voucher");
//            ServiceBeUsingVoucherReq beRequest = requestMapper.toServiceBeReqForCancel(
//                    transaction, voucher
//            );
//
//            ServiceBeBeanResolve.setDefaultValueForCancel(beRequest);
//
//            ServiceBeBaseResponse beBaseResponse = serviceBeClient.requestUsingVoucher(beRequest);
//
//            postRequestServiceBeHandle(beBaseResponse, transaction, brand.getPosEncryptorKey(), response);
//            if (beBaseResponse.isOk()) {
//
//                return new ResponseData<>(response);
//            }
//        } catch (ApplicationException e) {
//            transaction.error(e.getMessage());
//            throw e;
//        } catch (Exception e) {
//            transaction.error(e.getMessage());
//            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
//        } finally {
//            transactionService.saveDto(transaction);
//        }
//        throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
//    }

    private void postRequestServiceBeHandle(
            ServiceBeBaseResponse beResponse,
            PosTransactionDto transaction,
            String encryptSecret,
            TransactionIdOnlyResponse response) throws JsonProcessingException {
        if (beResponse.isOk()) {
            log.info("be handler using voucher oke");
            // Response
            encryptData(response, encryptSecret);
            // Add formatted date
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = now.format(formatter);
            response.setResponseTime(formattedDate);

            // transaction
            transaction.success(objectMapper.writeValueAsString(response));
        } else {
            log.info("service be return fail");
            transaction.error(objectMapper.writeValueAsString(beResponse));
        }
    }
}

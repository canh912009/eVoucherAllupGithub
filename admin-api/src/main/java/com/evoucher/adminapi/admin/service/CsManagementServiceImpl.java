package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.CsManagementRepository;
import com.evoucher.adminapi.admin.dao.EVoucherRepository;
import com.evoucher.adminapi.admin.dao.models.EVoucher;
import com.evoucher.adminapi.admin.dao.models.VoucherTransferHistory;
import com.evoucher.adminapi.admin.service.models.*;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.config.PropertyConverter;
import com.evoucher.adminapi.common.enums.VoucherStatusCode;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.evoucher.adminapi.common.utils.DataUtils.getPageInfo;
import static com.evoucher.adminapi.common.utils.DateUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsManagementServiceImpl implements CsManagementService{
    private final PropertyConverter converter;
    private final CsManagementRepository repository;
    private final EVoucherRepository eVoucherRepository;
    private final VoucherExchangeHistoryService exchangeHistoryService;
    private final TransferHistoryService transferHistoryService;
    private final RestTemplate restTemplate;

    @Value("${servers.evoucherServiceBe}")
    private String evoucherServiceBeUrl;

    @Value("${servers.publishServer}")
    private String publishServiceUrl;

    @Override
    public Page<CsManagementResponse> search(CsManagementFilterRequest request, Integer page, Integer pageSize) throws CustomCodeException {
        log.info("search cs management with filter request: {}", request);
        try {
            //encrypt mobile number
            if (request.getTargetNumbers() != null && !request.getTargetNumbers().isEmpty()) {
                request.setTargetNumbers(request.getTargetNumbers().stream().map(converter::convertToDatabaseColumn).collect(Collectors.toSet()));
            }
            if (request.getTargetNames() != null && !request.getTargetNames().isEmpty()) {
                request.setTargetNames(request.getTargetNames().stream().map(converter::convertToDatabaseColumn).collect(Collectors.toSet()));
            }

            log.info("converted target number param: {}", request.getTargetNumbers());

            //convert string to date for start date and end date
            request.setStartDateObject(atStartOfDay(getDateFromStringWithCommonFormat(request.getStartDate())));
            request.setEndDateObject(atEndOfDay(getDateFromStringWithCommonFormat(request.getEndDate())));

            Pageable pageable = getPageInfo(page, pageSize);
            log.info("db filter: {}", request);

            List<CsManagementResponse> result = repository.search(request, pageable);
            if (result.isEmpty()) {
                return Page.empty();
            }

            long count = repository.count(request);
            log.info("success with {} results", count);
            return new PageImpl<>(result, pageable, count);
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
//            log.error("exception when get store table data with offset: " + offset + " and page size: " + pageSize);
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PinDetailResponse getPinDetail(@Valid @NotNull(message = "ev can not be null") String ev) throws CustomCodeException {
        log.info("get pin detail by id: {}", ev);
        try {
            PinDetailResponse pinDetail = exchangeHistoryService.findPinDetailById(ev);
            if (pinDetail == null) {
                log.error("can not find pin detail by id: {}", ev);
                throw CustomCodeException.internalException(new Exception("can not find pin detail by id: " + ev));
            }
            List<CsExchangeHistoryDTO> exchangeHistories = exchangeHistoryService.findExchangeHistoryByVoucherId(ev);
            Set<CsTransferHistoryDTO> transferHistories = transferHistoryService.findHistoryByVoucherId(ev);
            List<ChildOfChoiceVoucherResponse> childOfChoiceVoucherList =
                    exchangeHistoryService.findChildOfChoiceVoucherByChoiceVoucherId(ev);

            pinDetail.setExChangeHistories(exchangeHistories);
            pinDetail.setTransferHistories(transferHistories);
            pinDetail.setChildOfChoiceVoucherList(childOfChoiceVoucherList);

            return pinDetail;
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        }  catch (Exception e) {
            log.error(e.getMessage(), e);
            throw CustomCodeException.internalException(e);
        }
    }

    @Override
    public PinDetailResponse getPinDetailV2(@Valid @NotNull(message = "ev can not be null") String ev) throws CustomCodeException {
        log.info("get pin detail by id: {}", ev);
        try {
            PinDetailResponse pinDetail = exchangeHistoryService.findPinDetailById(ev);
            if (pinDetail == null) {
                log.error("can not find pin detail by id: {}", ev);
                throw CustomCodeException.internalException(new Exception("can not find pin detail by id: " + ev));
            }
            List<CsExchangeHistoryDTO> exchangeHistories = exchangeHistoryService.findExchangeHistoryByVoucherId(ev);
            Set<CsTransferHistoryDTO> transferHistories = transferHistoryService.findHistoryByVoucherIdV2(ev);
            List<ChildOfChoiceVoucherResponse> childOfChoiceVoucherList =
                    exchangeHistoryService.findChildOfChoiceVoucherByChoiceVoucherId(ev);

            pinDetail.setExChangeHistories(exchangeHistories);
            pinDetail.setTransferHistories(transferHistories);
            pinDetail.setChildOfChoiceVoucherList(childOfChoiceVoucherList);

            return pinDetail;
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        }  catch (Exception e) {
            log.error(e.getMessage(), e);
            throw CustomCodeException.internalException(e);
        }
    }

    @Override
    public void disableVoucher(VoucherDisableRequest voucherDisableRequest) {
        String ev = voucherDisableRequest.getEv();
        log.info("Get eVoucher info with eVoucherId: {}", ev);
        EVoucher eVoucher = eVoucherRepository.findById(ev)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.voucher.not.found"),
                        HttpStatus.BAD_REQUEST
                ));

        if (VoucherStatusCode.CAN_NOT_BE_DISABLE_VOUCHER
                .contains(VoucherStatusCode.valueOf(eVoucher.getVoucherStatusCode()))) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.voucher.status.was.disabled"),
                    HttpStatus.BAD_REQUEST);
        }

        log.info("Call api disable eVoucher with ev: {}", ev);
        callApiDisableVoucher(voucherDisableRequest);
    }

    @Override
    public void resendVoucher(VoucherResendRequest voucherResendRequest) {
        String ev = voucherResendRequest.getEv();
        log.info("Get eVoucher info with eVoucherId: {}", ev);
        boolean isAlreadyExistVoucher = eVoucherRepository.existsById(ev);
        if (!isAlreadyExistVoucher) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.voucher.not.found"),
                    HttpStatus.BAD_REQUEST);
        }

        log.info("Call api resend eVoucher with ev: {}", ev);
        callApiResendVoucher(voucherResendRequest);
    }

    private void callApiResendVoucher(VoucherResendRequest voucherResendRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("ClientId", LoggedInUserContext.getLoggedInUser().getId());
            HttpEntity entity = new HttpEntity(voucherResendRequest, headers);

            log.info("Call publish service resend voucher with ev: {}", voucherResendRequest.getEv());
            restTemplate.exchange(
                    publishServiceUrl + "/voucher/resend",
                    HttpMethod.POST,
                    entity,
                    Object.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Publish service error: {}", e.getMessage(), e);
            var error = Constant.gson.fromJson(e.getResponseBodyAsString(),
                    PublishServiceResponseException.class);
            throw new CustomCodeException(
                    error.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Resend voucher error: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.voucher.resend.error"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void callApiDisableVoucher(VoucherDisableRequest voucherDisableRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("ClientId", LoggedInUserContext.getLoggedInUser().getId());
            HttpEntity entity = new HttpEntity(voucherDisableRequest, headers);

            log.info("Call evoucher-service-be disable voucher with ev: {}", voucherDisableRequest.getEv());
            restTemplate.exchange(
                    evoucherServiceBeUrl + "/evouchers/disable",
                    HttpMethod.POST,
                    entity,
                    Object.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Evoucher service BE error: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.voucher.disabled.error"),
                    e.getStatusCode());
        } catch (Exception e) {
            log.error("Disable voucher error: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.voucher.disabled.error"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

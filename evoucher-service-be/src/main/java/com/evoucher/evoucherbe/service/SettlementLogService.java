package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.ExchangeType;
import com.evoucher.evoucherbe.common.enums.SettlementLogType;
import com.evoucher.evoucherbe.common.enums.SettlementMethodCode;
import com.evoucher.evoucherbe.common.enums.SettlementTarget;
import com.evoucher.evoucherbe.dto.*;
import com.evoucher.evoucherbe.entity.EVoucher;
import com.evoucher.evoucherbe.entity.Publish;
import com.evoucher.evoucherbe.entity.SettlementLog;
import com.evoucher.evoucherbe.entity.VoucherExchangeHistory;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.SettlementLogMapper;
import com.evoucher.evoucherbe.repository.SettlementLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SettlementLogService extends EntityService<SettlementLog, Integer, SettlementLogDto> {
    private final SettlementLogRepository repository;
    private final SettlementLogMapper mapper;


    public static void makeSettlementLog(SettlementLogDto.SettlementLogDtoBuilder builder,
                                                     VoucherDto voucher,
                                                     VoucherExchangeHistoryDto exchangeHistory,
                                                     GoodDto good,
                                                     CustomerContractDto customerContract,
                                                     SupplierContractDto supplierContract) {
        builder
                .ev(voucher.getEV())
                .publishId(voucher.getPublishId())
                .publishDetailId(voucher.getPublishDetailId())
                .logCreateDate(new Date())
                .voucherTypeCode(voucher.getVoucherTypeCode())
                .remainBalance(voucher.getBalance())

                .goodsId(good.getId())
                .brandId(good.getBrandId())

                .customerId(customerContract.getCustomerId())
                .supplierId(supplierContract.getSupplierId())


                .transactionId(exchangeHistory.getId())
                .transactionDate(exchangeHistory.getTransactionDate())
                .storeId(exchangeHistory.getStoreId())
                .userMobileNumber(exchangeHistory.getUserMobileNumber())
                .staffMobileNumber(exchangeHistory.getStaffMobileNumber())
                .campaignId(voucher.getCampaignId())
                .parentEv(voucher.getParentVoucherEv())
                .system(voucher.getSystem());
    }

    public static void updateCustomerSettlement(SettlementLogDto.SettlementLogDtoBuilder builder,
                                                PublishDto publish) {
        builder.settlementTarget(SettlementTarget.CUSTOMER)
                .settlementMethodCode(publish.getSellSettlementMethodCode())
                .listPrice(publish.getSellListPrice())
                .salesPrice(publish.getSellPrice())
                .discountRate(publish.getSellDiscountRate())
                .discountAmount(publish.getSellDiscountAmount())
                .vatIncludeYn(publish.getSellVatIncludeYn())
                .commissionRate(publish.getSellCommissionRate())
                .sendCost(publish.getSendCost());
    }

    public static void updateSupplierSettlement(SettlementLogDto.SettlementLogDtoBuilder builder,
                                                GoodDto good) {
        builder.settlementTarget(SettlementTarget.SUPPLIER)
                .settlementMethodCode(good.getSettlementMethodCode())
                .listPrice(good.getListPrice())
                .salesPrice(good.getSellPrice())
                .discountRate(good.getSupplyDiscountRate())
                .discountAmount(good.getSupplyDiscountAmount())
                .vatIncludeYn(good.getVatIncludeYn())
                .commissionRate(good.getSupplyCommissionRate());
    }

    @Override
    public JpaRepository<SettlementLog, Integer> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "settlement log";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Integer id) {
        return new EntityNotFoundException("can not find settlement log by id: " + id);
    }

    @Override
    public SettlementLog toEntity(SettlementLogDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public SettlementLogDto toDto(SettlementLog entity) {
        return mapper.toDto(entity);
    }

    public void saveAllDto(Collection<SettlementLogDto> dtoList) throws CustomCodeException {
        log.info("save all settlement log");
        try {
            var entities = dtoList.stream().map(mapper::toEntity).collect(Collectors.toList());
            entities = super.saveAll(entities);
            log.info("saved all settlement log: {}", entities.stream().map(SettlementLog::getLogId).collect(Collectors.toList()));
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static List<SettlementLogDto> createSettlementLogListForExchangeAndCancel(
            VoucherDto voucher, PublishDto publish, VoucherExchangeHistoryDto exchangeHistory, GoodDto good, CustomerContractDto customerContract, SupplierContractDto supplierContract) {
        List<SettlementLogDto> settlementLogs = new ArrayList<>();
        SettlementLogType settlementLogType = getSettlementLogType(exchangeHistory.getExchangeType());
        // check and save SettlementMethodCode: EXCHANGE / PER_USE_AMOUNT for Customer
        if (SettlementMethodCode.PER_EXCHANGE.equals(publish.getSellSettlementMethodCode())
                || SettlementMethodCode.PER_USE_AMOUNT.equals(publish.getSellSettlementMethodCode())) {
            log.info("Create SettlementLog for Customer with ev: {}", voucher.getEV());
            SettlementLogDto.SettlementLogDtoBuilder builder = SettlementLogDto.builder();
            SettlementLogService.makeSettlementLog(builder, voucher, exchangeHistory, good, customerContract, supplierContract);
            SettlementLogService.updateCustomerSettlement(builder, publish);

            settlementLogs.add(builder.build());
        }
        // check and save SettlementMethodCode: EXCHANGE / PER_USE_AMOUNT for Supplier
        if (SettlementMethodCode.PER_EXCHANGE.equals(good.getSettlementMethodCode())
                || SettlementMethodCode.PER_USE_AMOUNT.equals(good.getSettlementMethodCode())) {
            log.info("Create SettlementLog for Customer with ev: {}", voucher.getEV());
            SettlementLogDto.SettlementLogDtoBuilder builder = SettlementLogDto.builder();
            SettlementLogService.makeSettlementLog(builder, voucher, exchangeHistory, good, customerContract, supplierContract);
            SettlementLogService.updateSupplierSettlement(builder, good);

            settlementLogs.add(builder.build());
        }
        log.info("settlement logs: {}", settlementLogs);
        return settlementLogs;
    }

    public static SettlementLogType getSettlementLogType(ExchangeType exchangeType) {
        SettlementLogType settlementLogType;
        if (ExchangeType.USE.equals(exchangeType)) {
            settlementLogType = SettlementLogType.PER_EXCHANGE;
        } else if (ExchangeType.CANCEL.equals(exchangeType)) {
            settlementLogType = SettlementLogType.PER_EXCHANGE_CANCEL;
        } else {
            throw new IllegalArgumentException("Invalid value for Exchange type: " + exchangeType);
        }
        return settlementLogType;
    }

    public void updateActivationInfoForPublishLogByEv(String ev, Date activationDate, String phoneNumber) throws CustomCodeException {
        log.error("update activation info for publish log by ev: {}, date: {}, phone number: {}", ev, activationDate, phoneNumber);
        try {
            List<SettlementLog> logs = repository.getSettlementLogByEvAndSettlementMethodCode(ev, SettlementMethodCode.PER_PUBLISH);
            if (CollectionUtils.isEmpty(logs)) {
                log.warn("can not find publish settlement log by ev");
                return;
            }
            logs.forEach(log -> {
                log.setActivationDate(activationDate);
                log.setUserMobileNumber(phoneNumber);
            });
            log.info("save all update logs: {}", logs.stream().map(SettlementLog::getLogId).collect(Collectors.toList()));
            repository.saveAll(logs);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

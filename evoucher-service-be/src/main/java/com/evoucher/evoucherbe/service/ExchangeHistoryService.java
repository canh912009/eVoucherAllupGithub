package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.ExchangeType;
import com.evoucher.evoucherbe.dto.*;
import com.evoucher.evoucherbe.entity.VoucherExchangeHistory;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.VoucherExchangeHisMapper;
import com.evoucher.evoucherbe.repository.VoucherExchangeHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeHistoryService extends EntityService<VoucherExchangeHistory, Integer, VoucherExchangeHistoryDto> {
    private final VoucherExchangeHistoryRepository repository;
    private final VoucherExchangeHisMapper mapper;
    @Override
    public JpaRepository<VoucherExchangeHistory, Integer> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "Voucher exchange history";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Integer id) {
        return new EntityNotFoundException("voucher exchange history is not found by id: " + id);
    }

    @Override
    public VoucherExchangeHistory toEntity(VoucherExchangeHistoryDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public VoucherExchangeHistoryDto toDto(VoucherExchangeHistory entity) {
        return mapper.toDto(entity);
    }

    public VoucherExchangeHistoryDto saveDto(VoucherExchangeHistoryDto exchangeHistoryDto) {
        log.info("save voucher exchange history : {}", exchangeHistoryDto);
        VoucherExchangeHistory entity = toEntity(exchangeHistoryDto);

        save(entity);

        return toDto(entity);
    }

    public VoucherExchangeHistoryDto createExchangeHistory(
            VoucherExchangeReq exchangeRequest, GoodDto good, VoucherDto voucher) {
        VoucherExchangeHistoryDto history = mapper.createExchangeHistory(exchangeRequest, good, voucher);
        if (Objects.isNull(history.getTransactionDate())) {
            log.warn("Transaction date is null, using current date");
            history.setTransactionDate(new Date());
        }
        history.setExchangeType(ExchangeType.USE);
        log.info("created exchange history : {}", history);
        return history;
    }
    public VoucherExchangeHistoryDto createExchangeHistoryForCanceling(
            VoucherExchangeReq exchangeRequest, GoodDto good, VoucherDto voucher) {
        log.info("create exchange history");
        VoucherExchangeHistoryDto history = mapper.createExchangeHistory(exchangeRequest, good, voucher);
        history.setExchangeType(ExchangeType.CANCEL);
        return history;
    }
}

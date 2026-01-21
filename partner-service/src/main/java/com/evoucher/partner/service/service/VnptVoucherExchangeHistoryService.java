package com.evoucher.partner.service.service;

import com.evoucher.partner.service.bean.dtos.VnptVoucherExchangeHistoryDto;
import com.evoucher.partner.service.bean.entity.VnptVoucherExchangeHistory;
import com.evoucher.partner.service.bean.enum_type.VnptExchangeType;
import com.evoucher.partner.service.mapper.VnptVoucherExchangeHistoryMapper;
import com.evoucher.partner.service.repository.VnptVoucherExchangeHistoryRepository;
import com.evoucher.partner.service.vnpt.bean.request.TopUpRequestQuery;
import com.evoucher.partner.service.vnpt.bean.request.VnptQueryBaseRequest;
import com.evoucher.partner.service.vnpt.bean.response.Card;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;


@Slf4j
@RequiredArgsConstructor
@Service
public class VnptVoucherExchangeHistoryService extends BasicService<VnptVoucherExchangeHistory, Integer, VnptVoucherExchangeHistoryDto> {
    private static final VnptVoucherExchangeHistoryMapper MAPPER = VnptVoucherExchangeHistoryMapper.INSTANCE;
    private final VnptVoucherExchangeHistoryRepository repository;
    @Override
    public JpaRepository<VnptVoucherExchangeHistory, Integer> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "vnpt voucher exchange history";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Integer id) {
        return new  EntityNotFoundException("vnpt voucher exchange history not found");
    }

    @Override
    public VnptVoucherExchangeHistory toEntity(VnptVoucherExchangeHistoryDto dto) {
        return MAPPER.toEntity(dto);
    }

    @Override
    public VnptVoucherExchangeHistoryDto toDto(VnptVoucherExchangeHistory entity) {
        return MAPPER.toDto(entity);
    }

    public VnptVoucherExchangeHistoryDto fromExchangeRequest(VnptQueryBaseRequest request, String ev, VnptExchangeType exchangeType) {
        return MAPPER.toExchangeHistory(request, exchangeType, ev);
    }
    public void updateCardInfoToHistory(VnptVoucherExchangeHistoryDto history, Card card) {
        MAPPER.updateCardInfo(history, card);
    }

    public void updateCardInfoToHistory(VnptVoucherExchangeHistoryDto history, TopUpRequestQuery topup) {
        MAPPER.updateCardInfo(history, topup);
    }
}

package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.dto.ActivationRequest;
import com.evoucher.evoucherbe.dto.EndUserDto;
import com.evoucher.evoucherbe.dto.VoucherActivateHistoryDto;
import com.evoucher.evoucherbe.entity.VoucherActivateHistory;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.VoucherActivateHistoryMapper;
import com.evoucher.evoucherbe.repository.VoucherActivateHistoryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivationHistoryService extends EntityService<VoucherActivateHistory, Long, VoucherActivateHistoryDto> {
    @Getter
    private final VoucherActivateHistoryRepository repository;
    private final VoucherActivateHistoryMapper mapper;

    @Override
    public String getEntityType() {
        return "voucher activation history";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Long id) {
        return new EntityNotFoundException("can not find voucher activation history by id: " + id);
    }

    @Override
    public VoucherActivateHistory toEntity(VoucherActivateHistoryDto dto) {
        return mapper.mapDtoToEntity(dto);
    }

    @Override
    public VoucherActivateHistoryDto toDto(VoucherActivateHistory entity) {
        return mapper.mapEntityToDto(entity);
    }

    public void createNewHistory(ActivationRequest request, EndUserDto endUser) throws CustomCodeException {
        log.info("create new voucher activation history by request: {}, end user: {}", request, endUser);
        try {
            VoucherActivateHistory history = VoucherActivateHistory.builder()
                    .ev(request.getEv())
                    .serialNumber(request.getSerialNumber())
                    .userName(endUser.getUserNm())
                    .phoneNumber(request.getPhoneNumber())
                    .build();

            this.save(history);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

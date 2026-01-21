package com.castis.publishservice.service;

import com.castis.publishservice.dto.EvoucherDTO;
import com.castis.publishservice.entity.Voucher;
import com.castis.publishservice.exception.defineException.NotFoundException;
import com.castis.publishservice.mapper.VoucherMapper;
import com.castis.publishservice.repository.VoucherRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.castis.publishservice.utils.Constants.ERROR_CODE.VOUCHER_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoucherBasicService extends EntityService<Voucher, String, EvoucherDTO> {
    @Getter
    private final VoucherRepository repository;
    private static final VoucherMapper mapper = VoucherMapper.INSTANCE;


    @Override
    public String getEntityType() {
        return "voucher";
    }

    @Override
    public NotFoundException getNotFoundException(String id) {
        log.info("can not find voucher by id: {}", id);
        return new NotFoundException("can not find voucher by id: " + id, VOUCHER_NOT_FOUND);
    }

    @Override
    public Voucher toEntity(EvoucherDTO dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public EvoucherDTO toDto(Voucher entity) {
        return mapper.toDTO(entity);
    }
}

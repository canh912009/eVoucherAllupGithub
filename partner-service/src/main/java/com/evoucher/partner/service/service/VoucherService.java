package com.evoucher.partner.service.service;

import com.evoucher.partner.service.bean.dtos.VoucherDto;
import com.evoucher.partner.service.bean.entity.Voucher;
import com.evoucher.partner.service.bean.enum_type.VoucherStatusCode;
import com.evoucher.partner.service.exception.define_exception.CustomCodeException;
import com.evoucher.partner.service.mapper.VoucherMapper;
import com.evoucher.partner.service.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoucherService extends BasicService<Voucher, String, VoucherDto> {
    private static final VoucherMapper MAPPER = VoucherMapper.INSTANCE;
    private final VoucherRepository repository;

    public static class ErrorCode {
        private ErrorCode(){}
        public static final int NOT_FOUND = 1001;
        public  static final int INVALID_STATUS = 1004;
        public static final int BALANCE_NOT_ENOUGH = 1006;
        public static final int EXPIRED = 1020;


    }

    @Override
    public JpaRepository<Voucher, String> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "voucher";
    }

    @Override
    public EntityNotFoundException getNotFoundException(String id) {
        return new EntityNotFoundException("voucher is not found");
    }

    @Override
    public Voucher toEntity(VoucherDto dto) {
        return MAPPER.toEntity(dto);
    }

    @Override
    public VoucherDto toDto(Voucher entity) {
        return MAPPER.toDTO(entity);
    }

    public Long countVoucherByEvAndValidProviderCode(String ev, String providerCode) {
        try {
            return Optional.ofNullable(repository.countVoucherByEvAndValidProviderCode(ev, providerCode)).orElse(0L);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw CustomCodeException.serverError(e);
        }
    }

    public void validateExistAndExpire(VoucherDto dto, Collection<VoucherStatusCode> expected) throws CustomCodeException {
        AtomicBoolean matchExpected = new AtomicBoolean(false);

        expected.forEach(o -> {
            if (o.name().equalsIgnoreCase(dto.getVoucherStatusCode())) {
                matchExpected.set(true);
            }
        });

        if (!matchExpected.get()) {
            log.info("expected: {} but voucher status is: {}", expected, dto.getVoucherStatusCode());
            throw new CustomCodeException(
                    ErrorCode.INVALID_STATUS,
                    "voucher status is ".concat(dto.getVoucherStatusCode()).concat(". Expected: ").concat(expected.toString())
            );
        }
    }

    public void validateExpire(VoucherDto dto) throws CustomCodeException {
        if (dto.getExpirationDate().before(new Date())) {
            log.error("voucher expired at : {}", dto.getExpirationDate());
            throw new CustomCodeException(ErrorCode.EXPIRED, "voucher is expired");
        }
    }

    public void validateBalance(VoucherDto dto, Long min) throws CustomCodeException {
        if (dto.getBalance() < min) {
            log.error("voucher balance: {} < {}", dto.getBalance(), min);
            throw new CustomCodeException(ErrorCode.BALANCE_NOT_ENOUGH, "voucher balance is not enough");
        }
    }
}

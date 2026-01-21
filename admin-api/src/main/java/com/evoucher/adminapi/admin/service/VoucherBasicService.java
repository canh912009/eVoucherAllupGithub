package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.EVoucherRepository;
import com.evoucher.adminapi.admin.dao.models.EVoucher;
import com.evoucher.adminapi.admin.mapper.VoucherMapper;
import com.evoucher.adminapi.admin.service.models.VoucherDto;
import com.evoucher.adminapi.common.enums.VoucherStatusCode;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.service.EntityService;
import com.evoucher.adminapi.common.utils.DateUtils;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.EnumSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherBasicService extends EntityService<EVoucher, String, VoucherDto> {
    private static final EnumSet<VoucherStatusCode> ALLOWED_EXTEND_STATUS = EnumSet.of(VoucherStatusCode.NORMAL, VoucherStatusCode.PART_USED);
    private final EVoucherRepository repository;
    private final VoucherMapper mapper;

    public static class ErrorCode {
        private ErrorCode() {}
        public static final String STATUS_CANT_EXTEND = "voucher.status.can.not.extend";
    }
    @Override
    public JpaRepository<EVoucher, String> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "voucher";
    }

    @Override
    public EntityNotFoundException getNotFoundException(String id) {
        return new EntityNotFoundException(
                MessageUtils.getMessage("evoucher.voucher.not.found")
        );
    }

    @Override
    public EVoucher toEntity(VoucherDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public VoucherDto toDto(EVoucher entity) {
        return mapper.toDto(entity);
    }

    public static boolean allowExtendExpireDate(VoucherStatusCode status) {
        return ALLOWED_EXTEND_STATUS.contains(status);
    }

    /**
     * validate extendable of voucher's status code
     * only Normal, part_used status could be extended
     * @param statusCode current status
     * @throws CustomCodeException if current status is contained by allowed list
     */
    public static void validateExtendable(String statusCode) throws CustomCodeException {
        VoucherStatusCode voucherStatusCode = VoucherStatusCode.valueOf(statusCode);
        if (VoucherBasicService.allowExtendExpireDate(voucherStatusCode)) {
            log.info("voucher status {} is valid", voucherStatusCode);
        } else {
            log.error("voucher status {} is invalid", voucherStatusCode);
            throw new CustomCodeException(MessageUtils.getMessage(ErrorCode.STATUS_CANT_EXTEND), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void extendVoucherExpiredDate(String ev, Integer days) throws CustomCodeException {
        EVoucher voucher = this.findById(ev);
        log.info("extend voucher [{}] expire date {} days from {}", ev, days, voucher.getExpirationDate());
        voucher.setExpirationDate(DateUtils.extendDays(voucher.getExpirationDate(), days));

        this.save(voucher);
    }


    @Transactional
    public VoucherDto findRequestVoucherById(String ev) throws EntityNotFoundException {
        return mapper.toOperatorRequestInfo(findById(ev));
    }

    public void validateExpireDate(Date voucherExpireDate) throws CustomCodeException {
        if (voucherExpireDate.before(new Date())) {
            log.error("voucher is expired");
            throw new CustomCodeException("Voucher is expired", HttpStatus.BAD_REQUEST);
        }
    }
}

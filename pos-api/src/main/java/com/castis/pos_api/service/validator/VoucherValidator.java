package com.castis.pos_api.service.validator;

import com.castis.pos_api.dto.VoucherDto;
import com.castis.pos_api.enum_constant.SystemType;
import com.castis.pos_api.enum_constant.TransferStatusCode;
import com.castis.pos_api.enum_constant.VoucherStatusCode;
import com.castis.pos_api.enum_constant.VoucherTypeCode;
import com.castis.pos_api.exception.ApplicationException;
import com.castis.pos_api.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class VoucherValidator {
    static final EnumSet<VoucherStatusCode> USABLE_STATUSES = EnumSet.of(VoucherStatusCode.NORMAL, VoucherStatusCode.PART_USED);
    static final Set<TransferStatusCode> USABLE_TRANSFER_STATUSES = new HashSet<>();
    static final EnumSet<VoucherTypeCode> USABLE_TYPES = EnumSet.of(VoucherTypeCode.SI, VoucherTypeCode.PP);
    static final EnumSet<SystemType> USABLE_SYSTEM_TYPES = EnumSet.of(SystemType.INTERNAL);

    static {
        // transfer status null means haven't be transferred yet, could be used'
        USABLE_TRANSFER_STATUSES.add(TransferStatusCode.RETURN);
        USABLE_TRANSFER_STATUSES.add(null);
    }

    /**
     * @param vouchers all vouchers
     * @return unique voucher that has status code in (NORMAL, PART_USED)
     * transfer status code is null or RETURN
     */
    public static VoucherDto getUniqueUsableVoucher(List<VoucherDto> vouchers) {
        if (vouchers.isEmpty()) {
            throw new ApplicationException(CustomResponse.E4404_NOT_FOUND.getCode(), "Voucher not found");
        }
        vouchers = vouchers.stream().filter(o -> {
                    log.info("validate voucher status: {}, transfer status: {}, voucher type {}"
                            , o.getVoucherStatusCode(), o.getTransferStatusCode(), o.getVoucherTypeCd());
                    return
                            USABLE_STATUSES.contains(o.getVoucherStatusCode()) &&
                                    USABLE_TRANSFER_STATUSES.contains(o.getTransferStatusCode()) &&
                                    USABLE_TYPES.contains(o.getVoucherTypeCd());
                }
        ).collect(Collectors.toList());

        switch (vouchers.size()) {
            case 0:
                throw new ApplicationException(CustomResponse.E4401_INVALID_VOUCHER_STATE);
            case 1:
                log.info("found: {}", vouchers.get(0).getId());
                return vouchers.get(0);
            default:
                log.error("find more than one valid voucher");
                throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), "Found more than one valid voucher");
        }
    }

    public static void validateVoucherPassword(@NotNull VoucherDto voucher, @NotNull String password) {
        if (!Objects.equals(voucher.getExternalPinPassword(), password)) {
            throw new ApplicationException(CustomResponse.E4401_INVALID_VOUCHER_STATE.getCode(),
                    String.format("voucher password is different with provided password: %s", password));
        }
        log.info("voucher password is valid");
    }

    /**
     * @param voucher not null
     */
    public static void validateVoucherType(@NotNull VoucherDto voucher) {
        log.info("validate voucher system type");
        if (!USABLE_SYSTEM_TYPES.contains(voucher.getSystem())) {
            throw new ApplicationException(CustomResponse.E4401_INVALID_VOUCHER_STATE.getCode(),
                    String.format("voucher system type : %s is not valid. not in %s", voucher.getSystem(), USABLE_SYSTEM_TYPES));
        }
        log.info("voucher system type is valid");
    }

    /**
     * @param voucher not null
     */
    public static void validateBalance(@NotNull VoucherDto voucher, Double exchangeAmount) {
        log.info("validate voucher balance");
        if (voucher.getBalance() < exchangeAmount) {
            log.error("voucher balance is {} is not enough {}", voucher.getBalance(), exchangeAmount);
            throw new ApplicationException(CustomResponse.E6003_NOT_ENOUGH_BALANCE);
        }
        log.info("voucher balance is enough");
    }
}

package asia.castis.evoucher.api.common;

import asia.castis.evoucher.api.common.enums.SystemType;
import asia.castis.evoucher.api.common.enums.VoucherStatusCode;
import asia.castis.evoucher.api.elastic.model.VoucherModel;
import asia.castis.evoucher.api.entity.EVoucher;

import java.util.EnumSet;
import java.util.Objects;

public class VoucherUtils {
    public static boolean isActivated(EVoucher voucher) {
        return Objects.nonNull(voucher.getUserMobileNumber()) && !voucher.getUserMobileNumber().isEmpty();
    }

    public static boolean isActivated(VoucherModel voucherModel) {
        return (Objects.nonNull(voucherModel.getUserName()) && !voucherModel.getUserName().isEmpty())
                || (Objects.nonNull(voucherModel.getUserMobileNumber()) && !voucherModel.getUserMobileNumber().isEmpty());
    }

    public static boolean isOtpRequired(EVoucher voucher) {
        // Choice, bulk
        // Vnpt epay before purchasing
        return EnumSet.of(SystemType.CHOICE, SystemType.BULK).contains(voucher.getSystem())
                || (SystemType.VNPT_EPAY.equals(voucher.getSystem()) && !voucher.getVoucherStatusCode().equals(VoucherStatusCode.USED));
    }

    public static String getVoucherIdFromUrl(String url) {
        // given https://ev.aqua.gift/yuQxK get substring after last '/'
        return url.substring(url.lastIndexOf('/') + 1);
    }
}

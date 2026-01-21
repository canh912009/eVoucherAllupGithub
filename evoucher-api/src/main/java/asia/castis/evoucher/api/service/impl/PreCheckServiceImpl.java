package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.common.Constant;
import asia.castis.evoucher.api.common.DateUtils;
import asia.castis.evoucher.api.common.Encryption;
import asia.castis.evoucher.api.common.VoucherUtils;
import asia.castis.evoucher.api.common.enums.ApiVersion;
import asia.castis.evoucher.api.common.enums.SMSType;
import asia.castis.evoucher.api.dto.otpservice.generate.OtpData;
import asia.castis.evoucher.api.dto.otpservice.generate.OtpResponse;
import asia.castis.evoucher.api.dto.response.PreCheckResponse;
import asia.castis.evoucher.api.entity.EVoucher;
import asia.castis.evoucher.api.entity.Goods;
import asia.castis.evoucher.api.entity.Publish;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.repository.GoodsRepository;
import asia.castis.evoucher.api.repository.PublishRepository;
import asia.castis.evoucher.api.repository.VoucherRepository;
import asia.castis.evoucher.api.service.OtpConnector;
import asia.castis.evoucher.api.service.PreCheckService;
import asia.castis.evoucher.api.service.WebViewerService;
import asia.castis.evoucher.api.service.base.BaseVoucherService;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PreCheckServiceImpl implements PreCheckService {
    private final VoucherRepository voucherRepository;
    private final Encryption encryption;
    private final WebViewerService webViewerService;
    private final GoodsRepository goodsRepository;
    private final BaseVoucherService baseVoucherService;

    @Override
    public PreCheckResponse voucherPreCheck(String shortLinkKey) {
        String ev = webViewerService.getEvFromShortLink(shortLinkKey);

        EVoucher voucher = voucherRepository.findById(ev)
                .orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));

        Goods goods = goodsRepository.findById(voucher.getGoodsId())
                .orElseThrow(() -> new ApplicationException(ResponseString.CAN_NOT_FIND_GOODS, ErrorCode.CAN_NOT_FIND_GOODS));

        String otpExpireDt = null;
        Boolean otpExists = null;
        //if (VoucherUtils.isOtpRequired(voucher)) {
            if (Objects.isNull(voucher.getUserMobileNumber()) || voucher.getUserMobileNumber().isEmpty()) {
                otpExists = false;
            } else {
                OtpData otpByPhoneNumber;
                try {
                    otpByPhoneNumber = baseVoucherService.getOtpByPhoneNumber(voucher.getUserMobileNumber());
                    otpExists = true;
                    otpExpireDt = otpByPhoneNumber.getExpireDtStr();
                } catch (Exception e) {
                    log.error(e.getMessage());
                    otpExists = false;
                }
            }
        //}

        boolean activated = VoucherUtils.isActivated(voucher);
        return PreCheckResponse.builder()
                .version(voucher.getVoucherVersion() == Constant.VERSION_1 ? ApiVersion.version_1 : ApiVersion.version_2)
                .activated(activated)
                .phoneNumber(activated ? encryption.decryptData(voucher.getUserMobileNumber()) : null)

                .otpRequired(VoucherUtils.isOtpRequired(voucher))
                .otpExists(otpExists)
                .otpExpireDt(otpExpireDt)

                .voucherImageUrl(voucher.getImageUrl())
                .voucherName(goods.getGoodsName())
                .build();
    }
}

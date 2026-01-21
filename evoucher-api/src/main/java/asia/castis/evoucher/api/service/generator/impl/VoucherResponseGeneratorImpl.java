package asia.castis.evoucher.api.service.generator.impl;

import asia.castis.evoucher.api.common.DateUtils;
import asia.castis.evoucher.api.common.Encryption;
import asia.castis.evoucher.api.common.enums.SystemType;
import asia.castis.evoucher.api.common.enums.VoucherTypeCode;
import asia.castis.evoucher.api.dto.response.*;
import asia.castis.evoucher.api.dto.response.vnpt.VnptResponse;
import asia.castis.evoucher.api.entity.*;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.mapper.VoucherMapper;
import asia.castis.evoucher.api.repository.*;
import asia.castis.evoucher.api.service.generator.VnptResponseGenerator;
import asia.castis.evoucher.api.service.generator.VoucherResponseGenerator;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherResponseGeneratorImpl implements VoucherResponseGenerator {
    private static final Logger log = LoggerFactory.getLogger(VoucherResponseGeneratorImpl.class);
    private final VoucherRepository voucherRepository;
    private final PublishRepository publishRepository;
    private final CustomerRepository customerRepository;
    private final GoodsRepository goodsRepository;
    private static final VoucherMapper voucherMapper = VoucherMapper.INSTANCE;
    private final Encryption encryption;
    private final CommonGenerator commonGenerator;
    private final VnptResponseGenerator vnptResponseGenerator;


    @Override
    public VoucherResponseWrapper getVoucherResponseWrapper(EVoucher voucher) {
        return VoucherResponseWrapper.builder()
                .extPinId(voucher.getExternalPinId())
                .extPinNo(voucher.getExternalPinNo())
                .extPinType(Objects.nonNull(voucher.getExternalPinType()) ? voucher.getExternalPinType().name() : null)
                .displayType(Objects.nonNull(voucher.getExternalPinType()) ? voucher.getExternalPinType().name() : null)
                .externalPinPassword(voucher.getExternalPinPassword())
                .system(Objects.nonNull(voucher.getSystem()) ? voucher.getSystem().name() : null)
                .contentLink(voucher.getContentLink())
                .contentImagePath(voucher.getContentImagePath())
                .contentImageName(voucher.getContentImageName())
                .voucher(getVoucherResponse(voucher))
                .serialNo(voucher.getSerialNo())
                .build();
    }

    @Override
    public VoucherResponse getVoucherResponse(EVoucher voucher) {
        Publish publish = publishRepository.findById(voucher.getPublishId())
                .orElseThrow(() -> new ApplicationException(ResponseString.CAN_NOT_FIND_PUBLISH, ErrorCode.CAN_NOT_FIND_PUBLISH));

        VoucherResponse response = voucherMapper.toVoucherResponse(voucher);
        response.setId(voucher.getEV());
        response.setUserName(encryption.decryptData(voucher.getUserName()));
        response.setUserMobileNumber(encryption.decryptData(voucher.getUserMobileNumber()));
        response.setVoucherStatus(voucher.getVoucherStatusCode());
        response.setVoucherType(voucher.getVoucherTypeCode());
        response.setSmsType(Objects.nonNull(publish.getSmsType()) ? publish.getSmsType().name() : null);
        response.setInitialAmount(voucher.getInitAmount());

        response.setTransferStatus(voucher.getTransferStatusCode());
        response.setTransferMessage(voucher.getTransferMessage());

        if (Objects.nonNull(voucher.getOriginalEv()) && !voucher.getOriginalEv().isEmpty()) {
            EVoucher origVoucher = voucherRepository.findById(voucher.getOriginalEv())
                    .orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));
            response.setOriginalVoucherId(voucher.getOriginalEv());
            response.setOriginalUserName(encryption.decryptData(origVoucher.getUserName()));
        }

        response.setParentEv(voucher.getParentVoucherEv());
        // Sensitive data, must not return to the client
//        response.setParentToken(voucher.getParentVoucherToken());

        response.setCreateDate(DateUtils.toDateTimeString(voucher.getCreationDate()));
        response.setExpireDate(DateUtils.toDateTimeString(voucher.getExpirationDate()));
        response.setPublishDate(DateUtils.toDateTimeString(voucher.getPublishDate()));
        response.setLastExchangeDate(DateUtils.toDateTimeString(voucher.getLastExchangeDate()));
        response.setDisuseDate(DateUtils.toDateTimeString(voucher.getDisuseDate()));
        response.setCancelDate(DateUtils.toDateTimeString(voucher.getCancelDate()));
        response.setTransferDate(DateUtils.toDateTimeString(voucher.getTransferDate()));
        response.setActivationDate(DateUtils.toDateTimeString(voucher.getActivationDate()));

        response.setCustomer(Objects.isNull(publish.getCustomerId()) ? null : getCustomerResponse(publish.getCustomerId()));
        response.setGoods(Objects.isNull(voucher.getGoodsId()) ? null : getGoodsResponse(voucher));

        response.setShowPopupYn(publish.getShowPopupYn());
        return response;
    }

    @Override
    public VoucherResponse getVoucherResponse(String ev) {
        EVoucher voucher = voucherRepository.findById(ev)
                .orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));
        return getVoucherResponse(voucher);
    }

    private CustomerResponse getCustomerResponse(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ApplicationException(ResponseString.CAN_NOT_FIND_USER, ErrorCode.CAN_NOT_FIND_USER));
        return CustomerResponse.builder()
                .id(customer.getId())
                .customerName(customer.getCustomerName())
                .build();
    }

    private GoodsResponse getGoodsResponse(EVoucher sourceVoucher) {
        Integer goodsId = sourceVoucher.getGoodsId();
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new ApplicationException(ResponseString.CAN_NOT_FIND_GOODS, ErrorCode.CAN_NOT_FIND_GOODS));
        GoodsResponse goodsResponse = commonGenerator.toGoodsResponse(goods);

        if (VoucherTypeCode.CH.equals(goods.getGoodsType())) {
            goodsResponse.setChoices(getChoices(goodsId));
        }

        if (SystemType.VNPT_EPAY.equals(goods.getSystem())) {
            goodsResponse.setVnpt(getVnpt(sourceVoucher, goods));
        }

        goodsResponse.setCategories(commonGenerator.getCategoriesResponse(goodsId));
        goodsResponse.setBrand(Objects.isNull(goods.getBrandId()) ? null : commonGenerator.getBrandResponse(goods.getBrandId()));

        return goodsResponse;
    }

    private VnptResponse getVnpt(EVoucher sourceVoucher, Goods goods) {
        switch (sourceVoucher.getVoucherStatusCode()) {
            case USED: // Used VNPT voucher = requested to purchase PIN or topup to VNPT successfully
                return vnptResponseGenerator.getPurchasedVnptResponse(sourceVoucher.getEV(), goods.getId());
            case NORMAL: // Show list of available providers
                return vnptResponseGenerator.getCleanVnptResponse(sourceVoucher.getEV(), goods.getId());
            default:
                log.warn("Voucher status isn't NORMAL or USED so there will be no VNPT info provided. Status: {}", sourceVoucher.getVoucherStatusCode());
                return new VnptResponse();
        }
    }

    private List<GoodsResponse> getChoices(int parentGoodsId) {
        List<Goods> childGoods = goodsRepository.findGoodsChoiceById(parentGoodsId);
        return childGoods.stream().map(commonGenerator::toGoodsResponse).collect(Collectors.toList());
    }

}

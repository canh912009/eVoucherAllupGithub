package com.castis.publishservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class GiftPopCreateVoucherResponse {
    private OrderInfo orderInfo;
    private List<VoucherGiftPopResponse> voucherList;

    @Getter
    @NoArgsConstructor
    public static class OrderInfo {
        private String brandCode;
        private String expiryDate;
        private String goodsId;
        private String goodsName;
        private String goodsType;
        private String orderNo;
        private int quantity;
        private String resCode;
        private String resMessage;
    }
}

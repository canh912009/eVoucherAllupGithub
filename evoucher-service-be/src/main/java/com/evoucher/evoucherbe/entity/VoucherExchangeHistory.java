package com.evoucher.evoucherbe.entity;

import com.evoucher.evoucherbe.config.PropertyConverter;
import com.evoucher.evoucherbe.common.enums.ExchangeType;
import com.evoucher.evoucherbe.common.enums.VoucherTypeCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "TB_EXCHANGE_HISTORY")
public class VoucherExchangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private Integer id;

    @Column(name = "EXCHANGE_TYPE")
    @Enumerated(EnumType.STRING)
    private ExchangeType exchangeType;

    @Column(name = "TRANSACTION_DT")
    private Date transactionDate;

    @Column(name = "STORE_ID")
    private String storeId;

    @Column(name = "EV")
    private String ev;

    @Column(name = "VOUCHER_TYPE_CD")
    @Enumerated(EnumType.STRING)
    private VoucherTypeCode voucherTypeCode;

    @Column(name = "GOODS_ID")
    private Integer goodsId;

    @Column(name = "GOODS_NM")
    private String goodsName;

    @Column(name = "LIST_PRICE")
    private Double listPrice;

    @Column(name = "DC_RATE")
    private Double discountRate;

    @Column(name = "DC_AMOUNT")
    private Double discountAmount;

    @Column(name = "EXCHANGE_AMOUNT")
    private Double exchangeAmount;

    @Column(name = "USER_MOBILE_NUM")
    @Convert(converter = PropertyConverter.class)
    private String userMobileNumber;

    @Column(name = "STAFF_MOBILE_NUM")
    @Convert(converter = PropertyConverter.class)
    private String staffMobileNumber;
    @Column(name = "usage_remaining_count")
    private Integer usageRemainingCount;
}

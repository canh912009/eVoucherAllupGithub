package com.castis.pos_api.entity;

import com.castis.pos_api.enum_constant.SystemType;
import com.castis.pos_api.enum_constant.TransferStatusCode;
import com.castis.pos_api.enum_constant.VoucherStatusCode;
import com.castis.pos_api.enum_constant.VoucherTypeCode;
import com.castis.pos_api.service.common.PropertyConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@FieldDefaults(level= AccessLevel.PRIVATE)
@Table(name = "tb_voucher")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ev")
    private String id;

    @Basic
    @Column(name = "balance")
    private Double balance;

    @Basic
    @Column(name = "expiration_dt")
    private Date expirationDate;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "goods_id", referencedColumnName = "goods_id", insertable = false, updatable = false)
    Goods goods;

    @Column(name = "USER_MOBILE_NUM")
    @Convert(converter = PropertyConverter.class)
    private String userMobileNumber;

    @Column(name = "USER_NM")
    @Convert(converter = PropertyConverter.class)
    private String userName;

    @Basic
    @Column(name = "voucher_price")
    private Double voucherPrice;

    @Column(name = "voucher_status_cd")
    @Enumerated(EnumType.STRING)
    private VoucherStatusCode voucherStatusCode;

    @Column(name = "voucher_type_cd")
    @Enumerated(EnumType.STRING)
    private VoucherTypeCode voucherTypeCd;

    @Column(name = "EXT_PIN_ID")
    private Long extPinId;

    @Column(name = "EXT_PIN_NO")
    private String extPinNo;

    @Column(name = "SYSTEM")
    @Enumerated(EnumType.STRING)
    private SystemType system;

    @Column(name = "EXT_PIN_PASSWORD")
    private String externalPinPassword;

    @Column(name = "TRANSFER_STATUS_CD")
    @Enumerated(EnumType.STRING)
    private TransferStatusCode transferStatusCode;

    @Column(name = "SERIAL_NO")
    private String serialNo;
}

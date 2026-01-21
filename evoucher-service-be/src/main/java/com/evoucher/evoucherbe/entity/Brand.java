package com.evoucher.evoucherbe.entity;

import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import com.evoucher.evoucherbe.common.enums.SystemType;
import com.evoucher.evoucherbe.common.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "TB_BRAND")
public class Brand extends BaseEntity {

    @Id
    @Column(name = "BRAND_ID")
    private String id;

    @Column(name = "BRAND_NM")
    private String brandName;

    @Column(name = "BRAND_IMG_PATH")
    private String brandImagePath;

    @Column(name = "BRAND_IMG_NM")
    private String brandImageName;

    @Column(name = "[DESC]")
    private String description;

    @Column(name = "SUPPLIER_ID")
    private String supplierId;

    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

    @Column(name = "DEFAULT_BRAND_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn defaultBrandYn;

    @Column(name = "DISPLAY_TYPE")
    private String displayType;

    @Column(name = "SYSTEM")
    @Enumerated(EnumType.STRING)
    private SystemType system;

    @Column(name = "BRAND_CD")
    private String brandCode;

    @Column(name = "APP_ID")
    private String appId;

    @Column(name = "AUTH_KEY")
    private String authenticationKey;

    @Column(name = "ENC_KEY")
    private String encryptionKey;

    @Column(name = "SERIAL_NUMBER_PREFIX")
    private String serialNumberPrefix;

    @Column(name = "SERIAL_NUMBER_TOTAL_LENGTH")
    private Integer serialNumberTotalLength;

    @Column(name = "IP_WHITE_LIST")
    private String ipWhiteList;
}

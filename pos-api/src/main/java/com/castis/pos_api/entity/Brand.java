package com.castis.pos_api.entity;

import com.castis.pos_api.enum_constant.EnumValidYn;
import com.castis.pos_api.enum_constant.SystemType;
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
public class Brand {

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

    @Column(name = "IS_POS_LINK")
    @Enumerated(EnumType.STRING)
    private EnumValidYn isPosLink;

    @Column(name = "SYSTEM")
    @Enumerated(EnumType.STRING)
    private SystemType system;

    @Column(name = "BRAND_CD")
    private String brandCode;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "auth_key")
    private String authenticationKey;

    @Column(name = "enc_key")
    private String encryptionKey;

    @Column(name = "serial_number_prefix")
    private String serialNumberPrefix;

    @Column(name = "serial_number_total_length")
    private int serialNumberTotalLength;

    @Column(name = "ip_white_list")
    private String ipWhiteList;
}

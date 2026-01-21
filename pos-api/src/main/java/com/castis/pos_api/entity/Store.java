package com.castis.pos_api.entity;

import com.castis.pos_api.enum_constant.EnumValidYn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "TB_STORE")
public class Store {

    @Id
    @Column(name = "STORE_ID")
    private String id;

    @Column(name = "STORE_NM")
    private String storeName;

    @Column(name = "STORE_IMG_PATH")
    private String storeImagePath;

    @Column(name = "STORE_IMG_NM")
    private String storeImageName;

    @Column(name = "BRAND_ID")
    private String brandId;

    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

    @Column(name = "MAP_CD")
    private String mapCode;

    @Column(name = "LATITUDE")
    private String latitude;

    @Column(name = "LONGITUDE")
    private String longitude;

    @Column(name = "MAP_INTERATION_TYPE")
    private String mapInteractionType;

    @Column(name = "REGION")
    private String region;

    @Column(name = "STORE_TYPE")
    private String storeType;

    @Column(name = "FULL_ADDRESS")
    private String fullAddress;

    @Column(name = "TEL")
    private String telephoneNumber;
    @Column(name = "store_cd")
    private String storeCode;
}

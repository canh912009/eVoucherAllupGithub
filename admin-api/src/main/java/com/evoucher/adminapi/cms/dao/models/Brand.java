package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.admin.service.models.CsExchangeHistoryDTO;
import com.evoucher.adminapi.cms.service.models.BrandWithCategorySearchResponse;
import com.evoucher.adminapi.cms.service.models.vnptEPay.response.BrandSearchDTO;
import com.evoucher.adminapi.common.enums.SystemType;
import com.evoucher.adminapi.common.models.BaseEntity;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@SqlResultSetMapping(
        name = "vnpt_search_brand_mapping",
        classes = @ConstructorResult(
                targetClass = BrandSearchDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = String.class),
                        @ColumnResult(name = "brandTitle", type = String.class),
                        @ColumnResult(name = "numberOfGifts", type = Integer.class)
                }
        )
)

@SqlResultSetMapping(
        name = "BrandCategoryMapping",
        entities = {
                @EntityResult(entityClass = Brand.class, fields = {
                        @FieldResult(name = "id", column = "BRAND_ID"),
                        @FieldResult(name = "brandName", column = "BRAND_NM"),
                        @FieldResult(name = "brandImagePath", column = "BRAND_IMG_PATH"),
                        @FieldResult(name = "brandImageName", column = "BRAND_IMG_NM"),
                        @FieldResult(name = "brandLogoPath", column = "BRAND_LOGO_PATH"),
                        @FieldResult(name = "brandLogoName", column = "BRAND_LOGO_NM"),
                        @FieldResult(name = "description", column = "[DESC]"),
                        @FieldResult(name = "supplierId", column = "SUPPLIER_ID"),
                        @FieldResult(name = "validYn", column = "VALID_YN"),
                        @FieldResult(name = "defaultBrandYn", column = "DEFAULT_BRAND_YN"),
                        @FieldResult(name = "displayType", column = "DISPLAY_TYPE"),
                        @FieldResult(name = "system", column = "SYSTEM"),
                        @FieldResult(name = "brandCode", column = "BRAND_CD"),
                        @FieldResult(name = "authenticationKey", column = "AUTH_KEY"),
                        @FieldResult(name = "encryptionKey", column = "ENC_KEY"),
                        @FieldResult(name = "appId", column = "APP_ID"),
                        @FieldResult(name = "serialNumberPrefix", column = "SERIAL_NUMBER_PREFIX"),
                        @FieldResult(name = "serialNumberTotalLength", column = "SERIAL_NUMBER_TOTAL_LENGTH"),
                        @FieldResult(name = "ipWhiteList", column = "IP_WHITE_LIST"),
                        @FieldResult(name = "regId", column = "REG_ID"),
                        @FieldResult(name = "regDt", column = "REG_DT"),
                        @FieldResult(name = "updtId", column = "UPDT_ID"),
                        @FieldResult(name = "updtDt", column = "UPDT_DT")
                })
        },
        columns = {
                @ColumnResult(name = "categoryCode", type = String.class),
                @ColumnResult(name = "supplierName", type = String.class)
        }
)

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

    @Column(name = "BRAND_LOGO_PATH")
    private String brandLogoPath;

    @Column(name = "BRAND_LOGO_NM")
    private String brandLogoName;

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

    @Column(name = "APP_ID", unique = true)
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

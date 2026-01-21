package asia.castis.evoucher.api.entity;

import asia.castis.evoucher.api.common.enums.EnumValidYn;
import asia.castis.evoucher.api.common.enums.SystemType;
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

    @Column(name = "IS_POS_LINK")
    @Enumerated(EnumType.STRING)
    private EnumValidYn isPosLink;

    @Column(name = "SYSTEM")
    @Enumerated(EnumType.STRING)
    private SystemType system;

    @Column(name = "BRAND_CD")
    private String brandCode;
}

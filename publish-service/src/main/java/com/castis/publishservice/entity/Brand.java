package com.castis.publishservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "TB_BRAND")
@Builder
public class Brand{

    @Id
    @Column(name = "BRAND_ID")
    private String id;

    @Column(name = "BRAND_NM")
    private String brandName;

    @Column(name = "BRAND_IMG_PATH")
    private String brandImagePath;

    @Column(name = "BRAND_IMG_NM")
    private String brandImageName;

    @Column(name = "DESC")
    private String description;

    @Column(name = "SUPPLIER_ID")
    private String supplierId;

    @Column(name = "VALID_YN")
    private String validYn;

    @Column(name = "DEFAULT_BRAND_YN")
    private String defaultBrandYn;
    @CreatedBy
    @Column(name = "REG_ID", updatable = false)
    private String regId;

    @CreatedDate
    @Column(name = "REG_DT", updatable = false)
    private Date regDt;

    @LastModifiedBy
    @Column(name = "UPDT_ID")
    private String updtId;

    @LastModifiedDate
    @Column(name = "UPDT_DT")
    private Date updtDt;

    @Column(name = "IS_POS_LINK")
    private String isPosLink;

    @Column(name = "SYSTEM")
    private String system;

    @Column(name = "DISPLAY_TYPE")
    private String displayType;
}

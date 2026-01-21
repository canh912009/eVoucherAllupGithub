package com.castis.publishservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "tb_category")
public class Category {
    @Id
    @Column(name = "ctgr_cd")
    private String id;

    @Column(name = "ctgr_nm")
    private String categoryName;

    @Column(name = "valid_yn")
    private String validYn;
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
}

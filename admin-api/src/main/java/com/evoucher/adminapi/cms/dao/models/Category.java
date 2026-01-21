package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tb_category")
public class Category extends BaseEntity {
    @Id
    @Column(name = "ctgr_cd")
    private String categoryCode;

    @Column(name = "ctgr_nm")
    private String categoryName;

    @Column(name = "valid_yn")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

    @Column(name = "ctgr_img_nm")
    private String imageName;

    @Column(name = "ctgr_img_path")
    private String imagePath;
}

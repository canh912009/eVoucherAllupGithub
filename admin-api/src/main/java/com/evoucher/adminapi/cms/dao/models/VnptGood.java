package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_goods_vnpt")
@Data
public class VnptGood extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_goods_id", referencedColumnName = "goods_id", nullable = false)
    @JsonBackReference
    private Goods vnptParentGood;

    @Column(name = "provider_cd", length = 50)
    private String providerCode;

    @OneToOne
    @JoinColumn(name = "provider_cd", referencedColumnName = "provider_cd", insertable = false, updatable = false)
    @JsonManagedReference
    private VnptProvider vnptProvider;

    @Column(name = "face_value", nullable = false)
    private Double faceValue;

    @Column(name = "valid_yn", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

    @Column(name = "description", length = 500)
    private String description;
}

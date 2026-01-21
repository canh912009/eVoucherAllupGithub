package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_bulk_ctgr")
@Data
public class BulkCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulk_ctgr_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false)
    @JsonBackReference
    private Goods parentGoods;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "bulkCategory", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private List<BulkBrand> bulkBrands;

    @Column(name = "ctgr_cd", nullable = false, length = 50)
    private String categoryCode;

    @OneToOne
    @JoinColumn(name = "ctgr_cd", referencedColumnName = "ctgr_cd", insertable = false, updatable = false)
    @JsonManagedReference
    private Category category;

    @Column(name = "display_idx", nullable = false)
    private int displayIndex;

    @Column(name = "valid_yn", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;
}

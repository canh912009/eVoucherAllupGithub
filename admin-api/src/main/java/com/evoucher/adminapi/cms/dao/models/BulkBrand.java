package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tb_bulk_brand")
@Data
public class BulkBrand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulk_brand_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bulk_ctgr_id", referencedColumnName = "bulk_ctgr_id")
    @JsonBackReference
    private BulkCategory bulkCategory;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "bulkBrand", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private Set<BulkGoods> bulkGoods;

    @OneToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id", insertable = false, updatable = false)
    @JsonManagedReference
    private Brand brand;

    @Column(name = "brand_id", nullable = false, length = 20)
    private String brandId;

    @Column(name = "display_idx", nullable = false)
    private int displayIndex;

    @Column(name = "valid_yn", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;
}
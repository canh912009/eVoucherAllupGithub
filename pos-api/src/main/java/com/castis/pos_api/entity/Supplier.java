package com.castis.pos_api.entity;

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
@Table(name = "TB_SUPPLIER")
public class Supplier {
    @Id
    @Column(name = "supplier_id")
    private String id;

    @Column(name = "SUPPLIER_NM")
    private String supplierName;
}

package com.evoucher.adminapi.auth.dao.models;

import com.evoucher.adminapi.common.models.BaseEntity;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "TB_ROLE")
public class Role extends BaseEntity {

    @Id
    @Column(name = "ROLE_CODE")
    private String roleCode;

    @Column(name = "ROLE_NM")
    private String roleName;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder;

    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;
}

package com.evoucher.adminapi.auth.dao.models;

import com.evoucher.adminapi.common.models.BaseEntity;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CodeId.class)
@Table(name = "TB_CODE")
public class Code extends BaseEntity {

    @Id
    @Column(name = "CD_ID")
    private String codeId;

    @Id
    @Column(name = "CD_GRP_ID")
    private String codeGroupId;

    @Column(name = "CD_NM")
    private String codeName;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder;

    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;
}

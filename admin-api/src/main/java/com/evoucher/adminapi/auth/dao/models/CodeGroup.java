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
@Table(name = "TB_CODE_GROUP")
public class CodeGroup extends BaseEntity {

    @Id
    @Column(name = "CD_GRP_ID")
    private String codeGroupId;

    @Column(name = "CD_GRP_NM")
    private String codeGroupName;

    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;
}

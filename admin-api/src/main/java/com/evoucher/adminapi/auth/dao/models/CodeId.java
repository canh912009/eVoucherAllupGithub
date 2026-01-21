package com.evoucher.adminapi.auth.dao.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeId implements Serializable {

    @Column(name = "CD_ID")
    private String codeId;

    @Column(name = "CD_GRP_ID")
    private String codeGroupId;
}

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
public class RoleMenuId implements Serializable {
    @Column(name = "ROLE_CODE")
    private String roleCode;

    @Column(name = "MENU_ID")
    private Integer menuId;
}

package com.evoucher.adminapi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum EnumRole {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_OPERATOR("ROLE_OPERATOR"),
    ROLE_SUPPLIER("ROLE_SUPPLIER"),
    ROLE_BRAND("ROLE_BRAND"),
    ROLE_STORE("ROLE_STORE"),
    ROLE_CUSTOMER("ROLE_CUSTOMER");

    private String value;
}

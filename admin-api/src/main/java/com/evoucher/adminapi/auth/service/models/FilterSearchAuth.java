package com.evoucher.adminapi.auth.service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterSearchAuth {
    private String adminId;
    private String adminName;
    private String email;
    private String corporationName;
    private String mobilePhone;
    private String roleCode;
    private Set<String> roleCodes;
    private String roleName;
    private Integer menuId;
    private String menuName;
    private String menuUrl;
    private Integer menuGroupId;
    private String menuGroupName;
    private String codeId;
    private String codeName;
    private String codeGroupId;
    private String codeGroupName;
    private String keyWord;
    private Integer page;
    private Integer pageSize;
    private String ipId;
    private String ipAddress;
}

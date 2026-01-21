package com.evoucher.adminapi.auth.service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
    @NotBlank(message = "Role code is empty!")
    private String roleCode;
    @NotBlank(message = "Role name is empty!")
    private String roleName;
    @NotNull(message = "Sort order is empty!")
    private Integer sortOrder;
    private List<MenuDTO> menus;
}

package com.evoucher.adminapi.auth.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RoleDTO extends BaseDTO {

    private String roleCode;
    private String roleName;
    private Integer sortOrder;
    private List<MenuDTO> menus;
    private List<MenuGroupDTO> menuGroups;
    private String validYn;
}

package com.evoucher.adminapi.auth.service.models;

import com.evoucher.adminapi.auth.dao.models.Menu;
import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MenuGroupDTO extends BaseDTO {
    private Integer id;
    private String menuGroupName;
    private Integer sortOrder;
    private String validYn;
    private List<MenuDTO> menus;

    public MenuGroupDTO(List<Menu> value) {
    }
}

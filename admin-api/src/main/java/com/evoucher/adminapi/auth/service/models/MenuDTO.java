package com.evoucher.adminapi.auth.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO extends BaseDTO {
    private Integer id;
    private Integer menuGroupId;
    private String menuName;
    private Integer sortOrder;
    private String menuUrl;
    private String validYn;
}

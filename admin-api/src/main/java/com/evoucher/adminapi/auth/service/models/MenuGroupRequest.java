package com.evoucher.adminapi.auth.service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuGroupRequest {
    @NotBlank(message = "Menu group name is empty!")
    private String menuGroupName;
    @NotNull(message = "Sort order is empty!")
    private Integer sortOrder;
}

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
public class MenuRequest {
    @NotNull(message = "Menu group id is empty!")
    private Integer menuGroupId;
    @NotBlank(message = "Menu name is empty!")
    private String menuName;
    @NotNull(message = "Sort order is empty!")
    private Integer sortOrder;
    @NotBlank(message = "Menu URL is empty!")
    private String menuUrl;
}

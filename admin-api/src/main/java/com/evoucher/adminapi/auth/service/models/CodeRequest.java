package com.evoucher.adminapi.auth.service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeRequest {
    @NotNull(message = "Code group id is empty")
    @Size(max = 20, min = 1)
    private String codeId;
    @NotNull(message = "Code group id is empty")
    @Size(max = 20, min = 1)
    private String codeGroupId;
    @NotBlank(message = "Code name is empty!")
    private String codeName;
    @NotNull(message = "Sort order is empty!")
    private Integer sortOrder;
}

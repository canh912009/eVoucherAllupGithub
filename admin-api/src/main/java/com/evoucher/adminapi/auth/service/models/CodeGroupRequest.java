package com.evoucher.adminapi.auth.service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeGroupRequest {
    @NotBlank(message = "Code group ID is empty!")
    @Size(max = 20, min = 1)
    private String codeGroupId;
    @NotBlank(message = "Code group name is empty!")
    private String codeGroupName;
}

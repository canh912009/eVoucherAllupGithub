package com.evoucher.adminapi.auth.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CodeGroupDTO extends BaseDTO {
    private String codeGroupId;
    private String codeGroupName;
    private String validYn;
    private List<CodeDTO> codes;
}

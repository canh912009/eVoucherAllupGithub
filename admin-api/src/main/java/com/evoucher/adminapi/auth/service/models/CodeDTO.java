package com.evoucher.adminapi.auth.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CodeDTO extends BaseDTO {
    private String codeId;
    private String codeGroupId;
    private String codeName;
    private Integer sortOrder;
    private String validYn;
}

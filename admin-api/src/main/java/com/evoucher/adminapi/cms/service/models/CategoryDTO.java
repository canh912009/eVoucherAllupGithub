package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CategoryDTO extends BaseDTO {
    private String categoryCode;
    private String categoryName;
    private EnumValidYn validYn;
    private String imageName;
    private String imagePath;
}

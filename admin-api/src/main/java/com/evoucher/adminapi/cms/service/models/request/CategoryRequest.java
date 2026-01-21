package com.evoucher.adminapi.cms.service.models.request;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CategoryRequest extends BaseDTO {
    @NotBlank(message = "Category code is not null")
    private String categoryCode;
    @NotBlank(message = "Category name is not null")
    private String categoryName;
    private EnumValidYn validYn;
    private String imageName;
    private String imagePath;

}

package com.evoucher.adminapi.cms.service.models.request;

import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryGoodsRelRequest extends BaseDTO {
    private String categoryId;
    private Integer goodsId;
}

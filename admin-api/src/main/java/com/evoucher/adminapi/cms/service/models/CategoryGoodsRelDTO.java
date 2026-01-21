package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryGoodsRelDTO extends BaseDTO {
    private String categoryId;
    private int goodsId;
}

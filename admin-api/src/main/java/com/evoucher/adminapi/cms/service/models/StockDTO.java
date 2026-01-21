package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class StockDTO extends BaseDTO {
    private Integer id;
    private String supplierId;
    private String supplierName;
    private String brandId;
    private String brandName;
    private Integer goodsId;
    private String goodsName;
    private Date expireTime;
    private Integer remainDays;
    private Integer totalQuantity;
    private Integer totalAmount;
    private Date insertedAt;
    private Integer diff1d;
    private Integer diff7d;
    private Integer diff30d;
}
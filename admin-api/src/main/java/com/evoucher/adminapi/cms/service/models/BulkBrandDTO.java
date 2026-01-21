package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseDTO;
import com.evoucher.adminapi.common.models.SortableObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkBrandDTO implements SortableObject {
    private Long id;
    private LinkedHashSet<BulkGoodDTO> bulkGoods;
    private BrandDTO brand;
    private String brandId;
    private int displayIndex;
    private EnumValidYn validYn;
    private String regId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date regDt;
    private String updtId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updtDt;

    @Override
    public void setDisplayIndex(Integer index) {
        this.displayIndex = index;
    }

    @Override
    public String getNameAndIdentify() {
        return "    bulk brand - " + Optional.ofNullable(this.getId()).orElse(0L);
    }
}

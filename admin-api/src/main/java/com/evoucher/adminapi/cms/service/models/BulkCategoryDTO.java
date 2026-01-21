package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.SortableObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkCategoryDTO implements SortableObject {
    private Long id;
    private LinkedList<BulkBrandDTO> bulkBrands;

    private CategoryDTO category;
    private String categoryCode;
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
        return "bulk category - " + Optional.ofNullable(this.getId()).orElse(0L);
    }
}

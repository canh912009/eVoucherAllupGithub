package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.SortableObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Optional;

//@EqualsAndHashCode(callSuper = true)
@Data
//@SuperBuilder
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkGoodDTO implements SortableObject/* extends BaseDTO*/ {
    private Long id;
    private GoodsDTO goods;
    private Long goodsId;
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
        return "        bulk good - " + Optional.ofNullable(this.getId()).orElse(0L);
    }
}

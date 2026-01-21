package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterSearchSupplierContract extends BaseDTO {
    private Integer id;
    private String contractName;
    private String supplierId;
    private String supplierName;
    private String approveStatusCode;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private String validYn;
}

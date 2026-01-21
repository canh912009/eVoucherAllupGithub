package com.evoucher.adminapi.auth.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminDTO extends BaseDTO {
    private String id;
    private String adminName;
    private String email;
    private String mobileNumber;
    private String telephone;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastLoginDate;
    private String passwordInitYn;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date passwordUpdateDate;
    private Integer loginFailCount;
    private String validYn;
    private String adminCorporationId;
    private String adminCorporationName;
    private String roleCode;

}

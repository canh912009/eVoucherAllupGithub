package com.evoucher.adminapi.auth.dao.models;

import com.evoucher.adminapi.common.models.BaseEntity;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "TB_ADMIN")
@Builder
@ToString
public class Admin extends BaseEntity {
    @Id
    @Column(name = "ADMIN_ID")
    private String id;
    @Column(name = "PASSWD")
    private String password;
    @Column(name = "ADMIN_NM")
    private String adminName;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "MOBILE_NO")
    private String mobileNumber;
    @Column(name = "TEL")
    private String telephone;
    @Column(name = "LAST_LOGIN_DT")
    private Date lastLoginDate;
    @Column(name = "PASSWD_INIT_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn passwordInitYn;
    @Column(name = "PASSWD_UPDT_DT")
    private Date passwordUpdateDate;
    @Column(name = "LOGIN_FAIL_CNT")
    private Integer loginFailCount;
    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;
    @Column(name = "ROLE_CD")
    private String roleCode;
    @Column(name = "ADMIN_CORP_ID")
    private String adminCorporationId;
}

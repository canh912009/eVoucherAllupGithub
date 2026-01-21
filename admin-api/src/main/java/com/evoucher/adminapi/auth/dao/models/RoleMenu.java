package com.evoucher.adminapi.auth.dao.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TB_ROLE_MENU_REL")
@IdClass(RoleMenuId.class)
@EntityListeners(AuditingEntityListener.class)
public class RoleMenu {

    @Id
    @Column(name = "ROLE_CODE")
    private String roleCode;

    @Id
    @Column(name = "MENU_ID")
    private Integer menuId;

    @Column(name = "REG_ID", updatable = false)
    @CreatedBy
    private String regId;

    @Column(name = "REG_DT", updatable = false)
    @CreatedDate
    private Date regDt;
}

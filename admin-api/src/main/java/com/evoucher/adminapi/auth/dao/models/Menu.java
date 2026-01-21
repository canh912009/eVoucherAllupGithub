package com.evoucher.adminapi.auth.dao.models;

import com.evoucher.adminapi.common.models.BaseEntity;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_MENU")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_ID")
    private Integer id;

    @Column(name = "MENU_GRP_ID")
    private Integer menuGroupId;

    @Column(name = "MENU_NM")
    private String menuName;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder;

    @Column(name = "MENU_URL")
    private String menuUrl;

    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;
}

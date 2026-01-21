package com.evoucher.adminapi.auth.dao.models;

import com.evoucher.adminapi.common.models.BaseEntity;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_MENU_GROUP")
public class MenuGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_GRP_ID")
    private Integer id;

    @Column(name = "MENU_GRP_NM")
    private String menuGroupName;

    @Column(name = "SORT_ORDER")
    private Integer sortOrder;

    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;
}

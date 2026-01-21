package com.evoucher.adminapi.auth.dao.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "TB_IP_WHITELIST")
public class IpWhitelist extends BaseEntity {

    @Id
    @Column(name = "IP_ID")
    private String id;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

    @PrePersist
    protected void onCreate() {
        validYn = EnumValidYn.Y;
    }
}

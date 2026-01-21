package com.evoucher.evoucherbe.entity;

import com.evoucher.evoucherbe.common.enums.ExternalPinStatus;
import com.evoucher.evoucherbe.common.enums.ExternalPinType;
import com.evoucher.evoucherbe.common.enums.PinDisplayType;
import com.evoucher.evoucherbe.common.models.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_EXT_PIN")
public class ExternalPin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EXT_PIN_NO")
    private String externalPinNo;

    @Column(name = "UPLOAD_ID")
    private Integer uploadId;

    @Column(name = "GOODS_ID")
    private Long goodsId;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ExternalPinStatus status;

    @Column(name = "EXT_PIN_TYPE")
    @Enumerated(EnumType.STRING)
    private ExternalPinType externalPinType;

    @Column(name = "EXPIRE_TIME")
    private Date expireTime;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "display_type")
    @Enumerated(EnumType.STRING)
    private PinDisplayType displayType;
}

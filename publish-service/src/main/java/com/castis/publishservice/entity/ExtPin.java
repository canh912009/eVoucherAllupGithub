package com.castis.publishservice.entity;

import com.castis.publishservice.utils.enum_template.ExtPinStatus;
import com.castis.publishservice.utils.enum_template.PinDisplayType;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tb_ext_pin")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExtPin {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "ext_pin_no", nullable = true, length = 100)
    private String extPinNo;
    @Basic
    @Column(name = "goods_id", nullable = true)
    private Long goodsId;
    @Basic
    @Column(name = "upload_id", nullable = true)
    private Long uploadId;
    @Basic
    @Column(name = "status", nullable = true, length = 20)
    @Enumerated(EnumType.STRING)
    private ExtPinStatus status;
    @Basic
    @Column(name = "ext_pin_type", nullable = true, length = 20)
    private String extPinType;

    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "reg_id", nullable = true, length = 20)
    private String regId;
    @Basic
    @Column(name = "reg_dt", nullable = true)
    private Date regDt;
    @Basic
    @Column(name = "updt_id", nullable = true, length = 20)
    private String updtId;
    @Basic
    @Column(name = "updt_dt", nullable = true)
    private Date updtDt;

    @Column(name = "EXPIRE_TIME")
    private Date expireTime;
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "display_type")
    @Enumerated(EnumType.STRING)
    private PinDisplayType displayType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtPin extPin = (ExtPin) o;
        return id == extPin.id && Objects.equals(extPinNo, extPin.extPinNo) && Objects.equals(goodsId, extPin.goodsId) && Objects.equals(uploadId, extPin.uploadId) && Objects.equals(status, extPin.status) && Objects.equals(extPinType, extPin.extPinType) && Objects.equals(regId, extPin.regId) && Objects.equals(regDt, extPin.regDt) && Objects.equals(updtId, extPin.updtId) && Objects.equals(updtDt, extPin.updtDt) && Objects.equals(expireTime, extPin.expireTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, extPinNo, goodsId, uploadId, status, extPinType, regId, regDt, updtId, updtDt, expireTime);
    }
}

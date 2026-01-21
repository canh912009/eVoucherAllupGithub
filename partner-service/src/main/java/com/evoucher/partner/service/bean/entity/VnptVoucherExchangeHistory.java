package com.evoucher.partner.service.bean.entity;

import com.evoucher.partner.service.bean.enum_type.ProcessResult;
import com.evoucher.partner.service.bean.enum_type.VnptExchangeType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_vnpt_voucher_exchange_history")
@Data
public class VnptVoucherExchangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ev", nullable = false, length = 20)
    private String ev;

    @Column(name = "exchange_type", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private VnptExchangeType exchangeType;

    @Column(name = "request_history_id", nullable = false)
    private Long requestHistoryId;

    @Column(name = "vnpt_request_id", nullable = false, length = 50)
    private String vnptRequestId;

    @Column(name = "card_serial", length = 50)
    private String cardSerial;

    @Column(name = "card_pin", length = 50)
    private String cardPin;

    @Column(name = "provider_code", length = 10, nullable = false)
    private String providerCode;

    @Column(name = "target_phone_no", length = 100)
    private String targetPhone;

    @Column(name = "face_value")
    private Long faceValue;

    @Column(name = "expire_date")
    private Date expireDate;

    @Column(name = "result")
    @Enumerated(EnumType.STRING)
    private ProcessResult result;

    @CreatedDate
    @Column(name = "exchange_date", nullable = false)
    private Date exchangeDate;
}

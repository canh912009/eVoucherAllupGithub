package asia.castis.evoucher.api.entity;

import asia.castis.evoucher.api.common.enums.EnumResult;
import asia.castis.evoucher.api.common.enums.VnptCardAction;
import lombok.Data;

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
    private VnptCardAction exchangeType;

    @Column(name = "request_history_id", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String requestHistoryId;

    @Column(name = "vnpt_request_id", nullable = false, length = 50)
    private String vnptRequestId;

    @Column(name = "card_serial", length = 50)
    private String cardSerial;

    @Column(name = "card_pin", length = 50)
    private String cardPin;

    @Column(name = "provider_code", length = 10)
    private String providerCode;

    @Column(name = "face_value")
    private Long faceValue;

    @Column(name = "expire_date")
    private Date expireDate;

    @Column(name = "result", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private EnumResult result;

    @Column(name = "exchange_date", nullable = false)
    private Date exchangeDate;

    @Column(name = "target_phone_no", length = 100)
    private String targetPhoneNo;
}
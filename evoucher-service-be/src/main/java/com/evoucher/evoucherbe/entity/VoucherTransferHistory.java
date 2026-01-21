package com.evoucher.evoucherbe.entity;

import com.evoucher.evoucherbe.common.enums.TransferStatusCode;
import com.evoucher.evoucherbe.common.enums.VoucherTypeCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "TB_TRANSFER_HISTORY")
public class VoucherTransferHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private Integer id;

    @Column(name = "TRANSFER_STATUS_CD")
    @Enumerated(EnumType.STRING)
    private TransferStatusCode transferStatusCode;

    @Column(name = "TRANSACTION_DT")
    private Date transactionDate;

    @Column(name = "RECEIPT_CONFIRM_DT")
    private Date receiptConfirmDatetime;

    @Column(name = "RETURN_DT")
    private Date returnDate;

    @Column(name = "FROM_VOUCHER_SHORT_LINK")
    private String fromVoucherShortLink;

    @Column(name = "FROM_EV")
    private String fromEv;

    @Column(name = "[FROM]")
    private String fromMobileNumber;

    @Column(name = "from_user_id")
    private Long fromUserId;

    @Column(name = "TO_VOUCHER_SHORT_LINK")
    private String toVoucherShortLink;

    @Column(name = "TO_EV")
    private String toEv;

    @Column(name = "[TO]")
    private String toMobileNumber;
    @Column(name = "to_user_id")
    private Long toUserId;

    @Column(name = "VOUCHER_TYPE_CD")
    @Enumerated(EnumType.STRING)
    private VoucherTypeCode voucherTypeCode;

    @Column(name = "INIT_AMOUNT")
    private Double initAmount;

    @Column(name = "TRANSFER_AMOUNT")
    private Double transferAmount;
}

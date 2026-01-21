package com.evoucher.adminapi.admin.dao.models;

import com.evoucher.adminapi.admin.service.models.CsTransferHistoryDTO;
import com.evoucher.adminapi.cms.dao.models.EndUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQuery(
        name = "findFirstHistoryNodeByVoucherId",
        query = "select * from (select tth.transaction_id as id,\n" +
                "        'FROM_VOUCHER' as type,\n" +
                "       tth.from_ev as voucherUUID,\n" +
                "       tth.to_ev as toVoucherUUID,\n" +
                "       max(tth.transaction_dt) as transferDate,\n" +
                "       tth.to as targetNumber,\n" +
                "       tv.user_nm as targetName,\n" +
                "       tv.short_link as accessLink,\n" +
                "       tv.voucher_status_cd as pinStatus,\n" +
                "       tv.ext_pin_no as pin,\n" +
                "       tv.creation_dt as startDate,\n" +
                "       tv.expiration_dt as endDate,\n" +
                "       tth.transfer_status_cd as transferStatusCode\n" +
                "from tb_transfer_history tth\n" +
                "         left join tb_voucher tv on tth.from_ev = tv.ev\n" +
                "where tv.ev = :ev\n" +
                "group by tth.from_ev, tth.to_ev\n" +
                "order by transferDate DESC\n" +
                "limit 1) fromVoucher\n" +
                "union all\n" +
                "select * from (select tth.transaction_id as id,\n" +
                "       'TO_VOUCHER' as type,\n" +
                "       tth.from_ev as voucherUUID,\n" +
                "       tth.to_ev as toVoucherUUID,\n" +
                "       max(tth.transaction_dt) as transferDate,\n" +
                "       tth.to as targetNumber,\n" +
                "       tv.user_nm as targetName,\n" +
                "       tv.short_link as accessLink,\n" +
                "       tv.voucher_status_cd as pinStatus,\n" +
                "       tv.ext_pin_no as pin,\n" +
                "       tv.creation_dt as startDate,\n" +
                "       tv.expiration_dt as endDate,\n" +
                "       tth.transfer_status_cd as transferStatusCode\n" +
                "from tb_transfer_history tth\n" +
                "         left join tb_voucher tv on tth.to_ev = tv.ev\n" +
                "where tv.ev = :ev\n" +
                "group by tth.from_ev, tth.to_ev\n" +
                "order by transferDate\n" +
                "               limit 1) toVoucher",
        resultSetMapping = "transfer_history_dto"
)
@NamedNativeQuery(
        name = "findHistoryListInRange",
        query = "select tth.transaction_id as id,\n" +
                "       '' as type,\n" +
                "       tth.from_ev as voucherUUID,\n" +
                "       tth.to_ev as toVoucherUUID,\n" +
                "       tth.transaction_dt as transferDate,\n" +
                "       tth.to as targetNumber,\n" +
                "       tv.user_nm as targetName,\n" +
                "       tv.short_link as accessLink,\n" +
                "       tv.voucher_status_cd as pinStatus,\n" +
                "       tv.ext_pin_no as pin,\n" +
                "       tv.creation_dt as startDate,\n" +
                "       tv.expiration_dt as endDate,\n" +
                "       tth.transfer_status_cd as transferStatusCode\n" +
                "from tb_transfer_history tth\n" +
                "         left join tb_voucher tv on tth.from_ev = tv.ev\n" +
                "where tth.transaction_dt >= :startDate and tth.transaction_dt <= :endDate\n" +
                "group by tth.from_ev, tth.to_ev",
        resultSetMapping = "transfer_history_dto"
)

@SqlResultSetMapping(
        name = "transfer_history_dto",
        classes = @ConstructorResult(
                targetClass = CsTransferHistoryDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "type", type = String.class),
                        @ColumnResult(name = "voucherUUID", type = String.class),
                        @ColumnResult(name = "toVoucherUUID", type = String.class),
                        @ColumnResult(name = "transferDate", type = Date.class),
                        @ColumnResult(name = "targetNumber", type = String.class),
                        @ColumnResult(name = "targetName", type = String.class),
                        @ColumnResult(name = "accessLink", type = String.class),
                        @ColumnResult(name = "pinStatus", type = String.class),
                        @ColumnResult(name = "pin", type = String.class),
                        @ColumnResult(name = "startDate", type = Date.class),
                        @ColumnResult(name = "endDate", type = Date.class),
                        @ColumnResult(name = "transferStatusCode", type = String.class),
                }
        )
)
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
    private String transferStatusCode;

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

//    @OneToOne
//    @JoinColumn(name = "from_user_id", referencedColumnName = "id")
//    private EndUser fromUser;

    @Column(name = "from_user_id")
    private Long fromUserId;

    @Column(name = "TO_VOUCHER_SHORT_LINK")
    private String toVoucherShortLink;

    @Column(name = "TO_EV")
    private String toEv;

    @Column(name = "[TO]")
    private String toMobileNumber;

    @OneToOne
    @JoinColumn(name = "to_user_id", referencedColumnName = "id")
    private EndUser toUser;


    @Column(name = "VOUCHER_TYPE_CD")
    private String voucherTypeCode;

    @Column(name = "INIT_AMOUNT")
    private Double initAmount;

    @Column(name = "TRANSFER_AMOUNT")
    private Double transferAmount;
}

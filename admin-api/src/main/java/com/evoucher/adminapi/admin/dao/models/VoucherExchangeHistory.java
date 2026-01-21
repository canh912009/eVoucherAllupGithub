package com.evoucher.adminapi.admin.dao.models;

import com.evoucher.adminapi.admin.service.models.ChildOfChoiceVoucherResponse;
import com.evoucher.adminapi.admin.service.models.CsExchangeHistoryDTO;
import com.evoucher.adminapi.admin.service.models.PinDetailResponse;
import com.evoucher.adminapi.cms.dao.models.Store;
import com.evoucher.adminapi.common.enums.SystemType;
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
@Table(name = "TB_EXCHANGE_HISTORY")

@NamedNativeQuery(
        name = "finsExchangeHistoryByVoucherId",
        query = "select teh.transaction_id as id,\n" +
                "    teh.transaction_dt as exchangeDate,\n" +
                "       teh.store_id as storeId,\n" +
                "       ts.store_nm as storeName,\n" +
                "       teh.staff_mobile_num as storeStaff,\n" +
                "       tv.voucher_status_cd as pinStatus,\n" +
                "       teh.exchange_type as exchangeType,\n" +
                "       teh.exchange_amount as exchangeAmount,\n" +
                "       teh.usage_remaining_count as remainingCount\n" +
                "from tb_exchange_history teh\n" +
                "    left join tb_voucher tv on teh.ev = tv.ev\n" +
                "    left join tb_store ts on teh.store_id = ts.store_id\n" +
                "where teh.ev=:ev",
        resultSetMapping = "cs_exchange_history"
)

@NamedNativeQuery(
        name = "findPinDetailById",
        query = "select tv.ev as voucherUUID,\n" +
                "       tc.campaign_nm as campaignName,\n" +
                "       tv.user_nm as targetName,\n" +
                "       tu.user_mobile_num as targetNumber,\n" +
                "       tu.email as targetEmail,\n" +
                "       tv.campaign_id as campaignId,\n" +
                "       tv.short_link as accessLink,\n" +
                "       tp.publish_nm as deliveryName,\n" +
                "       tv.ext_pin_no as pin,\n" +
                "       tv.publish_id as deliveryId,\n" +
                "       tv.voucher_status_cd as pinStatus,\n" +
                "       tg.goods_nm as productName,\n" +
                "       tv.publish_dt as deliveryDate,\n" +
                "       tv.creation_dt as startDate,\n" +
                "       tv.expiration_dt as endDate,\n" +
                "       max(teh.transaction_dt) as exchangeDate,\n" +
                "       tpd.sms_type as messageType,\n" +
                "       tpd.publish_dtl_status_cd as result, \n" +
                "       tv.ext_pin_password as pinPassword, \n" +
                "       tv.parent_voucher_token as parentVoucherToken, \n" +
                "       p_ev.`system` as parentSystem, \n"+
                "       tv.voucher_type_cd as voucherTypeCode, \n" +
                "       tv.parent_voucher_ev as parentVoucherEv, \n" +
                "       tpd.publish_dtl_status_cd as publishDetailStatusCode, \n" +
                "       tv.serial_no as serialNo, \n" +
                "       tv.activation_url as activationUrl, \n" +
                "       tv.activation_dt as activationDate,\n" +
                "       tv.`system` as system, \n" +
                "       count(distinct case when tor.req_status = 'APPROVED' then tor.req_id end) as requestCount," +
                "       count(distinct case when tor.req_status = 'REQUESTED' then tor.req_id end) AS requestingCount\n" +
                "from tb_voucher tv\n" +
                "    left join tb_voucher p_ev on tv.parent_voucher_ev = p_ev.ev\n" +
                "    left join tb_publish tp on tv.publish_id = tp.publish_id\n" +
                "    left join tb_ext_pin tep on tv.ext_pin_no = tep.ext_pin_no\n" +
                "    left join tb_goods tg on tg.goods_id = tv.goods_id\n" +
                "    left join tb_campaign tc on tp.campaign_id = tc.campaign_id\n" +
                "    left join tb_publish_detail tpd on tv.publish_dtl_id = tpd.publish_dtl_id\n" +
                "    left join tb_exchange_history teh on tv.ev = teh.ev\n" +
                "    left join tb_operator_request tor on tv.ev = tor.ev " +
                "    left join tb_user tu on tv.user_id = tu.id " +
                "where tv.ev = :ev\n" +
                "group by tv.ev",
        resultSetMapping = "pin_detail_dto"
)
@NamedNativeQuery(
        name = "findChildOfChoiceVoucherByChoiceVoucherId",
        query = "select tv.ev as voucherUUID,\n" +
                "       tc.campaign_nm as campaignName,\n" +
                "       tv.user_nm as targetName,\n" +
                "       tu.user_mobile_num as targetNumber,\n" +
                "       tu.email as targetEmail,\n" +
                "       tv.campaign_id as campaignId,\n" +
                "       tv.short_link as accessLink,\n" +
                "       tp.publish_nm as deliveryName,\n" +
                "       tv.ext_pin_no as pin,\n" +
                "       tv.publish_id as deliveryId,\n" +
                "       tv.voucher_status_cd as pinStatus,\n" +
                "       tg.goods_nm as productName,\n" +
                "       tv.publish_dt as deliveryDate,\n" +
                "       tv.creation_dt as startDate,\n" +
                "       tv.expiration_dt as endDate,\n" +
                "       max(teh.transaction_dt) as exchangeDate,\n" +
                "       tpd.sms_type as messageType,\n" +
                "       tp.publish_status_cd as result, \n" +
                "       tv.ext_pin_password as pinPassword, \n" +
                "       tv.parent_voucher_token as parentVoucherToken, \n" +
                "       tv.voucher_type_cd as voucherTypeCode, \n" +
                "       tv.parent_voucher_ev as parentVoucherEv \n" +
                "from tb_voucher tv\n" +
                "    left join tb_publish tp on tv.publish_id = tp.publish_id\n" +
                "    left join tb_ext_pin tep on tv.ext_pin_no = tep.ext_pin_no\n" +
                "    left join tb_goods tg on tg.goods_id = tv.goods_id\n" +
                "    left join tb_campaign tc on tp.campaign_id = tc.campaign_id\n" +
                "    left join tb_publish_detail tpd on tv.publish_dtl_id = tpd.publish_dtl_id\n" +
                "    left join tb_exchange_history teh on tv.ev = teh.ev \n" +
                "    left join tb_user tu on tv.user_id = tu.id \n" +
                "where tv.parent_voucher_ev = :ev\n" +
                "group by tv.ev",
        resultSetMapping = "ChildOfChoiceVoucherResponse"
)
@SqlResultSetMapping(
        name = "pin_detail_dto",
        classes = @ConstructorResult(
                targetClass = PinDetailResponse.class,
                columns = {
                        @ColumnResult(name = "voucherUUID", type = String.class),
                        @ColumnResult(name = "campaignName", type = String.class),
                        @ColumnResult(name = "targetName", type = String.class),
                        @ColumnResult(name = "targetNumber", type = String.class),
                        @ColumnResult(name = "targetEmail", type = String.class),
                        @ColumnResult(name = "campaignId", type = Long.class),
                        @ColumnResult(name = "accessLink", type = String.class),
                        @ColumnResult(name = "deliveryName", type = String.class),
                        @ColumnResult(name = "pin", type = String.class),
                        @ColumnResult(name = "deliveryId", type = Long.class),
                        @ColumnResult(name = "pinStatus", type = String.class),
                        @ColumnResult(name = "productName", type = String.class),
                        @ColumnResult(name = "deliveryDate", type = Date.class),
                        @ColumnResult(name = "startDate", type = Date.class),
                        @ColumnResult(name = "endDate", type = Date.class),
                        @ColumnResult(name = "exchangeDate", type = Date.class),
                        @ColumnResult(name = "messageType", type = String.class),
                        @ColumnResult(name = "result", type = String.class),
                        @ColumnResult(name = "pinPassword", type = String.class),
                        @ColumnResult(name = "parentVoucherToken", type = String.class),
                        @ColumnResult(name = "voucherTypeCode", type = String.class),
                        @ColumnResult(name = "parentVoucherEv", type = String.class),
                        @ColumnResult(name = "parentSystem", type = String.class),
                        @ColumnResult(name = "publishDetailStatusCode", type = String.class),
                        @ColumnResult(name = "serialNo", type = String.class),
                        @ColumnResult(name = "activationUrl", type = String.class),
                        @ColumnResult(name = "activationDate", type = Date.class),
                        @ColumnResult(name = "system", type = String.class),
                        @ColumnResult(name = "requestCount", type = Integer.class),
                        @ColumnResult(name = "requestingCount", type = Integer.class)
                }
        )
)
@SqlResultSetMapping(
        name = "cs_exchange_history",
        classes = @ConstructorResult(
                targetClass = CsExchangeHistoryDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "exchangeDate", type = Date.class),
                        @ColumnResult(name = "storeId", type = String.class),
                        @ColumnResult(name = "storeName", type = String.class),
                        @ColumnResult(name = "storeStaff", type = String.class),
                        @ColumnResult(name = "pinStatus", type = String.class),
                        @ColumnResult(name = "exchangeType", type = String.class),
                        @ColumnResult(name = "exchangeAmount", type = Double.class),
                        @ColumnResult(name = "remainingCount", type = Integer.class),
                }
        )
)
@SqlResultSetMapping(
        name = "ChildOfChoiceVoucherResponse",
        classes = @ConstructorResult(
                targetClass = ChildOfChoiceVoucherResponse.class,
                columns = {
                        @ColumnResult(name = "voucherUUID", type = String.class),
                        @ColumnResult(name = "campaignName", type = String.class),
                        @ColumnResult(name = "targetName", type = String.class),
                        @ColumnResult(name = "targetNumber", type = String.class),
                        @ColumnResult(name = "targetEmail", type = String.class),
                        @ColumnResult(name = "campaignId", type = Long.class),
                        @ColumnResult(name = "accessLink", type = String.class),
                        @ColumnResult(name = "deliveryName", type = String.class),
                        @ColumnResult(name = "pin", type = String.class),
                        @ColumnResult(name = "deliveryId", type = Long.class),
                        @ColumnResult(name = "pinStatus", type = String.class),
                        @ColumnResult(name = "productName", type = String.class),
                        @ColumnResult(name = "deliveryDate", type = Date.class),
                        @ColumnResult(name = "startDate", type = Date.class),
                        @ColumnResult(name = "endDate", type = Date.class),
                        @ColumnResult(name = "exchangeDate", type = Date.class),
                        @ColumnResult(name = "messageType", type = String.class),
                        @ColumnResult(name = "result", type = String.class),
                        @ColumnResult(name = "pinPassword", type = String.class),
                        @ColumnResult(name = "voucherTypeCode", type = String.class),
                }
        )
)
public class VoucherExchangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private Integer id;

    @Column(name = "EXCHANGE_TYPE")
    private String exchangeType;

    @Column(name = "TRANSACTION_DT")
    private Date transactionDate;

    @Column(name = "STORE_ID")
    private String storeId;

    @Column(name = "EV")
    private String ev;

    @Column(name = "VOUCHER_TYPE_CD")
    private String voucherTypeCode;

    @Column(name = "GOODS_ID")
    private Integer goodsId;

    @Column(name = "GOODS_NM")
    private String goodsName;

    @Column(name = "LIST_PRICE")
    private Double listPrice;

    @Column(name = "DC_RATE")
    private Double discountRate;

    @Column(name = "DC_AMOUNT")
    private Double discountAmount;

    @Column(name = "EXCHANGE_AMOUNT")
    private Double exchangeAmount;

    @Column(name = "USER_MOBILE_NUM")
    private String userMobileNumber;

    @Column(name = "STAFF_MOBILE_NUM")
    private String staffMobileNumber;
    @Column(name = "usage_remaining_count")
    private Integer usageRemainingCount;
}

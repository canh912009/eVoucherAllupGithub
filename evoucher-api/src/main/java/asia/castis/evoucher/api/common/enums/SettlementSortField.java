package asia.castis.evoucher.api.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SettlementSortField {
    LOG_ID("logId", "sl.log_id"),
    EV("ev", "sl.ev"),
    SETTLEMENT_LOG_TYPE("settlementLogType", "sl.settlement_log_type"),
    PUBLISH_ID("publishId", "sl.publish_id"),
    PUBLISH_NAME("publishName", "p.publish_nm"),
    TRANSACTION_ID("transactionId", "sl.transaction_id"),
    TRANSACTION_DATE("transactionDate", "sl.transaction_dt"),
    LOG_CREATE_DATE("logCreateDate", "sl.log_create_dt"),
    VOUCHER_TYPE_CODE("voucherTypeCode", "sl.voucher_type_cd"),
    GOODS_NAME("goodsName", "g.goods_nm"),
    COMPANY_NAME("companyName", "s.supplier_nm, c.customer_nm"),
    BRAND_NAME("brandName", "b.brand_nm"),
    STORE_NAME("storeName", "st.store_nm"),
    USER_MOBILE_NUMBER("userMobileNumber", ""),
    SETTLEMENT_COMPLETE_YN("settlementCompleteYn", "sl.settlement_complete_yn"),
    SETTLEMENT_COMPLETE_DATE("settlementCompleteDate", "sl.settlement_complete_dt"),
    SETTLEMENT_TARGET("settlementTarget", "sl.settlement_target"),
    SETTLEMENT_METHOD_CODE("settlementMethodCode", "sl.settlement_method_cd"),
    LIST_PRICE("listPrice", "sl.list_price"),
    SALES_PRICE("salesPrice", "sl.sales_price"),
    VAT_INCLUDE_YN("vatIncludeYn", "sl.vat_inc_yn"),
    COMMISSION_RATE("commissionRate", "sl.commission_rate"),
    SEND_COST("sendCost", "sl.send_cost"),
    SETTLEMENT_EXCEPT_REASON_CODE("settlementExceptReasonCode", "sl.settlement_except_reason_cd"),
    SETTLEMENT_EXCEPT_REASON("settlementExceptReason", "sl.settlement_except_reason");

    private final String field;
    private final String fieldDB;

}

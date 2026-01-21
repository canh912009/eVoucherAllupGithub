package com.evoucher.adminapi.admin.enums;

import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SettlementSortField {
    LOG_ID("logId", "sl.log_id"),
    EV("ev", "sl.ev"),
    SETTLEMENT_LOG_TYPE("settlementLogType", "sl.settlement_log_type"),
    PUBLISH_ID("publishId", "sl.publish_id"),
    PARENT_VOUCHER_EV("parentVoucherEv", "v.parent_voucher_ev"),
    ORIGINAL_EV("originalEv", "v.orig_ev"),
    PUBLISH_NAME("publishName", "p.publish_nm"),
    SERIAL_NO("serialNo", "v.serial_no"),
    ACTIVATION_DATE("activationDate", "v.activation_dt"),
//    PUBLISH_DETAIL_ID("publishDetailId", "sl.publish_dtl_id"),
    TRANSACTION_ID("transactionId", "sl.transaction_id"),
    TRANSACTION_DATE("transactionDate", "sl.transaction_dt"),
    LOG_CREATE_DATE("logCreateDate", "sl.log_create_dt"),
    VOUCHER_TYPE_CODE("voucherTypeCode", "sl.voucher_type_cd"),
//    GOODS_ID("goodsId", "sl.goods_id"),
    GOODS_NAME("goodsName", "g.goods_nm"),
//    CUSTOMER_ID("customerId", "sl.customer_id"),
//    CUSTOMER_NAME("customerName", "c.customer_nm"),
//    SUPPLIER_ID("supplierId", "sl.supplier_id"),
//    SUPPLIER_NAME("supplierName", "s.supplier_nm"),
    COMPANY_NAME("companyName", "s.supplier_nm, c.customer_nm"),
//    BRAND_ID("brandId", "sl.brand_id"),
    BRAND_NAME("brandName", "b.brand_nm"),
//    STORE_ID("storeId", "sl.store_id"),
    STORE_NAME("storeName", "st.store_nm"),
    USER_MOBILE_NUMBER("userMobileNumber", "sl.user_mobile_num"),
    USER_EMAIL("userEmail", "u.email"),
    PIN("pin", "v.ext_pin_no"),
    MANAGER_NAME("managerName", "c.mngr_nm"),
    REMAIN_AMOUNT("remainAmount", "sl.remain_balance"),
    INIT_AMOUNT("initAmount", "v.init_amount"),
//    STAFF_MOBILE_NUMBER("staffMobileNumber", "sl.user_mobile_num"),
    SETTLEMENT_COMPLETE_YN("settlementCompleteYn", "sl.settlement_complete_yn"),
    SETTLEMENT_COMPLETE_DATE("settlementCompleteDate", "sl.settlement_complete_dt"),
    SETTLEMENT_TARGET("settlementTarget", "sl.settlement_target"),
    SETTLEMENT_METHOD_CODE("settlementMethodCode", "sl.settlement_method_cd"),
    LIST_PRICE("listPrice", "sl.list_price"),
    SALES_PRICE("salesPrice", "sl.sales_price"),
//    DISCOUNT_RATE("discountRate", "sl.dc_rate"),
//    DISCOUNT_AMOUNT("discountAmount", "sl.dc_amount"),
//    DISCOUNT_APPLIED_AMOUNT("discountAppliedAmount", "sl.dc_applied_amount"),
//    SETTLEMENT_AMOUNT("settlementAmount", "sl.settlement_amount"),
    VAT_INCLUDE_YN("vatIncludeYn", "sl.vat_inc_yn"),
//    VAT_AMOUNT("vatAmount", "sl.vat_amount"),
    COMMISSION_RATE("commissionRate", "sl.commission_rate"),
//    COMMISSION_AMOUNT("commissionAmount", "sl.commission_amount"),
    SEND_COST("sendCost", "sl.send_cost"),
    SETTLEMENT_EXCEPT_REASON_CODE("settlementExceptReasonCode", "sl.settlement_except_reason_cd"),
    SETTLEMENT_EXCEPT_REASON("settlementExceptReason", "sl.settlement_except_reason");
//    CAMPAIGN_ID("campaignId", "sl.campaign_id"),
//    CAMPAIGN_NAME("campaignName", "cp.campaign_nm");

    private final String field;
    private final String fieldDB;



    public static SettlementSortField fromField(String field) {
        for (SettlementSortField settlementSortField : SettlementSortField.values()) {
            if (settlementSortField.field.equalsIgnoreCase(field)) {
                return settlementSortField;
            }
        }
        throw new CustomCodeException(
                MessageUtils.getMessage("evoucher.settlement.sort.field.not.found", field),
                HttpStatus.BAD_REQUEST);
    }
}

package com.evoucher.adminapi.settlement.dao;

import com.evoucher.adminapi.admin.enums.SettlementSortField;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.config.PropertyConverter;
import com.evoucher.adminapi.common.enums.DirectionSort;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.utils.DateUtils;
import com.evoucher.adminapi.settlement.enums.SettlementTarget;
import com.evoucher.adminapi.settlement.service.model.FilterSearchSettlement;
import com.evoucher.adminapi.settlement.service.model.SettlementLogAllDataDto;
import com.evoucher.adminapi.settlement.service.model.SettlementLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
@Transactional
public class SettlementLogRepositoryImpl implements SettlementLogRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PropertyConverter propertyConverter;


    @Override
    public List<SettlementLogDTO> searchSettlementLog(FilterSearchSettlement filterSearchSettlement, Pageable pageable) {
        List<SettlementLogDTO> result = new ArrayList<>();
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        // set sql query
        querySearchSettlementLog(strQuery);
        querySettlementLogTable(strQuery);
        queryWhereAlwaysTrue(strQuery);

        setParam(filterSearchSettlement, strQuery, mapParam);

        strQuery.append(" group by log_id ");

        sortParam(filterSearchSettlement, strQuery);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        // set Page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        // add scala for dto
        addQueryScalaSettlementLog(query);
        query.setResultTransformer(Transformers.aliasToBean(SettlementLogDTO.class));

        List<SettlementLogDTO> list = query.getResultList();

        result = convertDataSettlementLogDTOS(result, list);
        return result;
    }

    @Override
    public long countSettlementLog(FilterSearchSettlement filterSearchSettlement) {
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT count(distinct sl.log_id) ");
        querySettlementLogTable(strQuery);
        queryWhereAlwaysTrue(strQuery);

        setParam(filterSearchSettlement, strQuery, mapParam);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        Object result = query.getSingleResult();
        return Long.parseLong(result.toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SettlementLogAllDataDto> getSettlementLogData(FilterSearchSettlement filterSearchSettlement) {
        List<SettlementLogAllDataDto> result = new ArrayList<>();
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        // create sql query
        querySearchSettlementLog(strQuery);
        querySearchSettlementForAll(strQuery);
        querySettlementLogTable(strQuery);
        querySettlementLogTableForAll(strQuery);
        queryWhereAlwaysTrue(strQuery);

        setParam(filterSearchSettlement, strQuery, mapParam);

        strQuery.append(" group by log_id ");

        sortParam(filterSearchSettlement, strQuery);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        // add scalar for dto
        addQueryScalaSettlementLog(query);
        addQueryScalaSettlementLogForAll(query);
        query.setResultTransformer(Transformers.aliasToBean(SettlementLogAllDataDto.class));

        List<SettlementLogAllDataDto> list = query.getResultList();

        result = (List<SettlementLogAllDataDto>) convertDataSettlementLogForAll(result, list);
        return result;
    }

    private void querySearchSettlementLog(StringBuilder strQuery) {
        strQuery.append("SELECT sl.log_id logId, sl.ev ev, sl.settlement_log_type settlementLogType, sl.publish_id publishId, ");
        strQuery.append("       sl.publish_dtl_id publishDetailId, sl.transaction_dt transactionDate, ");
        strQuery.append("       sl.log_create_dt logCreateDate, sl.voucher_type_cd voucherTypeCode, sl.goods_id goodsId, sl.customer_id customerId, ");
        strQuery.append("       sl.supplier_id supplierId, sl.brand_id brandId, sl.store_id storeId, sl.user_mobile_num userMobileNumber, ");
        strQuery.append("       sl.staff_mobile_num staffMobileNumber, sl.settlement_complete_yn settlementCompleteYn, ");
        strQuery.append("       sl.settlement_complete_dt settlementCompleteDate, sl.settlement_target settlementTarget, sl.settlement_method_cd  settlementMethodCode, ");
        strQuery.append("       sl.list_price listPrice, sl.sales_price salesPrice, sl.dc_rate discountRate, sl.dc_amount discountAmount, ");
        strQuery.append("       sl.dc_applied_amount discountAppliedAmount, sl.settlement_amount settlementAmount, sl.vat_inc_yn vatIncludeYn, ");
        strQuery.append("       sl.vat_amount vatAmount, sl.commission_rate commissionRate, sl.commission_amount commissionAmount, ");
        strQuery.append("       sl.send_cost sendCost, sl.settlement_except_reason_cd settlementExceptReasonCode, ");
        strQuery.append("       sl.settlement_except_reason settlementExceptReason, sl.remain_balance remainAmount, ");
        strQuery.append("       s.supplier_nm supplierName, c.customer_nm customerName, c.mngr_nm as managerName, b.brand_nm brandName, ");
        strQuery.append("       st.store_nm storeName, g.goods_nm goodsName, p.publish_nm publishName, ");
        strQuery.append("       sl.campaign_id campaignId, cp.campaign_nm campaignName, ");
        strQuery.append("       v.serial_no serialNo, v.activation_dt activationDate, v.parent_voucher_ev parentVoucherEv, v.orig_ev originalEv, v.init_amount as initAmount, ");
        strQuery.append("       u.email userEmail, ");
        strQuery.append("       v.ext_pin_no as pin ");
    }

    private void querySearchSettlementForAll(StringBuilder strQuery) {
        strQuery.append(", v.usage_remaining_count as remainingCount ");
        strQuery.append(", v.parent_voucher_token as otp ");
        strQuery.append(", v.`system` as `system` ");
        strQuery.append(", pin.transaction_id as transactionId ");
        strQuery.append(", pin.password as password ");
        strQuery.append(", vnpt.face_value as faceValue ");
        strQuery.append(", vnpt.card_serial as cardSerial ");
        strQuery.append(", vnpt.card_pin as cardPin ");
        strQuery.append(", vnpt.target_phone_no as topupNumber ");
        strQuery.append(", vnpt.provider_code as provider ");
        strQuery.append(", vnpt.vnpt_request_id as requestId ");
    }

    private void querySettlementLogTable(StringBuilder strQuery) {
        strQuery.append("FROM tb_settlement_log sl ");
        strQuery.append("LEFT JOIN tb_voucher v ON sl.ev = v.ev ");
        strQuery.append("LEFT JOIN tb_publish p ON sl.publish_id = p.publish_id ");
        strQuery.append("LEFT JOIN tb_supplier s ON sl.supplier_id = s.supplier_id ");
        strQuery.append("LEFT JOIN tb_customer c ON sl.customer_id = c.customer_id ");
        strQuery.append("LEFT JOIN tb_brand b ON sl.brand_id = b.brand_id ");
        strQuery.append("LEFT JOIN tb_store st ON sl.store_id = st.store_id ");
        strQuery.append("LEFT JOIN tb_goods g ON sl.goods_id = g.goods_id ");
        strQuery.append("LEFT JOIN tb_campaign cp ON sl.campaign_id = cp.campaign_id ");
        strQuery.append("LEFT JOIN tb_user u ON v.user_id = u.id ");
    }

    private void querySettlementLogTableForAll(StringBuilder strQuery) {
        strQuery.append("left join tb_ext_pin pin on v.ext_pin_no = pin.ext_pin_no ");
        strQuery.append("left join tb_vnpt_voucher_exchange_history vnpt on sl.settlement_log_type = 'PER_EXCHANGE' and sl.ev = vnpt.ev and vnpt.result = 'SUCCESS' ");
    }

    private void queryWhereAlwaysTrue(StringBuilder strQuery) {
        strQuery.append("WHERE 1=1 ");
    }

    private void setParam(FilterSearchSettlement filterSearchSettlement, StringBuilder strQuery, Map<String, Object> mapParam) {
        if (ObjectUtils.isEmpty(filterSearchSettlement)) return;

        if (StringUtils.isNotBlank(filterSearchSettlement.getSettlementTarget())) {
            strQuery.append(" AND sl.settlement_target = :settlementTarget");
            mapParam.put("settlementTarget", filterSearchSettlement.getSettlementTarget().trim());
        }
        if (StringUtils.isNotBlank(filterSearchSettlement.getCompanyName())) {
            strQuery.append(" AND (s.supplier_nm LIKE :supplierName");
            strQuery.append("      OR c.customer_nm LIKE :customerName) ");
            mapParam.put("supplierName", '%' + filterSearchSettlement.getCompanyName().trim() + '%');
            mapParam.put("customerName", '%' + filterSearchSettlement.getCompanyName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchSettlement.getVoucherTypeCode())) {
            strQuery.append(" AND sl.voucher_type_cd = :voucherTypeCode");
            mapParam.put("voucherTypeCode", filterSearchSettlement.getVoucherTypeCode().trim());

        }
        if (StringUtils.isNotBlank(filterSearchSettlement.getSettlementMethodCode())) {
            strQuery.append(" AND sl.settlement_method_cd = :settlementMethodCode");
            mapParam.put("settlementMethodCode", filterSearchSettlement.getSettlementMethodCode().trim());
        }
        if (Objects.nonNull(filterSearchSettlement.getTransactionStartDate())) {
            Date startDate = filterSearchSettlement.getTransactionStartDate();
            strQuery.append(" AND sl.transaction_dt >= :transactionStartDate");
            mapParam.put("transactionStartDate", startDate);
        }
        if (Objects.nonNull(filterSearchSettlement.getTransactionEndDate())) {
            Date endDate = DateUtils.atEndOfDay(filterSearchSettlement.getTransactionEndDate());
            strQuery.append(" AND sl.transaction_dt <= :transactionEndDate");
            mapParam.put("transactionEndDate", endDate);
        }
        if (StringUtils.isNotBlank(filterSearchSettlement.getCampaignId())) {
            strQuery.append(" AND sl.campaign_id = :campaignId");
            mapParam.put("campaignId", filterSearchSettlement.getCampaignId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchSettlement.getCampaignName())) {
            strQuery.append(" AND cp.campaign_nm LIKE :campaignName");
            mapParam.put("campaignName", '%' + filterSearchSettlement.getCampaignName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchSettlement.getPublishId())) {
            strQuery.append(" AND sl.publish_id = :publishId");
            mapParam.put("publishId", filterSearchSettlement.getPublishId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchSettlement.getPublishName())) {
            strQuery.append(" AND p.publish_nm LIKE :publishName");
            mapParam.put("publishName", '%' + filterSearchSettlement.getPublishName().trim() + '%');
        }

        // Set authority role
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        String adminCorpId = user.getAdminCorpId();
        EnumRole role = Enum.valueOf(EnumRole.class, user.getAdminType());
        log.info("Validate permission with user info: {}", user);
        switch (role) {
            case ROLE_ADMIN:
            case ROLE_OPERATOR:
                break;
            case ROLE_CUSTOMER:
                strQuery.append(" AND sl.customer_id = :adminCorpId");
                mapParam.put("adminCorpId", adminCorpId);
                strQuery.append(" AND sl.settlement_target = :settlementTargetAuthority");
                mapParam.put("settlementTargetAuthority", SettlementTarget.CUSTOMER.name());
                break;
            case ROLE_SUPPLIER:
                strQuery.append(" AND sl.supplier_id = :adminCorpId");
                mapParam.put("adminCorpId", adminCorpId);
                strQuery.append(" AND sl.settlement_target = :settlementTargetAuthority");
                mapParam.put("settlementTargetAuthority", SettlementTarget.SUPPLIER.name());
                break;
            case ROLE_BRAND: {
                strQuery.append(" AND sl.brand_id = :adminCorpId");
                mapParam.put("adminCorpId", adminCorpId);
                strQuery.append(" AND sl.settlement_target = :settlementTargetAuthority");
                mapParam.put("settlementTargetAuthority", SettlementTarget.SUPPLIER.name());
                break;
            }
        }
    }

    private void addQueryScalaSettlementLog(NativeQuery query) {
        query.addScalar("logId", StandardBasicTypes.INTEGER);
        query.addScalar("ev", StandardBasicTypes.STRING);
        query.addScalar("settlementLogType", StandardBasicTypes.STRING);
        query.addScalar("publishId", StandardBasicTypes.INTEGER);
        query.addScalar("publishName", StandardBasicTypes.STRING);
        query.addScalar("publishDetailId", StandardBasicTypes.INTEGER);
        query.addScalar("transactionDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("logCreateDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("voucherTypeCode", StandardBasicTypes.STRING);
        query.addScalar("goodsId", StandardBasicTypes.INTEGER);
        query.addScalar("goodsName", StandardBasicTypes.STRING);
        query.addScalar("customerId", StandardBasicTypes.STRING);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.addScalar("supplierId", StandardBasicTypes.STRING);
        query.addScalar("supplierName", StandardBasicTypes.STRING);
        query.addScalar("brandId", StandardBasicTypes.STRING);
        query.addScalar("brandName", StandardBasicTypes.STRING);
        query.addScalar("storeId", StandardBasicTypes.STRING);
        query.addScalar("storeName", StandardBasicTypes.STRING);
        query.addScalar("managerName", StandardBasicTypes.STRING);
        query.addScalar("userMobileNumber", StandardBasicTypes.STRING);
        query.addScalar("userEmail", StandardBasicTypes.STRING);
        query.addScalar("staffMobileNumber", StandardBasicTypes.STRING);
        query.addScalar("settlementCompleteYn", StandardBasicTypes.STRING);
        query.addScalar("settlementCompleteDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("settlementTarget", StandardBasicTypes.STRING);
        query.addScalar("settlementMethodCode", StandardBasicTypes.STRING);
        query.addScalar("listPrice", StandardBasicTypes.DOUBLE);
        query.addScalar("salesPrice", StandardBasicTypes.DOUBLE);
        query.addScalar("discountRate", StandardBasicTypes.DOUBLE);
        query.addScalar("discountAmount", StandardBasicTypes.DOUBLE);
        query.addScalar("discountAppliedAmount", StandardBasicTypes.DOUBLE);
        query.addScalar("settlementAmount", StandardBasicTypes.DOUBLE);
        query.addScalar("vatIncludeYn", StandardBasicTypes.STRING);
        query.addScalar("vatAmount", StandardBasicTypes.DOUBLE);
        query.addScalar("commissionRate", StandardBasicTypes.DOUBLE);
        query.addScalar("commissionAmount", StandardBasicTypes.DOUBLE);
        query.addScalar("sendCost", StandardBasicTypes.DOUBLE);
        query.addScalar("settlementExceptReasonCode", StandardBasicTypes.STRING);
        query.addScalar("settlementExceptReason", StandardBasicTypes.STRING);
        query.addScalar("remainAmount", StandardBasicTypes.DOUBLE);
        query.addScalar("initAmount", StandardBasicTypes.DOUBLE);
        query.addScalar("campaignId", StandardBasicTypes.INTEGER);
        query.addScalar("campaignName", StandardBasicTypes.STRING);
        query.addScalar("serialNo", StandardBasicTypes.STRING);
        query.addScalar("activationDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("originalEv", StandardBasicTypes.STRING);
        query.addScalar("parentVoucherEv", StandardBasicTypes.STRING);
        query.addScalar("pin", StandardBasicTypes.STRING);
    }

    private void addQueryScalaSettlementLogForAll(NativeQuery query) {
        query.addScalar("remainingCount", StandardBasicTypes.INTEGER );
        query.addScalar("otp", StandardBasicTypes.STRING);
        query.addScalar("system", StandardBasicTypes.STRING);
        query.addScalar("transactionId", StandardBasicTypes.STRING);
        query.addScalar("faceValue", StandardBasicTypes.LONG);
        query.addScalar("cardSerial", StandardBasicTypes.STRING);
        query.addScalar("cardPin", StandardBasicTypes.STRING);
        query.addScalar("topupNumber", StandardBasicTypes.STRING);
        query.addScalar("provider", StandardBasicTypes.STRING);
        query.addScalar("requestId", StandardBasicTypes.STRING);
        query.addScalar("password", StandardBasicTypes.STRING);
    }

    private void sortParam(FilterSearchSettlement filterSearchSettlement, StringBuilder strQuery) {
        if (StringUtils.isNotBlank(filterSearchSettlement.getSort())) {
            SettlementSortField settlementSortField =
                    SettlementSortField.fromField(filterSearchSettlement.getSort());

            String sortFieldDB = settlementSortField.getFieldDB();
            DirectionSort direction = Objects.nonNull(filterSearchSettlement.getDirection()) ?
                    filterSearchSettlement.getDirection() : DirectionSort.ASC;

            strQuery.append(" ORDER BY ").append(sortFieldDB).append(" ").append(direction.name());
        }
    }

    private List<SettlementLogDTO> convertDataSettlementLogDTOS(List<SettlementLogDTO> result, List<SettlementLogDTO> list) {
        if (list != null && !list.isEmpty()) {
            // set company name
            result = list.stream().peek(this::convertEachDataSettlementLog).collect(Collectors.toList());
        }
        return result;
    }

    private List<? extends SettlementLogDTO> convertDataSettlementLogForAll(List<? extends SettlementLogDTO> result, List<? extends SettlementLogDTO> list) {
        if (list != null && !list.isEmpty()) {
            // set company name
            result = list.stream().peek(this::convertEachDataSettlementLog).collect(Collectors.toList());
        }
        return result;
    }

    private void convertEachDataSettlementLog(SettlementLogDTO s) {
        String companyName = SettlementTarget.SUPPLIER.name().equals(s.getSettlementTarget()) ? s.getSupplierName() : s.getCustomerName();
        s.setCompanyName(companyName);

        // decrypt user mobile number
        if (StringUtils.isNotBlank(s.getUserMobileNumber())) {
            s.setUserMobileNumber(propertyConverter.convertToEntityAttribute(s.getUserMobileNumber()));
        }
    }
}

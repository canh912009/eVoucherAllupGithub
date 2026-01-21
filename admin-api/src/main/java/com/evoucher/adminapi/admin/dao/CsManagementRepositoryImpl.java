package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.service.models.CsManagementFilterRequest;
import com.evoucher.adminapi.admin.service.models.CsManagementResponse;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.EnumRole;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

import static com.evoucher.adminapi.common.utils.DataUtils.createSortQuery;
import static com.evoucher.adminapi.common.utils.DataUtils.setPageInfoToQuery;

@Repository
@RequiredArgsConstructor
@Transactional
public class CsManagementRepositoryImpl implements CsManagementRepository {
    private final EntityManager entityManager;

    @Override
    public List<CsManagementResponse> search(CsManagementFilterRequest filter, Pageable pageable) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("WITH vnpt AS (\n" +
                "    select * ,\n" +
                "           row_number() over ( partition by ev order by exchange_date desc ) as vnpt_num\n" +
                "    from tb_vnpt_voucher_exchange_history\n" +
                ")");
        queryString.append("select \n");
        queryString.append("       tv.ev as voucherUUID,\n");
        queryString.append("       tc.campaign_nm as campaignName,\n");
        queryString.append("       tp.publish_nm as deliveryName,\n");
        queryString.append("       tu.user_nm as targetName,\n");
        queryString.append("       tu.email as targetEmail,\n");
        queryString.append("       tg.goods_nm as productName,\n");
        queryString.append("        tu.user_mobile_num as targetNumber,\n");
        queryString.append("        tv.ext_pin_no as pin,\n");
        queryString.append("        tv.voucher_status_cd as pinStatus,\n");
        queryString.append("        tv.short_link as accessLink,\n");
        queryString.append("        tv.publish_dt as deliveryDate,\n");
        queryString.append("        tv.voucher_type_cd as voucherType, \n");
        queryString.append("        tg.goods_id as productId, \n");
        queryString.append("        tv.expiration_dt as expireDate \n");

        queryString.append("        , tv.`system` as `system` \n");
        queryString.append("        , tv.parent_voucher_token as otp\n");
        queryString.append("        , tv.voucher_status_cd as status\n");
        queryString.append("        , tv.usage_remaining_count as remainingCount\n");
        queryString.append("        , tv.balance as remainingBalance\n");
        queryString.append("        , p.password as password\n");
        queryString.append("        , vnpt.face_value as faceValue\n");
        queryString.append("        , vnpt.card_serial as cardSerial\n");
        queryString.append("        , vnpt.card_pin as cardPin\n");
        queryString.append("        , vnpt.target_phone_no as topupNumber\n");
        queryString.append("        , vnpt.provider_code as provider\n");
        queryString.append("        , vnpt.vnpt_request_id as requestId\n");
        queryString.append("        ,coalesce(p.transaction_id, vnpt.request_history_id) as transactionId\n");


        queryString.append("from tb_voucher tv\n");
        queryString.append("    left join tb_publish tp on tv.publish_id = tp.publish_id\n");
        queryString.append("    left join tb_campaign tc on tv.campaign_id = tc.campaign_id\n");
        queryString.append("    left join tb_goods tg on tv.goods_id = tg.goods_id\n");
        queryString.append("    left join tb_user tu on tv.user_id = tu.id\n");
        queryString.append("    left join tb_ext_pin p on tv.ext_pin_id = p.id\n");
        queryString.append("    left join vnpt on tv.ev = vnpt.ev and vnpt.vnpt_num = 1\n");
        queryString.append("where 0=0 ");
        createWhereQuery(queryString, filter);
//        queryString.append(" group by tv.ev ");

        createSortQuery(queryString, filter.getDirection(), filter.getSort());

        NativeQueryImpl query = (NativeQueryImpl) entityManager.createNativeQuery(queryString.toString());
        setWhereParameter(query, filter);

        query.addScalar("voucherUUID", StringType.INSTANCE);
        query.addScalar("campaignName", StringType.INSTANCE);
        query.addScalar("deliveryName", StringType.INSTANCE);
        query.addScalar("productName", StringType.INSTANCE);
        query.addScalar("targetNumber", StringType.INSTANCE);
        query.addScalar("targetEmail", StringType.INSTANCE);
        query.addScalar("targetName", StringType.INSTANCE);
        query.addScalar("pin", StringType.INSTANCE);
        query.addScalar("pinStatus", StringType.INSTANCE);
        query.addScalar("accessLink", StringType.INSTANCE);
        query.addScalar("deliveryDate", TimestampType.INSTANCE);
        query.addScalar("voucherType", StringType.INSTANCE);
        query.addScalar("productId", LongType.INSTANCE);
        query.addScalar("expireDate", TimestampType.INSTANCE);
        query.addScalar("transactionId", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("otp", StringType.INSTANCE);
        query.addScalar("remainingCount", IntegerType.INSTANCE);
        query.addScalar("remainingBalance", DoubleType.INSTANCE);
        query.addScalar("password", StringType.INSTANCE);
        query.addScalar("system", StringType.INSTANCE);
        query.addScalar("faceValue", LongType.INSTANCE);
        query.addScalar("cardSerial", StringType.INSTANCE);
        query.addScalar("cardPin", StringType.INSTANCE);
        query.addScalar("topupNumber", StringType.INSTANCE);
        query.addScalar("provider", StringType.INSTANCE);
        query.addScalar("requestId", StringType.INSTANCE);

        setPageInfoToQuery(query, pageable);

        query.setResultTransformer(Transformers.aliasToBean(CsManagementResponse.class));

        return query.getResultList();
    }

    @Override
    public long count(CsManagementFilterRequest filter) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("select count (distinct cs.id) from (");
        queryString.append("select tv.ev as id\n");
        queryString.append("from tb_voucher tv \n");
        queryString.append("    left join tb_publish tp on tv.publish_id = tp.publish_id\n");
        queryString.append("    left join tb_user tu on tv.user_id = tu.id\n");
        queryString.append("    left join tb_campaign tc on tp.campaign_id = tc.campaign_id\n");
        queryString.append("    left join tb_goods tg on tv.goods_id = tg.goods_id\n");
        queryString.append("where 0=0 ");
        createWhereQuery(queryString, filter);
        queryString.append("  ) cs");
        NativeQueryImpl query = (NativeQueryImpl) entityManager.createNativeQuery(queryString.toString());
        setWhereParameter(query, filter);


        return Long.parseLong(query.getSingleResult().toString());
    }

    private void createWhereQuery(StringBuilder queryString, CsManagementFilterRequest filter) {
        if (filter.getStartDateObject() != null) {
            queryString.append(" and tv.publish_dt >= :creationDate ");
        }
        if (filter.getEndDateObject() != null) {
            queryString.append(" and tv.publish_dt <= :expirationDate ");
        }
        if (filter.getPinStatus() != null && !filter.getPinStatus().isBlank()) {
            queryString.append(" and tv.voucher_status_cd = :pinStatus");
        }
        if (filter.getDeliveryName() != null && !filter.getDeliveryName().isBlank()) {
            queryString.append(" and tp.publish_nm like :deliveryName");
        }
        if (filter.getCampaignName() != null && !filter.getCampaignName().isBlank()) {
            queryString.append(" and tc.campaign_nm like :campaignName");
        }
        if (filter.getPins() != null && !filter.getPins().isEmpty()) {
            queryString.append(" and tv.ext_pin_no regexp :pins");
        }
        if (filter.getTargetNumbers() != null && !filter.getTargetNumbers().isEmpty()) {
            queryString.append(" and tu.user_mobile_num in (:targetNumbers)");
        }
        if (filter.getTargetNames() != null && !filter.getTargetNames().isEmpty()) {
            queryString.append(" and tu.user_nm in (:targetNames)");
        }
        if (filter.getDeliveryId() != null && !filter.getDeliveryId().isEmpty()) {
            queryString.append(" and tp.publish_id = :deliveryId");
        }
        if (filter.getCampaignId() != null) {
            queryString.append(" and tc.campaign_id = :campaignId");
        }
        if (StringUtils.isNotBlank(filter.getVoucherUUID())) {
            queryString.append(" and tv.ev = :ev");
        }
        if (StringUtils.isNotBlank(filter.getDeliveryDate())) {
            queryString.append(" and DATE(tv.publish_dt) = :deliveryDate");
        }
        if (StringUtils.isNotBlank(filter.getSerialNo()) && !filter.getSerialNo().isEmpty()) {
            queryString.append(" and tv.serial_no like :serialNo");
        }
        if (Objects.nonNull(filter.getProductId())) {
            queryString.append(" and tg.goods_id = :goodId");
        }
        if (StringUtils.isNotBlank(filter.getProductName())) {
            queryString.append(" and tg.goods_nm like :goodName");
        }
        if (Objects.nonNull(filter.getVoucherExpireBefore())) {
            queryString.append(" and tv.expiration_dt < :voucherExpireBefore");
        }

        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        EnumRole userRole = EnumRole.valueOf(user.getAdminType());

        if (userRole == EnumRole.ROLE_SUPPLIER) {
            queryString.append(" and tg.supplier_id = :supplierId ");
        }
    }

    private void setWhereParameter(NativeQueryImpl query, CsManagementFilterRequest filter) {
        if (filter.getStartDateObject() != null) {
            query.setParameter("creationDate", filter.getStartDateObject());
        }
        if (filter.getEndDateObject() != null) {
            query.setParameter("expirationDate", filter.getEndDateObject());
        }
        if (filter.getPinStatus() != null && !filter.getPinStatus().isBlank()) {
            query.setParameter("pinStatus", filter.getPinStatus());
        }
        if (filter.getDeliveryName() != null && !filter.getDeliveryName().isBlank()) {
            query.setParameter("deliveryName", String.format("%%%s%%", filter.getDeliveryName()));
        }
        if (filter.getCampaignName() != null && !filter.getCampaignName().isBlank()) {
            query.setParameter("campaignName", String.format("%%%s%%", filter.getCampaignName()));
        }
        if (filter.getPins() != null && !filter.getPins().isEmpty()) {
            query.setParameter("pins", String.join("|", filter.getPins()));
        }
        if (filter.getTargetNumbers() != null && !filter.getTargetNumbers().isEmpty()) {
            query.setParameterList("targetNumbers", filter.getTargetNumbers());
        }
        if (filter.getTargetNames() != null && !filter.getTargetNames().isEmpty()) {
            query.setParameterList("targetNames", filter.getTargetNames());
        }
        if (filter.getDeliveryId() != null && !filter.getDeliveryId().isEmpty()) {
            query.setParameter("deliveryId", filter.getDeliveryId());
        }
        if (filter.getCampaignId() != null) {
            query.setParameter("campaignId", filter.getCampaignId());
        }
        if (StringUtils.isNotBlank(filter.getVoucherUUID())) {
            query.setParameter("ev", filter.getVoucherUUID());
        }
        if (StringUtils.isNotBlank(filter.getDeliveryDate())) {
            query.setParameter("deliveryDate", filter.getDeliveryDate());
        }
        if (filter.getSerialNo() != null && !filter.getSerialNo().isEmpty()) {
            query.setParameter("serialNo", String.format("%%%s%%", filter.getSerialNo()));
        }
        if (Objects.nonNull(filter.getProductId())) {
            query.setParameter("goodId", filter.getProductId());
        }
        if (StringUtils.isNotBlank(filter.getProductName())) {
            query.setParameter("goodName", String.format("%%%s%%", filter.getProductName()));
        }
        if (Objects.nonNull(filter.getVoucherExpireBefore())) {
            query.setParameter("voucherExpireBefore", filter.getVoucherExpireBefore());
        }

        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        EnumRole userRole = EnumRole.valueOf(user.getAdminType());

        if (userRole == EnumRole.ROLE_SUPPLIER) {
            query.setParameter("supplierId", user.getAdminCorpId());
        }
    }
}

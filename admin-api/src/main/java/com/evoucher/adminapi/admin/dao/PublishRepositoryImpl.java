package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.service.models.FilterSearchAdmin;
import com.evoucher.adminapi.admin.service.models.SearchPublishResponse;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class PublishRepositoryImpl implements PublishRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<SearchPublishResponse> searchPublish(FilterSearchAdmin filterSearchAdmin, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        List<SearchPublishResponse> result = new ArrayList<>();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT p.publish_id id, p.campaign_id campaignId, p.goods_id goodsId, p.publish_nm publishName, ");
        strQuery.append(" p.booking_yn bookingYn, p.apprv_status_cd approveStatusCode, p.apprver_dt approveDate, ");
        strQuery.append(" p.publish_status_cd statusCode, p.sms_type smsType, ");
        strQuery.append(" p.reg_dt regDt, p.updt_dt updtDt, ");
        strQuery.append(" c.campaign_nm campaignName, g.goods_nm goodsName ");
        strQuery.append(" FROM tb_publish p ");
        strQuery.append(" LEFT JOIN tb_campaign c ON p.campaign_id = c.campaign_id ");
        strQuery.append(" LEFT JOIN tb_goods g ON p.goods_id = g.goods_id ");
        strQuery.append(" WHERE 1 = 1 ");

        setParam(filterSearchAdmin, strQuery, mapParam);
        strQuery.append(" ORDER BY p.publish_id DESC ");

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        // set Page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("id", StandardBasicTypes.INTEGER);
        query.addScalar("publishName", StandardBasicTypes.STRING);
        query.addScalar("smsType", StandardBasicTypes.STRING);
        query.addScalar("campaignId", StandardBasicTypes.INTEGER);
        query.addScalar("campaignName", StandardBasicTypes.STRING);
        query.addScalar("bookingYn", StandardBasicTypes.STRING);
        query.addScalar("goodsName", StandardBasicTypes.STRING);
        query.addScalar("statusCode", StandardBasicTypes.STRING);
        query.addScalar("approveDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(SearchPublishResponse.class));

        List<SearchPublishResponse> list = query.getResultList();

        if (list != null && list.size() > 0) {
            result = list;
        }
        return result;
    }

    @Override
    public long countPublish(FilterSearchAdmin filterSearchAdmin) {
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT COUNT(p.publish_id) ");
        strQuery.append(" FROM tb_publish p ");
        strQuery.append(" LEFT JOIN tb_campaign c ON p.campaign_id = c.campaign_id ");
        strQuery.append(" LEFT JOIN tb_goods g ON p.goods_id = g.goods_id ");
        strQuery.append(" WHERE 1 = 1 ");

        setParam(filterSearchAdmin, strQuery, mapParam);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        Object publishNumber = query.getSingleResult();

        return Long.parseLong(publishNumber.toString());
    }

    private void setParam(FilterSearchAdmin filterSearchAdmin, StringBuilder strQuery, Map<String, Object> mapParam) {
        setParamCommon(filterSearchAdmin, strQuery, mapParam);

        // Set authority role
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        EnumRole userRole = EnumRole.valueOf(user.getAdminType());
//        boolean hasRoleCustomer = EnumRole.ROLE_CUSTOMER.toString().equals(user.getAdminType());

        switch (userRole) {
            case ROLE_CUSTOMER:
                strQuery.append(" AND p.customer_id = :adminCorpId ");
                mapParam.put("adminCorpId", user.getAdminCorpId());
                break;
            case ROLE_SUPPLIER:
                strQuery.append(" and p.supplier_id = :supplierId ");
                mapParam.put("supplierId", user.getAdminCorpId());
                break;
        }
//        if (hasRoleCustomer) {
//            strQuery.append(" AND p.customer_id = :adminCorpId ");
//            mapParam.put("adminCorpId", user.getAdminCorpId());
//        }
    }

    private void setParamCommon(FilterSearchAdmin filterSearchAdmin, StringBuilder strQuery, Map<String, Object> mapParam) {
        if (ObjectUtils.isEmpty(filterSearchAdmin)) return;

        if (StringUtils.isNotBlank(filterSearchAdmin.getPublishId())) {
            strQuery.append(" AND p.publish_id = :publishId");
            mapParam.put("publishId", filterSearchAdmin.getPublishId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getPublishName())) {
            strQuery.append(" AND p.publish_nm LIKE :publishName");
            mapParam.put("publishName", '%' + filterSearchAdmin.getPublishName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getStatusCode())) {
            if (Constant.EMPTY.equals(filterSearchAdmin.getStatusCode())) {
                strQuery.append(" AND p.publish_status_cd IS NULL");
            } else {
                strQuery.append(" AND p.publish_status_cd = :statusCode");
                mapParam.put("statusCode", filterSearchAdmin.getStatusCode().trim());
            }
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getCampaignName())) {
            strQuery.append(" AND c.campaign_nm LIKE :campaignName");
            mapParam.put("campaignName", '%' + filterSearchAdmin.getCampaignName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getGoodsName())) {
            strQuery.append(" AND g.goods_nm LIKE :goodsName");
            mapParam.put("goodsName", '%' + filterSearchAdmin.getGoodsName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getSmsType())) {
            strQuery.append(" AND p.sms_type LIKE :smsType");
            mapParam.put("smsType", filterSearchAdmin.getSmsType().trim());
        }
    }
}

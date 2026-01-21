package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CustomerDTO> searchCustomer(FilterSearchCms FilterSearchCms, Pageable pageable) {
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        EnumRole userRole = EnumRole.valueOf(user.getAdminType());

        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT c.customer_id id, c.customer_nm customerName, c.taxcode taxcode ");
        strQuery.append(", c.apprv_status_cd approveStatusCode ");
        strQuery.append(", c.representative_email representativeMail, c.representative_mobile_no representativeMobile ");
        strQuery.append(", c.valid_yn validYn, c.reg_id regId, c.reg_dt regDt, c.updt_dt updtDt, c.updt_id updtId, ");
        strQuery.append(" c.customer_type customerTypeCode ");
        strQuery.append(" FROM tb_customer c ");

        if (userRole == EnumRole.ROLE_SUPPLIER) {
            strQuery.append(" inner join tb_campaign cp on c.customer_id = cp.customer_id");
            strQuery.append("  inner join tb_campaign_goods_rel cgr on cp.campaign_id = cgr.campaign_id");
            strQuery.append("  inner join tb_goods g on cgr.goods_id = g.goods_id");
            strQuery.append("  inner join tb_brand b on g.brand_id = b.brand_id");
            strQuery.append("  inner join tb_supplier s on b.supplier_id = s.supplier_id");
        }

        strQuery.append(" WHERE c.valid_yn = 'Y' ");

        setParam(FilterSearchCms, strQuery, mapParam);

        if (userRole == EnumRole.ROLE_SUPPLIER) {
            strQuery.append(" group by c.customer_id ");
        }

        strQuery.append(" ORDER BY c.updt_dt DESC ");


        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        // set Page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("id", StandardBasicTypes.STRING);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.addScalar("taxcode", StandardBasicTypes.STRING);
//        query.addScalar("primaryContactName", StandardBasicTypes.STRING);
        query.addScalar("representativeMail", StandardBasicTypes.STRING);
        query.addScalar("representativeMobile", StandardBasicTypes.STRING);
        query.addScalar("approveStatusCode", StandardBasicTypes.STRING);
        query.addScalar("customerTypeCode", StandardBasicTypes.STRING);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(CustomerDTO.class));

        return (List<CustomerDTO>) query.getResultList();
    }

    @Override
    public Long countCustomer(FilterSearchCms FilterSearchCms, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        EnumRole userRole = EnumRole.valueOf(user.getAdminType());

        strQuery.append("SELECT count(distinct c.customer_id) ");
        strQuery.append(" FROM tb_customer c ");

        if (userRole == EnumRole.ROLE_SUPPLIER) {
            strQuery.append(" inner join tb_campaign cp on c.customer_id = cp.customer_id");
            strQuery.append("  inner join tb_campaign_goods_rel cgr on cp.campaign_id = cgr.campaign_id");
            strQuery.append("  inner join tb_goods g on cgr.goods_id = g.goods_id");
            strQuery.append("  inner join tb_brand b on g.brand_id = b.brand_id");
            strQuery.append("  inner join tb_supplier s on b.supplier_id = s.supplier_id");
        }

        strQuery.append(" WHERE 1 = 1 ");


        strQuery.append(" AND c.valid_yn = 'Y' ");

        setParam(FilterSearchCms, strQuery, mapParam);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        Object result = query.getSingleResult();
        return Long.parseLong(result.toString());
    }

    private void setParam(FilterSearchCms filterSearchCms, StringBuilder strQuery, Map<String, Object> mapParam) {
        setParamCommon(filterSearchCms, strQuery, mapParam);
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        EnumRole userRole = EnumRole.valueOf(user.getAdminType());
//        boolean hasRoleCustomer = EnumRole.ROLE_CUSTOMER.toString().equals(user.getAdminType());

        switch (userRole) {
            case ROLE_CUSTOMER:
                String customerId = user.getAdminCorpId();
                strQuery.append(" AND c.customer_id = :customerIdAdmin");
                mapParam.put("customerIdAdmin", customerId);
                break;
            case ROLE_SUPPLIER:
                String supplierId = user.getAdminCorpId();
                strQuery.append(" and s.supplier_id = :supplierId");
                mapParam.put("supplierId", supplierId);
                break;
        }
    }

    private void setParamCommon(FilterSearchCms filterSearchCms, StringBuilder strQuery, Map<String, Object> mapParam) {
        if (ObjectUtils.isEmpty(filterSearchCms)) return;

        if (StringUtils.isNotBlank(filterSearchCms.getCustomerId())) {
            strQuery.append(" AND c.customer_id = :customerId");
            mapParam.put("customerId", filterSearchCms.getCustomerId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchCms.getCustomerName())) {
            strQuery.append(" AND c.customer_nm LIKE :customerName");
            mapParam.put("customerName", '%' + filterSearchCms.getCustomerName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchCms.getTaxcode())) {
            strQuery.append(" AND c.taxcode LIKE :taxcode");
            mapParam.put("taxcode", '%' + filterSearchCms.getTaxcode().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchCms.getApproveStatusCode())) {
            if (Constant.EMPTY.equals(filterSearchCms.getApproveStatusCode())) {
                strQuery.append(" AND c.apprv_status_cd IS NULL ");
            } else {
                strQuery.append(" AND c.apprv_status_cd = :approveStatusCode ");
                mapParam.put("approveStatusCode", filterSearchCms.getApproveStatusCode());
            }
        }
        if (StringUtils.isNotBlank(filterSearchCms.getCustomerTypeCode())) {
            strQuery.append(" AND c.customer_type = :customerType");
            mapParam.put("customerType", filterSearchCms.getCustomerTypeCode().trim());
        }
    }
}
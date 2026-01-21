package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.service.models.*;
import com.evoucher.adminapi.cms.utils.CmsConstant;
import com.evoucher.adminapi.cms.utils.CmsDataUtil;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
@Transactional
public class AdminRepositoryImpl implements AdminRepositoryCustom {
    private static final String KEYWORD = "keyWord";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<SearchAdminResponse> searchAdmin(FilterSearchAuth filterSearchAuth, Pageable pageable) {
        StringBuilder sql = new StringBuilder();
        List<SearchAdminResponse> result = new ArrayList<>();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        sql.append("SELECT a.admin_id id, a.admin_nm adminName, a.email email, a.mobile_no mobileNumber, ");
        sql.append("       a.role_cd roleCode, a.admin_corp_id adminCorporationId, c.customer_nm customerName, ");
        sql.append("       s.supplier_nm supplierName, b.brand_nm brandName, st.store_nm storeName ");
        sql.append("FROM tb_admin a ");
        sql.append("LEFT JOIN tb_supplier s ON a.admin_corp_id = s.supplier_id ");
        sql.append("LEFT JOIN tb_brand b ON a.admin_corp_id = b.brand_id ");
        sql.append("LEFT JOIN tb_store st ON a.admin_corp_id = st.store_id ");
        sql.append("LEFT JOIN tb_customer c ON a.admin_corp_id = c.customer_id ");
        sql.append("WHERE a.valid_yn = 'Y' ");

        setParam(filterSearchAuth, sql, mapParam);

        NativeQuery query = session.createNativeQuery(sql.toString());
        mapParam.forEach(query::setParameter);

        // set Page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("id", StandardBasicTypes.STRING);
        query.addScalar("adminName", StandardBasicTypes.STRING);
        query.addScalar("email", StandardBasicTypes.STRING);
        query.addScalar("mobileNumber", StandardBasicTypes.STRING);
        query.addScalar("roleCode", StandardBasicTypes.STRING);
        query.addScalar("adminCorporationId", StandardBasicTypes.STRING);
        query.addScalar("supplierName", StandardBasicTypes.STRING);
        query.addScalar("brandName", StandardBasicTypes.STRING);
        query.addScalar("storeName", StandardBasicTypes.STRING);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.setResultTransformer(Transformers.aliasToBean(SearchAdminResponse.class));

        List<SearchAdminResponse> list = query.getResultList();

        if (!CollectionUtils.isEmpty(list)) {
            result = list.stream()
                    .peek(admin -> admin.setAdminCorporationName(admin))
                    .collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public Long countAdmin(FilterSearchAuth filterSearchAuth) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        sql.append("SELECT COUNT(a.admin_id) ");
        sql.append("FROM tb_admin a ");
        sql.append("LEFT JOIN tb_supplier s ON a.admin_corp_id = s.supplier_id ");
        sql.append("LEFT JOIN tb_brand b ON a.admin_corp_id = b.brand_id ");
        sql.append("LEFT JOIN tb_store st ON a.admin_corp_id = st.store_id ");
        sql.append("LEFT JOIN tb_customer c ON a.admin_corp_id = c.customer_id ");
        sql.append("WHERE a.valid_yn = 'Y' ");

        setParam(filterSearchAuth, sql, mapParam);

        NativeQuery query = session.createNativeQuery(sql.toString());
        mapParam.forEach(query::setParameter);

        Object count = query.getSingleResult();

        return Long.parseLong(count.toString());
    }

    private void setParam(FilterSearchAuth filterSearchAuth, StringBuilder sql, Map<String, Object> mapParam) {
        setParamCommon(filterSearchAuth, sql, mapParam);

        // Set authority role
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        EnumRole role = EnumRole.valueOf(user.getAdminType());

        switch (role) {
            case ROLE_ADMIN:
            case ROLE_OPERATOR: {
                break;
            }
            case ROLE_SUPPLIER: {
                String supplierIdPrefix = user.getAdminCorpId() + CmsConstant.UNDERSCORE_SYMBOL + '%';
                sql.append(" AND a.role_cd IN ('ROLE_BRAND', 'ROLE_STORE') AND a.admin_corp_id LIKE :supplierIdPrefix ");
                mapParam.put("supplierIdPrefix", supplierIdPrefix);
                break;
            }
            case ROLE_BRAND: {
                String brandIdPrefix = user.getAdminCorpId() + CmsConstant.UNDERSCORE_SYMBOL + '%';
                sql.append(" AND a.role_cd = 'ROLE_STORE' AND a.admin_corp_id LIKE :brandIdPrefix ");
                mapParam.put("brandIdPrefix", brandIdPrefix);
                break;
            }
            default: {
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
                        HttpStatus.UNAUTHORIZED);
            }
        }
    }

    private void setParamCommon(FilterSearchAuth filterSearchAuth, StringBuilder sql, Map<String, Object> mapParam) {
        if (Objects.isNull(filterSearchAuth)) return;

        if (StringUtils.isNotBlank(filterSearchAuth.getAdminId())) {
            sql.append(" AND a.admin_id = :id ");
            mapParam.put("id", filterSearchAuth.getAdminId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getAdminName())) {
            sql.append(" AND a.admin_nm LIKE :adminName ");
            mapParam.put("adminName", '%' + filterSearchAuth.getAdminName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getMobilePhone())) {
            sql.append(" AND a.mobile_no LIKE :mobilePhone ");
            mapParam.put("mobilePhone", '%' + filterSearchAuth.getMobilePhone().trim() + '%');
        }
        if (StringUtils.isNotEmpty(filterSearchAuth.getEmail())) {
            sql.append(" AND a.email LIKE :email ");
            mapParam.put("email", '%' + filterSearchAuth.getEmail().trim() + '%');
        }
        if (StringUtils.isNotEmpty(filterSearchAuth.getRoleCode())) {
            sql.append(" AND a.role_cd = :roleCode ");
            mapParam.put("roleCode", filterSearchAuth.getRoleCode().trim());
        }
        if (!CollectionUtils.isEmpty(filterSearchAuth.getRoleCodes())) {
            sql.append(" AND a.role_cd IN (:roleCodes) ");
            mapParam.put("roleCodes", filterSearchAuth.getRoleCodes());
        }
        if (StringUtils.isNotEmpty(filterSearchAuth.getCorporationName())) {
            sql.append(" AND (s.supplier_nm LIKE :corporationName OR b.brand_nm = :corporationName ");
            sql.append("      OR st.store_nm LIKE :corporationName OR c.customer_nm = :corporationName) ");
            mapParam.put("corporationName", '%' + filterSearchAuth.getCorporationName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAuth.getKeyWord())) {
            sql.append(" AND (a.admin_id LIKE :keyWord OR a.admin_nm LIKE :keyWord OR a.mobile_no LIKE :keyWord) ");
            mapParam.put(KEYWORD, '%' + filterSearchAuth.getKeyWord().trim() + '%');
        }
    }
}

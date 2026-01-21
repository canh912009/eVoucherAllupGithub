package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.SupplierDTO;
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

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class SupplierRepositoryImpl implements SupplierRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<SupplierDTO> searchSupplier(FilterSearchCms filterSearchCms, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        List<SupplierDTO> result = new ArrayList<>();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT s.supplier_id id, s.taxcode taxcode, s.supplier_nm supplierName, ");
        strQuery.append(" s.primary_contact_nm primaryContactName, s.primary_contact_email primaryContactEmail, ");
        strQuery.append(" s.primary_contact_mobile_no primaryContactMobile, s.apprv_status_cd approveStatusCode, ");
        strQuery.append(" s.valid_yn validYn, s.reg_id regId, s.reg_dt regDt, s.updt_dt updtDt, s.updt_id updtId ");
        strQuery.append(" FROM tb_supplier s WHERE s.valid_yn = 'Y' ");

        setParam(filterSearchCms, strQuery, mapParam);
        strQuery.append(" ORDER BY s.updt_dt DESC ");

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        // set Page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("id", StandardBasicTypes.STRING);
        query.addScalar("taxcode", StandardBasicTypes.STRING);
        query.addScalar("supplierName", StandardBasicTypes.STRING);
        query.addScalar("primaryContactName", StandardBasicTypes.STRING);
        query.addScalar("primaryContactEmail", StandardBasicTypes.STRING);
        query.addScalar("primaryContactMobile", StandardBasicTypes.STRING);
        query.addScalar("approveStatusCode", StandardBasicTypes.STRING);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(SupplierDTO.class));

        List<SupplierDTO> list = query.getResultList();

        if (list != null && list.size() > 0) {
            result = list;
        }
        return result;
    }

    @Override
    public long countSupplier(FilterSearchCms filterSearchCms) {
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT COUNT(s.supplier_id) ");
        strQuery.append(" FROM tb_supplier s WHERE s.valid_yn = 'Y' ");

        setParam(filterSearchCms, strQuery, mapParam);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        Object supplierNumber = query.getSingleResult();

        return Long.parseLong(supplierNumber.toString());
    }

    private void setParam(FilterSearchCms filterSearchCms, StringBuilder strQuery, Map<String, Object> mapParam) {
        setParamCommon(filterSearchCms, strQuery, mapParam);

        // Set authority role
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        boolean hasRoleSupplier = EnumRole.ROLE_SUPPLIER.toString().equals(user.getAdminType());

        if (hasRoleSupplier) { // ROLE_SUPPLIER
            String supplierId = user.getAdminCorpId();
            strQuery.append(" AND s.supplier_id = :supplierIdAdmin");
            mapParam.put("supplierIdAdmin", supplierId);
        }
    }

    private void setParamCommon(FilterSearchCms filterSearchCms, StringBuilder strQuery, Map<String, Object> mapParam) {
        if (ObjectUtils.isEmpty(filterSearchCms)) return;

        if (StringUtils.isNotBlank(filterSearchCms.getSupplierId())) {
            strQuery.append(" AND s.supplier_id = :supplierId");
            mapParam.put("supplierId", filterSearchCms.getSupplierId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchCms.getTaxcode())) {
            strQuery.append(" AND s.taxcode LIKE :taxcode");
            mapParam.put("taxcode", '%' + filterSearchCms.getTaxcode().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchCms.getSupplierName())) {
            strQuery.append(" AND s.supplier_nm LIKE :supplierName");
            mapParam.put("supplierName", '%' + filterSearchCms.getSupplierName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchCms.getApproveStatusCode())) {
            if (Constant.EMPTY.equals(filterSearchCms.getApproveStatusCode())) {
                strQuery.append(" AND s.apprv_status_cd IS NULL ");
            } else {
                strQuery.append(" AND s.apprv_status_cd = :approveStatusCode ");
                mapParam.put("approveStatusCode", filterSearchCms.getApproveStatusCode());
            }
        }
    }
}

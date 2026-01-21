package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.service.models.FilterSearchAdmin;
import com.evoucher.adminapi.admin.service.models.FilterSearchSupplierContract;
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
import org.hibernate.type.StandardBasicTypes;
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
public class SupplierContractRepositoryImpl implements SupplierContractRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<FilterSearchSupplierContract> searchContract(FilterSearchAdmin filterSearchAdmin, Pageable pageable) {
        List<FilterSearchSupplierContract> result = new ArrayList<>();
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT c.supplier_contract_id id, c.contract_nm contractName, c.st_dt startDate, c.ed_dt endDate, ");
        strQuery.append(" c.supplier_id supplierId, s.supplier_nm supplierName, c.apprv_status_cd approveStatusCode, ");
        strQuery.append(" c.valid_yn validYn, c.reg_id regId, c.reg_dt regDt, c.updt_dt updtDt, c.updt_id updtId ");
        strQuery.append(" FROM tb_supplier_contract c ");
        strQuery.append(" LEFT JOIN tb_supplier s ON c.supplier_id = s.supplier_id ");
        strQuery.append(" WHERE c.valid_yn = 'Y' ");

        setParam(filterSearchAdmin, strQuery, mapParam);
        strQuery.append(" ORDER BY c.updt_dt DESC ");

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        // set Page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("id", StandardBasicTypes.INTEGER);
        query.addScalar("contractName", StandardBasicTypes.STRING);
        query.addScalar("startDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("endDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("supplierId", StandardBasicTypes.STRING);
        query.addScalar("supplierName", StandardBasicTypes.STRING);
        query.addScalar("approveStatusCode", StandardBasicTypes.STRING);
        query.addScalar("startDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("endDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(FilterSearchSupplierContract.class));

        List<FilterSearchSupplierContract> supplierContractDTOS = query.getResultList();
        if (!supplierContractDTOS.isEmpty()) {
            result = supplierContractDTOS;
        }
        return result;
    }

    @Override
    public Long countContract(FilterSearchAdmin filterSearchAdmin, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT count(c.supplier_contract_id) ");
        strQuery.append(" FROM tb_supplier_contract c ");
        strQuery.append(" LEFT JOIN tb_supplier s ON c.supplier_id = s.supplier_id ");
        strQuery.append(" WHERE c.valid_yn = 'Y' ");

        setParam(filterSearchAdmin, strQuery, mapParam);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        Object result = query.getSingleResult();
        return Long.parseLong(result.toString());
    }

    private void setParam(FilterSearchAdmin filterSearchAdmin, StringBuilder strQuery, Map<String, Object> mapParam) {
        setParamCommon(filterSearchAdmin, strQuery, mapParam);

        // Set authority role
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        boolean hasRoleSupplier = EnumRole.ROLE_SUPPLIER.toString().equals(user.getAdminType());

        if (hasRoleSupplier) { // ROLE_SUPPLIER
            String supplierId = user.getAdminCorpId();
            strQuery.append(" AND c.supplier_id = :supplierIdAdmin");
            mapParam.put("supplierIdAdmin", supplierId);
        }
    }

    private void setParamCommon(FilterSearchAdmin filterSearchAdmin, StringBuilder strQuery, Map<String, Object> mapParam) {
        if (ObjectUtils.isEmpty(filterSearchAdmin)) return;

        if (StringUtils.isNotBlank(filterSearchAdmin.getContractId())) {
            strQuery.append(" AND c.supplier_contract_id LIKE :contractId");
            mapParam.put("contractId", filterSearchAdmin.getContractId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getContractName())) {
            strQuery.append(" AND c.contract_nm LIKE :contractName");
            mapParam.put("contractName", '%' + filterSearchAdmin.getContractName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getSupplierId())) {
            strQuery.append(" AND c.supplier_id = :supplierId");
            mapParam.put("supplierId", filterSearchAdmin.getSupplierId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getSupplierName())) {
            strQuery.append(" AND s.supplier_nm LIKE :supplierName");
            mapParam.put("supplierName", '%' + filterSearchAdmin.getSupplierName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getApproveStatusCode())) {
            if (Constant.EMPTY.equals(filterSearchAdmin.getApproveStatusCode())) {
                strQuery.append(" AND c.apprv_status_cd IS NULL ");
            } else {
                strQuery.append(" AND c.apprv_status_cd = :approveStatusCode ");
                mapParam.put("approveStatusCode", filterSearchAdmin.getApproveStatusCode());
            }
        }
    }
}

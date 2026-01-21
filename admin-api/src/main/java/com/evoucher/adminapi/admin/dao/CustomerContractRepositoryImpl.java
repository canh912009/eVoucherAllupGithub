package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.service.models.CustomerContractDTO;
import com.evoucher.adminapi.admin.service.models.FilterSearchAdmin;
import com.evoucher.adminapi.admin.service.models.FilterSearchCustomerContract;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.enums.EnumValidYn;
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
public class CustomerContractRepositoryImpl implements CustomerContractRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<FilterSearchCustomerContract> searchContract(FilterSearchAdmin filterSearchAdmin, Pageable pageable) {
        List<FilterSearchCustomerContract> result = new ArrayList<>();
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT c.customer_contract_id id, c.contract_nm contractName, ");
        strQuery.append(" c.customer_id customerId, c.apprv_status_cd approveStatusCode, ");
        strQuery.append(" c.st_dt startDate, c.ed_dt endDate, cus.customer_nm customerName, ");
        strQuery.append(" c.valid_yn validYn, c.reg_id regId, c.reg_dt regDt, c.updt_dt updtDt, c.updt_id updtId ");
        strQuery.append(" FROM tb_customer_contract c ");
        strQuery.append(" LEFT JOIN tb_customer cus ON c.customer_id = cus.customer_id ");
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
        query.addScalar("customerId", StandardBasicTypes.STRING);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.addScalar("approveStatusCode", StandardBasicTypes.STRING);
        query.addScalar("startDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("endDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(FilterSearchCustomerContract.class));

        List<FilterSearchCustomerContract> customerContractDTOS = query.getResultList();
        if (!customerContractDTOS.isEmpty()) {
            result = customerContractDTOS;
        }
        return result;
    }

    @Override
    public Long countContract(FilterSearchAdmin filterSearchAdmin, Pageable pageable) {

        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT count(c.customer_contract_id) ");
        strQuery.append(" FROM tb_customer_contract c ");
        strQuery.append(" LEFT JOIN tb_customer cus ON c.customer_id = cus.customer_id ");
        strQuery.append(" WHERE c.valid_yn = 'Y' ");

        strQuery.append(" AND c.valid_yn = :validYn AND cus.valid_yn = :validYn ");
        mapParam.put("validYn", EnumValidYn.Y.toString());

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
        boolean hasRoleCustomer = EnumRole.ROLE_CUSTOMER.toString().equals(user.getAdminType());

        if (hasRoleCustomer) { // ROLE_CUSTOMER
            String customerId = user.getAdminCorpId();
            strQuery.append(" AND c.customer_id = :customerIdAdmin");
            mapParam.put("customerIdAdmin", customerId);
        }
    }

    private void setParamCommon(FilterSearchAdmin filterSearchAdmin, StringBuilder strQuery, Map<String, Object> mapParam) {
        if (ObjectUtils.isEmpty(filterSearchAdmin)) return;

        if (StringUtils.isNotBlank(filterSearchAdmin.getContractId())) {
            strQuery.append(" AND c.customer_contract_id = :contractId");
            mapParam.put("contractId", filterSearchAdmin.getContractId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getContractName())) {
            strQuery.append(" AND c.contract_nm LIKE :contractName");
            mapParam.put("contractName", '%' + filterSearchAdmin.getContractName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getCustomerId())) {
            strQuery.append(" AND c.customer_id = :customerId");
            mapParam.put("customerId", filterSearchAdmin.getCustomerId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getCustomerName())) {
            strQuery.append(" AND cus.customer_nm LIKE :customerName");
            mapParam.put("customerName", '%' + filterSearchAdmin.getCustomerName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getApproveStatusCode())) {
            if (Constant.EMPTY.equals(filterSearchAdmin.getApproveStatusCode())) {
                strQuery.append(" AND c.apprv_status_cd IS NULL ");
            } else {
                strQuery.append(" AND c.apprv_status_cd = :approveStatusCode ");
                mapParam.put("approveStatusCode", filterSearchAdmin.getApproveStatusCode().trim());
            }
        }
    }
}

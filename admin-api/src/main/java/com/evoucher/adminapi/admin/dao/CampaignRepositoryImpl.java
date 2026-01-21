package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.enums.CampaignStatusCode;
import com.evoucher.adminapi.admin.service.models.FilterSearchAdmin;
import com.evoucher.adminapi.admin.service.models.SearchCampaignResponse;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.models.EndUser;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumRole;
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
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class CampaignRepositoryImpl implements CampaignRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<SearchCampaignResponse> searchCampaign(FilterSearchAdmin filterSearchAdmin, Pageable pageable) {

        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        EnumRole userRole = EnumRole.valueOf(user.getAdminType());

        List<SearchCampaignResponse> result = new ArrayList<>();

        // validate filterSearchAdmin
        boolean isValid = validateBeforeSearch(filterSearchAdmin);
        if (isValid) return result;

        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append(" SELECT c.campaign_id id, c.campaign_nm campaignName, c.customer_id customerId, c.st_dt startDate, ");
        strQuery.append(" c.ed_dt endDate, c.apprv_status_cd approveStatusCode, ");
        strQuery.append(" c.valid_yn validYn, c.reg_id regId, c.reg_dt regDt, c.updt_dt updtDt, c.updt_id updtId, ");
        strQuery.append(" cus.customer_nm customerName, cus.customer_type customerType ");
        strQuery.append(" FROM tb_campaign c LEFT JOIN tb_customer cus on c.customer_id = cus.customer_id ");

        if (userRole == EnumRole.ROLE_SUPPLIER) {
            strQuery.append(" inner  join tb_campaign_goods_rel cgr on c.campaign_id = cgr.campaign_id ");
            strQuery.append(" inner join tb_goods g on cgr.goods_id = g.goods_id");
            strQuery.append(" inner join tb_brand b on g.brand_id = b.brand_id");
            strQuery.append(" inner join tb_supplier s on b.supplier_id = s.supplier_id");
        }
        strQuery.append(" WHERE c.valid_yn = 'Y' ");

        setParam(filterSearchAdmin, strQuery, mapParam);

        if (userRole == EnumRole.ROLE_SUPPLIER) {
            strQuery.append(" group by c.campaign_id ");
        }
        strQuery.append(" ORDER BY c.campaign_id DESC ");

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        // set Page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("id", StandardBasicTypes.INTEGER);
        query.addScalar("campaignName", StandardBasicTypes.STRING);
        query.addScalar("customerId", StandardBasicTypes.STRING);
        query.addScalar("customerName", StandardBasicTypes.STRING);
        query.addScalar("startDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("endDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("approveStatusCode", StandardBasicTypes.STRING);
        query.addScalar("customerType", StandardBasicTypes.STRING);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(SearchCampaignResponse.class));

        List<SearchCampaignResponse> list = query.getResultList();

        if (list != null && list.size() > 0) {
            result = list.stream().peek(c -> c.setStatusCode(c)).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public long countCampaign(FilterSearchAdmin filterSearchAdmin) {

        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        EnumRole userRole = EnumRole.valueOf(user.getAdminType());

        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT COUNT(distinct c.campaign_id) ");
        strQuery.append(" FROM tb_campaign c LEFT JOIN tb_customer cus on c.customer_id = cus.customer_id ");

        if (userRole == EnumRole.ROLE_SUPPLIER) {
            strQuery.append(" inner  join tb_campaign_goods_rel cgr on c.campaign_id = cgr.campaign_id ");
            strQuery.append(" inner join tb_goods g on cgr.goods_id = g.goods_id");
            strQuery.append(" inner join tb_brand b on g.brand_id = b.brand_id");
            strQuery.append(" inner join tb_supplier s on b.supplier_id = s.supplier_id");
        }

        strQuery.append(" WHERE 1 = 1 AND c.valid_yn = 'Y' ");

        setParam(filterSearchAdmin, strQuery, mapParam);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        Object result = query.getSingleResult();

        return Long.parseLong(result.toString());
    }

    private boolean validateBeforeSearch(FilterSearchAdmin filterSearchAdmin) {
        // validate status code request belong CampaignApproveStatus
        if (StringUtils.isNotBlank(filterSearchAdmin.getStatusCode())) {
            try {
                CampaignStatusCode.valueOf(filterSearchAdmin.getStatusCode().trim());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return true;
            }
        }
        return false;
    }

    private void setParam(FilterSearchAdmin filterSearchAdmin, StringBuilder strQuery, Map<String, Object> mapParam) {
        setParamCommon(filterSearchAdmin, strQuery, mapParam);

        // Set authority role
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

//        if (hasRoleCustomer) { // ROLE_CUSTOMER
//            String customerId = user.getAdminCorpId();
//            strQuery.append(" AND c.customer_id = :customerIdAdmin");
//            mapParam.put("customerIdAdmin", customerId);
//        } else if ()
    }

    private void setParamCommon(FilterSearchAdmin filterSearchAdmin, StringBuilder strQuery, Map<String, Object> mapParam) {
        if (ObjectUtils.isEmpty(filterSearchAdmin)) return;

        if (StringUtils.isNotBlank(filterSearchAdmin.getCampaignId())) {
            strQuery.append(" AND c.campaign_id = :campaignId");
            mapParam.put("campaignId", filterSearchAdmin.getCampaignId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getCampaignName())) {
            strQuery.append(" AND c.campaign_nm LIKE :campaignName");
            mapParam.put("campaignName", '%' + filterSearchAdmin.getCampaignName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getCustomerName())) {
            strQuery.append(" AND cus.customer_nm LIKE :customerName");
            mapParam.put("customerName", '%' + filterSearchAdmin.getCustomerName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchAdmin.getStatusCode())) {

            CampaignStatusCode campaignStatusCode =
                    CampaignStatusCode.valueOf(filterSearchAdmin.getStatusCode().trim());
            switch (campaignStatusCode) {
                case WAIT_APPRV:
                    strQuery.append(" AND c.apprv_status_cd = :approveStatusCode ");
                    mapParam.put("approveStatusCode", ApproveStatus.REQ.name());
                    break;
                case REJECTED:
                    strQuery.append(" AND c.apprv_status_cd = :approveStatusCode ");
                    mapParam.put("approveStatusCode", ApproveStatus.REJCT.name());
                    break;
                case APPROVED:
                    strQuery.append(" AND c.apprv_status_cd = :approveStatusCode ");
                    mapParam.put("approveStatusCode", ApproveStatus.APPRV.name());
                    strQuery.append(" AND c.st_dt > :currentDate ");
                    mapParam.put("currentDate", new Date());
                    break;
                case CANCEL_APPRV:
                    strQuery.append(" AND c.apprv_status_cd = :approveStatusCode ");
                    mapParam.put("approveStatusCode", ApproveStatus.CANCEL_APPRV.name());
                    break;
                case PROCESSING:
                    strQuery.append(" AND c.apprv_status_cd = :approveStatusCode ");
                    mapParam.put("approveStatusCode", ApproveStatus.APPRV.name());
                    strQuery.append(" AND c.st_dt <= :currentDate AND c.ed_dt >= :currentDate ");
                    mapParam.put("currentDate", new Date());
                    break;
                case END:
                    strQuery.append(" AND c.apprv_status_cd = :approveStatusCode ");
                    mapParam.put("approveStatusCode", ApproveStatus.APPRV.name());
                    strQuery.append(" AND c.ed_dt < :currentDate ");
                    mapParam.put("currentDate", new Date());
                    break;
                case EMPTY:
                    strQuery.append(" AND c.apprv_status_cd IS NULL ");
                default:
                    break;
            }
        }
    }
}

package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.SearchStoreResponse;
import com.evoucher.adminapi.cms.service.models.StoreSynchronizeDTO;
import com.evoucher.adminapi.cms.utils.CmsDataUtil;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
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

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private static final String KEYWORD = "keyWord";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<SearchStoreResponse> searchStore(FilterSearchCms filterSearchCms, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        List<SearchStoreResponse> result = new ArrayList<>();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT s.store_id id, s.store_nm storeName, s.brand_id brandId, ");
        strQuery.append(" s.region region, sup.supplier_id supplierId, sup.supplier_nm supplierName, b.brand_nm brandName, ");
        strQuery.append(" s.valid_yn validYn, s.reg_id regId, s.reg_dt regDt, s.updt_dt updtDt, s.updt_id updtId ");
        strQuery.append(" FROM tb_store s ");
        strQuery.append(" LEFT JOIN tb_brand b ON b.brand_id = s.brand_id ");
        strQuery.append(" LEFT JOIN tb_supplier sup ON sup.supplier_id = b.supplier_id ");
        strQuery.append(" WHERE 1 = 1 ");

        setParam(filterSearchCms, strQuery, mapParam);
        strQuery.append(" ORDER BY s.updt_dt DESC ");

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        // set Page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("id",  StandardBasicTypes.STRING);
        query.addScalar("storeName",  StandardBasicTypes.STRING);
        query.addScalar("brandId",  StandardBasicTypes.STRING);
        query.addScalar("brandName", StandardBasicTypes.STRING);
        query.addScalar("region",  StandardBasicTypes.STRING);
        query.addScalar("supplierName",  StandardBasicTypes.STRING);
        query.addScalar("supplierId",  StandardBasicTypes.STRING);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(SearchStoreResponse.class));

        List<SearchStoreResponse> list = query.getResultList();

        if (list != null && list.size() > 0) {
            result = list;
        }
        return result;
    }

    @Override
    public long countStore(FilterSearchCms filterSearchCms) {
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT COUNT(s.store_id) ");
        strQuery.append(" FROM tb_store s ");
        strQuery.append(" LEFT JOIN tb_brand b ON b.brand_id = s.brand_id ");
        strQuery.append(" LEFT JOIN tb_supplier sup ON sup.supplier_id = b.supplier_id ");
        strQuery.append(" WHERE s.valid_yn = 'Y'");

        setParam(filterSearchCms, strQuery, mapParam);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        Object storeNumber = query.getSingleResult();

        return Long.parseLong(storeNumber.toString());
    }

    @Override
    public List<StoreSynchronizeDTO> findListStoreDTOByListStoreId(List<String> storeIds) {
        StringBuilder strQuery = new StringBuilder();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT s.store_id id, s.store_nm storeName, s.store_img_path storeImagePath, ");
        strQuery.append(" s.store_img_nm storeImageName, s.brand_id brandId, s.full_address fullAddress, ");
        strQuery.append(" s.map_interation_type mapInteractionType, s.map_cd mapCode, s.tel telephoneNumber, ");
        strQuery.append(" s.valid_yn validYn, s.reg_id regId, s.reg_dt regDt, s.updt_dt updtDt, s.updt_id updtId, ");
        strQuery.append(" s.region, s.store_type storeType, sup.supplier_id supplierId ");
        strQuery.append(" FROM tb_store s ");
        strQuery.append(" LEFT JOIN tb_brand b ON b.brand_id = s.brand_id ");
        strQuery.append(" LEFT JOIN tb_supplier sup ON sup.supplier_id = b.supplier_id ");
        strQuery.append(" WHERE s.store_id IN :storeIds ");

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        query.setParameter("storeIds", storeIds);

        query.addScalar("id",  StandardBasicTypes.STRING);
        query.addScalar("storeName",  StandardBasicTypes.STRING);
        query.addScalar("storeImagePath",  StandardBasicTypes.STRING);
        query.addScalar("storeImageName",  StandardBasicTypes.STRING);
        query.addScalar("brandId",  StandardBasicTypes.STRING);
        query.addScalar("supplierId",  StandardBasicTypes.STRING);
        query.addScalar("mapCode",  StandardBasicTypes.STRING);
        query.addScalar("mapInteractionType",  StandardBasicTypes.STRING);
        query.addScalar("region", StandardBasicTypes.STRING);
        query.addScalar("storeType",  StandardBasicTypes.STRING);
        query.addScalar("fullAddress",  StandardBasicTypes.STRING);
        query.addScalar("telephoneNumber",  StandardBasicTypes.STRING);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(StoreSynchronizeDTO.class));

        return query.getResultList();
    }

    private void setParam(FilterSearchCms filterSearchCms, StringBuilder strQuery, Map<String, Object> mapParam) {
        setParamCommon(filterSearchCms, strQuery, mapParam);

        // Set authority role
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        boolean hasRoleSupplier = EnumRole.ROLE_SUPPLIER.toString().equals(user.getAdminType());
        boolean hasRoleBrand = EnumRole.ROLE_BRAND.toString().equals(user.getAdminType());

        if (hasRoleSupplier) {
            String supplierId = CmsDataUtil.getSupplierIdByAdminCorpId(user.getAdminCorpId());
            strQuery.append(" AND sup.supplier_id = :supplierIdAdmin ");
            mapParam.put("supplierIdAdmin", supplierId);
        } else if (hasRoleBrand) {
            String brandId = CmsDataUtil.getBrandIdByAdminCorpId(user.getAdminCorpId());
            strQuery.append(" AND b.brand_id = :brandIdAdmin ");
            mapParam.put("brandIdAdmin", brandId);
        }

    }

    private void setParamCommon(FilterSearchCms filterSearchCms, StringBuilder strQuery, Map<String, Object> mapParam) {
        if (ObjectUtils.isEmpty(filterSearchCms)) return;

        if (StringUtils.isNotBlank(filterSearchCms.getStoreId())) {
            strQuery.append(" AND s.store_id = :storeId ");
            mapParam.put("storeId", filterSearchCms.getStoreId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchCms.getStoreName())) {
            strQuery.append(" AND s.store_nm LIKE :storeName ");
            mapParam.put("storeName", '%' + filterSearchCms.getStoreName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchCms.getSupplierName())) {
            strQuery.append(" AND sup.supplier_nm LIKE :supplierName ");
            mapParam.put("supplierName", '%' + filterSearchCms.getSupplierName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchCms.getSupplierId())) {
            strQuery.append(" AND sup.supplier_id = :supplierId");
            mapParam.put("supplierId", filterSearchCms.getSupplierId().strip());
        }
        BrandRepositoryImpl.setBrandIdNameConditions(filterSearchCms, strQuery, mapParam);
        if (StringUtils.isNotBlank(filterSearchCms.getRegion())) {
            strQuery.append(" AND s.region LIKE :region");
            mapParam.put("region", '%' + filterSearchCms.getRegion().trim() + '%');
        }
        if (Objects.nonNull(filterSearchCms.getValidYn())) {
            strQuery.append(" AND s.valid_yn = :validYn");
            mapParam.put("validYn", filterSearchCms.getValidYn().name());
        }
    }
}

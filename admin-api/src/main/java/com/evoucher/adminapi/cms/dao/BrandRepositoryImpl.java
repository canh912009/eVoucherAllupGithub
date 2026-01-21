package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.SearchBrandResponse;
import com.evoucher.adminapi.cms.service.models.vnptEPay.response.BrandSearchDTO;
import com.evoucher.adminapi.cms.utils.CmsDataUtil;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.enums.SystemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static com.evoucher.adminapi.common.utils.DataUtils.setPageInfoToQuery;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class BrandRepositoryImpl implements BrandRepositoryCustom {

    private static final String KEYWORD = "keyWord";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<SearchBrandResponse> searchBrand(FilterSearchCms filterSearchCms, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        List<SearchBrandResponse> result = new ArrayList<>();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT b.brand_id id, b.brand_nm brandName, b.supplier_id supplierId, b.default_brand_yn defaultBrandYn, ");
        strQuery.append(" b.valid_yn validYn, b.reg_id regId, b.reg_dt regDt, b.updt_dt updtDt, b.updt_id updtId, ");
        strQuery.append(" s.supplier_nm supplierName, b.`system` `system`, b.brand_cd brandCode,  b.display_type displayType ");
        strQuery.append(" FROM tb_brand b ");
        strQuery.append(" LEFT JOIN tb_supplier s ON b.supplier_id = s.supplier_id ");
        strQuery.append(" WHERE 1 = 1 ");

        setParam(filterSearchCms, strQuery, mapParam);
        strQuery.append(" ORDER BY b.updt_dt DESC, b.brand_id ");

        log.info(strQuery.toString());

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        // set Page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("id", StandardBasicTypes.STRING);
        query.addScalar("brandName", StandardBasicTypes.STRING);
        query.addScalar("supplierId", StandardBasicTypes.STRING);
        query.addScalar("supplierName", StandardBasicTypes.STRING);
        query.addScalar("defaultBrandYn", StandardBasicTypes.STRING);
        query.addScalar("system", StandardBasicTypes.STRING);
        query.addScalar("brandCode", StandardBasicTypes.STRING);
        query.addScalar("displayType", StandardBasicTypes.STRING);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.setResultTransformer(Transformers.aliasToBean(SearchBrandResponse.class));

        List<SearchBrandResponse> list = query.getResultList();

        if (list != null && list.size() > 0) {
            result = list;
        }
        return result;
    }

    @Override
    public long countBrand(FilterSearchCms filterSearchCms) {
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT COUNT(b.brand_id) ");
        strQuery.append(" FROM tb_brand b ");
        strQuery.append(" LEFT JOIN tb_supplier s ON b.supplier_id = s.supplier_id ");
        strQuery.append(" WHERE 1=1 ");

        setParam(filterSearchCms, strQuery, mapParam);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        Object brandNumber = query.getSingleResult();

        return Long.parseLong(brandNumber.toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<BrandSearchDTO> searchBrandByBrandIdAndBrandTitle(String supplierId, String brandID, String brandTitle, Pageable pageable) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("select tb.brand_id as id,\n");
        queryString.append("       tb.brand_nm as brandTitle,\n");
        queryString.append("       count(tg.goods_id) as numberOfGifts\n");
        queryString.append("from tb_brand tb\n");
        queryString.append("    inner join tb_supplier ts on tb.supplier_id = ts.supplier_id\n");
        queryString.append("    left join tb_goods tg on tb.brand_id = tg.brand_id\n");
        queryString.append("where tb.supplier_id = :supplierId\n");

        appendWhereClauseForSearchBrandQuery(queryString, brandID, brandTitle);

        queryString.append("group by tb.brand_id");

        NativeQueryImpl query = (NativeQueryImpl) em.createNativeQuery(queryString.toString(), "vnpt_search_brand_mapping");

        setParamForWhereClauseOfSearchBrandQuery(query, supplierId, brandID, brandTitle);
        setPageInfoToQuery(query, pageable);
        return query.getResultList();
    }

    @Override
    public long countBrandByBrandIdAndBrandTitle(String supplierId, String brandId, String brandTitle) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("select count(tb.brand_id)\n");
                queryString.append("    from tb_brand tb\n" );
                queryString.append("    inner join tb_supplier ts on tb.supplier_id = ts.supplier_id\n" );
                queryString.append("where tb.supplier_id = :supplierId");

        appendWhereClauseForSearchBrandQuery(queryString, brandId, brandTitle);

        NativeQueryImpl query = (NativeQueryImpl) em.createNativeQuery(queryString.toString());

        setParamForWhereClauseOfSearchBrandQuery(query, supplierId, brandId, brandTitle);
        return Long.parseLong(query.getSingleResult().toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> searchBrandByCategoryAndFilter(FilterSearchCms filter, Pageable pageable) {
        StringBuilder queryString = new StringBuilder("select tb.*, coalesce(concat('[', group_concat(distinct concat('\"', concat(c.ctgr_cd), '\"') separator ','), ']'), '[]') as categoryCode, ts.supplier_nm as supplierName ");
        queryString.append("FROM tb_brand tb");
        queryString.append(" inner join tb_goods g on tb.brand_id = g.brand_id");
        queryString.append(" left join tb_category_goods_rel cgr on g.goods_id = cgr.goods_id");
        queryString.append(" left join tb_category c on cgr.ctgr_cd = c.ctgr_cd");
        queryString.append(" left join tb_supplier ts on tb.supplier_id = ts.supplier_id");

        getConditionForSearchBrandByCategoryAndFilter(filter, queryString);
        queryString.append(" group by tb.brand_id");
        log.info(queryString.toString());
        NativeQueryImpl query = (NativeQueryImpl) em
                .createNativeQuery(queryString.toString(), "BrandCategoryMapping");

        setParamForSearchBrandByCategoryAndFilter(filter, query);
        setPageInfoToQuery(query, pageable);

        return query.getResultList();
    }

    @Override
    public Long countBrandByCategoryAndFilter(FilterSearchCms filter, Pageable pageable) {
        StringBuilder queryString = new StringBuilder("SELECT count(distinct tb.brand_id) ");
        queryString.append("FROM tb_brand tb");
        queryString.append(" inner join tb_goods g on tb.brand_id = g.brand_id");
        queryString.append(" left join tb_category_goods_rel cgr on g.goods_id = cgr.goods_id");
        queryString.append(" left join tb_category c on cgr.ctgr_cd = c.ctgr_cd");

        getConditionForSearchBrandByCategoryAndFilter(filter, queryString);

        log.info(queryString.toString());
        Query query = em
                .createNativeQuery(queryString.toString());

        setParamForSearchBrandByCategoryAndFilter(filter, query);

        return Optional.ofNullable((BigInteger)query.getSingleResult()).orElse(BigInteger.valueOf(0L)).longValue();

    }

    private void getConditionForSearchBrandByCategoryAndFilter(FilterSearchCms filter, StringBuilder queryString) {
        queryString.append(" where 0=0 ");
        if (StringUtils.isNotBlank(filter.getBrandId())) {
            queryString.append(" AND tb.brand_id like :brandId ");
        }
        if (StringUtils.isNotBlank(filter.getBrandName())) {
            queryString.append(" AND tb.brand_nm LIKE :brandName ");
        }
        if (StringUtils.isNotBlank(filter.getCategoryCode())) {
            queryString.append(" AND c.ctgr_cd = :categoryCode");
        }
        if (filter.getValidYn() != null) {
            queryString.append(" AND tb.valid_yn = :validYN");
        }
        if (!CollectionUtils.isEmpty(filter.getSystems())) {
            queryString.append(" and tb.`system` in (:systems)");
        }

    }

    private void setParamForSearchBrandByCategoryAndFilter(FilterSearchCms filter, Query query) {
        String brandId = StringUtils.isNotBlank(filter.getBrandId()) ? "%".concat(filter.getBrandId()).concat(filter.getBrandId()).concat("%") : "";
        setBrandIdTitleParam(query, brandId, filter.getBrandName());
        if (StringUtils.isNotBlank(filter.getCategoryCode())) {
            query.setParameter("categoryCode", filter.getCategoryCode());
        }
        if (filter.getValidYn() != null) {
            query.setParameter("validYN", filter.getValidYn().name());
        }
        if (!CollectionUtils.isEmpty(filter.getSystems())) {
            query.setParameter("systems", filter.getSystems().stream().map(SystemType::name).collect(Collectors.toList()));
        }
    }
    private void appendWhereClauseForSearchBrandQuery(StringBuilder queryString, String brandId, String brandTitle) {
        queryString.append(" and ( 0=0 ");

        if(brandId != null && !brandId.isBlank()) {
            queryString.append(" and tb.brand_id = :brandId");
        }
        if (brandTitle != null && !brandTitle.isBlank()) {
            queryString.append(" and tb.brand_nm like :brandTitle");
        }
        queryString.append(" )");
    }

    private void setParamForWhereClauseOfSearchBrandQuery(Query query, String supplierId, String brandId, String brandTitle) {
        setBrandIdTitleParam(query, brandId, brandTitle);
        query.setParameter("supplierId", supplierId);
    }

    private void setBrandIdTitleParam(Query query, String brandId, String brandTitle) {
        if(brandId != null && !brandId.isBlank()) {
            query.setParameter("brandId", brandId);
        }
        if (brandTitle != null && !brandTitle.isBlank()) {
            query.setParameter("brandName", "%" + brandTitle + "%");
        }
    }

    private void setParam(FilterSearchCms filterSearchCms, StringBuilder strQuery, Map<String, Object> mapParam) {
        if (ObjectUtils.isEmpty(filterSearchCms)) return;
        setParamCommon(filterSearchCms, strQuery, mapParam);

        // Set authority role
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        boolean hasRoleSupplier = EnumRole.ROLE_SUPPLIER.toString().equals(user.getAdminType());

        if (hasRoleSupplier) { // ROLE_SUPPLIER
            String supplierId = CmsDataUtil.getSupplierIdByAdminCorpId(user.getAdminCorpId());
            strQuery.append(" AND b.supplier_id = :supplierId");
            mapParam.put("supplierId", supplierId);
        }
    }

    private void setParamCommon(FilterSearchCms filterSearchCms, StringBuilder strQuery, Map<String, Object> mapParam) {
        setBrandCondition(filterSearchCms, strQuery, mapParam);
        if (StringUtils.isNotBlank(filterSearchCms.getSupplierId())) {
            strQuery.append(" AND s.supplier_id = :supplierId ");
            mapParam.put("supplierId", filterSearchCms.getSupplierId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchCms.getSupplierName())) {
            strQuery.append(" AND s.supplier_nm LIKE :supplierName ");
            mapParam.put("supplierName", '%' + filterSearchCms.getSupplierName().trim() + '%');
        }
    }

    public void setBrandCondition(FilterSearchCms filterSearchCms, StringBuilder strQuery, Map<String, Object> mapParam) {
        setBrandIdNameConditions(filterSearchCms, strQuery, mapParam);
        if (Objects.nonNull(filterSearchCms.getValidYn())) {
            strQuery.append(" AND b.valid_yn = :validYn ");
            mapParam.put("validYn", filterSearchCms.getValidYn().name());
        }
    }

    static void setBrandIdNameConditions(FilterSearchCms filterSearchCms, StringBuilder strQuery, Map<String, Object> mapParam) {
        if (StringUtils.isNotBlank(filterSearchCms.getBrandId())) {
            strQuery.append(" AND b.brand_id = :brandId ");
            mapParam.put("brandId", filterSearchCms.getBrandId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchCms.getBrandName())) {
            strQuery.append(" AND b.brand_nm LIKE :brandName ");
            mapParam.put("brandName", '%' + filterSearchCms.getBrandName().trim() + '%');
        }
    }
}

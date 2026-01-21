package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.SearchGroupResponse;
import com.evoucher.adminapi.cms.service.models.vnptEPay.response.GiftSearchWithDateDTO;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.enums.EnumValidYn;
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
public class GoodsRepositoryImpl implements GoodsRepositoryCustom {

    private static final String KEYWORD = "keyWord";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<SearchGroupResponse> searchGoods(
            FilterSearchCms filterSearchCms,
            Pageable pageable,
            boolean ignorePermissions) {
        StringBuilder strQuery = new StringBuilder();
        List<SearchGroupResponse> result = new ArrayList<>();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT g.goods_id as id, g.brand_id as brandId, g.end_dt as endDate, ");
        strQuery.append("       g.except_store_ids as exceptStoreIds, g.goods_desc as goodsDescription, ");
        strQuery.append("       g.goods_img_nm as goodsImgName, g.goods_img_path as goodsImgPath, g.goods_nm as goodsName, ");
        strQuery.append("       g.goods_status_cd as goodsStatusCode, g.goods_type as goodsType, g.list_price as listPrice, ");
        strQuery.append("       g.period_expire_date as periodExpireDate, g.period_term as periodTerm, g.period_type as periodType, ");
        strQuery.append("       g.sell_price as sellPrice, g.settlement_method_cd as settlementMethodCode, g.strt_dt as startDate, ");
        strQuery.append("       g.supplier_contract_id as supplierContractId, g.supplier_goods_id as supplierGoodsId, g.supplier_id as supplierId, ");
        strQuery.append("       g.supply_commission_rate as supplyCommissionRate, g.supply_dc_amount as supplyDiscountAmount, ");
        strQuery.append("       g.supply_dc_rate as supplyDiscountRate, g.use_info as useInfo, g.vat_inc_yn as vatIncludeYn, ");
        strQuery.append("       sup.supplier_nm supplierName, bra.brand_nm brandName, ");
        strQuery.append("       g.valid_yn validYn, g.reg_id regId, g.reg_dt regDt, g.updt_dt updtDt, g.updt_id updtId, ");
        strQuery.append("       g.`system` `system` ");
        strQuery.append(" FROM tb_goods g ");
        strQuery.append(" LEFT JOIN tb_supplier sup ON g.supplier_id = sup.supplier_id ");
        strQuery.append(" LEFT JOIN tb_brand bra ON g.brand_id = bra.brand_id ");
        strQuery.append(" WHERE 1 = 1 ");

        setParam(filterSearchCms, strQuery, mapParam, ignorePermissions);
        strQuery.append(" ORDER BY g.updt_dt DESC ");

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        // set Page
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        query.addScalar("id", StandardBasicTypes.INTEGER);
        query.addScalar("goodsName", StandardBasicTypes.STRING);
        query.addScalar("supplierId", StandardBasicTypes.STRING);
        query.addScalar("supplierName", StandardBasicTypes.STRING);
        query.addScalar("brandId", StandardBasicTypes.STRING);
        query.addScalar("brandName", StandardBasicTypes.STRING);
        query.addScalar("goodsStatusCode", StandardBasicTypes.STRING);
        query.addScalar("supplierGoodsId", StandardBasicTypes.STRING);
        query.addScalar("supplierContractId", StandardBasicTypes.INTEGER);
        query.addScalar("listPrice", StandardBasicTypes.DOUBLE);
        query.addScalar("sellPrice", StandardBasicTypes.DOUBLE);
        query.addScalar("supplyDiscountRate", StandardBasicTypes.DOUBLE);
        query.addScalar("supplyDiscountAmount", StandardBasicTypes.DOUBLE);
        query.addScalar("supplyCommissionRate", StandardBasicTypes.DOUBLE);
        query.addScalar("vatIncludeYn", StandardBasicTypes.STRING);
        query.addScalar("settlementMethodCode", StandardBasicTypes.STRING);
        query.addScalar("goodsDescription", StandardBasicTypes.STRING);
        query.addScalar("useInfo", StandardBasicTypes.STRING);
        query.addScalar("goodsImgPath", StandardBasicTypes.STRING);
        query.addScalar("goodsImgName", StandardBasicTypes.STRING);
        query.addScalar("startDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("endDate", StandardBasicTypes.TIMESTAMP);
        query.addScalar("exceptStoreIds", StandardBasicTypes.STRING);
        query.addScalar("goodsType", StandardBasicTypes.STRING);
        query.addScalar("periodType", StandardBasicTypes.STRING);
        query.addScalar("periodTerm", StandardBasicTypes.DOUBLE);
        query.addScalar("periodExpireDate", StandardBasicTypes.STRING);
        query.addScalar("validYn", StandardBasicTypes.STRING);
        query.addScalar("regId", StandardBasicTypes.STRING);
        query.addScalar("regDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("updtId", StandardBasicTypes.STRING);
        query.addScalar("updtDt", StandardBasicTypes.TIMESTAMP);
        query.addScalar("system", StandardBasicTypes.STRING);
        query.setResultTransformer(Transformers.aliasToBean(SearchGroupResponse.class));

        List<SearchGroupResponse> listQuery = query.getResultList();
        if (!listQuery.isEmpty()) {
            result = listQuery;
        }
        return result;
    }

    @Override
    public Long countGoods(FilterSearchCms FilterSearchCms,
                           Pageable pageable,
                           boolean ignorePermissions) {
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> mapParam = new HashMap<>();
        Session session = em.unwrap(Session.class);

        strQuery.append("SELECT count(g.goods_id) ");
        strQuery.append(" FROM tb_goods g ");
        strQuery.append(" LEFT JOIN tb_supplier sup ON g.supplier_id = sup.supplier_id ");
        strQuery.append(" LEFT JOIN tb_brand bra ON g.brand_id = bra.brand_id ");
        strQuery.append(" WHERE 1 = 1 ");

        setParam(FilterSearchCms, strQuery, mapParam, ignorePermissions);

        NativeQuery query = session.createNativeQuery(strQuery.toString());
        mapParam.forEach(query::setParameter);

        Object result = query.getSingleResult();
        return Long.parseLong(result.toString());
    }

    @Override
    public List<GiftSearchWithDateDTO> searchVnptGift(String supplierId, String brandId, String brandTitle, String giftTitle, Pageable pageable) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("select tg.goods_id as id,\n");
        queryString.append("       tg.goods_nm as giftTitle,\n");
        queryString.append("       tb.brand_nm as brandName,\n");
        queryString.append("       tg.sell_price as price,\n");
        queryString.append("       count(tep.ext_pin_no) as quantity,\n");
        queryString.append("       tg.reg_dt as createDate");
        queryString.append("    from tb_goods tg\n");
        queryString.append("    inner join tb_brand tb on tg.brand_id = tb.brand_id\n");
        queryString.append("    left join tb_ext_pin tep on tg.goods_id = tep.goods_id\n");
        createWhereClauseForSearchVnptGift(queryString, supplierId, brandId, brandTitle, giftTitle);
        queryString.append("    group by tg.goods_id");

        NativeQueryImpl query = (NativeQueryImpl) em.createNativeQuery(queryString.toString(), "vnpt_search_gift_mapping");
        setParamForSearchVnptGift(query, supplierId, brandId, brandTitle, giftTitle);

        setPageInfoToQuery(query, pageable);
        return query.getResultList();
    }
    @Override
    public Long countVnptGift(String supplierId, String brandId, String brandTitle, String giftTitle) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("select count(tg.goods_id)");
        queryString.append("    from tb_goods tg\n");
        queryString.append("    inner join tb_brand tb on tg.brand_id = tb.brand_id\n");
        createWhereClauseForSearchVnptGift(queryString, supplierId, brandId, brandTitle, giftTitle);

        Query query = em.createNativeQuery(queryString.toString());
        setParamForSearchVnptGift(query, supplierId, brandId, brandTitle, giftTitle);
        return Long.valueOf(query.getSingleResult().toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> searchGoodByCatBrandAndFilter(FilterSearchCms filter, Pageable pageable) {
        StringBuilder queryString = new StringBuilder("select g.*, ");
        queryString.append(" coalesce(concat('[', group_concat(distinct cgr.ctgr_cd separator ','), ']'), '[]') as categoryCodes, ");
        queryString.append(" coalesce(concat('[', group_concat(distinct tb.brand_id separator ','), ']'), '[]') as brandIds");
        getFromStmForSearchBulkGood(queryString);


        getConditionForSearchByCategoryAndFilter(filter, queryString);
        queryString.append(" group by g.goods_id");
        log.info(queryString.toString());
        NativeQueryImpl query = (NativeQueryImpl) em
                .createNativeQuery(queryString.toString(), "GoodCategoryMapping");

        setParamForSearchByCategoryAndFilter(filter, query);
        setPageInfoToQuery(query, pageable);
        return query.getResultList();
    }

    private void getFromStmForSearchBulkGood(StringBuilder queryString) {
        queryString.append(" from tb_goods g ");
        queryString.append(" inner join tb_brand tb on g.brand_id = tb.brand_id ");
        queryString.append(" inner join tb_category_goods_rel cgr on g.goods_id = cgr.goods_id");
    }

    public void getConditionForSearchByCategoryAndFilter(FilterSearchCms filter, StringBuilder queryString) {
        queryString.append(" where 0=0 ");
        if (StringUtils.isNotBlank(filter.getBrandId())) {
            queryString.append(" AND tb.brand_id = :brandId ");
        }
        if (StringUtils.isNotBlank(filter.getCategoryCode())) {
            queryString.append(" AND cgr.ctgr_cd = :categoryCode");
        }
        if (StringUtils.isNotBlank(filter.getGoodsId())) {
            queryString.append(" AND g.goods_id like :goodsId");
        }
        if (filter.getValidYn() != null) {
            queryString.append(" AND g.valid_yn = :validYN");
        }
        if (StringUtils.isNotBlank(filter.getGoodsName())) {
            queryString.append(" AND g.goods_nm like :goodName");
        }
    }

    public void setParamForSearchByCategoryAndFilter(FilterSearchCms filter, Query query) {
        if (StringUtils.isNotBlank(filter.getCategoryCode())) {
            query.setParameter("categoryCode", filter.getCategoryCode());
        }
        if (filter.getValidYn() != null) {
            query.setParameter("validYN", filter.getValidYn().name());
        }
        if (StringUtils.isNotBlank(filter.getBrandId())) {
            query.setParameter("brandId", filter.getBrandId());
        }
        if (StringUtils.isNotBlank(filter.getGoodsId())) {
            query.setParameter("goodsId", "%".concat(filter.getGoodsId()).concat("%"));
        }
        if (StringUtils.isNotBlank(filter.getGoodsName())) {
            query.setParameter("goodName", "%".concat(filter.getGoodsName()).concat("%"));
        }
    }

    @Override
    public Long countGoodByCatBrandAndFilter(FilterSearchCms filer, Pageable pageable) {
        StringBuilder queryString = new StringBuilder("select count(distinct g.goods_id) ");
        getFromStmForSearchBulkGood(queryString);

        getConditionForSearchByCategoryAndFilter(filer, queryString);
        log.info(queryString.toString());
        Query query = em
                .createNativeQuery(queryString.toString());

        setParamForSearchByCategoryAndFilter(filer, query);
        return ((BigInteger)query.getSingleResult()).longValue();
    }

    private void createWhereClauseForSearchVnptGift(StringBuilder query, String supplierId, String brandId, String brandTitle, String giftTitle) {
        query.append(" where tb.supplier_id = :supplierId");
        if (brandTitle != null && !brandTitle.isBlank()) {
            query.append(" and tb.brand_nm like :brandTitle");
        }
        if (giftTitle != null && !giftTitle.isBlank()) {
            query.append(" and tg.goods_nm like :giftTitle");
        }
        if (brandId != null && !brandId.isBlank()) {
            query.append(" and tg.brand_id = :brandId");
        }
    }

    private void setParamForSearchVnptGift(Query query, String supplierId, String brandId, String brandTitle, String giftTitle) {
        query.setParameter("supplierId", supplierId);
        if (brandTitle != null && !brandTitle.isBlank()) {
            query.setParameter("brandTitle", "%" + brandTitle + "%");
        }
        if (giftTitle != null && !giftTitle.isBlank()) {
            query.setParameter("giftTitle", "%" + giftTitle + "%");
        }
        if (brandId != null && !brandId.isBlank()) {
            query.setParameter("brandId", brandId);
        }
    }

    private void setParam(FilterSearchCms filterSearchCms,
                          StringBuilder strQuery,
                          Map<String, Object> mapParam,
                          boolean ignorePermissions) {
        setParamCommon(filterSearchCms, strQuery, mapParam);

        if (ignorePermissions) {
            return;
        }

        // Set authority role
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        String adminCorpId = user.getAdminCorpId();
        EnumRole role = Enum.valueOf(EnumRole.class, user.getAdminType());
        log.info("Validate permission with user info: {}", user);
        switch (role) {
            case ROLE_ADMIN:
            case ROLE_OPERATOR:
                break;
            case ROLE_SUPPLIER:
                strQuery.append(" AND g.supplier_id = :adminCorpId");
                mapParam.put("adminCorpId", adminCorpId);
                break;
            case ROLE_BRAND: {
                strQuery.append(" AND g.brand_id = :adminCorpId");
                mapParam.put("adminCorpId", adminCorpId);
                break;
            }
        }
    }

    private static void setParamCommon(FilterSearchCms filterSearchCms, StringBuilder strQuery, Map<String, Object> mapParam) {
        if (ObjectUtils.isEmpty(filterSearchCms)) return;

        if (StringUtils.isNotBlank(filterSearchCms.getGoodsId())) {
            strQuery.append(" AND g.goods_id = :goodsId");
            mapParam.put("goodsId", filterSearchCms.getGoodsId().trim());
        }
        if (StringUtils.isNotBlank(filterSearchCms.getGoodsName())) {
            strQuery.append(" AND g.goods_nm LIKE :goodsName");
            mapParam.put("goodsName", '%' + filterSearchCms.getGoodsName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchCms.getSupplierName())) {
            strQuery.append(" AND sup.supplier_nm LIKE :supplierName");
            mapParam.put("supplierName", '%' + filterSearchCms.getSupplierName().trim() + '%');
        }
        if (StringUtils.isNotBlank(filterSearchCms.getBrandName())) {
            strQuery.append(" AND bra.brand_nm LIKE :brandName");
            mapParam.put("brandName", '%' + filterSearchCms.getBrandName().trim() + '%');
        }
        if (Objects.nonNull(filterSearchCms.getValidYn())) {
            strQuery.append(" AND g.valid_yn = :validYn");
            mapParam.put("validYn", filterSearchCms.getValidYn().name());
        }
        if (Objects.nonNull(filterSearchCms.getIsExpired())) {
            if (EnumValidYn.Y.equals(filterSearchCms.getIsExpired())) {
                // Goods is expired
                // Goods is of type FIXED_DT and periodExpiredDate < current date
                strQuery.append(" AND ( g.period_type = 'FIXED_DT'");
                strQuery.append("       AND CONCAT(CAST(period_expire_date AS DATE), ' 23:59:59') < now() )");
            } else {
                // Goods is not expired
                // goods is of type FIXED_TERM or is of type FIXED_DT and periodExpiredDate >= current date
                strQuery.append(" AND ( g.period_type = 'FIXED_TERM'");
                strQuery.append("       OR g.`system` = 'EXTERNAL'");
                strQuery.append("       OR g.`system` = 'GIFTPOP'");
                strQuery.append("       OR g.`system` = 'UR_BOX'");
                strQuery.append("       OR g.`system` = 'WATANE'");
                strQuery.append("       OR CONCAT(CAST(period_expire_date AS DATE), ' 23:59:59') >= now() )");
            }
        }
        if (Objects.nonNull(filterSearchCms.getSystem())) {
            strQuery.append(" AND g.`system` = :system");
            mapParam.put("system", filterSearchCms.getSystem().name());
        }
        if (!CollectionUtils.isEmpty(filterSearchCms.getSystems())) {
            List<SystemType> systemTypeList = filterSearchCms.getSystems();
            List<String> placeholders = systemTypeList.stream()
                    .map(system -> ":system_" + system.ordinal())
                    .collect(Collectors.toList());

            String systems = String.join(", ", placeholders);

            strQuery.append(" AND g.`system` IN (").append(systems).append(")");

            systemTypeList.forEach(systemType ->
                    mapParam.put("system_" + systemType.ordinal(), systemType.name())
            );
        }
    }
}
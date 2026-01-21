package com.evoucher.adminapi.common.utils;

import com.evoucher.adminapi.cms.utils.CmsConstant;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import javax.persistence.Query;

@Slf4j
public class DataUtils {

    /**
     * Get SupplierId by BrandId
     * Eg: BrandId = 123456789-001 => 123456789
     * @param brandId
     * @return
     */
    public static String getSupplierIdByBrandId(String brandId) {
        log.info("Get SupplierId by brandId: {}", brandId);
        if (StringUtils.isBlank(brandId)) {
            return null;
        }

        String[] parts = brandId.split(CmsConstant.UNDERSCORE_SYMBOL);

        return parts[0];
    }

    /**
     * Get SupplierId by StoreId
     * Eg: StoreId = 123456789-001-001 => 123456789
     * @param storeId
     * @return
     */
    public static String getSupplierIdByStoreId(String storeId) {
        log.info("Get SupplierId by storeId: {}", storeId);
        if (StringUtils.isBlank(storeId)) {
            return null;
        }

        String[] parts = storeId.split(CmsConstant.UNDERSCORE_SYMBOL);

        return parts[0];
    }

    /**
     * Get BrandId by StoreId
     * Eg: StoreId = 123456789-001-001 => 123456789-001
     * @param storeId
     * @return
     */
    public static String getBrandIdByStoreId(String storeId) {
        log.info("Get BrandId by storeId: {}", storeId);
        if (StringUtils.isBlank(storeId)) {
            return null;
        }

        String[] parts = storeId.split(CmsConstant.UNDERSCORE_SYMBOL);

        return parts[0] + CmsConstant.UNDERSCORE_SYMBOL + parts[1];
    }
    public static Pageable getPageInfo(Integer offset, Integer pageSize) {
        int page = ObjectUtils.isEmpty(offset) ? Constant.DEFAULT_PAGE_OFFSET : offset - 1; //pageable count from 0
        pageSize = ObjectUtils.isEmpty(pageSize) ? Constant.DEFAULT_PAGE_SIZE : pageSize;
        return PageRequest.of(page, pageSize);
    }
    public static Pageable getPageInfo(Integer offset, Integer pageSize, Sort.Direction direction, String field) {
        int page = ObjectUtils.isEmpty(offset) ? Constant.DEFAULT_PAGE_OFFSET : offset - 1; //pageable count from 0
        pageSize = ObjectUtils.isEmpty(pageSize) ? Constant.DEFAULT_PAGE_SIZE : pageSize;
        return PageRequest.of(page, pageSize, Sort.by(direction, field));
    }
    public static void setPageInfoToQuery(Query query, Pageable pageable) {
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }

    public static void setEqualParam(Query query, String alias, Object value) {
        query.setParameter(alias, value);
    }

    public static void setLikeParam(Query query, String alias, String likeValue) {
        query.setParameter(alias, "%" + likeValue + "%");
    }

    public static void appendEqualsQuery(StringBuilder queryString, String column, String paramAlias) {
        queryString.append(String.format(" and %s = :%s \n",column, paramAlias));
    }

    public static void appendLikeQuery(StringBuilder queryString, String column, String paramAlias) {
        queryString.append(String.format(" and %s like :%s \n",column, paramAlias));
    }

    public static void createSortQuery(StringBuilder queryString, Sort.Direction direction, String... fields) {
        queryString.append(" order by ");
        queryString.append(String.join(",", fields));
        queryString.append(" ");
        queryString.append(direction.toString());
        queryString.append(", voucherUUID ASC");
    }
    public EnumValidYn booleanToYNEnum(Boolean boolValue) {
        return boolValue ? EnumValidYn.Y : EnumValidYn.N;
    }
}

package com.evoucher.adminapi.common.utils;

import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.common.config.gson.DateDeserializer;
import com.evoucher.adminapi.common.config.gson.DateSerializer;
import com.evoucher.adminapi.common.config.gson.LocalDateDeserializer;
import com.evoucher.adminapi.common.config.gson.LocalDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.Date;

public class Constant {
    private Constant() {}

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .excludeFieldsWithModifiers(Modifier.STATIC).create();
    public static final String ERROR_CODE = "-1";
    public static final int UNKNOWN_ERROR = 10001;
    public static final String UNKNOWN_ERROR_MSG = "unknown.error";
    public static final String SUCCESS_CODE = "0";
    public static final String MESSAGE_NOT_FOUND = "evoucher.id.not.found";
    public static final String MESSAGE_ACTION_FAIL = "evoucher.id.not.found";
    public static final String ANONYMOUS_USER = "ANONYMOUS_USER";

    public static final String EMPTY = "EMPTY";
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer DEFAULT_PAGE_OFFSET = 0;

    public static class Common {
        public final static String REGEX_SEARCH_SYMBOL = "%";
        public static final String COMMON_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String COMMON_DATE_FORMAT = "yyyy-MM-dd";
        public static final String SLIDE_DATE_FORMAT = "yyyy/MM/dd";
        public static final String ERROR_BRAND_CODE_EMPTY = "evoucher.brand.brand.code.empty";
    }

    public final static class BRAND {
        public final static String BRAND_FIRST = "-001";
        public final static int BRAND_SIZE_MAX = 999;
    }

    public final static class STORE {
        public final static String STORE_FIRST = "-0001";
        public final static int STORE_SIZE_MAX = 9999;
    }

    public final static class ROLE {
        public final static String ADMIN = "ROLE_ADMIN";
        public final static String SUPPLIER = "ROLE_SUPPLIER";
        public final static String BRAND = "ROLE_BRAND";
    }

    public final static class ACTION {
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
    }

    public final static class CUSTOMER {
        public final static String CUSTOMER_FIRST = "-001";
        public final static int CUSTOMER_SIZE_MAX = 999;
    }

    public final static class ERROR_MESSAGE_KEY {
        public final static class MESSAGE_TEMPLATE {
            public final static String NULL_ID = "evoucher.message_template.validate.id";
            public final static String NOT_FOUND = "evoucher.message_template.not_found";
        }
    }

    public static final class GIFT_POP {
        public static final String GIFTPOP_IN_CODE_GROUP_TABLE = "GIFTPOP";
        public static final String GIFTPOP_SUPPLIER_ID_IN_CODE_TABLE = "SUPPLIER_ID";
        public static final String GIFT_POP_STATUS_SUCCESS = "0000";
    }

    public static String toJsonString(Object o) {
        return gson.toJson(o);
    }

    public static Pageable pageableFromFilter(FilterSearchCms filterSearchCms, Integer inputPage, Integer inputPageSize){
        filterSearchCms.setPage(inputPage);
        filterSearchCms.setPageSize(inputPageSize);
        int page = ObjectUtils.isEmpty(filterSearchCms.getPage()) ? 0 : filterSearchCms.getPage() - 1; //pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchCms.getPageSize()) ? 10 : filterSearchCms.getPageSize();
        return PageRequest.of(page, pageSize);
    }
}

package com.evoucher.adminapi.utils;

import com.evoucher.adminapi.cms.utils.CmsDataUtil;
import com.evoucher.adminapi.common.utils.Constant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CmsDataUtilTests {

    public static final String SUPPLIER_ID = "SUP0001";

    @InjectMocks
    private CmsDataUtil cmsDataUtil;

    @Test
    void testCreateId_BrandId_From001_To003_Success() {
        List<String> brandIds = List.of("SUP0001-001", "SUP0001-002", "SUP0001-003");
        String idPrefix = SUPPLIER_ID + "-";
        String firstBrand = SUPPLIER_ID + Constant.BRAND.BRAND_FIRST;

        String result = CmsDataUtil.createId(brandIds, idPrefix, Constant.BRAND.BRAND_SIZE_MAX, firstBrand);
        assertEquals("SUP0001-004", result);
    }

    @Test
    void testCreateId_BrandId_Skip_003_Success() {
        List<String> brandIds = List.of("SUP0001-001", "SUP0001-002", "SUP0001-004");
        String idPrefix = SUPPLIER_ID + "-";
        String firstBrand = SUPPLIER_ID + Constant.BRAND.BRAND_FIRST;

        String result = CmsDataUtil.createId(brandIds, idPrefix, Constant.BRAND.BRAND_SIZE_MAX, firstBrand);
        assertEquals("SUP0001-003", result);
    }
}

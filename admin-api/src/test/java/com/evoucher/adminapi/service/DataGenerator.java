package com.evoucher.adminapi.service;

import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.Supplier;
import com.evoucher.adminapi.cms.service.models.request.BrandRequest;
import com.evoucher.adminapi.common.enums.*;

import static com.evoucher.adminapi.service.TestConstants.*;

public class DataGenerator {

    public static Brand createDummyBrand() {
        return Brand.builder()
                .id(DEFAULT_BRAND_ID)
                .brandName("Test Brand")
                .brandImagePath("/images/test-brand.jpg")
                .brandImageName("test-brand.jpg")
                .description("This is a test brand description")
                .supplierId(DEFAULT_SUPPLIER_ID)
                .validYn(EnumValidYn.Y)
                .defaultBrandYn(EnumValidYn.N)
                .displayType(PinDisplayType.BARCODE.name())
                .system(SystemType.INTERNAL)
                .brandCode(DEFAULT_BRAND_ID)
                .appId(DEFAULT_APP_ID)
                .authenticationKey(DEFAULT_AUTH_CD)
                .encryptionKey(DEFAULT_ENC_KEY)
                .serialNumberPrefix(DEFAULT_SERIAL_NUMBER_PREFIX)
                .serialNumberTotalLength(DEFAULT_SERIAL_TOTAL_LENGTH)
                .ipWhiteList("192.168.1.1,192.168.1.2")
                .build();
    }

    public static BrandRequest createDummyBrandRequest() {
        return BrandRequest.builder()
                .supplierId(DEFAULT_SUPPLIER_ID)
                .brandName("Test Brand Request")
                .brandImagePath("/images/test-brand-request.jpg")
                .brandImageName("test-brand-request.jpg")
                .description("This is a test brand request description")
                .validYn(EnumValidYn.Y)
                .defaultBrandYn(EnumValidYn.N)
                .displayType(PinDisplayType.BARCODE.name())
                .system(SystemType.INTERNAL)
                .brandCode(DEFAULT_BRAND_ID)
                .appId(DEFAULT_APP_ID)
                .serialNumberPrefix(DEFAULT_SERIAL_NUMBER_PREFIX)
                .serialNumberTotalLength(DEFAULT_SERIAL_TOTAL_LENGTH)
                .ipWhiteList(IP_WHITE_LIST)
                .build();
    }

    public static Supplier generateDummySupplier() {
        return Supplier.builder()
                .id(DEFAULT_SUPPLIER_ID)
                .taxcode("1234567890")
                .supplierName("Test Supplier")
                .bankName("Test Bank")
                .accountNumber("1234567890123")
                .accountName("Test Account")
                .settlementMethodCode(SettlementMethodCode.PER_EXCHANGE)
                .supplyDiscountRate(10.0)
                .supplyCommissionRate(5.0)
                .vatIncludeYn(EnumValidYn.Y)
                .managerName("John Doe")
                .managerEmail("john.doe@example.com")
                .managerMobileNumber("1234567890")
                .primaryContactName("Jane Smith")
                .primaryContactEmail("jane.smith@example.com")
                .primaryContactMobile("0987654321")
                .validYn(EnumValidYn.Y)
                .approveStatusCode(ApproveStatus.APPRV)
                .approveId("ADMIN123")
                .build();
    }

}

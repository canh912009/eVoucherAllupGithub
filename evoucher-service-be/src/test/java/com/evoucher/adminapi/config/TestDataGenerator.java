package com.evoucher.adminapi.config;

import com.evoucher.evoucherbe.common.enums.*;
import com.evoucher.evoucherbe.dto.EndUserDto;
import com.evoucher.evoucherbe.dto.GoodDto;
import com.evoucher.evoucherbe.entity.*;
import com.evoucher.evoucherbe.service.request.ChildVoucherGoodRequest;
import com.evoucher.evoucherbe.service.request.ChildVoucherRequest;
import com.evoucher.evoucherbe.service.request.PublishDetailRequest;
import com.evoucher.evoucherbe.service.request.PublishRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.evoucher.evoucherbe.common.enums.PinDisplayType.BARCODE;

public class TestDataGenerator {

    public static final Integer DEFAULT_PUBLISH_ID = 1;
    public static final Integer DEFAULT_PUBLISH_DETAIL_ID = 100;
    public static final String DEFAULT_MOBILE_NUMBER = "1234567890";
    public static final Integer DEFAULT_CAMPAIGN_ID = 500;
    public static final Long DEFAULT_GOODS_ID = 200L;
    public static final long DEFAULT_END_USER_ID = 8000;
    public static final String DEFAULT_BRAND_ID = "SUPPLIER_001-001";

    public static final String DEFAULT_SUPPLIER_ID = "SUPPLIER_001";
    public static final String DEFAULT_CUSTOMER_ID = "CUSTOMER_001";
    public static final Integer DEFAULT_CUSTOMER_CONTRACT_ID = 1000;
    public static final Integer DEFAULT_SUPPLIER_CONTRACT_ID = 2000;
    public static final Long DEFAULT_EXTERNAL_PIN_ID = 123456L;
    public static final String DEFAULT_EXTERNAL_PIN_NO = "EXT123456";
    public static final String DEFAULT_VOUCHER_ID = "VOUCHER-000123-98234-12828";

    public static final String DEFAULT_SHORT_URL = "https://127.0.0.1?voucher=abc123";
//    public static final String DEFAULT_ACTIVATION_URL = "https://127.0.0.1?activate=activate123";
//    public static final String DEFAULT_ACTIVATION_ID = "activate123";


    public static PublishRequest generateDummyPublishRequest() {
        return PublishRequest.builder()
                .publishId(DEFAULT_PUBLISH_ID)
                .publishDetails(List.of(
                        generateDummyPublishDetailRequest(DEFAULT_PUBLISH_DETAIL_ID),
                        generateDummyPublishDetailRequest(DEFAULT_PUBLISH_DETAIL_ID + 1)
                ))
                .build();
    }

    public static PublishDetailRequest generateDummyPublishDetailRequest(Integer publishDetailId) {
        return PublishDetailRequest.builder()
                .mobileNumber(DEFAULT_MOBILE_NUMBER)
                .publishDetailId(publishDetailId)
                .build();
    }

    public static EVoucher generateDummyEVoucher() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate;
        try {
            currentDate = format.parse(format.format(new Date()));
        } catch (ParseException e) {
            currentDate = new Date();
        }

        return EVoucher.builder()
                .eV(DEFAULT_VOUCHER_ID)
                .publishId(DEFAULT_PUBLISH_ID)
                .publishDetailId(DEFAULT_PUBLISH_DETAIL_ID)
                .goodsId(DEFAULT_GOODS_ID)
                .campaignId(DEFAULT_CAMPAIGN_ID)
                .registDate(currentDate)
                .userMobileNumber(DEFAULT_MOBILE_NUMBER)
                .userId(DEFAULT_END_USER_ID)
                .userName("Test User")
                .testYn(EnumValidYn.N)
                .creationDate(currentDate)
                .expirationDate(new Date(currentDate.getTime() + 30L * 24 * 60 * 60 * 1000)) // 30 days from now
                .publishDate(currentDate)
                .shortLink(DEFAULT_SHORT_URL)
                .voucherTypeCode(VoucherTypeCode.SI)
                .sticker("TEST-STICKER")
                .voucherStatusCode(VoucherStatusCode.NORMAL)
                .transferStatusCode(null)
                .subject("Test Voucher")
                .content("This is a test voucher")
                .useInfo("Use this voucher for testing")
                .imageUrl("https://test.com/image.jpg")
                .voucherPrice(100.0)
                .discountRate(10.0)
                .discountLimitPrice(90.0)
                .initAmount(100.0)
                .balance(100.0)
                .system(SystemType.INTERNAL)
                .serialNo("SN" + System.currentTimeMillis())
                .usageCount(0)
                .usageRemainingCount(1)
                .voucherVersion((short) 1)
                .build();
    }

    public static Publish generateDummyPublish() {
        Date currentDate = new Date();
        return Publish.builder()
                .id(DEFAULT_PUBLISH_ID)
                .campaign(generateDummyCampaign())
                .goods(generateDummyGoods())
                .publishName("Test Publish")
                .messageSubject("Test Subject")
                .messageContent("Test Content")
                .messageCallingNumber("1234567890")
                .bookingYn(EnumValidYn.N)
                .bookingDate(null)
                .testSendYn(EnumValidYn.N)
                .receiverNoDuplicateAllowYn(EnumValidYn.Y)
                .uploadType(UploadDataType.TEXT)
                .uploadFilePath(null)
                .uploadFileName(null)
                .uploadText(null)
                .smsType(SMSType.SMS)
                .supplier(generateDummySupplier())
                .customerId(DEFAULT_CUSTOMER_ID)
                .sellPrice(100.0)
                .sellListPrice(120.0)
                .sellDiscountRate(16.67)
                .sellDiscountAmount(20.0)
                .sellCommissionRate(5.0)
                .sellVatIncludeYn(EnumValidYn.Y)
                .sellSettlementMethodCode(SettlementMethodCode.PER_EXCHANGE)
                .sendCost(1.0)
                .publishDate(currentDate)
                .cancelDate(null)
                .publishStatusCode("PUBLISHED")
                .approveStatusCode(ApproveStatus.APPRV)
                .approveRequestId("REQ123")
                .approveRequestDate(currentDate)
                .approveId("APPR123")
                .approveDate(currentDate)
                .rejectId(null)
                .rejectDate(null)
                .rejectReason(null)
                .transactionId("TRANS123")
                .regDt(currentDate)
                .updtDt(currentDate)
                .contentLink("https://example.com/content")
                .contentImagePath("/path/to/image")
                .contentImageName("content_image.jpg")
                .build();
    }

    public static Campaign generateDummyCampaign() {
        Date currentDate = new Date();
        return Campaign.builder()
                .id(1)
                .campaignName("Test Campaign")
                .customerId("CUST123")
                .customerContractId(DEFAULT_CUSTOMER_CONTRACT_ID)
                .startDate(currentDate)
                .endDate(new Date(currentDate.getTime() + 30L * 24 * 60 * 60 * 1000)) // 30 days from now
                .messageSubject("Test Campaign Subject")
                .messageContent("Test Campaign Content")
                .messageCallingNumber(DEFAULT_MOBILE_NUMBER)
                .validYn(EnumValidYn.Y)
                .approveStatusCode(ApproveStatus.APPRV)
                .approveRequestId("REQ123")
                .approveRequestDate(currentDate)
                .approveId("APPR123")
                .approveDate(currentDate)
                .build();
    }
    public static Goods generateDummyGoods(Long goodId) {
        Date currentDate = new Date();
        return Goods.builder()
                .id(goodId)
                .goodsName("Test Goods")
                .supplierId(DEFAULT_SUPPLIER_ID)
                .brandId(DEFAULT_BRAND_ID)
                .goodsStatusCode("ACTIVE")
                .supplierGoodsId("SUPG123")
                .supplierContractId(DEFAULT_SUPPLIER_CONTRACT_ID)
                .listPrice(100.0)
                .sellPrice(90.0)
                .supplyDiscountRate(10.0)
                .supplyDiscountAmount(10.0)
                .supplyCommissionRate(5.0)
                .vatIncludeYn(EnumValidYn.Y)
                .settlementMethodCode(SettlementMethodCode.PER_EXCHANGE)
                .goodsDescription("Test goods description")
                .useInfo("Test usage information")
                .goodsImgPath("/path/to/goods/image")
                .goodsImgName("goods_image.jpg")
                .startDate(currentDate)
                .endDate(new Date(currentDate.getTime() + 90L * 24 * 60 * 60 * 1000)) // 90 days from now
                .validYn(EnumValidYn.Y)
                .exceptStoreIds("STORE1,STORE2")
                .goodsType(VoucherTypeCode.SI)
                .periodType(PeriodType.FIXED_TERM)
                .periodTerm(30)
                .periodExpireDate(null)
                .system(SystemType.INTERNAL)
                .usageCount(1)
                .build();
    }

    public static Goods generateDummyGoods() {
        return generateDummyGoods(DEFAULT_GOODS_ID);
    }

    public static GoodDto generateDummyGoodDto(Long id) {
        Date currentDate = new Date();
        GoodDto goodDto = new GoodDto();
        goodDto.setId(id);
        goodDto.setGoodsName("Test Goods");
        goodDto.setSupplierId(DEFAULT_SUPPLIER_ID);
        goodDto.setBrandId(DEFAULT_BRAND_ID);
        goodDto.setGoodsStatusCode("ACTIVE");
        goodDto.setSupplierGoodsId("SUPG123");
        goodDto.setSupplierContractId(DEFAULT_SUPPLIER_CONTRACT_ID);
        goodDto.setListPrice(100.0);
        goodDto.setSellPrice(90.0);
        goodDto.setSupplyDiscountRate(10.0);
        goodDto.setSupplyDiscountAmount(10.0);
        goodDto.setSupplyCommissionRate(5.0);
        goodDto.setVatIncludeYn(EnumValidYn.Y);
        goodDto.setSettlementMethodCode(SettlementMethodCode.PER_EXCHANGE);
        goodDto.setGoodsDescription("Test goods description");
        goodDto.setUseInfo("Test usage information");
        goodDto.setGoodsImgPath("/path/to/goods/image");
        goodDto.setGoodsImgName("goods_image.jpg");
        goodDto.setStartDate(currentDate);
        goodDto.setEndDate(new Date(currentDate.getTime() + 90L * 24 * 60 * 60 * 1000)); // 90 days from now
        goodDto.setValidYn(EnumValidYn.Y);
        goodDto.setExceptStoreIds("STORE1,STORE2");
        goodDto.setGoodsType(VoucherTypeCode.SI);
        goodDto.setPeriodType(PeriodType.FIXED_TERM);
        goodDto.setPeriodTerm(30);
        goodDto.setPeriodExpireDate(null);
        goodDto.setSystem(SystemType.INTERNAL);
        goodDto.setUsageCount(1);
        return goodDto;
    }
    public static GoodDto generateDummyGoodDto() {
        return generateDummyGoodDto(DEFAULT_GOODS_ID);
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

    public static Customer generateDummyCustomer() {
        Date currentDate = new Date();
        return Customer.builder()
                .id(DEFAULT_CUSTOMER_ID)
                .customerName("Test Customer")
                .taxcode("0987654321")
                .bankName("Customer Bank")
                .accountNumber("9876543210987")
                .accountName("Customer Account")
                .managerName("Alice Johnson")
                .managerEmail("alice.johnson@example.com")
                .managerMobileNo("1122334455")
                .validYn("Y")
                .approveStatusCode("APPROVED")
                .approveId("ADMIN456")
                .customerTypeCode("REGULAR")
                .regId("SYSTEM")
                .regDt(currentDate)
                .updtId("SYSTEM")
                .updtDt(currentDate)
                .build();
    }

    public static CustomerContract generateDummyCustomerContract() {
        Date currentDate = new Date();
        return CustomerContract.builder()
                .id(1)
                .contractName("Test Customer Contract")
                .startDate(currentDate)
                .endDate(new Date(currentDate.getTime() + 365L * 24 * 60 * 60 * 1000)) // 1 year from now
                .customerId("CUST123")
                .sellDiscountAmount(5.0)
                .sellDiscountRate(10.0)
                .sellCommissionRate(3.0)
                .sellVatIncludeYn(EnumValidYn.Y)
                .sellSettlementMethodCode(SettlementMethodCode.PER_EXCHANGE)
                .approveStatusCode(ApproveStatus.APPRV)
                .approveRequestId("REQ123")
                .approveRequestDate(currentDate)
                .approveId("ADMIN789")
                .approveDate(currentDate)
                .contractFilePath("/path/to/customer/contract")
                .contractFileName("customer_contract.pdf")
                .validYn(EnumValidYn.Y)
                .build();
    }

    public static SupplierContract generateDummySupplierContract() {
        Date currentDate = new Date();
        return SupplierContract.builder()
                .id(DEFAULT_CUSTOMER_CONTRACT_ID)
                .contractName("Test Supplier Contract")
                .startDate(currentDate)
                .endDate(new Date(currentDate.getTime() + 365L * 24 * 60 * 60 * 1000)) // 1 year from now
                .supplierId(DEFAULT_SUPPLIER_ID)
                .supplyDiscountAmount(10.0)
                .supplyDiscountRate(15.0)
                .supplyCommissionRate(5.0)
                .supplyVatIncludeYn(EnumValidYn.Y)
                .supplySettlementMethodCode(SettlementMethodCode.PER_EXCHANGE)
                .approveStatusCode(ApproveStatus.APPRV)
                .approveRequestId("REQ456")
                .approveRequestDate(currentDate)
                .approveId("ADMIN012")
                .approveDate(currentDate)
                .contractFilePath("/path/to/supplier/contract")
                .contractFileName("supplier_contract.pdf")
                .validYn(EnumValidYn.Y)
                .build();
    }

    public static PublishDetail generateDummyPublishDetail(Integer id) {
        Date currentDate = new Date();
        return PublishDetail.builder()
                .id(id)
                .publishId(DEFAULT_PUBLISH_ID)
                .receiverMobileNo(DEFAULT_MOBILE_NUMBER)
                .userId(DEFAULT_END_USER_ID)
                .publishStatusCd("PUBLISHED")
                .publishResultMessage("Success")
                .smsId("SMS123")
                .smsType("SMS")
                .smsSendDt(currentDate)
                .smsSendResultDate(new Date(currentDate.getTime() + 60 * 1000)) // 1 minute after send date
                .externalPinId(null)
                .regDt(currentDate)
                .updateDate(currentDate)
                .build();
    }

    public static EndUserDto generateDummyEndUserDto() {
        return EndUserDto.builder()
                .id(DEFAULT_END_USER_ID)
                .userMobileNum(DEFAULT_MOBILE_NUMBER)
                .userNm("John Doe")
                .gender("M")
                .birthday(new Date())
                .address("123 Test Street, Test City")
                .email("john.doe@example.com")
                .build();
    }

    public static Brand generateDummyBrand() {
        return Brand.builder()
                .id(DEFAULT_BRAND_ID)
                .brandName("Test Brand")
                .brandImagePath("/path/to/brand/image")
                .brandImageName("brand_image.jpg")
                .description("This is a test brand")
                .supplierId(DEFAULT_SUPPLIER_ID)
                .validYn(EnumValidYn.Y)
                .defaultBrandYn(EnumValidYn.N)
                .displayType(BARCODE.name())
                .system(SystemType.INTERNAL)
                .brandCode("TB123")
                .appId("APP123")
                .authenticationKey("auth_key_123")
                .encryptionKey("enc_key_123")
                .serialNumberPrefix("TB")
                .serialNumberTotalLength(10)
                .ipWhiteList("192.168.1.1,192.168.1.2")
                .build();
    }

    public static ExternalPin generateDummyExternalPin() {
        Date currentDate = new Date();
        return ExternalPin.builder()
                .id(DEFAULT_EXTERNAL_PIN_ID)
                .externalPinNo(DEFAULT_EXTERNAL_PIN_NO)
                .uploadId(1)
                .goodsId(DEFAULT_GOODS_ID)
                .status(ExternalPinStatus.AVAILABLE)
                .expireTime(new Date(currentDate.getTime() + 30L * 24 * 60 * 60 * 1000))  // 30 days from now
                .password("secretpin123")
                .displayType(BARCODE)
                .build();
    }

    public static ChildVoucherRequest generateDummyChildVoucherRequest() {
        return ChildVoucherRequest.builder()
                .parentId(DEFAULT_VOUCHER_ID)
                .publishId(DEFAULT_PUBLISH_ID)
                .products(Arrays.asList(
                        generateDummyChildVoucherGoodRequest(DEFAULT_GOODS_ID, 1),
                        generateDummyChildVoucherGoodRequest(DEFAULT_GOODS_ID + 1, 2)
                ))
                .type(SystemType.INTERNAL)
                .build();
    }

    private static ChildVoucherGoodRequest generateDummyChildVoucherGoodRequest(Long goodsId, int quantity) {
        return ChildVoucherGoodRequest.builder()
                .goodsId(goodsId)
                .quantity(quantity)
                .build();
    }
}

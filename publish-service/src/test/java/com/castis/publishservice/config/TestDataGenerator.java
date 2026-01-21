package com.castis.publishservice.config;

import com.castis.publishservice.dto.*;
import com.castis.publishservice.dto.request.ChoiceChosenItem;
import com.castis.publishservice.dto.request.PublishRequest;
import com.castis.publishservice.dto.response.VoucherResponse;
import com.castis.publishservice.entity.*;
import com.castis.publishservice.mapper.PublishDetailMapper;
import com.castis.publishservice.mapper.PublishMapper;
import com.castis.publishservice.utils.Constants;
import com.castis.publishservice.utils.enum_template.*;
import com.castis.publishservice.utils.status.EnumYN;
import com.castis.publishservice.utils.status.PublishStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestDataGenerator {

    public static final Long DEFAULT_PUBLISH_ID = 1L;
    public static final Long DEFAULT_PUBLISH_DETAIL_ID = 100L;
    public static final String DEFAULT_MOBILE_NUMBER = "1234567890";
    public static final String DEFAULT_MOBILE_NUMBER2 = "1111222233";
    public static final Long DEFAULT_CAMPAIGN_ID = 500L;
    public static final Long DEFAULT_GOODS_ID = 200L;
    public static final long DEFAULT_END_USER_ID = 8000;
    public static final String DEFAULT_BRAND_ID = "SUPPLIER_001-001";

    public static final String DEFAULT_SUPPLIER_ID = "SUPPLIER_001";
    public static final String DEFAULT_CUSTOMER_ID = "CUSTOMER_001";
    public static final Integer DEFAULT_CUSTOMER_CONTRACT_ID = 1000;
    public static final Long DEFAULT_SUPPLIER_CONTRACT_ID = 2000L;
    public static final Long DEFAULT_EXTERNAL_PIN_ID = 123456L;
    public static final String DEFAULT_EXTERNAL_PIN_NO = "EXT123456";

    public static final String DEFAULT_SHORT_URL = "https://127.0.0.1?voucher=abc123";
    public static final String DEFAULT_VOUCHER_ID = "VOUCHER-000123-98234-12828";


    public static PublishRequest generateDummyPublishRequest() {
        return PublishRequest.builder().publishId(DEFAULT_PUBLISH_ID).users(List.of(generateDummyEndUserDto(DEFAULT_END_USER_ID, DEFAULT_MOBILE_NUMBER), generateDummyEndUserDto(DEFAULT_END_USER_ID + 1, DEFAULT_MOBILE_NUMBER2))).build();
    }


    public static Voucher generateDummyVoucher() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate;
        try {
            currentDate = format.parse(format.format(new Date()));
        } catch (ParseException e) {
            currentDate = new Date();
        }

        return Voucher.builder().id(DEFAULT_VOUCHER_ID).publishId(DEFAULT_PUBLISH_ID).publishDtlId(DEFAULT_PUBLISH_DETAIL_ID).goods(generateDummyGoods()).campaignId(DEFAULT_CAMPAIGN_ID).regDt(currentDate).userMobileNumber("1234567890").user(generateDummyEndUser(DEFAULT_END_USER_ID, DEFAULT_MOBILE_NUMBER)).userName("Test User").testYn(EnumYN.N.name()).creationDate(currentDate).expirationDate(new Date(currentDate.getTime() + 30L * 24 * 60 * 60 * 1000)) // 30 days from now
                .publishDate(currentDate).shortLink(DEFAULT_SHORT_URL).voucherTypeCd("SI").sticker("TEST-STICKER").voucherStatusCode(VoucherStatusCode.NORMAL.name()).transferStatusCode(null).subject("Test Voucher").content("This is a test voucher").useInfo("Use this voucher for testing").imgUrl("https://test.com/image.jpg").voucherPrice(100.0).discountRate(10.0).discountLimitPrice(90.0).initialAmount(100.0).balance(100.0).system(SystemType.INTERNAL).serialNo("SN" + System.currentTimeMillis()).voucherVersion((short) 2).build();
    }

    public static Publish generateDummyPublish() {
        Date currentDate = new Date();
        return Publish.builder().id(DEFAULT_PUBLISH_ID).campaign(generateDummyCampaign()).good(generateDummyGoods()).publishName("Test Publish").messageSubject("Test Subject").messageContent("Test Content").messageCallingNumber("1234567890").bookingYn(EnumYN.N.name()).bookingDate(null).testSendYn(EnumYN.N.name()).receiverNoDuplicateAllowYn(EnumYN.N.name()).uploadType("TEXT").uploadFilePath(null).uploadFileName(null).uploadText(null).smsType(SmsType.SMS.name()).supplierId(DEFAULT_SUPPLIER_ID).customerId(DEFAULT_CUSTOMER_ID).sellPrice(100.0).sellListPrice(120.0).sellDiscountRate(16.67).sellDiscountAmount(20.0).sellCommissionRate(5.0).sellVatIncludeYn(EnumYN.Y.name()).sellSettlementMethodCode("PER_EXCHANGE").sendCost(1.0).publishDate(currentDate).cancelDate(null).publishStatusCode(PublishStatus.PUBLISHING).approveStatusCode("APPRV").approveRequestId("REQ123").approveRequestDate(currentDate).approveId("APPR123").approveDate(currentDate).rejectId(null).rejectDate(null).rejectReason(null).regDt(currentDate).updtDt(currentDate).build();
    }

    public static PublishDTO generateDummyPublishDTO() {
        return PublishMapper.INSTANCE.toDTO(generateDummyPublish());
    }

    public static Campaign generateDummyCampaign() {
        Date currentDate = new Date();
        return Campaign.builder().id(DEFAULT_CAMPAIGN_ID).campaignName("Test Campaign").customerId("CUST123").customerContractId(DEFAULT_CUSTOMER_CONTRACT_ID).startDate(currentDate).endDate(new Date(currentDate.getTime() + 30L * 24 * 60 * 60 * 1000)) // 30 days from now
                .messageSubject("Test Campaign Subject").messageContent("Test Campaign Content").messageCallingNumber(DEFAULT_MOBILE_NUMBER).validYn(EnumYN.Y.name()).approveRequestId("REQ123").approveRequestDate(currentDate).approveId("APPR123").approveDate(currentDate).build();
    }

    public static Goods generateDummyGoods() {
        Date currentDate = new Date();
        return Goods.builder().id(DEFAULT_GOODS_ID).goodsName("Test Goods").supplierId(DEFAULT_SUPPLIER_ID).brandId(DEFAULT_BRAND_ID).goodsStatusCd(null).supplierGoodsId("SUPG123").listPrice(100.0).sellPrice(90.0).supplyDiscountRate(10.0).supplyDiscountAmount(10.0).supplyCommissionRate(5.0).vatIncludeYn(EnumYN.Y.name()).settlementMethodCode("PER_EXCHANGE").goodsDescription("Test goods description").useInfo("Test usage information").goodsImgPath("/path/to/goods/image").goodsImgName("goods_image.jpg").startDate(currentDate).endDate(new Date(currentDate.getTime() + 90L * 24 * 60 * 60 * 1000)) // 90 days from now
                .validYn(EnumYN.Y.name()).exceptStoreIds("STORE1,STORE2").type(GoodType.SI).periodType("FIXED_TERM").periodTerm(30.0).periodExpireDate(null).system(SystemType.INTERNAL).build();
    }

    public static VoucherResponse generateDummyVoucherResponse() {
        return VoucherResponse.builder()
                .code(Constants.SUCCESS_CODE)
                .message("Success")
                .data(List.of(DEFAULT_VOUCHER_ID)).build();
    }

    public static GoodsDTO generateDummyGoodDto() {
        Date currentDate = new Date();
        GoodsDTO goodDto = new GoodsDTO();
        goodDto.setId(DEFAULT_GOODS_ID);
        goodDto.setGoodsName("Test Goods");
        goodDto.setSupplierId(DEFAULT_SUPPLIER_ID);
        goodDto.setBrandId(DEFAULT_BRAND_ID);
        goodDto.setGoodsStatusCd(null);
        goodDto.setSupplierGoodsId("SUPG123");
        goodDto.setListPrice(100.0);
        goodDto.setSellPrice(90.0);
        goodDto.setSupplyDiscountRate(10.0);
        goodDto.setSupplyDiscountAmount(10.0);
        goodDto.setSupplyCommissionRate(5.0);
        goodDto.setVatIncludeYn(EnumYN.Y.name());
        goodDto.setSettlementMethodCode("PER_EXCHANGE");
        goodDto.setGoodsDescription("Test goods description");
        goodDto.setUseInfo("Test usage information");
        goodDto.setGoodsImgPath("/path/to/goods/image");
        goodDto.setGoodsImgName("goods_image.jpg");
        goodDto.setStartDate(currentDate);
        goodDto.setEndDate(new Date(currentDate.getTime() + 90L * 24 * 60 * 60 * 1000)); // 90 days from now
        goodDto.setValidYn(EnumYN.Y.name());
        goodDto.setExceptStoreIds("STORE1,STORE2");
        goodDto.setType(GoodType.SI);
        goodDto.setPeriodType("FIXED_TERM");
        goodDto.setPeriodTerm(30.0);
        goodDto.setPeriodExpireDate(null);
        goodDto.setSystem(SystemType.INTERNAL);
        return goodDto;
    }

    public static Supplier generateDummySupplier() {
        return Supplier.builder().id(DEFAULT_SUPPLIER_ID).taxcode("1234567890").supplierName("Test Supplier").bankName("Test Bank").accountNumber("1234567890123").accountName("Test Account").settlementMethodCode("PER_EXCHANGE").supplyDiscountRate(10.0).supplyCommissionRate(5.0).vatIncludeYn(EnumYN.Y.name()).managerName("John Doe").managerEmail("john.doe@example.com").managerMobileNumber("1234567890").primaryContactName("Jane Smith").primaryContactEmail("jane.smith@example.com").primaryContactMobile("0987654321").validYn(EnumYN.Y.name()).approveStatusCode("APPRV").approveId("ADMIN123").build();
    }

    public static Customer generateDummyCustomer() {
        Date currentDate = new Date();
        return Customer.builder().id(DEFAULT_CUSTOMER_ID).customerName("Test Customer").taxcode("0987654321").bankName("Customer Bank").accountNumber("9876543210987").accountName("Customer Account").managerName("Alice Johnson").managerEmail("alice.johnson@example.com").managerMobileNo("1122334455").validYn("Y").approveStatusCd("APPRV").approverId("ADMIN456").regId("SYSTEM").regDt(currentDate).updtId("SYSTEM").updtDt(currentDate).build();
    }

    public static PublishDetail generateDummyPublishDetail(Long id) {
        Date currentDate = new Date();
        return PublishDetail.builder().publishDtlId(id).publishId(DEFAULT_PUBLISH_ID).receiverMobileNo(DEFAULT_MOBILE_NUMBER).userId(DEFAULT_END_USER_ID).publishStatusCd("STRT_PUB").publishResultMessage("Success").smsId("SMS123").smsType("SMS").smsSendDt(currentDate).smsSendResultDate(new Date(currentDate.getTime() + 60 * 1000)) // 1 minute after send date
                .extPinId(null).regDt(currentDate).updateDate(currentDate).build();
    }

    public static PublishDetailDTO generateDummyPublishDetailDTO(Long id) {
        return PublishDetailMapper.INSTANCE.toDTO(generateDummyPublishDetail(id));
    }

    public static EndUserDto generateDummyEndUserDto(long id, String mobileNumber) {
        return EndUserDto.builder().id(id).userMobileNum(mobileNumber).userNm("John Doe").gender("M").birthday(new Date()).address("123 Test Street, Test City").email("john.doe@example.com").build();
    }

    public static EndUser generateDummyEndUser(long id, String mobileNumber) {
        return EndUser.builder().id(id).userMobileNum(mobileNumber).userNm("John Doe").gender("M").birthday(new Date()).address("123 Test Street, Test City").email("john.doe@example.com").build();
    }

    public static Brand generateDummyBrand() {
        return Brand.builder().id(DEFAULT_BRAND_ID).brandName("Test Brand").brandImagePath("/path/to/brand/image").brandImageName("brand_image.jpg").description("This is a test brand").supplierId(DEFAULT_SUPPLIER_ID).validYn(EnumYN.Y.name()).defaultBrandYn(EnumYN.N.name()).displayType(PinDisplayType.BARCODE.name()).system(SystemType.INTERNAL.name()).build();
    }

    public static PurchaseChildRequest generateDummyPurchaseChildRequest() {
        PurchaseChildRequest request = new PurchaseChildRequest();
        request.setParentVoucherId(DEFAULT_VOUCHER_ID);
        request.setProducts(List.of(generateDummyChoiceChosenItem(DEFAULT_GOODS_ID), generateDummyChoiceChosenItem(DEFAULT_GOODS_ID + 1)));
        request.setType(SystemType.CHOICE);
        return request;
    }

    private static ChoiceChosenItem generateDummyChoiceChosenItem(Long goodId) {
        ChoiceChosenItem item = new ChoiceChosenItem();
        item.setGoodsId(goodId);
        item.setQuantity(1);
        return item;
    }
}

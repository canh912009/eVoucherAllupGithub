package asia.castis.evoucherservicefe.publishrequest.service;

import asia.castis.evoucherservicefe.common.dto.messagetemplate.MessageTemplate;
import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.*;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.PublishMessage;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.RequestToPushAgent;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.TemplateData;
import asia.castis.evoucherservicefe.common.enums.*;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.common.model.publish.*;
import asia.castis.evoucherservicefe.common.service.EsPublishService;
import asia.castis.evoucherservicefe.common.utils.ValidateResult;
import asia.castis.evoucherservicefe.exceptions.*;
import asia.castis.evoucherservicefe.publishrequest.sender.PublishRequestSender;
import asia.castis.evoucherservicefe.publishrequest.service.impl.EmailService;
import asia.castis.evoucherservicefe.publishrequest.service.impl.PublishRequestServiceImpl;
import asia.castis.evoucherservicefe.publishrequest.utils.PublishValidator;
import asia.castis.evoucherservicefe.voucherhandler.service.VoucherService;
import org.elasticsearch.ElasticsearchGenerationException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PublishRequestServiceImplTest {
    public static final String DATE_STRING = "2022-01-01 00:00:00";
    @InjectMocks
    PublishRequestServiceImpl publishRequestServiceImpl;

    @Mock
    VoucherService voucherService;
    @Mock
    EsPublishService esPublishService;
    @Mock
    MessageService smsMessageService;
    @Mock
    MessageService zaloMessageService;
    @Mock
    PublishRequestSender publishRequestSender;
    @Mock
    ModelMapper modelMapper;
    @Mock
    PublishValidator validator;
    @Mock
    EmailService emailService;
    @Mock
    MessageResultService messageResultService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // initialize the instance of PublishRequestServiceImpl and inject the mocks automatically
        this.publishRequestServiceImpl = new PublishRequestServiceImpl(
                voucherService,
                esPublishService,
                smsMessageService,
                publishRequestSender,
                modelMapper,
                validator,
                emailService,
                messageResultService
        );
    }

    /**
     * This method is used to test the handling of incoming publish requests when the request data is null.
     * It verifies that the method does not interact with the ES Publish Service.
     */
    @Test
    void testIncomingPublishHandling_NullRequestData() {
        publishRequestServiceImpl.incomingPublishHandling(null, new Date());
        verifyNoInteractions(esPublishService);
    }

    @Test
    void testIncomingPublishHanding_NullPublishId() {
        publishRequestServiceImpl.incomingPublishHandling(null, new Date());
        verifyNoInteractions(esPublishService);
    }

    @Test
    void testIncomingPublishHanding_Invalid() throws SendMessageToQueueException {
        when(validator.validate(any())).thenReturn(ValidateResult.builder().isValid(false).build());
        publishRequestServiceImpl.incomingPublishHandling(createDummyRequestFromBE(), new Date());
        // Send error message back and terminate the process
        verify(publishRequestSender, times(1)).sendCreateMessageResult(any());
        verifyNoInteractions(esPublishService);
    }

    @Test
    void testIncomingPublishHanding_ParseException() throws SendMessageToQueueException {
        RequestFromBE request = createDummyRequestFromBE();

        when(validator.validate(any())).thenReturn(ValidateResult.builder().isValid(true).build());
        when(modelMapper.map(request, PublishModel.class)).thenThrow(new RuntimeException());
        publishRequestServiceImpl.incomingPublishHandling(request, new Date());
        verify(publishRequestSender, times(1)).sendCreateMessageResult(any());
        verifyNoInteractions(esPublishService);
    }

    @Test
    void testIncomingPublishHanding_NormalVoucher_Success() throws SendMessageToQueueException, ParseRequestException, CreateMessageException, NotFoundException {
        RequestFromBE request = createDummyRequestFromBE();
        PublishModel publishModel = createDummyPublishModel();
        VoucherModel voucherModel = createDummyVoucherModel();
        RequestToPushAgent requestToPushAgent = createDummyRequestToPA();

        when(validator.validate(any())).thenReturn(ValidateResult.builder().isValid(true).build());
        when(modelMapper.map(request, PublishModel.class)).thenReturn(publishModel);
        when(voucherService.parseVoucher(request.getPublishDetails().get(0).getVoucher(),
                request.getPublishDetails().get(0).getId(),
                publishModel.getSmsType())
        ).thenReturn(voucherModel);
        when(esPublishService.save(publishModel)).thenReturn(publishModel);
        when(voucherService.findById(voucherModel.getId())).thenReturn(voucherModel);
        when(modelMapper.map(request, RequestToPushAgent.class)).thenReturn(requestToPushAgent);
        publishRequestServiceImpl.incomingPublishHandling(request, new Date());
        verify(publishRequestSender, atLeast(2)).sendCreateMessageResult(any());
        verify(voucherService, times(1)).saveVouchers(any());
        verify(esPublishService, atLeast(2)).save(any(PublishModel.class));
        verify(smsMessageService, times(1)).createMessage(any(), any(), any());
        verify(publishRequestSender, times(1)).sendRequestToPushAgent(requestToPushAgent);
    }

    @Test
    void testIncomingPublishHanding_NormalVoucher_EsSaveVoucherException() throws ParseRequestException {
        RequestFromBE request = createDummyRequestFromBE();
        PublishModel publishModel = createDummyPublishModel();
        VoucherModel voucherModel = createDummyVoucherModel();

        when(validator.validate(any())).thenReturn(ValidateResult.builder().isValid(true).build());
        when(modelMapper.map(request, PublishModel.class)).thenReturn(publishModel);
        when(voucherService.parseVoucher(request.getPublishDetails().get(0).getVoucher(),
                request.getPublishDetails().get(0).getId(),
                publishModel.getSmsType())
        ).thenReturn(voucherModel);
        doThrow(new ElasticsearchGenerationException("ES exception")).when(voucherService).saveVouchers(List.of(voucherModel));

        publishRequestServiceImpl.incomingPublishHandling(request, new Date());

        // Expect rolling back manually by deleting all vouchers and publish just created
        verify(esPublishService, times(1)).delete(publishModel.getId());
        verify(voucherService, times(1)).delete(any());
    }

    @Test
    void testIncomingPublishHanding_NormalVoucher_Download() throws SendMessageToQueueException, ParseRequestException, NotFoundException {
        RequestFromBE request = createDummyRequestFromBE();
        request.setSmsType(EnumMessageType.DOWNLOAD.getValue());
        PublishModel publishModel = createDummyPublishModel();
        publishModel.setSmsType(EnumMessageType.DOWNLOAD);
        VoucherModel voucherModel = createDummyVoucherModel();
        voucherModel.setSmsType(EnumMessageType.DOWNLOAD);
        RequestToPushAgent requestToPushAgent = createDummyRequestToPA();

        when(validator.validate(any())).thenReturn(ValidateResult.builder().isValid(true).build());
        when(modelMapper.map(request, PublishModel.class)).thenReturn(publishModel);
        when(voucherService.parseVoucher(request.getPublishDetails().get(0).getVoucher(),
                request.getPublishDetails().get(0).getId(),
                publishModel.getSmsType())
        ).thenReturn(voucherModel);
        when(esPublishService.save(publishModel)).thenReturn(publishModel);
        when(voucherService.findById(voucherModel.getId())).thenReturn(voucherModel);
        publishRequestServiceImpl.incomingPublishHandling(request, new Date());
        verify(voucherService, times(1)).saveVouchers(any());
        verify(publishRequestSender, times(2)).sendCreateMessageResult(any());
        // No request will be sent to push agent
        verify(publishRequestSender, never()).sendRequestToPushAgent(requestToPushAgent);
    }


    public RequestFromBE createDummyRequestFromBE() {
        Supplier dummySupplier = new Supplier();
        dummySupplier.setId("supplierId");
        dummySupplier.setName("supplierName");
        dummySupplier.setDescription("supplierDescription");
        dummySupplier.setImgUrl("supplierImgUrl");

        Brand dummyBrand = new Brand();
        dummyBrand.setId("brandId");
        dummyBrand.setName("brandName");
        dummyBrand.setDescription("brandDescription");
        dummyBrand.setImgUrl("brandImgUrl");
        dummyBrand.setSystem("brandSystem");
        dummyBrand.setIsPosLink(true);
        dummyBrand.setSupplier(dummySupplier);

        Category dummyCategory = new Category();
        dummyCategory.setId("categoryId");
        dummyCategory.setName("categoryName");
        dummyCategory.setDescription("categoryDescription");

        Goods dummyGoods = createDummyGoodsRequest(dummyCategory, dummyBrand);

        Voucher dummyVoucher = createDummyVoucherRequest(dummyGoods);

        PublishDetail dummyPublishDetail = new PublishDetail();
        dummyPublishDetail.setId(1L);
        dummyPublishDetail.setVoucher(dummyVoucher);

        Campaign dummyCampaign = new Campaign();
        dummyCampaign.setId(1L);
        dummyCampaign.setStartDate(DATE_STRING);
        dummyCampaign.setEndDate(DATE_STRING);
        dummyCampaign.setMessageTemplate(new MessageTemplate());

        RequestFromBE requestFromBE = new RequestFromBE();
        requestFromBE.setId(1L);
        requestFromBE.setType(EnumPublishType.NORMAL);
        requestFromBE.setSmsType(EnumMessageType.SMS.getValue());
        requestFromBE.setBookingDate(DATE_STRING);
        requestFromBE.setPublishDate(DATE_STRING);
        requestFromBE.setCancelDate(DATE_STRING);
        requestFromBE.setCampaign(dummyCampaign);
        requestFromBE.setPublishDetails(Collections.singletonList(dummyPublishDetail));

        return requestFromBE;

    }

    @Test
    void testIncomingPublishHanding_Transfer_Success() throws SendMessageToQueueException, ParseRequestException, CreateMessageException, NotFoundException, IOException {
        RequestFromBE request = createDummyRequestFromBE();
        request.setType(EnumPublishType.TRANSFER);
        PublishModel publishModel = createDummyPublishModel();
        publishModel.setType(EnumPublishType.TRANSFER);
        VoucherModel voucherModel = createDummyVoucherModel();
        RequestToPushAgent requestToPushAgent = createDummyRequestToPA();

        when(validator.validate(any())).thenReturn(ValidateResult.builder().isValid(true).build());
        when(modelMapper.map(request, PublishModel.class)).thenReturn(publishModel);
        when(voucherService.parseVoucher(request.getPublishDetails().get(0).getVoucher(),
                request.getPublishDetails().get(0).getId(),
                publishModel.getSmsType())
        ).thenReturn(voucherModel);
        when(esPublishService.save(publishModel)).thenReturn(publishModel);
        when(voucherService.findById(voucherModel.getId())).thenReturn(voucherModel);
        when(modelMapper.map(request, RequestToPushAgent.class)).thenReturn(requestToPushAgent);
        publishRequestServiceImpl.incomingPublishHandling(request, new Date());
        verify(publishRequestSender, atLeast(2)).sendCreateMessageResult(any());
        verify(voucherService, times(1)).saveVouchers(any());
        verify(esPublishService, atLeast(2)).save(any(PublishModel.class));
        verify(smsMessageService, times(1)).createMessage(any(), any(), any());
        verify(publishRequestSender, times(1)).sendRequestToPushAgent(requestToPushAgent);
    }

    @Test
    void testIncomingPublishHanding_Transfer_Rollback() throws ParseRequestException, NotFoundException {
        RequestFromBE request = createDummyRequestFromBE();
        request.setType(EnumPublishType.TRANSFER);
        PublishModel publishModel = createDummyPublishModel();
        publishModel.setType(EnumPublishType.TRANSFER);
        VoucherModel voucherModel = createDummyVoucherModel();

        when(validator.validate(any())).thenReturn(ValidateResult.builder().isValid(true).build());
        when(esPublishService.getPublish(1L)).thenReturn(publishModel);
        when(modelMapper.map(request, PublishModel.class)).thenReturn(publishModel);
        when(voucherService.parseVoucher(request.getPublishDetails().get(0).getVoucher(),
                request.getPublishDetails().get(0).getId(),
                publishModel.getSmsType())
        ).thenReturn(voucherModel);

        when(esPublishService.save(publishModel)).thenReturn(publishModel);
        doThrow(new ElasticsearchGenerationException("ES exception")).when(voucherService).saveVouchers(List.of(voucherModel));
        publishRequestServiceImpl.incomingPublishHandling(request, new Date());
        // Rollback for both publish and vouchers
        verify(esPublishService, times(1)).rollback(eq(request.getId()), any(PublishModel.class));
        verify(voucherService, times(1)).delete(any());
    }


    private static @NotNull Goods createDummyGoodsRequest(Category dummyCategory, Brand dummyBrand) {
        Goods dummyGoods = new Goods();
        dummyGoods.setId(1L);
        dummyGoods.setName("goodsName");
        dummyGoods.setListPrice(100.0);
        dummyGoods.setSellPrice(80.0);
        dummyGoods.setSupplyDiscountAmount(20.0);
        dummyGoods.setSupplyDiscountRate(0.2);
        dummyGoods.setSupplyFeeRate(0.1);
        dummyGoods.setSupplyVatInclude(true);
        dummyGoods.setSupplyCalculateMethodCode("supplyMethodCode");
        dummyGoods.setSellDiscountRate(0.1);
        dummyGoods.setSellDiscountCost(10.0);
        dummyGoods.setSellFeeRate(0.05);
        dummyGoods.setSellCalculateMethod("sellCalculateMethod");
        dummyGoods.setSendCost(5.0);
        dummyGoods.setStartDate(DATE_STRING);
        dummyGoods.setEndDate(DATE_STRING);
        dummyGoods.setValid(true);
        dummyGoods.setSticker("sticker");
        dummyGoods.setExceptStoreIds("store1,store2");
        dummyGoods.setImagePath("/path/to/image");
        dummyGoods.setImageName("imageName");
        dummyGoods.setPeriodType("periodType");
        dummyGoods.setPeriodTerm(30.0);
        dummyGoods.setPeriodExpireDate(DATE_STRING);
        dummyGoods.setDescription("description");
        dummyGoods.setSystem("goodsSystem");
        dummyGoods.setRemainingCount(100);
        dummyGoods.setType("type");
        dummyGoods.setCategories(Collections.singletonList(dummyCategory));
        dummyGoods.setBrand(dummyBrand);
        dummyGoods.setChoices(null);
        return dummyGoods;
    }

    public PublishModel createDummyPublishModel() {
        CustomerModel dummyCustomerModel = new CustomerModel();
        dummyCustomerModel.setId("customerId");
        dummyCustomerModel.setName("customerName");

        CampaignModel dummyCampaignModel = new CampaignModel();
        dummyCampaignModel.setId(1L);
        dummyCampaignModel.setName("campaignName");
        dummyCampaignModel.setStartDate(new Date());
        dummyCampaignModel.setEndDate(new Date());

        PublishDetailModel dummyPublishDetailModel = new PublishDetailModel();
        dummyPublishDetailModel.setId(1L);
        dummyPublishDetailModel.setMessageId("messageId");
        dummyPublishDetailModel.setMessageType(EnumMessageType.SMS.getValue());
        dummyPublishDetailModel.setPublishStatusCode(null);
        dummyPublishDetailModel.setVoucherId("voucherId");

        List<PublishDetailModel> publishDetailModelList = new ArrayList<>();
        publishDetailModelList.add(dummyPublishDetailModel);

        PublishModel dummyPublishModel = new PublishModel();
        dummyPublishModel.setId(1L);
        dummyPublishModel.setName("publishName");
        dummyPublishModel.setType(EnumPublishType.NORMAL);
        dummyPublishModel.setCampaign(dummyCampaignModel);
        dummyPublishModel.setCustomer(dummyCustomerModel);
        dummyPublishModel.setPublishDetails(publishDetailModelList);
        dummyPublishModel.setBooking(true);
        dummyPublishModel.setMessageSubject("subject");
        dummyPublishModel.setMessageContent("content");
        dummyPublishModel.setMessageCallingNumber("callingNumber");
        dummyPublishModel.setBookingDate(new Date());
        dummyPublishModel.setPublishDate(new Date());
        dummyPublishModel.setCancelDate(new Date());
        dummyPublishModel.setTestSend(false);
        dummyPublishModel.setReceiverNoDuplicateAllowed(true);
        dummyPublishModel.setSmsType(EnumMessageType.SMS);
        dummyPublishModel.setTemplateId("templateId");
        dummyPublishModel.setPublishStatusCode(EnumPublishStatus.PUBLISHING);
        dummyPublishModel.setSenderName("senderName");
        dummyPublishModel.setActivationId("activationId");
        dummyPublishModel.setActivationUrl("activationUrl");

        return dummyPublishModel;
    }

    public Voucher createDummyVoucherRequest(Goods goods) {
        Voucher voucher = new Voucher();

        voucher.setId("voucherId");
        voucher.setOriginalVoucherId("originalVoucherId");
        voucher.setTransferMessage("transferMessage");
        voucher.setShortLink("shortLink");
        voucher.setVoucherType("voucherType");
        voucher.setGoods(goods); // Assuming createDummyGoods() method exists
        voucher.setUserName("userName");
        voucher.setUserMobileNumber("userMobileNumber");
        voucher.setSubject("subject");
        voucher.setContent("content");
        voucher.setContentLink("contentLink");
        voucher.setContentImagePath("contentImagePath");
        voucher.setContentImageName("contentImageName");
        voucher.setExternalPinPassword("externalPinPassword");
        voucher.setUseInfo("useInfo");
        voucher.setSticker("sticker");
        voucher.setVoucherStatus(EnumVoucherStatus.NORMAL);
        voucher.setTransferStatus(null);
        voucher.setVoucherPrice(100.0);
        voucher.setDiscountRate(0.2);
        voucher.setDiscountLimitPrice(50.0);
        voucher.setInitialAmount(200.0);
        voucher.setBalance(150.0);
        voucher.setCreateDate(LocalDate.now().toString());
        voucher.setExpireDate(LocalDate.now().plusDays(30).toString());
        voucher.setPublishDate(LocalDate.now().plusDays(1).toString());
        voucher.setLastExchangeDate(LocalDate.now().plusDays(5).toString());
        voucher.setDisuseDate(LocalDate.now().plusDays(10).toString());
        voucher.setCancelDate(LocalDate.now().plusDays(7).toString());
        voucher.setTransferDate(LocalDate.now().plusDays(20).toString());
        voucher.setSystem("system");
        voucher.setExtPinId(100L);
        voucher.setExtPinNo("extPinNo");
        voucher.setParentVoucherToken("choiceToken");
        voucher.setParentVoucherEv("choiceVoucherEv");
        voucher.setPublishDetailId(1L);
        voucher.setSerialNo("serialNo");
        voucher.setActivationUrl("activationUrl");
        voucher.setActivationDate(new Date(System.currentTimeMillis()));
        voucher.setActivationId("activationId");
        voucher.setExtPinType("QRCODE"); // Use proper value here

        return voucher;
    }

    private static @NotNull SupplierModel createDummySupplierModel() {
        SupplierModel dummySupplierModel = new SupplierModel();
        dummySupplierModel.setId("supplierId");
        dummySupplierModel.setName("supplierName");
        dummySupplierModel.setDescription("supplierDescription");
        dummySupplierModel.setImgUrl("supplierImgUrl");
        return dummySupplierModel;
    }

    private static @NotNull BrandModel createDummyBrandModel(SupplierModel dummySupplierModel) {
        BrandModel dummyBrandModel = new BrandModel();
        dummyBrandModel.setId("brandId");
        dummyBrandModel.setName("brandName");
        dummyBrandModel.setDescription("brandDescription");
        dummyBrandModel.setImgUrl("brandImgUrl");
        dummyBrandModel.setIsPosLink(true);
        dummyBrandModel.setSystem("brandSystem");
        dummyBrandModel.setSupplier(dummySupplierModel);
        return dummyBrandModel;
    }

    private static @NotNull CategoryModel createDummyCategoryModel() {
        CategoryModel dummyCategoryModel = new CategoryModel();
        dummyCategoryModel.setId("categoryId");
        dummyCategoryModel.setName("categoryName");
        dummyCategoryModel.setDescription("categoryDescription");
        return dummyCategoryModel;
    }

    private static @NotNull GoodsModel createDummyGoodsModel(List<CategoryModel> categoryModelList, BrandModel dummyBrandModel) {
        GoodsModel dummyGoodsModel = new GoodsModel();
        dummyGoodsModel.setId(1L);
        dummyGoodsModel.setName("goodsName");
        dummyGoodsModel.setListPrice(100.0);
        dummyGoodsModel.setSellPrice(90.0);
        dummyGoodsModel.setSupplyDiscountRate(0.2);
        dummyGoodsModel.setSupplyDiscountAmount(20.0);
        dummyGoodsModel.setSupplyFeeRate(0.1);
        dummyGoodsModel.setSupplyVatInclude(true);
        dummyGoodsModel.setSupplyCalculateMethodCode("supplyMethodCode");
        dummyGoodsModel.setSellDiscountRate(0.1);
        dummyGoodsModel.setSendCost(10.0);
        dummyGoodsModel.setValid(true);
        dummyGoodsModel.setCategories(categoryModelList);
        dummyGoodsModel.setBrand(dummyBrandModel);
        return dummyGoodsModel;
    }

    public VoucherModel createDummyVoucherModel() {
        SupplierModel supplierModel = createDummySupplierModel();
        BrandModel brandModel = createDummyBrandModel(supplierModel);
        CategoryModel categoryModel = createDummyCategoryModel();
        GoodsModel goodsModel = createDummyGoodsModel(List.of(categoryModel), brandModel);
        VoucherModel voucherModel = new VoucherModel();

        voucherModel.setId("voucherId");
        voucherModel.setOriginalVoucherId("originalVoucherId");
        voucherModel.setTransferMessage("transferMessage");
        voucherModel.setPublishDetailId(1L);
        voucherModel.setGoods(goodsModel); // Assuming createDummyGoodsModel() method exists
        voucherModel.setShortLink("shortLink");
        voucherModel.setVoucherType(EnumVoucherType.PP); // Replace EnumVoucherType.CASH with the right enum value
        voucherModel.setUserName("userName");
        voucherModel.setUserMobileNumber("userMobileNumber");
        voucherModel.setSubject("subject");
        voucherModel.setContent("content");
        voucherModel.setContentLink("contentLink");
        voucherModel.setContentImageName("contentImageName");
        voucherModel.setContentImagePath("contentImagePath");
        voucherModel.setExternalPinPassword("externalPinPassword");
        voucherModel.setUseInfo("useInfo");
        voucherModel.setSticker("sticker");
        voucherModel.setVoucherStatus(EnumVoucherStatus.NORMAL);
        voucherModel.setTransferStatus(null);
        voucherModel.setVoucherPrice(100.0);
        voucherModel.setDiscountRate(0.2);
        voucherModel.setDiscountLimitPrice(50.0);
        voucherModel.setInitialAmount(1000.0);
        voucherModel.setBalance(900.0);
        voucherModel.setCreateDate(new Date());
        voucherModel.setExpireDate(new Date());
        voucherModel.setPublishDate(new Date());
        voucherModel.setLastExchangeDate(new Date());
        voucherModel.setDisuseDate(new Date());
        voucherModel.setCancelDate(new Date());
        voucherModel.setTransferDate(new Date());
        voucherModel.setSystem("system");
        voucherModel.setExtPinId(100L);
        voucherModel.setExtPinNo("extPinNo");
        voucherModel.setChoiceToken("choiceToken");
        voucherModel.setChoiceVoucherEv("choiceVoucherEv");
        voucherModel.setSmsType(EnumMessageType.SMS); // Replace EnumMessageType.TEXT with the right enum value
        voucherModel.setOutgoingRequest("outgoingRequest");
        voucherModel.setSerialNo("serialNo");
        voucherModel.setActivationUrl("activationUrl");
        voucherModel.setActivationDate(new Date());
        voucherModel.setActivationId("activationUrl");
        voucherModel.setExtPinType("extPinType");

        return voucherModel;
    }

    public RequestToPushAgent createDummyRequestToPA() {
        TemplateData templateData = new TemplateData();
        templateData.setCustomerName("Tai");
        templateData.setSender("Castis");
        templateData.setMessage("Hello from the other side");

        PublishMessage publishMessage = new PublishMessage();
        publishMessage.setPublishDetailId(123L);
        publishMessage.setReceiverMobileNumber("0123456789");
        publishMessage.setTemplateId("tempId");
        publishMessage.setTemplateData(templateData);
        publishMessage.setMessage("test message");
        publishMessage.setImageUrl("http://test/image/url");

        List<PublishMessage> publishMessageList = new ArrayList<>();
        publishMessageList.add(publishMessage);

        RequestToPushAgent requestToPushAgent = new RequestToPushAgent();
        requestToPushAgent.setPublishId(456L);
        requestToPushAgent.setCampaignId(789L);
        requestToPushAgent.setCampaignName("Campaign Name");
        requestToPushAgent.setSmsType(EnumMessageType.SMS.getValue());
        requestToPushAgent.setBrandName("Brand Name");
        requestToPushAgent.setTemplateId("Template Id");
        requestToPushAgent.setTotalCount(100);
        requestToPushAgent.setPublishMessageList(publishMessageList);
        return requestToPushAgent;
    }
}

package com.evoucher.adminapi.service;

import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import com.evoucher.evoucherbe.common.enums.PeriodType;
import com.evoucher.evoucherbe.common.enums.SystemType;
import com.evoucher.evoucherbe.common.enums.VoucherTypeCode;
import com.evoucher.evoucherbe.dto.EndUserDto;
import com.evoucher.evoucherbe.dto.GoodDto;
import com.evoucher.evoucherbe.dto.VoucherGenerateInfo;
import com.evoucher.evoucherbe.entity.*;
import com.evoucher.evoucherbe.exception.ChoiceVoucherProcessException;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.repository.*;
import com.evoucher.evoucherbe.service.*;
import com.evoucher.evoucherbe.service.remote.ShortURLService;
import com.evoucher.evoucherbe.service.request.ChildVoucherRequest;
import com.evoucher.evoucherbe.service.request.PublishDetailRequest;
import com.evoucher.evoucherbe.service.request.PublishRequest;
import com.evoucher.evoucherbe.service.typed.BalancePayingService;
import com.evoucher.evoucherbe.service.typed.LockingService;
import com.evoucher.evoucherbe.service.typed.ServiceFactory;
import com.evoucher.evoucherbe.service.typed.system.ExternalTypeService;
import com.evoucher.evoucherbe.service.typed.system.InternalService;
import com.evoucher.evoucherbe.service.typed.system.SystemBridgeService;
import com.evoucher.evoucherbe.service.typed.type.SITypeService;
import com.evoucher.evoucherbe.utils.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.evoucher.adminapi.config.TestDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EVoucherServiceTest {
    @InjectMocks
    private EVoucherServiceImpl eVoucherService;

    @Mock
    private EVoucherRepository eVoucherRepository;
    @Mock
    private GoodsRepository goodsRepository;
    @Mock
    private PublishRepository publishRepository;
    @Mock
    private PublishDetailRepository publishDetailRepository;
    @Mock
    private EndUserRepository endUserRepository;
    @Mock
    private CustomerContractRepository customerContractRepository;
    @Mock
    private SupplierContractRepository supplierContractRepository;
    @Mock
    private ExternalPinRepository externalPinRepository;
    @Mock
    private SettlementLogRepository settlementLogRepository;
    @Mock
    private VoucherServiceCommon voucherServiceCommon;
    @Mock
    private VoucherChoiceHistory voucherChoiceHistory;
    @Mock
    private ShortURLService shortURLService;
    @Mock
    private VoucherChoiceHistoryService voucherChoiceHistoryService;
    @Mock
    private ServiceFactory integratedPinServiceFactory;
    @Mock
    private ExternalTypeService externalTypeService;
    @Mock
    private LockingService lockingService;
    @Mock
    private PublishBasicService publishBasicService;
    @Mock
    private PublishDetailBasicService publishDetailBasicService;
    @Mock
    private EndUserBasicService endUserBasicService;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private GoodBasicService goodService;
    @Mock
    private VoucherBasicService voucherBasicService;
    @Mock
    private ServiceFactory serviceFactory;
    @Mock
    private InternalService internalService;
    @Mock
    private SystemBridgeService systemBridgeService;
    @Mock
    private SITypeService siTypeService;
    @Mock
    private BalancePayingService balancePayingService;

    @Test
    void test_GenerateEachVoucher_PublishStatusIsNotPublish() {
        PublishRequest publishRequest = generateDummyPublishRequest();
        Publish publish = generateDummyPublish();


        when(publishBasicService.findById(DEFAULT_PUBLISH_ID)).thenReturn(publish);

        assertThrows(CustomCodeException.class, () -> {
            eVoucherService.generateEachVoucher(publishRequest);
        }, "Should throw BadRequestException when publish status is NOT PUBLISHING");
        verify(publishBasicService).findById(DEFAULT_PUBLISH_ID);
    }

    @Test
    void test_GenerateEachVoucher_Internal_Success() {
        PublishRequest publishRequest = generateDummyPublishRequest();
        Publish publish = generateDummyPublish();
        publish.setPublishStatusCode("PUBLISHING");
        GoodDto goodDto = generateDummyGoodDto();
        CustomerContract customerContract = generateDummyCustomerContract();
        SupplierContract supplierContract = generateDummySupplierContract();
        PublishDetail publishDetail1 = generateDummyPublishDetail(DEFAULT_PUBLISH_DETAIL_ID);
        PublishDetail publishDetail2 = generateDummyPublishDetail(DEFAULT_PUBLISH_DETAIL_ID + 1);
        EndUserDto endUser = generateDummyEndUserDto();
        Brand brand = generateDummyBrand();


        when(publishBasicService.findById(DEFAULT_PUBLISH_ID)).thenReturn(publish);
        when(goodService.toDto(any())).thenReturn(goodDto);
        when(customerContractRepository.findByIdAndValidYn(DEFAULT_CUSTOMER_CONTRACT_ID, EnumValidYn.Y)).thenReturn(Optional.ofNullable(customerContract));
        when(supplierContractRepository.findByIdAndValidYn(DEFAULT_SUPPLIER_CONTRACT_ID, EnumValidYn.Y)).thenReturn(Optional.ofNullable(supplierContract));
        when(publishDetailBasicService.findById(DEFAULT_PUBLISH_DETAIL_ID)).thenReturn(publishDetail1);
        when(publishDetailBasicService.findById(DEFAULT_PUBLISH_DETAIL_ID + 1)).thenReturn(publishDetail2);
        when(endUserBasicService.findDtoById(DEFAULT_END_USER_ID)).thenReturn(endUser);
        when(brandRepository.findByIdAndValidYn(DEFAULT_BRAND_ID, EnumValidYn.Y)).thenReturn(Optional.ofNullable(brand));
        when(shortURLService.createShortURLForEVoucher(anyString())).thenReturn(DEFAULT_SHORT_URL);

        eVoucherService.generateEachVoucher(publishRequest);
        verify(publishDetailBasicService, times(2)).findById(anyInt());
        // verify vouchers are saved
        verify(eVoucherRepository).saveAll(anyList());
        // verify voucher histories are saved
        verify(settlementLogRepository).saveAll(anyList());
    }

    @Test
    void test_CreateVoucher_Internal_Success() {
        PublishDetailRequest publishDetailRequest = generateDummyPublishDetailRequest(DEFAULT_PUBLISH_DETAIL_ID);
        Campaign campaign = generateDummyCampaign();
        Publish publish = generateDummyPublish();
        publish.setPublishStatusCode("PUBLISHING");
        GoodDto goodDto = generateDummyGoodDto();
        CustomerContract customerContract = generateDummyCustomerContract();
        SupplierContract supplierContract = generateDummySupplierContract();
        PublishDetail publishDetail = generateDummyPublishDetail(DEFAULT_PUBLISH_DETAIL_ID);
        EndUserDto endUser = generateDummyEndUserDto();
        Brand brand = generateDummyBrand();
        VoucherGenerateInfo voucherGenerateInfo = new VoucherGenerateInfo();

        when(publishDetailBasicService.findById(DEFAULT_PUBLISH_DETAIL_ID)).thenReturn(publishDetail);
        when(endUserBasicService.findDtoById(DEFAULT_END_USER_ID)).thenReturn(endUser);
        when(brandRepository.findByIdAndValidYn(DEFAULT_BRAND_ID, EnumValidYn.Y)).thenReturn(Optional.ofNullable(brand));
        when(shortURLService.createShortURLForEVoucher(anyString())).thenReturn(DEFAULT_SHORT_URL);

        eVoucherService.createVoucher(campaign, publish, customerContract, supplierContract, goodDto, publishDetailRequest,
                voucherGenerateInfo);

        // Internal voucher will not trigger external pin call
        verify(externalPinRepository, never()).findById(anyLong());
        EVoucher resultVoucher = voucherGenerateInfo.getEVouchers().get(0);

        assertNotNull(resultVoucher);
        // Always v2 for all vouchers from now on
        assertEquals(Constant.Common.VERSION_2, resultVoucher.getVoucherVersion());
        assertNull(resultVoucher.getActivationUrl());
        assertNull(resultVoucher.getActivationId());
        // Serial number must be created
        assertNotNull(resultVoucher.getSerialNo());
        // Serial number length must be equal to brand's serial number total length
        assertEquals(brand.getSerialNumberTotalLength(), resultVoucher.getSerialNo().length());
        // Serial number must start with brand's serial number prefix
        assertTrue(resultVoucher.getSerialNo().startsWith(brand.getSerialNumberPrefix()));
        // External pin id and no must be null for internal vouchers
        assertNull(resultVoucher.getExternalPinId());
        assertNull(resultVoucher.getExternalPinNo());

        // Brand's external pin type must be used for internal vouchers
        assertEquals(brand.getDisplayType(), resultVoucher.getExternalPinType().name());
    }

    @Test
    void test_CreateVoucher_Choice_Success() {
        PublishDetailRequest publishDetailRequest = generateDummyPublishDetailRequest(DEFAULT_PUBLISH_DETAIL_ID);
        GoodDto goodDto = generateDummyGoodDto();
        goodDto.setSystem(SystemType.CHOICE);
        Goods goods = generateDummyGoods();
        goods.setSystem(SystemType.CHOICE);

        Campaign campaign = generateDummyCampaign();
        Publish publish = generateDummyPublish();
        publish.setPublishStatusCode("PUBLISHING");
        publish.setGoods(goods);

        CustomerContract customerContract = generateDummyCustomerContract();
        SupplierContract supplierContract = generateDummySupplierContract();
        PublishDetail publishDetail = generateDummyPublishDetail(DEFAULT_PUBLISH_DETAIL_ID);
        EndUserDto endUser = generateDummyEndUserDto();
        Brand brand = generateDummyBrand();
        brand.setSystem(SystemType.CHOICE);
        VoucherGenerateInfo voucherGenerateInfo = new VoucherGenerateInfo();

        when(publishDetailBasicService.findById(DEFAULT_PUBLISH_DETAIL_ID)).thenReturn(publishDetail);
        when(endUserBasicService.findDtoById(DEFAULT_END_USER_ID)).thenReturn(endUser);
        when(brandRepository.findByIdAndValidYn(DEFAULT_BRAND_ID, EnumValidYn.Y)).thenReturn(Optional.ofNullable(brand));
        when(shortURLService.createShortURLForEVoucher(anyString())).thenReturn(DEFAULT_SHORT_URL);

        eVoucherService.createVoucher(campaign, publish, customerContract, supplierContract, goodDto, publishDetailRequest,
                voucherGenerateInfo);

        // Choice voucher will not trigger external pin call
        verify(externalPinRepository, never()).findById(anyLong());
        EVoucher resultVoucher = voucherGenerateInfo.getEVouchers().get(0);

        assertNotNull(resultVoucher);
        // Always v2 for all vouchers from now on
        assertEquals(Constant.Common.VERSION_2, resultVoucher.getVoucherVersion());
        assertNull(resultVoucher.getActivationUrl());
        assertNull(resultVoucher.getActivationId());
        // Serial number must be created
        assertNotNull(resultVoucher.getSerialNo());
        // Serial number length must be equal to brand's serial number total length
        assertEquals(brand.getSerialNumberTotalLength(), resultVoucher.getSerialNo().length());
        // Serial number must start with brand's serial number prefix
        assertTrue(resultVoucher.getSerialNo().startsWith(brand.getSerialNumberPrefix()));
        // External pin id and no must be null for internal vouchers
        assertNull(resultVoucher.getExternalPinId());
        assertNull(resultVoucher.getExternalPinNo());

        // Brand's external pin type must be used for internal vouchers
        assertNull(resultVoucher.getExternalPinType());

        // Choice related
        assertNull(resultVoucher.getParentVoucherToken());
    }

    @Test
    void test_CreateVoucher_External_Success() {
        PublishDetailRequest publishDetailRequest = generateDummyPublishDetailRequest(DEFAULT_PUBLISH_DETAIL_ID);
        Campaign campaign = generateDummyCampaign();
        Publish publish = generateDummyPublish();
        publish.setPublishStatusCode("PUBLISHING");

        GoodDto goodDto = generateDummyGoodDto();
        // External system type
        goodDto.setSystem(SystemType.EXTERNAL);

        ExternalPin externalPin = generateDummyExternalPin();

        CustomerContract customerContract = generateDummyCustomerContract();
        SupplierContract supplierContract = generateDummySupplierContract();

        PublishDetail publishDetail = generateDummyPublishDetail(DEFAULT_PUBLISH_DETAIL_ID);
        publishDetail.setExternalPinId(DEFAULT_EXTERNAL_PIN_ID);

        EndUserDto endUser = generateDummyEndUserDto();
        Brand brand = generateDummyBrand();
        VoucherGenerateInfo voucherGenerateInfo = new VoucherGenerateInfo();

        when(publishDetailBasicService.findById(DEFAULT_PUBLISH_DETAIL_ID)).thenReturn(publishDetail);
        when(externalPinRepository.findById(DEFAULT_EXTERNAL_PIN_ID)).thenReturn(Optional.ofNullable(externalPin));
        when(endUserBasicService.findDtoById(DEFAULT_END_USER_ID)).thenReturn(endUser);
        when(brandRepository.findByIdAndValidYn(DEFAULT_BRAND_ID, EnumValidYn.Y)).thenReturn(Optional.ofNullable(brand));
        when(shortURLService.createShortURLForEVoucher(anyString())).thenReturn(DEFAULT_SHORT_URL);

        eVoucherService.createVoucher(campaign, publish, customerContract, supplierContract, goodDto, publishDetailRequest,
                voucherGenerateInfo);

        // External pin repository must be called
        verify(externalPinRepository).findById(anyLong());
        EVoucher resultVoucher = voucherGenerateInfo.getEVouchers().get(0);

        assertNotNull(resultVoucher);
        // Always v2 for all vouchers from now on
        assertEquals(Constant.Common.VERSION_2, resultVoucher.getVoucherVersion());
        assertNull(resultVoucher.getActivationUrl());
        assertNull(resultVoucher.getActivationId());
        // Serial number must be created
        assertNotNull(resultVoucher.getSerialNo());
        // Serial number length must be equal to brand's serial number total length
        assertEquals(brand.getSerialNumberTotalLength(), resultVoucher.getSerialNo().length());
        // Serial number must start with brand's serial number prefix
        assertTrue(resultVoucher.getSerialNo().startsWith(brand.getSerialNumberPrefix()));
        // External pin id and no must present for external vouchers
        assertEquals(externalPin.getId(), resultVoucher.getExternalPinId());
        assertEquals(externalPin.getExternalPinNo(), resultVoucher.getExternalPinNo());

        // External pin's expire time must be used for external vouchers
        assertEquals(externalPin.getExpireTime(), resultVoucher.getExpirationDate());

        // Brand's external pin type must be used for internal vouchers
        assertEquals(brand.getDisplayType(), resultVoucher.getExternalPinType().name());
        assertNotNull(resultVoucher.getExternalPinId());
        assertNotNull(resultVoucher.getExternalPinNo());

        // Brand's external pin type must be used for internal vouchers
        assertEquals(brand.getDisplayType(), resultVoucher.getExternalPinType().name());
    }

    @Test
    void test_CreateChoiceVoucherV2_WhenGoodNotFound_ThenThrowEntityNotFoundException() {
        ChildVoucherRequest request = generateDummyChildVoucherRequest();
        EVoucher voucher = generateDummyEVoucher();

        when(voucherBasicService.findById(anyString())).thenReturn(voucher);
        when(goodService.getGoodMapByIdIn(anyList())).thenReturn(new HashMap<>());
        when(goodService.getNotFoundException(DEFAULT_GOODS_ID))
                .thenThrow(new EntityNotFoundException("Can not find good by id: " + DEFAULT_GOODS_ID, com.evoucher.evoucherbe.utils.ErrorCode.GOOD_NOT_FOUND));

        assertThrows(EntityNotFoundException.class, () -> eVoucherService.processCreateChildVouchersV2(request));
    }

    @Test
    void test_CreateChoiceVoucherV2_WhenBalanceNotEnough_ThenThrowChoiceVoucherProcessException() {
        ChildVoucherRequest request = generateDummyChildVoucherRequest();
        EVoucher voucher = generateDummyEVoucher();
        Map<Long, GoodDto> goodMap = new HashMap<>();
        // Default good price = 90, buy 3 items
        // Default balance = 100
        goodMap.put(DEFAULT_GOODS_ID, generateDummyGoodDto(DEFAULT_GOODS_ID));
        goodMap.put(DEFAULT_GOODS_ID + 1, generateDummyGoodDto(DEFAULT_GOODS_ID + 1));

        when(voucherBasicService.findById(anyString())).thenReturn(voucher);
        when(goodService.getGoodMapByIdIn(anyList())).thenReturn(goodMap);
        assertThrows(ChoiceVoucherProcessException.class, () -> eVoucherService.processCreateChildVouchersV2(request));
    }

    @Test
    void test_CreateChoiceVoucherV2_InternalChild_Success() {
        ChildVoucherRequest request = generateDummyChildVoucherRequest();
        EVoucher parentVoucher = generateDummyEVoucher();
        // Default balance = 100
        parentVoucher.setBalance(900.0);
        Map<Long, GoodDto> goodMap = new HashMap<>();
        // Default good price = 90, buy 3 items
        goodMap.put(DEFAULT_GOODS_ID, generateDummyGoodDto(DEFAULT_GOODS_ID));
        goodMap.put(DEFAULT_GOODS_ID + 1, generateDummyGoodDto(DEFAULT_GOODS_ID + 1));
        Publish publish = generateDummyPublish();
        PublishDetail publishDetail = generateDummyPublishDetail(DEFAULT_PUBLISH_DETAIL_ID);
        EndUserDto endUser = generateDummyEndUserDto();
        CustomerContract customerContract = generateDummyCustomerContract();
        SupplierContract supplierContract = generateDummySupplierContract();

        when(serviceFactory.getGoodServiceByType(any(SystemType.class))).thenReturn(internalService);
        when(serviceFactory.getGoodTypeServiceByType(any(VoucherTypeCode.class))).thenReturn(siTypeService);
        when(siTypeService.getPayingService()).thenReturn(balancePayingService);
        doNothing().when(internalService).validateGoodExpiredDate(any(), any(), any(), any());
        doNothing().when(balancePayingService).doPaying(any(), any());


        when(voucherBasicService.findById(anyString())).thenReturn(parentVoucher);
        when(goodService.getGoodMapByIdIn(anyList())).thenReturn(goodMap);
        when(publishBasicService.findById(anyInt())).thenReturn(publish);
        when(publishDetailBasicService.findById(anyInt())).thenReturn(publishDetail);
        when(endUserBasicService.findDtoById(anyLong())).thenReturn(endUser);
        when(customerContractRepository.findByIdAndValidYn(anyInt(), eq(EnumValidYn.Y))).thenReturn(Optional.ofNullable(customerContract));
        when(supplierContractRepository.findByIdAndValidYn(anyInt(), eq(EnumValidYn.Y))).thenReturn(Optional.ofNullable(supplierContract));
        // INTERNAL child voucher
        when(serviceFactory.isThirdPartyType(any(SystemType.class))).thenReturn(false);
        when(shortURLService.createShortURLForEVoucher(anyString())).thenReturn(DEFAULT_SHORT_URL);
        when(brandRepository.findByIdAndValidYn(anyString(), eq(EnumValidYn.Y))).thenReturn(Optional.ofNullable(generateDummyBrand()));

        eVoucherService.processCreateChildVouchersV2(request);
        verify(voucherServiceCommon).saveInformationOfVoucherChoice(any(VoucherGenerateInfo.class), any(EVoucher.class));
    }
}

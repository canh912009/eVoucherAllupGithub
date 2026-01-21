package com.castis.publishservice.service;

import com.castis.publishservice.dto.BrandDTO;
import com.castis.publishservice.dto.GoodsDTO;
import com.castis.publishservice.dto.PurchaseChildRequest;
import com.castis.publishservice.dto.request.ChoiceChosenItem;
import com.castis.publishservice.config.TestDataGenerator;
import com.castis.publishservice.entity.Voucher;
import com.castis.publishservice.exception.defineException.CustomCodeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcessServiceTest {
    @Mock
    private GoodsService goodsService;
    @Mock
    private BrandService brandService;
    @Mock
    private VoucherBasicService voucherBasicService;

    @InjectMocks
    private ProcessService processService;

    @Test
    public void ProcessService_validateChoicePurchase_ThrowVoucherExpiredException() throws ParseException {
        Voucher voucher = Voucher.builder()
                .expirationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-05-28"))
                .build();

        CustomCodeException exception = Assertions.assertThrows(CustomCodeException.class, () -> {
            processService.validateChoiceVoucherParent(voucher);
        });

        Assertions.assertEquals("Parent voucher is expired.", exception.getMessage());
    }

    @Test
    public void ProcessService_validateChoicePurchase_ThrowEmptyChoiceDataException() {

        List<ChoiceChosenItem> items = new ArrayList<>();

        CustomCodeException exception = Assertions.assertThrows(CustomCodeException.class, () -> {
            processService.validateChoicePurchase(items);
        });

        Assertions.assertEquals("Voucher purchase data is empty.", exception.getMessage());
    }

    @Test
    public void ProcessService_validateChoicePurchase_ThrowNullChoiceDataException() {


        CustomCodeException exception = Assertions.assertThrows(CustomCodeException.class, () -> {
            processService.validateChoicePurchase(null);
        });

        Assertions.assertEquals("Voucher purchase data is empty.", exception.getMessage());
    }
    @Test
    public void ProcessService_validateChoicePurchase_ThrowNullGoodIdException() {

        ChoiceChosenItem item1 = ChoiceChosenItem.builder()
                .quantity(5)
                .build();

        CustomCodeException exception = Assertions.assertThrows(CustomCodeException.class, () -> {
            processService.validateChoicePurchase(List.of(item1));
        });

        Assertions.assertEquals("Good id can not be null.", exception.getMessage());
    }
    @Test
    public void ProcessService_validateChoicePurchase_ThrowInactiveGoodIdException() {

        ChoiceChosenItem item1 = ChoiceChosenItem.builder()
                .goodsId(1L)
                .quantity(5)
                .build();

        List<GoodsDTO> inactiveGoods = List.of(
                GoodsDTO.builder()
                        .id(1L)
                        .validYn("N")
                        .build()
        );

        when(goodsService.findAllByIdIn(List.of(1L))).thenReturn(inactiveGoods);

        CustomCodeException exception = Assertions.assertThrows(CustomCodeException.class, () -> {
            processService.validateChoicePurchase(List.of(item1));
        });

        Assertions.assertEquals("Can not buy inactive good.", exception.getMessage());
    }
    @Test
    public void ProcessService_validateChoicePurchase_ThrowInactiveBrandException() {

        ChoiceChosenItem item1 = ChoiceChosenItem.builder()
                .goodsId(1L)
                .quantity(5)
                .build();

        List<GoodsDTO> activeGoods = List.of(
                GoodsDTO.builder()
                        .id(1L)
                        .validYn("Y")
                        .brandId("1")
                        .build()
        );

        List<BrandDTO> inactiveBrand = List.of(
                BrandDTO.builder()
                        .id("1")
                        .validYn("N")
                        .build()
        );

        when(goodsService.findAllByIdIn(List.of(1L))).thenReturn(activeGoods);
        when(brandService.findByIdIn(Set.of("1"))).thenReturn(inactiveBrand);

        CustomCodeException exception = Assertions.assertThrows(
                CustomCodeException.class,
                () -> processService.validateChoicePurchase(List.of(item1)));

        Assertions.assertEquals("Can not buy good of inactive brand.", exception.getMessage());
    }

    @Test
    void Test_ChooseChoiceItemV2_WhenVoucherIsExpired_ThenThrowException() {
        Voucher parentVoucher = TestDataGenerator.generateDummyVoucher();
        // Set date to yesterday to make it expired
        parentVoucher.setExpirationDate(Date.from(
                LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        PurchaseChildRequest request = TestDataGenerator.generateDummyPurchaseChildRequest();

        when(voucherBasicService.findById(request.getParentVoucherId())).thenReturn(parentVoucher);

        CustomCodeException exception = Assertions.assertThrows(
                CustomCodeException.class,
                () -> processService.chooseChoiceItemV2(request));

        Assertions.assertEquals("Parent voucher is expired.", exception.getMessage());
    }
}

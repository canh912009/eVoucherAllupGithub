package asia.castis.evoucher.api.service.generator;

import asia.castis.evoucher.api.common.TestUtils;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.repository.VnptGoodsRepository;
import asia.castis.evoucher.api.repository.VnptVoucherExchangeHistoryRepository;
import asia.castis.evoucher.api.service.generator.impl.VnptResponseGeneratorImpl;
import asia.castis.evoucher.api.utils.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VnptResponseGeneratorImplTest {
    @InjectMocks
    VnptResponseGeneratorImpl responseGenerator;

    @Mock
    VnptGoodsRepository vnptGoodsRepository;
    @Mock
    VnptVoucherExchangeHistoryRepository exchangeHistoryRepository;


    @Test
    void testGetPurchasedVnptResponse_NoExchangeHistoryFound_ThrowError() {
        when(exchangeHistoryRepository.findByEvOrderByExchangeDateDesc(TestUtils.EV)).thenReturn(new ArrayList<>());
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> responseGenerator.getPurchasedVnptResponse(TestUtils.EV, TestUtils.GOODS_ID));
        assertEquals(exception.getCode(), ErrorCode.INVALID_VOUCHER_STATUS);
    }
}

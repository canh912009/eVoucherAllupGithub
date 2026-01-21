package asia.castis.evoucher.api.service.generator;

import asia.castis.evoucher.api.common.Encryption;
import asia.castis.evoucher.api.common.enums.EnumValidYn;
import asia.castis.evoucher.api.dto.response.BulkBrandResponse;
import asia.castis.evoucher.api.dto.response.BulkGoodsResponse;
import asia.castis.evoucher.api.entity.*;
import asia.castis.evoucher.api.mapper.VoucherMapper;
import asia.castis.evoucher.api.repository.*;
import asia.castis.evoucher.api.service.generator.impl.VoucherResponseGeneratorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static asia.castis.evoucher.api.common.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoucherResponseGeneratorImplTest {

    @InjectMocks
    VoucherResponseGeneratorImpl responseGenerator;
    @Mock
    VoucherRepository voucherRepository;
    @Mock
    PublishRepository publishRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    GoodsRepository goodsRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    VoucherMapper voucherMapper;
    @Mock
    Encryption encryption;
    @Mock
    BrandRepository brandRepository;
    @Mock
    SupplierRepository supplierRepository;
    @Mock
    BulkCategoryRepository bulkCategoryRepository;

//    @Test
//    void testGetVoucher() {
//        EVoucher dbVoucher = getDbVoucher();
//        Publish dbPublish = getDbPublish();
//
//        when(voucherRepository.findById(EV)).thenReturn(Optional.of(dbVoucher));
//        when(publishRepository.findById(PUBLISH_ID)).thenReturn(Optional.of(dbPublish));
//        responseGenerator.getVoucherResponse(EV);
//
//        verify(voucherRepository, times(1)).findById(EV);
//    }

}

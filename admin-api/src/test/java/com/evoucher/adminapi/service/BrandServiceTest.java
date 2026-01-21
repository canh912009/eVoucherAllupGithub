package com.evoucher.adminapi.service;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.BrandRepository;
import com.evoucher.adminapi.cms.dao.SupplierRepository;
import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.Supplier;
import com.evoucher.adminapi.cms.mapper.BrandMapper;
import com.evoucher.adminapi.cms.service.BrandServiceImpl;
import com.evoucher.adminapi.cms.service.models.BrandDTO;
import com.evoucher.adminapi.cms.service.models.request.BrandRequest;
import com.evoucher.adminapi.cms.utils.CmsConstant;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.utils.Constant;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.evoucher.adminapi.service.DataGenerator.*;
import static com.evoucher.adminapi.service.TestConstants.DEFAULT_APP_ID;
import static com.evoucher.adminapi.service.TestConstants.DEFAULT_SUPPLIER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {
    @InjectMocks
    private BrandServiceImpl brandService;
    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper mapper;
    @Mock
    private SupplierRepository supplierRepository;

    //    @Test
    void test_CreateBrand_Success() {
        // Given
        BrandRequest brandRequest = createDummyBrandRequest();
        Brand brand = createDummyBrand();
        brand.setId("SUPP001-005");

        Supplier supplier = generateDummySupplier();
        UserPrincipal userPrincipal = UserPrincipal.builder().build();
        userPrincipal.setAdminType(EnumRole.ROLE_ADMIN.getValue());

        // When
        when(brandRepository.findByAppId(DEFAULT_APP_ID)).thenReturn(Optional.empty());
        when(brandRepository.findAllBrandIdByBrandIdOrderByIdASC(DEFAULT_SUPPLIER_ID + CmsConstant.UNDERSCORE_SYMBOL + Constant.Common.REGEX_SEARCH_SYMBOL))
                .thenReturn(List.of("SUPP001-001", "SUPP001-002", "SUPP001-003", "SUPP001-004"));
        when(mapper.toBrand(brandRequest)).thenReturn(brand);
        when(brandRepository.save(brand)).thenReturn(brand);
        when(supplierRepository.findByIdAndValidYn(brandRequest.getSupplierId(), EnumValidYn.Y)).thenReturn(Optional.of(supplier));

        BrandDTO result = brandService.createBrand(brandRequest);

        assertEquals("SUPP001-005", result.getId());
    }


}

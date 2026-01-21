package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.cms.dao.models.BulkBrand;
import com.evoucher.adminapi.cms.service.models.BrandDTO;
import com.evoucher.adminapi.cms.service.models.BulkBrandDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.request.BrandRequest;
import com.evoucher.adminapi.cms.service.models.SearchBrandResponse;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.SystemType;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface BrandService {

    BrandDTO findDtoById(String id);
    List<BrandDTO> findAllBySystemAndValid(SystemType systemType, EnumValidYn valid);

    BrandDTO createBrand(BrandRequest brandRequest);

    BrandDTO updateBrand(String id, BrandRequest brandRequest);

    String deleteBrandById(String id);

    Page<? extends SearchBrandResponse> searchBrandDTO(FilterSearchCms filterSearchCms);
    public void validateExistByIdIn(Collection<String> ids) throws EntityNotFoundException;
    BulkBrandDTO toDto(BulkBrand entity);
}

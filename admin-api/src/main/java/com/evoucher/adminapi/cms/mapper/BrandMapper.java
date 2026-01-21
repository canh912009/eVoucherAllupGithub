package com.evoucher.adminapi.cms.mapper;

import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.BulkBrand;
import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.dao.models.Store;
import com.evoucher.adminapi.cms.service.models.BrandDTO;
import com.evoucher.adminapi.cms.service.models.BrandWithCategorySearchResponse;
import com.evoucher.adminapi.cms.service.models.BulkBrandDTO;
import com.evoucher.adminapi.cms.service.models.SearchBrandResponse;
import com.evoucher.adminapi.cms.service.models.request.BrandRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(builder = @Builder(disableBuilder = true))
public interface BrandMapper {
    BrandMapper INSTANT = Mappers.getMapper(BrandMapper.class);

    BrandDTO toBrandDTO(Brand brand);

    @Mapping(source = "listGoods" ,target = "listGoods")
    @Mapping(source = "stores" ,target = "stores")
    BrandDTO toBrandDTO(Brand brand, List<Goods> listGoods, List<Store> stores);

    Brand toBrand(BrandDTO searchBrandResponse);

    @Mapping(source = "brandLogoPath", target = "brandLogoPath")
    @Mapping(source = "brandLogoName", target = "brandLogoName")
    Brand toBrand(BrandRequest brandRequest);

    List<BrandDTO> toListBrandDTO(List<Brand> brands);
    SearchBrandResponse toSearchResponse(Brand entity);
    BrandWithCategorySearchResponse toBrandWithCategory(Brand entity);
    BulkBrandDTO toDTO(BulkBrand entity);
    BulkBrand toEntity(BulkBrandDTO dTO);
    @Mapping(source = "authenticationKey", target = "authenticationKey")
    @Mapping(source = "encryptionKey", target = "encryptionKey")
    @Mapping(source = "supplierId", target = "supplierId")
    @BeanMapping(ignoreByDefault = true)
    void keepOldProperties(@MappingTarget Brand target, Brand source);
}

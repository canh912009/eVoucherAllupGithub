package com.castis.pos_api.service.validator;

import com.castis.pos_api.entity.Brand;
import com.castis.pos_api.entity.Goods;
import com.castis.pos_api.entity.Store;
import com.castis.pos_api.enum_constant.EnumValidYn;
import com.castis.pos_api.exception.ApplicationException;
import com.castis.pos_api.repositories.BrandRepository;
import com.castis.pos_api.repositories.GoodsRepository;
import com.castis.pos_api.repositories.StoreRepository;
import com.castis.pos_api.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BrandValidator {
    private final BrandRepository brandRepository;
    private final StoreRepository storeRepository;
    private final GoodsRepository goodsRepository;

    public String validateBrandRelatedReturnBrandId(Brand brandByAppId, String brandId, String storeId, Long goodId) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new ApplicationException(CustomResponse.E4404_NOT_FOUND.getCode(), "Brand not found"));
        if (!brandByAppId.getId().equals(brandId)) {
            throw new ApplicationException(CustomResponse.E4421_INVALID_BRAND_STATE.getCode(), "Brand by appId is not match with the submitted brand");
        }
        if (brand.getValidYn().name().equals(EnumValidYn.N.name())) {
            throw new ApplicationException(CustomResponse.E4421_INVALID_BRAND_STATE.getCode(), "Brand is not valid");
        }

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new ApplicationException(CustomResponse.E4404_NOT_FOUND.getCode(), "Store not found"));
        if (store.getValidYn().name().equals(EnumValidYn.N.name())) {
            throw new ApplicationException(CustomResponse.E4431_INVALID_STORE_STATE.getCode(), "Store is not valid");
        }
        if (!store.getBrandId().equals(brandId)) {
            throw new ApplicationException(CustomResponse.E4431_INVALID_STORE_STATE.getCode(), "Brand and store do not match");
        }
        Goods goods = goodsRepository.findById(goodId).orElseThrow(() -> new ApplicationException(CustomResponse.E4404_NOT_FOUND.getCode(), "Goods not found"));
        if (goods.getValidYn().name().equals(EnumValidYn.N.name())) {
            throw new ApplicationException(CustomResponse.E4441_INVALID_GOODS_STATE.getCode(), "Product is not valid");
        }
//        if (!goods.getBrand().getId().equals(brandId)) {
//            throw new ApplicationException(CustomResponse.E4441_INVALID_GOODS_STATE.getCode(), "Product and brand do not match");
//        }
        return brand.getId();
    }
}

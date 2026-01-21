package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.common.enums.EnumValidYn;
import asia.castis.evoucher.api.dto.response.StoreResponse;
import asia.castis.evoucher.api.entity.Goods;
import asia.castis.evoucher.api.entity.Store;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.repository.GoodsRepository;
import asia.castis.evoucher.api.repository.StoreRepository;
import asia.castis.evoucher.api.service.StoreService;
import asia.castis.evoucher.api.service.generator.StoreResponseGenerator;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreResponseGenerator storeResponseGenerator;
    private final GoodsRepository goodsRepository;


    @Override
    public List<StoreResponse> searchStoreByBrandId(String brandId) {
        if (Objects.isNull(brandId) || brandId.isEmpty()) {
            throw new ApplicationException("Null or empty brandId", ErrorCode.STORE_NOT_FOUND);
        }
        return storeRepository.findByBrandId(brandId).stream()
                .filter(store -> EnumValidYn.Y.equals(store.getValidYn()))
                .map(storeResponseGenerator::getStoreResponse).collect(Collectors.toList());
    }

    @Override
    public StoreResponse findById(String id) {
        if (Objects.isNull(id) || id.isBlank()) {
            throw new ApplicationException(ResponseString.STORE_ID_BLANK, ErrorCode.STORE_NOT_FOUND);
        }
        Store store = storeRepository.findById(id).orElseThrow(() -> new ApplicationException(ResponseString.STORE_NOT_FOUND, ErrorCode.STORE_NOT_FOUND));
        return storeResponseGenerator.getStoreResponse(store);
    }

    @Override
    public List<StoreResponse> searchStoreByGoodsId(Integer goodsId) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new ApplicationException(ResponseString.CAN_NOT_FIND_GOODS, ErrorCode.CAN_NOT_FIND_GOODS));

        switch (goods.getStoreQueryType()) {
            case EXCLUDE:
                String exceptStoreIds = goods.getExceptStoreIds();
                if (Objects.isNull(exceptStoreIds) || exceptStoreIds.isEmpty()) {
                    log.error("EXCLUDE - Except stores is null, return all stores under brand {}", goods.getBrandId());
                    return searchStoreByBrandId(goods.getBrandId());
                }

                log.info("EXCLUDE - Get all stores belong to brand {}, except {}", goods.getBrandId(), exceptStoreIds);
                return searchStoreByBrandId(goods.getBrandId()).stream()
                        .filter(storeResponse -> !exceptStoreIds.contains(storeResponse.getStoreId()))
                        .collect(Collectors.toList());
            case INCLUDE:
                // Get all stores from goods's include stores
                String includeStoreIds = goods.getIncludeStoreIds();

                if (Objects.isNull(includeStoreIds) || includeStoreIds.isEmpty()) {
                    log.error("INCLUDE - Includes stores is null, return empty store list");
                    return List.of();
                }

                log.info("INCLUDE - Get all stores in goods include list {}", includeStoreIds);
                String[] rawIds = includeStoreIds.split(",");
                List<String> finalIds = Arrays.stream(rawIds)
                        .filter(id -> !id.isEmpty())
                        .map(String::trim)
                        .collect(Collectors.toList());
                return storeRepository.findAllById(finalIds).stream()
                        .filter(store -> EnumValidYn.Y.equals(store.getValidYn()))
                        .map(storeResponseGenerator::getStoreResponse)
                        .collect(Collectors.toList());
            case NONE:
                log.info("NONE - Goods has no store");
                return List.of();
            default:
                log.warn("NULL - Goods has no store query type");
                return List.of();
        }
    }
}

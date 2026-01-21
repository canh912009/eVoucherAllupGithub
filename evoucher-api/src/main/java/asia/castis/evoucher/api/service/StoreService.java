package asia.castis.evoucher.api.service;

import asia.castis.evoucher.api.dto.response.StoreResponse;

import java.util.List;

public interface StoreService {

    List<StoreResponse> searchStoreByBrandId(String brandId);

    StoreResponse findById(String id);

    List<StoreResponse> searchStoreByGoodsId(Integer goodsId);
}

package asia.castis.evoucher.api.service.generator.impl;

import asia.castis.evoucher.api.dto.response.StoreResponse;
import asia.castis.evoucher.api.entity.Store;
import asia.castis.evoucher.api.service.generator.StoreResponseGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StoreResponseGeneratorImpl implements StoreResponseGenerator {
    @Override
    public StoreResponse getStoreResponse(Store sourceStore) {
        return StoreResponse.builder()
                .storeId(sourceStore.getId())
                .storeName(sourceStore.getStoreName())
                .storeImageName(sourceStore.getStoreImageName())
                .storeImagePath(sourceStore.getStoreImagePath())
                .fullAddress(sourceStore.getFullAddress())
                .brandId(sourceStore.getBrandId())
                .validYN(sourceStore.getValidYn().name())
                .build();
    }
}

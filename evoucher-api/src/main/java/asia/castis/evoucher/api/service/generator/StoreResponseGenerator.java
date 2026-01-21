package asia.castis.evoucher.api.service.generator;

import asia.castis.evoucher.api.dto.response.StoreResponse;
import asia.castis.evoucher.api.entity.Store;

public interface StoreResponseGenerator {
    StoreResponse getStoreResponse(Store sourceStore);
}

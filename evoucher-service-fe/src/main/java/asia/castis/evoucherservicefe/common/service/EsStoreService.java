package asia.castis.evoucherservicefe.common.service;

import asia.castis.evoucherservicefe.storerequest.model.StoreModel;

import java.io.IOException;
import java.util.List;

public interface EsStoreService {
    void insertAll(List<StoreModel> voucherModels);

    int updateAll(List<StoreModel> storeModels) throws IOException;

    int deleteAll(List<String> storeIds) throws IOException;
}

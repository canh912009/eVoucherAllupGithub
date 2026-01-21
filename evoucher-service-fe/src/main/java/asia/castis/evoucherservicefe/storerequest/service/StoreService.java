package asia.castis.evoucherservicefe.storerequest.service;

import asia.castis.evoucherservicefe.exceptions.InvalidException;
import asia.castis.evoucherservicefe.storerequest.dto.StoreRequest;

import java.io.IOException;
import java.util.List;

public interface StoreService {
    void syncStore(List<StoreRequest> storeRequests) throws InvalidException, IOException;
}

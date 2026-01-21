package asia.castis.evoucherservicefe.storerequest.service.impl;

import asia.castis.evoucherservicefe.common.enums.EnumAction;
import asia.castis.evoucherservicefe.common.service.EsStoreService;
import asia.castis.evoucherservicefe.common.utils.DateUtils;
import asia.castis.evoucherservicefe.exceptions.InvalidException;
import asia.castis.evoucherservicefe.storerequest.dto.StoreRequest;
import asia.castis.evoucherservicefe.storerequest.model.StoreModel;
import asia.castis.evoucherservicefe.storerequest.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StoreServiceImpl implements StoreService {
    private EsStoreService esStoreService;
    private ModelMapper modelMapper;

    @Autowired
    public StoreServiceImpl(ModelMapper modelMapper, EsStoreService esStoreService) {
        this.modelMapper = modelMapper;
        this.esStoreService = esStoreService;
    }

    @Override
    public void syncStore(List<StoreRequest> storeRequests) throws InvalidException, IOException {
        if (storeRequests.stream().anyMatch(storeRequest -> storeRequest.getAction() == null)) {
            throw new InvalidException("At least one item doesn't have store Action");
        }
        if (storeRequests.stream().anyMatch(storeRequest -> storeRequest.getStore().getStoreId() == null)) {
            throw new InvalidException("At least one item doesn't have store Id");
        }
        // filter out invalid stores
        // Delete store might not have all the fields, it just needs the store Id
        deleteAll(storeRequests);
        updateAll(storeRequests);
        insertAll(storeRequests);
    }

    public void insertAll(List<StoreRequest> storeRequests) {
        log.info("Start inserting stores");
        // Convert to store list
        List<StoreModel> insertStores = storeRequests.stream()
                .filter(storeRequest -> storeRequest.getAction() == EnumAction.POST)
                .map(storeRequest -> {
                    StoreModel storeModel = modelMapper.map(storeRequest.getStore(), StoreModel.class);
                    storeModel.setRegisterDate(DateUtils.toDateTime(storeRequest.getStore().getRegisterDate()));
                    storeModel.setUpdateDate(DateUtils.toDateTime(storeRequest.getStore().getUpdateDate()));
                    return storeModel;
                })
                .collect(Collectors.toList());
        if (insertStores.isEmpty()) {
            log.info("No store to insert");
        } else {
            log.info("Insert new stores={}",
                    insertStores.stream().map(store -> store.getStoreId()).collect(Collectors.joining(",")));
            // Persist to ES
            esStoreService.insertAll(insertStores);
        }
        log.info("All stores inserted");
    }

    public void updateAll(List<StoreRequest> storeRequests) throws IOException {
        log.info("Start updating stores");
        // Convert to store list
        List<StoreModel> updateStores = storeRequests.stream()
                .filter(storeRequest -> storeRequest.getAction() == EnumAction.PUT)
                .map(storeRequest -> {
                    StoreModel storeModel = modelMapper.map(storeRequest.getStore(), StoreModel.class);
                    storeModel.setRegisterDate(DateUtils.toDateTime(storeRequest.getStore().getRegisterDate()));
                    storeModel.setUpdateDate(DateUtils.toDateTime(storeRequest.getStore().getUpdateDate()));
                    return storeModel;
                })
                .collect(Collectors.toList());
        if (updateStores.isEmpty()) {
            log.info("No store to update");
        } else {
            log.info("Update stores={}",
                    updateStores.stream().map(store -> store.getStoreId()).collect(Collectors.joining(",")));
            // Persist to ES
            esStoreService.updateAll(updateStores);
        }
        log.info("All stores updated");
    }

    public void deleteAll(List<StoreRequest> storeRequests) throws IOException {
        log.info("Start deleting stores");
        List<String> deleteStoreIds = storeRequests.stream()
                .filter(storeRequest -> storeRequest.getAction() == EnumAction.DELETE)
                .map(storeRequest -> storeRequest.getStore().getStoreId())
                .collect(Collectors.toList());
        if (deleteStoreIds.isEmpty()) {
            log.info("No store to delete");
        } else {
            log.info("Delete stores={}", deleteStoreIds.stream().collect(Collectors.joining(",")));
            esStoreService.deleteAll(deleteStoreIds);
        }
        log.info("All stores deleted");
    }
}

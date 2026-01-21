package com.evoucher.adminapi.good.service.model.gift_pop;

import lombok.Data;

import java.util.List;

@Data
public class GiftpopStoreResponse {
    private String trId;
    private String resCode;
    private int totalCnt;
    private String brandCode;
    private List<Store> storeList;

    @Data
    public static class Store {
        private String brandOfStore;
        private String storeCode;
        private String storeName;
        private String storeAddr;
        private String storeTel;
        private String mapCoord;
        private String useYN;
        private String modDate;
    }
}

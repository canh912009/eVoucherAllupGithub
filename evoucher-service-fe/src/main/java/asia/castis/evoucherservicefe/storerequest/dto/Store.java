package asia.castis.evoucherservicefe.storerequest.dto;

import lombok.Data;

@Data
public class Store {
    private String storeId;
    private String storeName;
    private String storeImagePath;
    private String storeImageName;
    private String supplierId;
    private String brandId;
    private String validYN;
    private String registerDate;
    private String registerId;
    private String updateDate;
    private String mapCode;
    private String mapInteractionType;
    private String region;
    private String storeType;
    private String fullAddress;
    private String updateId;
    private String tel;
}

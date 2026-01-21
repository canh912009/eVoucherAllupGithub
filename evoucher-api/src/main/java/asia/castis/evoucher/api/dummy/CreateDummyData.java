package asia.castis.evoucher.api.dummy;

import asia.castis.evoucher.api.elastic.model.StoreModel;

public class CreateDummyData {

    public static StoreModel createStore() {
        StoreModel storeModelResponse = new StoreModel();
        storeModelResponse.setStoreId("1234567890-12-024");
        storeModelResponse.setStoreName("Highland Coffee Dương Nội Hà Đông");
        storeModelResponse.setTel("+84 24 6687 2029");
        storeModelResponse.setSupplierId("1234567890");
        storeModelResponse.setBrandId("1234567890-12");
        storeModelResponse.setStoreImagePath("https://imgSvrIp:port/images/store/1234567890-12-024_1.jpg");
        storeModelResponse.setMapCode("7PH72RVP+PR");
        storeModelResponse.setProvince("Hà Nội");
        storeModelResponse.setDistrict("Thanh Xuân");
        storeModelResponse.setWard("Thanh Xuân Trung");
        storeModelResponse.setFullAddress("RIVERA PARK, 69 Đ. Vũ Trọng Phụng, Thanh Xuân Trung, Thanh Xuân, Hà Nội, 베트남");
        return storeModelResponse;
    }
}

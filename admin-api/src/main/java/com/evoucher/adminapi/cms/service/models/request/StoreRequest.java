package com.evoucher.adminapi.cms.service.models.request;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequest implements Comparable<StoreRequest> {
    @NotBlank(message = "Store name is empty!")
    private String storeName;
    @NotBlank(message = "Store image path is empty!")
    private String storeImagePath;
    @NotBlank(message = "Store image name is empty!")
    private String storeImageName;
    @NotBlank(message = "Brand ID is empty!")
    private String brandId;
//    @NotBlank(message = "Map code is empty!")
    private String mapCode;
    private String latitude;
    private String longitude;
    @NotBlank(message = "Region is empty!")
    private String region;
    private String storeType;
    private String mapInteractionType;
    @NotBlank(message = "Address is empty!")
    private String fullAddress;
    @NotBlank(message = "Telephone number is empty!")
    private String telephoneNumber;
    @NotNull(message = "Active is empty!")
    private EnumValidYn validYn;
    private String storeCode;
    private String storeId;

    @Override
    public boolean equals(Object o) {
        if (o instanceof StoreRequest) {
            StoreRequest obj = (StoreRequest) o;
            if (obj.getStoreCode() != null || this.getStoreCode() != null) {
                return Objects.equals(obj.getStoreCode(), this.storeCode);
            } else {
                return Objects.equals(obj.getStoreId(), this.storeId);
            }
        }
        return false;
    }
    @Override
    public int compareTo(StoreRequest obj) {
        if (obj.getStoreCode() != null || this.getStoreCode() != null) {
            return Objects.compare(obj.getStoreCode(), this.storeCode, Comparator.naturalOrder());
        } else {
            return Objects.compare(obj.getStoreId(), this.storeId, Comparator.naturalOrder());
        }
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.storeId, this.storeCode);
    }
}

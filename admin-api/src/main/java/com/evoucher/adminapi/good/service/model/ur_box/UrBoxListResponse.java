package com.evoucher.adminapi.good.service.model.ur_box;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class UrBoxListResponse<T> extends UrBoxResponse {
    private NestedData<T> data;
    // use for brand
    private Integer brand_count;
    // use for good
    private String totalResult;
    @Data
    public static class NestedData<T> {
        private Integer totalPage = 0;
        @ToString.Exclude
        List<T> items = new ArrayList<>();
    }
}

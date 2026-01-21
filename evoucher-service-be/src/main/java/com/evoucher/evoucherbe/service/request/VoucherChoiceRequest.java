package com.evoucher.evoucherbe.service.request;

import com.evoucher.evoucherbe.common.enums.SystemType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoucherChoiceRequest {
    @NotBlank(message = "Choice ev is empty!")
    private String parentId;
    @NotNull(message = "Publish ID is empty")
    private Integer publishId;
    @NotBlank(message = "Choice token is empty!")
    private String token;
    @NotNull(message = "List Goods choice is empty!")
    private List<VoucherGoodsChoiceRequest> products;
    @NotNull(message = "parent type is empty.")
    private SystemType type;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class VoucherGoodsChoiceRequest {
        @NotNull(message = "Goods ID is empty")
        private Long goodsId;
        @NotNull(message = "Quantity is empty")
        private Integer quantity;
    }
}

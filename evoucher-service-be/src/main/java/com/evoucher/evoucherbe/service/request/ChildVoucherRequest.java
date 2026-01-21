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
public class ChildVoucherRequest {
    @NotBlank(message = "Choice ev is empty!")
    private String parentId;
    @NotNull(message = "Publish ID is empty")
    private Integer publishId;
    @NotNull(message = "List Goods choice is empty!")
    private List<ChildVoucherGoodRequest> products;
    @NotNull(message = "parent type is empty.")
    private SystemType type;
}

package com.castis.pos_api.dto;

import com.castis.pos_api.dto.response.VoucherApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherApiDTO {
    private VoucherApiResponse voucherApiResponse;

    private String storeId;

    public VoucherApiDTO(VoucherApiResponse voucherApiResponse, String storeId) {
        this.voucherApiResponse = voucherApiResponse;
        this.storeId = storeId;

    }
}

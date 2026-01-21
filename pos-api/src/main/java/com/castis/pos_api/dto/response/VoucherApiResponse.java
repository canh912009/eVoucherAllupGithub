package com.castis.pos_api.dto.response;

import com.castis.pos_api.entity.DataVoucher;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherApiResponse {
    @Expose
    private int code;

    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private DataVoucher data;

}

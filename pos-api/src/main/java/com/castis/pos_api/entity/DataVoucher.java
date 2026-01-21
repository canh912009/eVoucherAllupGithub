package com.castis.pos_api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DataVoucher {
    @SerializedName("voucher")
    private Voucher voucher;

    @SerializedName("otp")
    @Expose
    private String otp;


    @SerializedName("approvementNo")
    @Expose
    private String approvementNo;

    @SerializedName("success")
    @Expose
    private boolean success;

}

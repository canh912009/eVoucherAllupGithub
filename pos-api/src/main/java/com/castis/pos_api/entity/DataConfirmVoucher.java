package com.castis.pos_api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DataConfirmVoucher {
    @SerializedName("approvementNo")
    @Expose
    private String approvementNo;

    @SerializedName("success")
    private boolean success;
}

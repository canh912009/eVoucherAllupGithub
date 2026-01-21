package com.evoucher.adminapi.common.enums;

import com.google.gson.annotations.SerializedName;

import java.util.EnumSet;
import java.util.Set;

public enum PublishDetailStatus {
    @SerializedName("STRT_PUB")
    STRT_PUB,
    @SerializedName("FAIL_PUB")
    FAIL_PUB,
    @SerializedName("END_PUB")
    END_PUB,
    @SerializedName("STRT_GEN_MSG")
    STRT_GEN_MSG,
    @SerializedName("FAIL_GEN_MSG")
    FAIL_GEN_MSG,
    @SerializedName("END_GEN_MSG")
    END_GEN_MSG,
    @SerializedName("STRT_SND_MSG")
    STRT_SND_MSG,
    @SerializedName("FAIL_SND_MSG")
    FAIL_SND_MSG,
    @SerializedName("RESULT_PENDING")
    RESULT_PENDING,
    @SerializedName("RESULT_SUCCESS")
    RESULT_SUCCESS,
    @SerializedName("RESULT_FAIL")
    RESULT_FAIL;

    public static final Set<PublishDetailStatus> STATUS_FAIL =
            EnumSet.of(FAIL_GEN_MSG, FAIL_SND_MSG, RESULT_FAIL, FAIL_PUB);

    public static final Set<PublishDetailStatus> CAN_NOT_BE_RESEND_VOUCHER =
            EnumSet.of(STRT_PUB, END_PUB, STRT_GEN_MSG, END_GEN_MSG, STRT_SND_MSG, RESULT_PENDING);
}

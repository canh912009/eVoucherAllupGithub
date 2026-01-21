package com.castis.publishservice.utils.status;

import com.google.gson.annotations.SerializedName;

public enum PublishStatus {
    @SerializedName("WAIT_APPRV")
    WAIT_APPRV,
    @SerializedName("CANCEL")
    CANCEL,
    @SerializedName("APPROVED")
    APPROVED,
    @SerializedName("CANCEL_APPRV")
    CANCEL_APPRV,
    @SerializedName("REJECTED")
    REJECTED,
    @SerializedName("PUBLISHING")
    PUBLISHING,
    @SerializedName("FAIL_PUBLISHING")
    FAIL_PUBLISHING,
    @SerializedName("GENERATING")
    GENERATING,
    @SerializedName("FAIL_GENERATING")
    FAIL_GENERATING,
    @SerializedName("SENDING")
    SENDING,
    @SerializedName("FAIL_SENDING")
    FAIL_SENDING,
    @SerializedName("WAIT_FOR_SEND_RESULT")
    WAIT_FOR_SEND_RESULT,
    @SerializedName("FINISHED")
    FINISHED;
}

package com.castis.publishservice.utils.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExternalPinUploadStatus {
    UPLOAD_COMPLETED,
    UPLOAD_FAILED
}

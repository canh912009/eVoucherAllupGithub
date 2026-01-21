package com.castis.publishservice.dto.request;

import com.castis.publishservice.utils.status.PublishStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StatusQueue implements Serializable {
    private long publishId;
    private PublishStatus publishStatus;
    List<SendMessageStatusRequest> detailStatus;
}

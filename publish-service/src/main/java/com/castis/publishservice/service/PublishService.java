package com.castis.publishservice.service;

import com.castis.publishservice.dto.PublishDTO;
import com.castis.publishservice.dto.queue.PublishQueueRequest;
import com.castis.publishservice.exception.defineException.NotFoundException;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import com.castis.publishservice.utils.status.PublishStatus;

import java.util.Date;

public interface PublishService {
    PublishDTO findById(long id) throws NotFoundException;

    PublishQueueRequest findRequestById(Long id) throws ServerRuntimeException;

    void updatePublishStatus(Long publishId, PublishStatus status) throws NotFoundException;

    void updatePublishStatusAndPublishDate(Long publishId, PublishStatus status, Date publishDate) throws NotFoundException;
}

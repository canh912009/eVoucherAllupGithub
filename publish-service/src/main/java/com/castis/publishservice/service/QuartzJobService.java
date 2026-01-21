package com.castis.publishservice.service;

import com.castis.publishservice.dto.PublishDTO;
import com.castis.publishservice.dto.request.PublishRequest;
import com.castis.publishservice.exception.defineException.QuartzCreationException;

public interface QuartzJobService {
    void createPublishSchedule(PublishDTO publish, PublishRequest publishRequest) throws QuartzCreationException;
    void cancelScheduledJob(String jobName, String jobGroup) throws QuartzCreationException;
}

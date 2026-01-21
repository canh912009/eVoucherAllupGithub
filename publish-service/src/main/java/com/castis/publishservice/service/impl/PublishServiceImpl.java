package com.castis.publishservice.service.impl;

import com.castis.publishservice.dto.PublishDTO;
import com.castis.publishservice.dto.queue.PublishQueueRequest;
import com.castis.publishservice.entity.Publish;
import com.castis.publishservice.exception.defineException.NotFoundException;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import com.castis.publishservice.mapper.PublishMapper;
import com.castis.publishservice.repository.PublishRepository;
import com.castis.publishservice.service.PublishService;
import com.castis.publishservice.utils.Utils;
import com.castis.publishservice.utils.status.PublishStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublishServiceImpl implements PublishService {
    private final PublishRepository repository;
    private static final PublishMapper mapper = PublishMapper.INSTANCE;

    @Override
    public PublishDTO findById(long id) throws NotFoundException {
        Publish publish = repository.findById(id)
                .orElseThrow(() -> NotFoundException.publish(id));
        return mapper.toDTO(publish);
    }

    @Override
    public PublishQueueRequest findRequestById(Long id)  {
        Publish publish = repository.findById(id)
                .orElseThrow(() -> NotFoundException.publish(id));
        return mapper.toRequest(publish);
    }

    @Override
    public void updatePublishStatus(Long publishId, PublishStatus status) throws NotFoundException {
        log.info("Update publish with publishId={} status->{}", publishId, status);
        Publish publish = repository.findById(publishId).orElseThrow(() -> NotFoundException.publish(publishId));
        publish.setPublishStatusCode(status);
        repository.save(publish);
    }

    @Override
    public void updatePublishStatusAndPublishDate(Long publishId, PublishStatus status, Date publishDate) throws NotFoundException {
        log.info("Update publish={} status->{} publish date->{}", publishId, status, Utils.toJson(publishDate));
        Publish publish = repository.findById(publishId).orElseThrow(() -> NotFoundException.publish(publishId));
        publish.setPublishStatusCode(status);
        publish.setPublishDate(publishDate);
        repository.save(publish);
    }
}

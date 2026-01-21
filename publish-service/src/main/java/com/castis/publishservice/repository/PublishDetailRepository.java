package com.castis.publishservice.repository;

import com.castis.publishservice.entity.PublishDetail;
import com.castis.publishservice.utils.status.PublishStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublishDetailRepository extends JpaRepository<PublishDetail, Long> {
    List<PublishDetail> getAllByPublishDtlIdIn(List<Long> ids);
    int countAllByPublishId(Long publishId);
}

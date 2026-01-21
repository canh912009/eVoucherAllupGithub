package com.castis.publishservice.repository;

import com.castis.publishservice.entity.PublishSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublishScheduleRepository extends JpaRepository<PublishSchedule, Long> {
}

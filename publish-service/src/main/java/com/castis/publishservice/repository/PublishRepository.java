package com.castis.publishservice.repository;

import com.castis.publishservice.entity.Publish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishRepository extends JpaRepository<Publish, Long> {
}

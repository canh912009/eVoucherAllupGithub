package com.castis.publishservice.repository;

import com.castis.publishservice.entity.ExternalPinUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalPinUploadRepository extends JpaRepository<ExternalPinUpload, Long> {
}

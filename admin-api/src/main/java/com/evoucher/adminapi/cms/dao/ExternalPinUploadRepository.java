package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.ExternalPinUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalPinUploadRepository extends JpaRepository<ExternalPinUpload, Integer>, ExternalPinUploadRepositoryCustom {
}

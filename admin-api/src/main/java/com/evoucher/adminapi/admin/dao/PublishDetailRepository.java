package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.PublishDetail;
import com.evoucher.adminapi.cms.service.models.EndUserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublishDetailRepository extends JpaRepository<PublishDetail, Integer> {
    List<PublishDetail> findAllByPublishId(Integer publishId);

    Optional<PublishDetail> findFirstByPublishId(Integer publishId);
    @Query(nativeQuery = true)
    List<EndUserDTO> publishMetaDataDetail(Integer publishId);
}

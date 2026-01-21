package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.EndUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EndUserRepository extends JpaRepository<EndUser, Long>, EndUserRepositoryCustom {
    boolean existsByUserMobileNum(String number);
    Optional<EndUser> findByUserMobileNum(String number);
    Page<EndUser> findAll(Pageable pageable);
    Page<EndUser> findByUserMobileNumOrUserNm(String userMobileNum, String userNm, Pageable pageable);
    Page<EndUser> findByUserMobileNumAndUserNm(String userMobileNum, String userNm, Pageable pageable);
}

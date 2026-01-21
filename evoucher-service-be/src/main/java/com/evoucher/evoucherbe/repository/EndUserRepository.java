package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.entity.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EndUserRepository extends JpaRepository<EndUser, Long> {
    Optional<EndUser> findByUserMobileNum(String mobileNumber);
}

package com.evoucher.adminapi.auth.dao;

import com.evoucher.adminapi.auth.dao.models.Admin;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String>, AdminRepositoryCustom {

    Optional<Admin> findByIdAndValidYn(String id, EnumValidYn validYn);

    Boolean existsByMobileNumber(String mobileNumber);

    Optional<Admin> findAdminByMobileNumberAndValidYn(String name, EnumValidYn validYn);
}

package com.evoucher.adminapi.auth.dao;
import com.evoucher.adminapi.auth.dao.models.IpWhitelist;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IpWhitelistRepository extends JpaRepository<IpWhitelist, String>/*, IpWhitelistRepositoryCustom*/ {

    Optional<IpWhitelist> findByIdAndValidYn(String id, EnumValidYn validYn);

    Optional<IpWhitelist> findByIpAddressAndValidYn(String ipAddress, EnumValidYn validYn);
}
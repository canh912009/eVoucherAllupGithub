package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import com.evoucher.evoucherbe.entity.CustomerContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerContractRepository extends JpaRepository<CustomerContract, Integer> {

    Optional<CustomerContract> findByIdAndValidYn(Integer id, EnumValidYn validYn);

    @Query(value = "select cc from CustomerContract cc left join Campaign c on cc.id = c.customerContractId where c.id = :campaignId and cc.validYn = :validYn")
    Optional<CustomerContract> findByCampaignIdAndValidYn(@Param("campaignId") Integer campaignId, @Param("validYn") EnumValidYn validYn);
}

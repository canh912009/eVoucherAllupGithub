package com.castis.publishservice.repository;

import com.castis.publishservice.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, String>, JpaSpecificationExecutor<Voucher> {
    @Query(value = "select c.customer_contract_id from tb_campaign c inner join tb_publish p on p.campaign_id =c.campaign_id where p.publish_id = ?1", nativeQuery = true)
    Long getContractIdByPublishId(Long publishId);
    @Query(value = "select publish_dtl_id from tb_voucher where ev =?1", nativeQuery = true)
    Long getDetailIdByVoucherId(String voucherId);
    List<Voucher> findAllByIdIn(Collection<String> ids);
}
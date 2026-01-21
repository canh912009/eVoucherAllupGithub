package com.castis.pos_api.repositories;

import com.castis.pos_api.entity.Voucher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String> {
    List<Voucher> findAll(Specification<Voucher> query);
    List<Voucher> findAllBySerialNo(String serialNo);
}

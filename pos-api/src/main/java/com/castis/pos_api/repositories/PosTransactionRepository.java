package com.castis.pos_api.repositories;

import com.castis.pos_api.entity.PosTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosTransactionRepository extends JpaRepository<PosTransaction, String> {
}

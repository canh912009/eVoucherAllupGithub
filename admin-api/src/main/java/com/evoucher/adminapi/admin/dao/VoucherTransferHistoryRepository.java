package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.VoucherTransferHistory;
import com.evoucher.adminapi.admin.service.models.CsTransferHistoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VoucherTransferHistoryRepository extends JpaRepository<VoucherTransferHistory, Integer> {
    @Query(name = "findFirstHistoryNodeByVoucherId", nativeQuery = true)
    List<CsTransferHistoryDTO> findFirstHistoryNodeByVoucherId(String ev);

    @Query(name = "findHistoryListInRange", nativeQuery = true)
    List<CsTransferHistoryDTO> findHistoryListInRange(Date startDate, Date endDate);

    @Query(
        value =
            "WITH RankedTransfers AS ( " +
            "    SELECT *, " +
            "           ROW_NUMBER() OVER (PARTITION BY from_ev ORDER BY transaction_id DESC) AS from_rank, " +
            "           ROW_NUMBER() OVER (PARTITION BY to_ev ORDER BY transaction_id DESC) AS to_rank " +
            "    FROM tb_transfer_history " +
            "    WHERE from_ev = :ev OR to_ev = :ev " +
            ") " +
            "SELECT * " +
            "FROM RankedTransfers " +
            "WHERE from_rank = 1 OR to_rank = 1",
        nativeQuery = true)
    List<VoucherTransferHistory> findTransferHistoryByVoucherId(String ev);
}

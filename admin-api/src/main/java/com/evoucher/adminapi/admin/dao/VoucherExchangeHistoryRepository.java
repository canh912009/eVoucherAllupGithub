package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.VoucherExchangeHistory;
import com.evoucher.adminapi.admin.service.models.ChildOfChoiceVoucherResponse;
import com.evoucher.adminapi.admin.service.models.CsExchangeHistoryDTO;
import com.evoucher.adminapi.admin.service.models.PinDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherExchangeHistoryRepository extends JpaRepository<VoucherExchangeHistory, Integer> {
    @Query(name = "finsExchangeHistoryByVoucherId", nativeQuery = true)
    List<CsExchangeHistoryDTO> findExchangeHistoryByVoucherId(@Param("ev") String ev);

    @Query(name = "findPinDetailById", nativeQuery = true)
    PinDetailResponse findPinDetailById(@Param("ev") String ev);

    @Query(name = "findChildOfChoiceVoucherByChoiceVoucherId", nativeQuery = true)
    List<ChildOfChoiceVoucherResponse> findChildOfChoiceVoucherByChoiceVoucherId(@Param("ev") String ev);
}

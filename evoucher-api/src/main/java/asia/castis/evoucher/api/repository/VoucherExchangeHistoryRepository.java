package asia.castis.evoucher.api.repository;

import asia.castis.evoucher.api.entity.VoucherExchangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherExchangeHistoryRepository extends JpaRepository<VoucherExchangeHistory, Integer> {
    List<VoucherExchangeHistory> findAllByEvOrderByTransactionDateDesc(String ev);
}

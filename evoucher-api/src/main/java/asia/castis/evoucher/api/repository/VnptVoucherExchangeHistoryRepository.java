package asia.castis.evoucher.api.repository;

import asia.castis.evoucher.api.common.enums.EnumResult;
import asia.castis.evoucher.api.entity.VnptVoucherExchangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VnptVoucherExchangeHistoryRepository extends JpaRepository<VnptVoucherExchangeHistory, Integer> {
    List<VnptVoucherExchangeHistory> findByEvOrderByExchangeDateDesc(String ev);

    boolean existsByEvAndResult(String ev, EnumResult result);
}

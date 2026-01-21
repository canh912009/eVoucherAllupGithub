package asia.castis.evoucher.api.repository;

import asia.castis.evoucher.api.entity.VoucherTransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherTransferHistoryRepository extends JpaRepository<VoucherTransferHistory, Integer> {
    VoucherTransferHistory findByToEv(String toEv);
}

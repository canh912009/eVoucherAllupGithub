package asia.castis.evoucher.api.repository;

import asia.castis.evoucher.api.entity.EVoucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<EVoucher, String> {
    Page<EVoucher> findAllByUserMobileNumberStartingWith(String choiceEv, Pageable pageable);

    List<EVoucher> findAllByParentVoucherEvOrderByCreationDateDesc(String ev);

    Optional<EVoucher> findBySerialNo(String serialNo);

    Optional<EVoucher> findBySerialNoAndShortLinkEndingWith(String serialNumber, String voucherId);
}

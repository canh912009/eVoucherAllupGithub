package asia.castis.web_hook.repository;

import asia.castis.web_hook.bean.entity.ExtPin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExtPinRepository extends JpaRepository<ExtPin, Long> {
    Optional<ExtPin> findByTransactionId(String transactionId);
    Optional<ExtPin> findByTrackingId(String trackingId);
    Optional<List<ExtPin>> findAllByExtPinNoIn(List<String> pinsNo);
}

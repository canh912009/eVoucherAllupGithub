package asia.castis.web_hook.repository;

import asia.castis.web_hook.bean.entity.ThirdPartyCallingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyCallingRepository extends JpaRepository<ThirdPartyCallingHistory, Long> {
}

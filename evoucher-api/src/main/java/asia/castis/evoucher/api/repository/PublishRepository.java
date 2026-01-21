package asia.castis.evoucher.api.repository;


import asia.castis.evoucher.api.entity.Publish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishRepository extends JpaRepository<Publish, Integer> {
}

package asia.castis.evoucher.api.repository;

import asia.castis.evoucher.api.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {
    List<Store> findByBrandId(String brandId);
}

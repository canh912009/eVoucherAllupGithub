package asia.castis.evoucher.api.repository;

import asia.castis.evoucher.api.entity.VnptGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VnptGoodsRepository extends JpaRepository<VnptGoods, Integer> {
    List<VnptGoods> findByParentGoodsId(Integer goodsId);
}

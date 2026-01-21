package asia.castis.evoucher.api.repository;

import asia.castis.evoucher.api.entity.BulkGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BulkGoodsRepository extends JpaRepository<BulkGoods, Long> {
    @Query("SELECT bg FROM BulkGoods bg JOIN bg.goods g WHERE bg.bulkBrandId = :bulkBrandId AND bg.validYn = 'Y' AND g.validYn = 'Y'")
    Page<BulkGoods> findAllByBulkBrandId(long bulkBrandId, Pageable pageable);

    @Query("SELECT bg FROM BulkGoods bg JOIN bg.goods g WHERE bg.bulkBrandId = :bulkBrandId AND g.goodsName LIKE %:name% AND bg.validYn = 'Y' AND g.validYn = 'Y'")
    Page<BulkGoods> findByBulkBrandIdAndGoodsName(long bulkBrandId, String name, Pageable pageable);
}

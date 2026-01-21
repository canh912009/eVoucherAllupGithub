package asia.castis.evoucher.api.repository;

import asia.castis.evoucher.api.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Integer> {
    @Query("SELECT g " +
            "FROM Goods g " +
            "LEFT JOIN GoodsChoice gc ON g.id = gc.goodsId " +
            "WHERE gc.parentGoodsId = :id AND gc.validYn = 'Y' AND g.validYn = 'Y' ORDER BY gc.displayIdx")
    List<Goods> findGoodsChoiceById(@Param(value = "id") Integer id);
}

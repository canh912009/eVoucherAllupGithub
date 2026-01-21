package asia.castis.evoucher.api.repository;

import asia.castis.evoucher.api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query(value = "select c.* from tb_category c " +
            "    join tb_category_goods_rel tcgr on c.ctgr_cd = tcgr.ctgr_cd " +
            "    join tb_goods tg on tg.goods_id = tcgr.goods_id " +
            " where tg.goods_id = ?1 and c.valid_yn = 'Y'; ", nativeQuery = true)
    List<Category> findByGoodIdAndValidYnIsYes(Integer goodsId);

}

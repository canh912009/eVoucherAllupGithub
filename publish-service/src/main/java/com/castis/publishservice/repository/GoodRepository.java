package com.castis.publishservice.repository;

import com.castis.publishservice.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodRepository extends JpaRepository<Goods, Long> {
    @Query(value = "select cg.* from tb_goods_choices gc inner join tb_goods cg on gc.goods_id = cg.goods_id where gc.parent_goods_id = :parentId and gc.valid_yn = 'Y';", nativeQuery = true)
    List<Goods> findAllByParentId(long parentId);
    List<Goods> findAllByIdIn(List<Long> ids);
}

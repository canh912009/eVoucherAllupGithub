package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import com.evoucher.evoucherbe.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {

//    Optional<Goods> findByIdAndValidYn(Long id, EnumValidYn validYn);

    @Query(value = "SELECT g.* " +
                        "FROM tb_goods g " +
                        "LEFT JOIN tb_campaign_goods_rel cg ON g.goods_id = cg.goods_id " +
                        "WHERE cg.campaign_id = ?1 AND g.valid_yn = 'Y'", nativeQuery = true)
    List<Goods> findListGoodsByCampaignId(Integer campaignId);
    Optional<List<Goods>> findAllByIdIn(Collection<Long> ids);
}

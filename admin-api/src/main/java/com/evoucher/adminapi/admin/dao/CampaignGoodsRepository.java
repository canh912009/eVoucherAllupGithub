package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.dao.models.CampaignGoods;
import com.evoucher.adminapi.admin.dao.models.CampaignGoodsId;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignGoodsRepository extends JpaRepository<CampaignGoods, CampaignGoodsId> {

    List<CampaignGoods> findAllByCampaignIdAndValidYn(Integer campaignId, EnumValidYn validYn);

    boolean existsByCampaignIdAndGoodsIdAndValidYn(Integer campaignId, Integer goodsId, EnumValidYn validYn);

    @Query("SELECT g, s, b" +
            " from CampaignGoods cg" +
            " left join Goods g on cg.goodsId = g.id" +
            " left join Supplier s on g.supplierId = s.id" +
            " left join Brand b on g.brandId = b.id" +
            " where cg.campaignId = :campaignId and cg.validYn = :validYn")
    List<Tuple> findGoodsSupplerBrandByCampaignIdAndValidYn(Integer campaignId, EnumValidYn validYn);

    List<CampaignGoods> findAllByCampaignId(Integer campaignId);
}

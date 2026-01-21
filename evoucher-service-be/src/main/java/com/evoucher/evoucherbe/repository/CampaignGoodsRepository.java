package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import com.evoucher.evoucherbe.entity.CampaignGoods;
import com.evoucher.evoucherbe.entity.CampaignGoodsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignGoodsRepository extends JpaRepository<CampaignGoods, CampaignGoodsId> {

    List<CampaignGoods> findAllByCampaignIdAndValidYn(Integer campaignId, EnumValidYn validYn);

    Optional<CampaignGoods> findByCampaignIdAndGoodsIdAndValidYn(Integer campaignId, Integer goodsId, EnumValidYn validYn);
}

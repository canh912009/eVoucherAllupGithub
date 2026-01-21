package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.ExternalPin;
import com.evoucher.adminapi.common.enums.ExternalPinStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExternalPinRepository extends JpaRepository<ExternalPin, Integer> {

    List<ExternalPin> findByGoodsIdAndStatusAndExpireTimeAfter(Integer goodsId, ExternalPinStatus status, Date expireTime);
}

package com.castis.publishservice.repository;

import com.castis.publishservice.entity.ExtPin;
import com.castis.publishservice.utils.enum_template.ExtPinStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ExtPinRepository extends JpaRepository<ExtPin, Long> {
    Long countAllByGoodsId(Long goodsId);
    Long countAllByStatusAndGoodsIdAndExpireTimeAfter(ExtPinStatus status, Long goodsId, Date expireTime);
    Page<ExtPin> findByStatusAndGoodsIdAndExpireTimeAfterOrderByRegDtAsc(ExtPinStatus status, Long goodsId, Date expireTime, Pageable pageable);
    Page<ExtPin> findByIdNotInAndStatusAndGoodsIdAndExpireTimeAfterOrderByRegDtAsc(List<Long> ids, ExtPinStatus status, Long goodsId, Date expireTime, Pageable pageable);
}

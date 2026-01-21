package com.evoucher.evoucherbe.repository;

import com.evoucher.evoucherbe.common.enums.ExternalPinStatus;
import com.evoucher.evoucherbe.entity.ExternalPin;
import com.evoucher.evoucherbe.service.ExternalPinService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExternalPinRepository extends JpaRepository<ExternalPin, Long> {

    @Query(value = "SELECT * FROM tb_ext_pin " +
                   "WHERE status = :status " +
                   "  AND goods_id = :goodsId " +
                   "ORDER BY reg_dt ASC " +
                   "LIMIT :rowNumber",
          nativeQuery = true)
    List<ExternalPin> findByStatusAndGoodsIdAndCountRowNumberOrderByRegDtASC(
            Long goodsId, String status, Integer rowNumber);

    List<ExternalPin> findByGoodsIdAndStatus(Long goodsId, ExternalPinStatus status);

    List<ExternalPin> findAllByIdIn(List<Long> ids);

    List<ExternalPin> findByExternalPinNo(String id);
    Page<ExternalPin> findByStatusAndGoodsIdAndExpireTimeAfterOrderByRegDtAsc(ExternalPinStatus status, Long goodsId, Date expireTime, Pageable pageable);
    Page<ExternalPin> findByIdNotInAndStatusAndGoodsIdAndExpireTimeAfterOrderByRegDtAsc(List<Long> ids, ExternalPinStatus status, Long goodsId, Date expireTime, Pageable pageable);
}

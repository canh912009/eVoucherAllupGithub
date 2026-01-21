package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.Category;
import com.evoucher.adminapi.cms.dao.models.Stock;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    @Query("SELECT s FROM Stock s WHERE DATE(s.insertedAt) = DATE(:fromDate) ")
    List<Stock> findAllByInsertedAtGreaterThanEqual(@Param("fromDate") Date fromDate);

    @Query("SELECT s FROM Stock s WHERE DATE(s.insertedAt) = DATE(:fromDate) ")
    Page<Stock> findAllByInsertedAtGreaterThanEqual(@Param("fromDate") Date fromDate, Pageable pageable);

    @Query("SELECT new map(" +
           "SUM(s.totalQuantity) as totalQuantity, " +
           "SUM(s.totalAmount) as totalAmount, " +
           "SUM(s.diff1d) as totalDiff1d, " +
           "SUM(s.diff7d) as totalDiff7d, " +
           "SUM(s.diff30d) as totalDiff30d) " +
           "FROM Stock s " +
           "WHERE DATE(s.insertedAt) = DATE(:fromDate) ")
    Map<String, Long> getStockSummary(@Param("fromDate") Date fromDate);
}

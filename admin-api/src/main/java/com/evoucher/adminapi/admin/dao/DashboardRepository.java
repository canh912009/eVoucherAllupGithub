package com.evoucher.adminapi.admin.dao;

import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DashboardRepository {
    List<Map<String, Object>> getSupplierChartData(Date startDate, Date endDate);
    List<Map<String, Object>> getCustomerChartData(Date startDate, Date endDate);
    List<Map<String, Object>> getCustomerTableData(Pageable pageable);
    long countCustomerTableData();
    List<Map<String, Object>> getCampaignChartData(String customerId);
    List<Map<String, Object>> getItemTableData(String supplierId, Pageable pageable);
    List<Map<String, Object>> getBrandItemTableData(String brandId, Pageable pageable);
    long countItemTableData(String supplierId);
    long countBrandItemTableData(String brandId);
    List<Map<String, Object>> getBrandTableData(String supplierId, Pageable pageable);
    long countBrandTableData(String supplierId);
    List<Map<String, Object>> getStoreTableData(String brandId, Pageable pageable);
    long countStoreTableData(String brandId);
    List<Map<String, Object>> getStoreGoodsChartData(String storeId, Date startDate, Date endDate);
    List<Map<String, Object>> getCampaignTableData(String customerId, Date startDate, Date endDate, Pageable pageable);
    long countCampaignTableDataCount(String customerId, Date startDate, Date endDate);
    List<Map<String, Object>> getDeliveryStatusTableData(String customerId, Date startDate, Date endDate, Pageable pageable);
    long countDeliveryStatusTableData(String customerId, Date startDate, Date endDate);
}

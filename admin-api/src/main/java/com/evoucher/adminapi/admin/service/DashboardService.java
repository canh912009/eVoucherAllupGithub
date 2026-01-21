package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.common.exception.CustomCodeException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    List<Map<String, Object>> getSupplierChartData(String startDate, String endDate) throws CustomCodeException;
    List<Map<String, Object>> getCustomerChartData(String startDate, String endDate) throws CustomCodeException;
    Page<Map<String, Object>> getCustomerTableData(Integer offset, Integer pageSize) throws CustomCodeException;
    List<Map<String, Object>> getCustomersCampaignChartData(String campaignId) throws CustomCodeException;
    Page<Map<String, Object>> getItemTableData(String supplierId, Integer offset, Integer pageSize) throws CustomCodeException;
    Page<Map<String, Object>> getBrandTableData(String supplierId, Integer offset, Integer pageSize) throws CustomCodeException;
    Page<Map<String, Object>> getStoreTableData(String brandId, Integer offset, Integer pageSize) throws CustomCodeException;
    List<Map<String, Object>> getStoreGoodsChartData(String storeId, String startDate, String endDate) throws CustomCodeException;
    Page<Map<String, Object>> getCampaignTableData(String customerId, String startDate, String endDate, Integer offset, Integer pageSize) throws CustomCodeException;
    Page<Map<String, Object>> getDeliveryTableData(String customerId, String startDate, String endDate, Integer offset, Integer pageSize) throws CustomCodeException;
}

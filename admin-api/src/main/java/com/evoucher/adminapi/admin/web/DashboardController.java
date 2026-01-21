package com.evoucher.adminapi.admin.web;

import com.evoucher.adminapi.admin.service.DashboardService;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService service;
    @GetMapping("/supplier-chart")
    BaseResponse getSupplierChartData(@RequestParam String startDate, @RequestParam String endDate) {
        return new BaseResponse(service.getSupplierChartData(startDate, endDate));
    }
    @GetMapping("/customer-chart")
    BaseResponse getCustomerChartData(@RequestParam String startDate, @RequestParam String endDate) {
        return new BaseResponse(service.getCustomerChartData(startDate, endDate));
    }

    @GetMapping("/customer-table/{offset}/{pageSize}")
    BaseResponse getCustomerTableData(@PathVariable Integer offset, @PathVariable Integer pageSize) {
        Page<Map<String, Object>> result = service.getCustomerTableData(offset, pageSize);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return res;
    }

    @GetMapping("/customer-campaign-chart")
    BaseResponse getCustomerCampaignChartData(@RequestParam String customerId) {
        return new BaseResponse(service.getCustomersCampaignChartData(customerId));
    }
    @GetMapping("/item-table/{offset}/{pageSize}")
    BaseResponse getItemTableData(@PathVariable Integer offset, @PathVariable Integer pageSize, @RequestParam @Nullable String supplierId) {
        Page<Map<String, Object>> result = service.getItemTableData(supplierId, offset, pageSize);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return res;
    }
    @GetMapping("/brand-table/{offset}/{pageSize}")
    BaseResponse getBrandTableData(@PathVariable Integer offset, @PathVariable Integer pageSize, @RequestParam @Nullable String supplierId) {
        Page<Map<String, Object>> result = service.getBrandTableData(supplierId, offset, pageSize);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return res;
    }
    @GetMapping("/store-table/{offset}/{pageSize}")
    BaseResponse getStoreTableData(@PathVariable Integer offset, @PathVariable Integer pageSize, @RequestParam @Nullable String brandId) {
        Page<Map<String, Object>> result = service.getStoreTableData(brandId, offset, pageSize);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return res;
    }
    @GetMapping("/goods-chart")
    BaseResponse getCustomerChartData(@RequestParam @Nullable String storeId, @RequestParam String startDate, @RequestParam String endDate) {
        return new BaseResponse(service.getStoreGoodsChartData(storeId, startDate, endDate));
    }
    @GetMapping("/campaign-table/{offset}/{pageSize}")
    BaseResponse getCampaignTableData(@PathVariable Integer offset, @PathVariable Integer pageSize, @RequestParam @Nullable String customerId, @RequestParam String startDate, @RequestParam String endDate) {
        Page<Map<String, Object>> result = service.getCampaignTableData(customerId, startDate, endDate, offset, pageSize);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return res;
    }
    @GetMapping("/delivery-table/{offset}/{pageSize}")
    BaseResponse getDeliveryTableData(@PathVariable Integer offset, @PathVariable Integer pageSize, @RequestParam @Nullable String customerId,  @RequestParam String startDate, @RequestParam String endDate) {
        Page<Map<String, Object>> result = service.getDeliveryTableData(customerId, startDate, endDate, offset, pageSize);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return res;
    }
}

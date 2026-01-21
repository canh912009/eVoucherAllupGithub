package com.evoucher.adminapi.cms.web;

import com.evoucher.adminapi.cms.dao.models.Stock;
import com.evoucher.adminapi.cms.service.StockService;
import com.evoucher.adminapi.cms.service.models.StockDTO;
import com.evoucher.adminapi.common.message.BaseResponse;
import com.evoucher.adminapi.good.service.model.ur_box.UrBoxBrand;
import com.evoucher.adminapi.good.service.typed_service.system.UrBoxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    @GetMapping("/{page}/{pageSize}")
    public ResponseEntity<BaseResponse> getAllWithPagination(
            @PathVariable Integer page,
            @PathVariable Integer pageSize,
            @RequestParam(required = false) String insertedAtFrom) {
        
        log.info("Request to get stocks with pagination from date: {}", insertedAtFrom);
        Page<StockDTO> result = stockService.getAllFromDate(insertedAtFrom, page, pageSize);
        
        BaseResponse response = new BaseResponse(result.getContent());
        response.setTotalCount(result.getTotalElements());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAll(
            @RequestParam(required = false) String insertedAtFrom) {
        
        log.info("Request to get all stocks from date: {}", insertedAtFrom);
        List<StockDTO> result = stockService.getAllFromDate(insertedAtFrom);
        
        BaseResponse response = new BaseResponse(result);
        response.setTotalCount((long) result.size());  // Convert to Long
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<BaseResponse> getSummary(
            @RequestParam(required = false) String insertedAtFrom) {
        
        log.info("Request to get stock summary from date: {}", insertedAtFrom);
        Map<String, Long> result = stockService.getStockSummary(insertedAtFrom);
        
        return ResponseEntity.ok(new BaseResponse(result));
    }
}

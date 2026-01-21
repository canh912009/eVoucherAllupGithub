package com.evoucher.adminapi.settlement.web;

import com.evoucher.adminapi.common.message.BaseResponse;
import com.evoucher.adminapi.settlement.service.SettlementLogService;
import com.evoucher.adminapi.settlement.service.model.FilterSearchSettlement;
import com.evoucher.adminapi.settlement.service.model.SettlementLogAllDataDto;
import com.evoucher.adminapi.settlement.service.model.SettlementLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/settlements")
public class SettlementLogController {

    private final SettlementLogService settlementLogService;

    @GetMapping(value = {"/search/{page}/{pageSize}"})
    public ResponseEntity<BaseResponse> searchSettlementLog(FilterSearchSettlement filterSearchSettlement,
                                                            @PathVariable(name = "page", required = false) Integer page,
                                                            @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<SettlementLogDTO> result = settlementLogService.searchSettlementLog(filterSearchSettlement);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = {"/all-data"})
    public ResponseEntity<BaseResponse> getSettlementLogData(FilterSearchSettlement filterSearchSettlement) {
        List<SettlementLogAllDataDto> result = settlementLogService.getDataSettlementLog(filterSearchSettlement);
        BaseResponse res = new BaseResponse(result);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

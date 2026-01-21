package com.evoucher.adminapi.cms.mapper;


import com.evoucher.adminapi.cms.dao.models.Stock;
import com.evoucher.adminapi.cms.dao.models.Supplier;
import com.evoucher.adminapi.cms.service.models.StockDTO;
import com.evoucher.adminapi.cms.service.models.SupplierDTO;
import com.evoucher.adminapi.cms.service.models.request.SupplierRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface StockMapper {

    StockMapper INSTANT = Mappers.getMapper(StockMapper.class);

    Stock toEntity(StockDTO StockDTO);

    StockDTO toDTO(Stock Stock);

    List<StockDTO> toListStockDTOS(List<Stock> Stocks);
}

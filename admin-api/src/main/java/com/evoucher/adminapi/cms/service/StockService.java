package com.evoucher.adminapi.cms.service;


import com.evoucher.adminapi.cms.dao.CategoryRepository;
import com.evoucher.adminapi.cms.dao.StockRepository;
import com.evoucher.adminapi.cms.dao.models.BulkCategory;
import com.evoucher.adminapi.cms.dao.models.Category;
import com.evoucher.adminapi.cms.dao.models.Stock;
import com.evoucher.adminapi.cms.mapper.CategoryMapper;
import com.evoucher.adminapi.cms.mapper.StockMapper;
import com.evoucher.adminapi.cms.service.models.BulkCategoryDTO;
import com.evoucher.adminapi.cms.service.models.CategoryDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.StockDTO;
import com.evoucher.adminapi.cms.service.models.request.CategoryRequest;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.service.EntityService;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService extends EntityService<Stock, Integer, StockDTO> {
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    @Override
    public JpaRepository<Stock, Integer> getRepository() {
        return stockRepository;
    }

    @Override
    public String getEntityType() {
        return "Stock";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Integer id) {
        return new EntityNotFoundException("Stock not found with ID: " + id);
    }

    @Override
    public Stock toEntity(StockDTO dto) {
        return stockMapper.toEntity(dto);
    }

    @Override
    public StockDTO toDto(Stock entity) {
        return stockMapper.toDTO(entity);
    }

    public List<StockDTO> getAll() {
        log.info("Getting all stocks");
        List<Stock> stocks = stockRepository.findAll();
        return stocks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<StockDTO> getAllFromDate(String fromDate) {
        log.info("Getting all stocks from date: {}", fromDate);
        try {
            Date startDate = parseDate(fromDate);
            List<Stock> stocks = stockRepository.findAllByInsertedAtGreaterThanEqual(startDate);
            return stocks.stream()
                    .map(stockMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting stocks: {}", e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Page<StockDTO> getAllFromDate(String fromDate, Integer page, Integer pageSize) {
        log.info("Getting stocks with pagination from date: {}", fromDate);
        try {
            Date startDate = parseDate(fromDate);
            
            // Convert page number to zero-based
            int pageNumber = (page != null ? page : 1) - 1;
            int size = pageSize != null ? pageSize : 10;
            Pageable pageable = PageRequest.of(pageNumber, size);

            Page<Stock> stockPage = stockRepository.findAllByInsertedAtGreaterThanEqual(startDate, pageable);
            
            List<StockDTO> stockDTOs = stockPage.getContent().stream()
                    .map(stockMapper::toDTO)
                    .collect(Collectors.toList());
            
            return new PageImpl<>(stockDTOs, pageable, stockPage.getTotalElements());
        } catch (Exception e) {
            log.error("Error getting stocks: {}", e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Date parseDate(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return new Date(0); // Return earliest possible date if not specified
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            throw new CustomCodeException("Invalid date format. Use yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        }
    }

    public Map<String, Long> getStockSummary(String fromDate) {
        log.info("Getting stock summary from date: {}", fromDate);
        try {
            Date startDate = parseDate(fromDate);
            return stockRepository.getStockSummary(startDate);
        } catch (Exception e) {
            log.error("Error getting stock summary: {}", e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

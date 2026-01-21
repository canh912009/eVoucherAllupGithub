package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.common.enums.SystemType;
import asia.castis.evoucher.api.dto.request.bulk.BulkBrandRequest;
import asia.castis.evoucher.api.dto.request.bulk.BulkCategoryRequest;
import asia.castis.evoucher.api.dto.request.bulk.BulkGoodsRequest;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV1;
import asia.castis.evoucher.api.dto.response.*;
import asia.castis.evoucher.api.entity.BulkBrand;
import asia.castis.evoucher.api.entity.BulkCategory;
import asia.castis.evoucher.api.entity.BulkGoods;
import asia.castis.evoucher.api.repository.BulkBrandRepository;
import asia.castis.evoucher.api.repository.BulkCategoryRepository;
import asia.castis.evoucher.api.repository.BulkGoodsRepository;
import asia.castis.evoucher.api.service.BeConnector;
import asia.castis.evoucher.api.service.BulkDetailService;
import asia.castis.evoucher.api.service.BulkVoucherService;
import asia.castis.evoucher.api.service.PurchaseChildService;
import asia.castis.evoucher.api.service.generator.BulkResponseGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static asia.castis.evoucher.api.service.version1.impl.VoucherServiceImpl.QUANTITY_SUCCESS_CODE;

@Service
@Slf4j
@RequiredArgsConstructor
public class BulkDetailServiceImpl implements BulkDetailService {
    public static final String DISPLAY_IDX = "displayIdx";
    public static final String SUCCESS = "Success";
    private final BulkCategoryRepository bulkCategoryRepository;
    private final BulkBrandRepository bulkBrandRepository;
    private final BulkGoodsRepository bulkGoodsRepository;
    private final BulkResponseGenerator bulkResponseGenerator;
    private final BeConnector beConnector;

    @Override
    public PageResponse<BulkCategoryResponse> getBulkCategories(BulkCategoryRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNum() - 1, request.getPageSize(), getSort());
        Page<BulkCategory> rawCategories;
        if (Objects.nonNull(request.getName()) && !request.getName().isEmpty()) {
            rawCategories = bulkCategoryRepository.findValidCategory_ByGoodsIdAndCategoryName(request.getGoodsId(), request.getName(), pageable);
        } else {
            rawCategories = bulkCategoryRepository.findValidCategory_ByGoodsId(request.getGoodsId(), pageable);
        }
        List<BulkCategoryResponse> categories = rawCategories.stream().map(bulkResponseGenerator::toBulkCategoryResponse).collect(Collectors.toList());

        // Return response
        PageResponse<BulkCategoryResponse> response = new PageResponse<>();
        response.setTotalCount(rawCategories.getTotalPages());
        response.setPageNum(rawCategories.getNumber() + 1);
        response.setPageSize(rawCategories.getSize());
        response.setStatus(SUCCESS);
        response.setPageData(categories);
        return response;
    }

    @Override
    public PageResponse<BulkBrandResponse> getBulkBrands(BulkBrandRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNum() - 1, request.getPageSize(), getSort());
        Page<BulkBrand> rawBrands;
        if (Objects.nonNull(request.getName()) && !request.getName().isEmpty()) {
            rawBrands = bulkBrandRepository.findValidBrand_ByBulkCtgrIdAndBrandName(request.getBulkCategoryId(), request.getName(), pageable);
        } else {
            rawBrands = bulkBrandRepository.findVlidBrand_ByBulkCtgrId(request.getBulkCategoryId(), pageable);
        }
        List<BulkBrandResponse> brands = rawBrands.stream().map(bulkResponseGenerator::toBulkBrandResponse).collect(Collectors.toList());
        // Return response
        PageResponse<BulkBrandResponse> response = new PageResponse<>();
        response.setTotalCount(rawBrands.getTotalPages());
        response.setPageNum(rawBrands.getNumber() + 1);
        response.setPageSize(rawBrands.getSize());
        response.setStatus(SUCCESS);
        response.setPageData(brands);
        return response;
    }

    @Override
    public PageResponse<BulkGoodsResponse> getBulkGoods(BulkGoodsRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNum() - 1, request.getPageSize(), getSort());
        Page<BulkGoods> rawGoods;
        if (Objects.nonNull(request.getName()) && !request.getName().isEmpty()) {
            rawGoods = bulkGoodsRepository.findByBulkBrandIdAndGoodsName(request.getBulkBrandId(), request.getName(), pageable);
        } else {
            rawGoods = bulkGoodsRepository.findAllByBulkBrandId(request.getBulkBrandId(), pageable);
        }
        List<BulkGoodsResponse> goodsList = rawGoods.stream().map(bulkResponseGenerator::toBulkGoodsResponse).collect(Collectors.toList());

        try {
            List<Long> goodsIds = goodsList.stream().map(bulkGoods -> bulkGoods.getGoods().getId()).collect(Collectors.toList());
            ResponseData<Map<Long, Integer>> goodsQuantityResponse = callBeApiGetRemainingCount(goodsIds);
            if (QUANTITY_SUCCESS_CODE == goodsQuantityResponse.getCode()) {
                Map<Long, Integer> remainingCount = goodsQuantityResponse.getData();
                goodsList.forEach(bulkGoodsResponse ->
                        bulkGoodsResponse.getGoods().setRemainingCount(
                                remainingCount.get(bulkGoodsResponse.getGoods().getId())));
            }
        } catch (Exception e) {
            log.error("Exception getting voucher quantity", e);
        }
        // Return response
        PageResponse<BulkGoodsResponse> response = new PageResponse<>();
        response.setTotalCount(rawGoods.getTotalPages());
        response.setPageNum(rawGoods.getNumber() + 1);
        response.setPageSize(rawGoods.getSize());
        response.setStatus(SUCCESS);
        response.setPageData(goodsList);
        return response;
    }

    private static Sort getSort() {
        Sort.Direction direction = Sort.Direction.ASC;
        return Sort.by(direction, DISPLAY_IDX);
    }

    private ResponseData<Map<Long, Integer>> callBeApiGetRemainingCount(List<Long> goodsId) throws URISyntaxException {
        ResponseData<Map<Long, Integer>> goodsQuantityResponse = beConnector.getRemainingCount(goodsId);
        log.info("Goods quantity response={}", goodsQuantityResponse);
        return goodsQuantityResponse;
    }

}

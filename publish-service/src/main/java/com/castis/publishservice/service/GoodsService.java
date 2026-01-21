package com.castis.publishservice.service;

import com.castis.publishservice.dto.GoodsDTO;
import com.castis.publishservice.dto.GoodsDTO;
import com.castis.publishservice.dto.queue.GoodsRequest;
import com.castis.publishservice.entity.Goods;
import com.castis.publishservice.exception.defineException.NotFoundException;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import com.castis.publishservice.mapper.GoodsMapper;
import com.castis.publishservice.repository.GoodRepository;
import com.castis.publishservice.utils.enum_template.GoodType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsService {
    private final GoodRepository repository;
    private static final EnumSet<GoodType> HAD_EXT_PIN = EnumSet.of(GoodType.SI, GoodType.PP);
    public boolean hasExtPinByType(GoodType type) {
        return type != null && HAD_EXT_PIN.contains(type);
    }
    private static final GoodsMapper mapper = GoodsMapper.INSTANCE;
    @Transactional
    public List<GoodsRequest> findAllGoodByParentId(Long parentId)
            throws ServerRuntimeException {
        if (Objects.isNull(parentId)) {
            throw new ServerRuntimeException("Null parent id is not allowed.");
        }
        List<Goods> products = repository.findAllByParentId(parentId);
        if (Objects.isNull(products) || products.isEmpty()) {
            log.warn("Can not find any product with parent id={}, return empty list", parentId);
            return new ArrayList<>();
        }

        return products.stream().map(mapper::toRequest).collect(Collectors.toList());
    }
    public GoodsDTO findById(Long goodsId) {
        Goods product = repository.findById(goodsId).orElseThrow(() -> NotFoundException.goods(goodsId));
        return mapper.toDTO(product);
    }

    public List<GoodsDTO> findAllByIdIn(List<Long> goodsId) {
        try {
            List<Goods> products = repository.findAllByIdIn(goodsId);

            if (Objects.isNull(products) || products.isEmpty()) {
                log.warn("Can not find any product with ids={}, return empty list", goodsId);
                return new ArrayList<>();
            }

            return products.stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServerRuntimeException(e.getMessage());
        }
    }
}

package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.SystemType;
import com.evoucher.evoucherbe.dto.GoodDto;
import com.evoucher.evoucherbe.entity.Goods;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.GoodMapper;
import com.evoucher.evoucherbe.repository.GoodsRepository;
import com.evoucher.evoucherbe.utils.ErrorCode;
import com.evoucher.evoucherbe.utils.MessageUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoodBasicService extends EntityService<Goods, Long, GoodDto> {
    private final GoodMapper mapper;
    public static final Set<SystemType> PARENT_VOUCHER = EnumSet.of(SystemType.CHOICE, SystemType.BULK);
    public static final Set<SystemType> EXTERNAL_GOOD = EnumSet.of(SystemType.EXTERNAL, SystemType.UR_BOX, SystemType.GIFTPOP, SystemType.WATANE);
    @Getter
    private final GoodsRepository repository;

    public class ErrorCode {
        public static final int EXPIRED_GOOD = 10014;
    }
    public class ErrorString {
        public static final String EXPIRE_GOOD = "evoucher.goods.expired";
    }
//    @Override
//    public JpaRepository<Goods, Long> getRepository() {
//        return repository;
//    }

    @Override
    public String getEntityType() {
        return "good";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Long id) {
        return new EntityNotFoundException("Can not find good by id: " + id, com.evoucher.evoucherbe.utils.ErrorCode.GOOD_NOT_FOUND);
    }

    @Override
    public Goods toEntity(GoodDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public GoodDto toDto(Goods entity) {
        return mapper.toDto(entity);
    }
    public Map<Long, GoodDto> getGoodMapByIdIn(Collection<Long> goodsId) {
        log.info("get goods by good id in {}", goodsId);
        if (CollectionUtils.isEmpty(goodsId)) {
            log.warn("id list is empty");
            return new HashMap<>();
        }
        try {
            List<Goods> goods = repository.findAllByIdIn(goodsId).orElse(new ArrayList<>());
            HashMap<Long, GoodDto> result = (HashMap<Long, GoodDto>) goods.stream()
                    .collect(Collectors.toMap(Goods::getId, mapper::toDto));
            log.info("found: {}", result);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new HashMap<>();
        }
    }
}

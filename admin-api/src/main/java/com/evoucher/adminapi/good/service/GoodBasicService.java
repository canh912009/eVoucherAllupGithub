package com.evoucher.adminapi.good.service;

import com.evoucher.adminapi.admin.service.VoucherBasicService;
import com.evoucher.adminapi.cms.dao.GoodsRepository;
import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.mapper.GoodsMapper;
import com.evoucher.adminapi.cms.service.models.GoodsDTO;
import com.evoucher.adminapi.common.enums.SystemType;
import com.evoucher.adminapi.common.enums.VoucherStatusCode;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.service.EntityService;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodBasicService extends EntityService<Goods, Integer, GoodsDTO> {
    private static final GoodsMapper mapper = GoodsMapper.INSTANCE;
    private final GoodsRepository repository;

    private static final EnumSet<SystemType> ALLOWED_EXTEND_SYSTEM = EnumSet.of(SystemType.BULK, SystemType.CHOICE, SystemType.INTERNAL, SystemType.VNPT_EPAY, SystemType.XPAY);
    public static class ErrorCode {
        private ErrorCode() {}
        public static final String SYSTEM_CAN_NOT_EXTEND = "good.system.can.not.extend";
    }


    @Override
    public JpaRepository<Goods, Integer> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "good";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Integer id) {
        return new EntityNotFoundException(
                MessageUtils.getMessage("evoucher.goods.not.found.with.id", id));
    }

    @Override
    public Goods toEntity(GoodsDTO dto) {
        return mapper.toEntity(dto);
    }

    @Override
    @Transactional
    public GoodsDTO toDto(Goods entity) {
        return mapper.toDTO(entity);
    }

    @Transactional
    public Map<Integer, GoodsDTO> getGoodMapByIdIn(Collection<Integer> goodsId) {
        log.info("get goods by good id in: {}", goodsId);
        if (CollectionUtils.isEmpty(goodsId)) {
            log.warn("id list is empty");
            return new HashMap<>();
        }
        try {
            List<Goods> goods = repository.findAllByIdIn(goodsId).orElse(new ArrayList<>());
            HashMap<Integer, GoodsDTO> result = (HashMap<Integer, GoodsDTO>) goods.stream()
                    .collect(Collectors.toMap(Goods::getId, mapper::toDTO));
            log.info("found: {}", result);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new HashMap<>();
        }
    }
    public Map<Integer, GoodsDTO> getGoodMapByBrandIdIn(Collection<String> brandIds) {
        log.info("get goods by brand id in: {}", brandIds);
        if (CollectionUtils.isEmpty(brandIds)) {
            log.warn("brand id list is empty");
            return new HashMap<>();
        }
        try {
            List<Goods> goods = repository.findAllByBrandIdIn(brandIds).orElse(new ArrayList<>());
            HashMap<Integer, GoodsDTO> result = (HashMap<Integer, GoodsDTO>) goods.stream()
                    .collect(Collectors.toMap(Goods::getId, this::toDto));
            log.info("found: {}", result);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new HashMap<>();
        }
    }
    @Transactional
    public Map<String, GoodsDTO> getGoodCodeObjectMapByBrandIdIn(Collection<String> brandIds) {
        log.info("get goods by brand id in: {}", brandIds);
        if (CollectionUtils.isEmpty(brandIds)) {
            log.warn("brand id list is empty");
            return new HashMap<>();
        }
        try {
            List<Goods> goods = repository.findAllByBrandIdIn(brandIds).orElse(new ArrayList<>());
            Map<String, GoodsDTO> result = goods.stream()
                    .collect(Collectors.toMap(Goods::getSupplierGoodsId, mapper::toDtoWithoutChildrenInfo, (old, newOne) -> old));
            log.info("found: {}", result);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new HashMap<>();
        }
    }

    public static boolean allowExtendUsageTime(SystemType type) {
        return ALLOWED_EXTEND_SYSTEM.contains(type);
    }

    public void validateExistByIdIn(Collection<Integer> ids) throws EntityNotFoundException {
        List<Integer> existedIds = this.findAllByIdIn(ids).stream().map(Goods::getId).collect(Collectors.toList());
        ids.forEach(o -> {
            if (!existedIds.contains(o)) {
                throw this.getNotFoundException(o);
            }
        });
    }

    public boolean existsBySupplierGoodsId(String supplierGoodsId){
        try {
            return repository.existsBySupplierGoodsId(supplierGoodsId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void updateStoreList(Map<String, GoodsDTO> updatedMap) {

        if (CollectionUtils.isEmpty(updatedMap)) {
            log.info("updating store good list is empty");
            return;
        }
        try {
            log.info("update good store: {}",
                    updatedMap.entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getIncludeStoreIds())));

            List<Goods> dbGoods = repository.findAllBySupplierGoodsIdIn(updatedMap.keySet());
            dbGoods.forEach(o ->
                    o.setIncludeStoreIds(updatedMap.get(o.getSupplierGoodsId()).getIncludeStoreIds()));

            repository.saveAll(dbGoods);
            log.info("update good store success");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


     public static void validateExtendable(SystemType systemType) throws CustomCodeException {
        if (allowExtendUsageTime(systemType)) {
            log.info("voucher system {} is valid", systemType);
        } else {
            log.error("voucher system {} is invalid", systemType);
            throw new CustomCodeException(MessageUtils.getMessage(ErrorCode.SYSTEM_CAN_NOT_EXTEND), HttpStatus.BAD_REQUEST);
        }
    }
}

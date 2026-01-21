package com.castis.publishservice.service;

import com.castis.publishservice.dto.BrandDTO;
import com.castis.publishservice.dto.queue.BrandRequest;
import com.castis.publishservice.entity.Brand;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import com.castis.publishservice.mapper.BrandMapper;
import com.castis.publishservice.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository repository;

    private static final BrandMapper mapper = BrandMapper.INSTANCE;

    public List<BrandRequest> findRequestByIds(List<String> ids) throws ServerRuntimeException {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            throw new ServerRuntimeException("Null or empty brand ids");
        }
        List<Brand> brands = repository.findAllById(ids);
        return brands.stream().map(mapper::toRequest).collect(Collectors.toList());
    }

    public List<BrandDTO> findByIdIn(Collection<String> ids) throws ServerRuntimeException {
        try {
            List<Brand> brands = repository.findAllById(ids);
            if (CollectionUtils.isEmpty(brands)) {
                log.warn("No brands found by ids={}", ids);
                return new ArrayList<>();
            }
            return brands.stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }
}

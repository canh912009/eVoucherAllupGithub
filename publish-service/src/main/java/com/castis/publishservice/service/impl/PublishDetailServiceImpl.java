package com.castis.publishservice.service.impl;

import com.castis.publishservice.dto.PublishDetailDTO;
import com.castis.publishservice.entity.PublishDetail;
import com.castis.publishservice.exception.defineException.NotFoundException;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import com.castis.publishservice.mapper.PublishDetailMapper;
import com.castis.publishservice.repository.PublishDetailRepository;
import com.castis.publishservice.service.PublishDetailService;
import com.castis.publishservice.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublishDetailServiceImpl implements PublishDetailService {
    private final PublishDetailRepository repository;
    private static final PublishDetailMapper mapper = PublishDetailMapper.INSTANCE;

    @Override
    public PublishDetailDTO toDTO(PublishDetail entity) {
        return mapper.toDTO(entity);
    }

    @Override
    public PublishDetailDTO getById(Long id) throws NotFoundException {
        PublishDetail detail = repository.findById(id).orElseThrow(() -> NotFoundException.publishDetail(id));
        return toDTO(detail);
    }

    @Override
    public List<PublishDetailDTO> getAllByIds(List<Long> ids) throws ServerRuntimeException {
        List<PublishDetail> details = repository.getAllByPublishDtlIdIn(ids);
        if (Objects.nonNull(details)) {
            return details.stream().map(this::toDTO).collect(Collectors.toList());
        }
        log.warn("Can not find publish detail by id in {}. Return empty list", ids);
        return new ArrayList<>();
    }

    @Override
    public List<PublishDetailDTO> saveAll(List<PublishDetailDTO> entitiesDto) throws ServerRuntimeException {
        if (Objects.isNull(entitiesDto) || entitiesDto.isEmpty()) {
            throw new ServerRuntimeException("Null or empty list of publish detail");
        }
        log.info("Save all publish details for publishId={}", entitiesDto.get(0).getPublishId());
        try {
            List<PublishDetail> entities = entitiesDto.stream().map(mapper::toEntity).collect(Collectors.toList());
            List<PublishDetail> details = repository.saveAll(entities);
            return details.stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public PublishDetailDTO save(PublishDetailDTO dto) throws ServerRuntimeException {
        log.info("Save publish detail={}", Utils.toJson(dto));
        if (Objects.isNull(dto)) {
            log.error("Publish detail can not be null");
            throw new ServerRuntimeException("Publish detail can not be null");
        }
        try {
            PublishDetail detail = repository.save(mapper.toEntity(dto));
            return mapper.toDTO(detail);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void transferVoucher(PublishDetailDTO target, PublishDetailDTO source) {
        mapper.transferVoucher(target, source);
    }
}

package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.OperatorRequestRepository;
import com.evoucher.adminapi.admin.dao.models.OperatorRequest;
import com.evoucher.adminapi.admin.enums.OperatorRequestStatus;
import com.evoucher.adminapi.admin.mapper.OperatorRequestMapper;
import com.evoucher.adminapi.admin.service.models.OperatorRequestDto;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.service.EntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperatorRequestBasicService extends EntityService<OperatorRequest, Long, OperatorRequestDto> {
    private final OperatorRequestRepository repository;
    private final OperatorRequestMapper mapper;
    @Override
    public JpaRepository<OperatorRequest, Long> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "operator request";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Long id) {
        return new EntityNotFoundException("can not find operator request by id: " + id);
    }

    @Override
    public OperatorRequest toEntity(OperatorRequestDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public OperatorRequestDto toDto(OperatorRequest entity) {
        return mapper.toDto(entity);
    }

    public List<OperatorRequestDto> findAllByEv(String ev) throws CustomCodeException {
        log.info("find all operator request by ev {}", ev);
        try {
            List<OperatorRequest> entities = repository.findAllByEv(ev);

            if (CollectionUtils.isEmpty(entities)) {
                log.info("found no requests by ev: {}", ev);
                return new ArrayList<>();
            }
            return entities.stream().map(mapper::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Long countAllByEvAndStatus(String ev, OperatorRequestStatus status) throws CustomCodeException {
        log.info("count all by ev - {} and status - {}", ev, status);
        try {
            return repository.countAllByEvAndReqStatus(ev, status);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

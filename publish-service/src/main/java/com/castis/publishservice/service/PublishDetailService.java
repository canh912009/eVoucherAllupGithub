package com.castis.publishservice.service;

import com.castis.publishservice.dto.PublishDetailDTO;
import com.castis.publishservice.entity.PublishDetail;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;

import java.util.List;

public interface PublishDetailService {
    PublishDetailDTO toDTO(PublishDetail entity);

    PublishDetailDTO getById(Long id);

    List<PublishDetailDTO> getAllByIds(List<Long> ids) throws ServerRuntimeException;

    List<PublishDetailDTO> saveAll(List<PublishDetailDTO> DTOs) throws ServerRuntimeException;

    PublishDetailDTO save(PublishDetailDTO dto) throws ServerRuntimeException;

    void transferVoucher(PublishDetailDTO target, PublishDetailDTO source);
}

package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.models.Publish;
import com.evoucher.adminapi.admin.mapper.PublishMapper;
import com.evoucher.adminapi.admin.service.models.PublishDTO;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.service.EntityService;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import static com.evoucher.adminapi.admin.service.PublishServiceImpl.EVOUCHER_PUBLISH_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublishBasicService extends EntityService<Publish, Integer, PublishDTO> {
    private final JpaRepository<Publish, Integer> repository;
    private final PublishMapper mapper;
    @Override
    public JpaRepository<Publish, Integer> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "publish";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Integer id) {
        return new EntityNotFoundException(MessageUtils.getMessage(EVOUCHER_PUBLISH_NOT_FOUND));
    }

    @Override
    public Publish toEntity(PublishDTO dto) {
        return mapper.toPublish(dto);
    }

    @Override
    public PublishDTO toDto(Publish entity) {
        return mapper.toPublishDTO(entity);
    }
    public PublishDTO findPublishRequestById(Integer id) throws EntityNotFoundException {
        return mapper.toOperatorReqInfo(findById(id));
    }
}

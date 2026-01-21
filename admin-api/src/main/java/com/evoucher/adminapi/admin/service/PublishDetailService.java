package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.PublishDetailRepository;
import com.evoucher.adminapi.admin.dao.models.PublishDetail;
import com.evoucher.adminapi.admin.mapper.PublishDetailMapper;
import com.evoucher.adminapi.admin.service.models.PublishDetailDto;
import com.evoucher.adminapi.cms.service.models.EndUserDTO;
import com.evoucher.adminapi.common.enums.PublishDetailStatus;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.service.EntityService;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

import static com.evoucher.adminapi.common.enums.PublishDetailStatus.STATUS_FAIL;

@RequiredArgsConstructor
@Service
@Slf4j
public class PublishDetailService extends EntityService<PublishDetail, Integer, PublishDetailDto> {
    @Getter
    private final PublishDetailRepository repository;
    private final PublishDetailMapper mapper;

    @Override
    public String getEntityType() {
        return "publish detail";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Integer id) {
        return new EntityNotFoundException(
                MessageUtils.getMessage("evoucher.publish.detail.not.found")
        );
    }

    @Override
    public PublishDetail toEntity(PublishDetailDto dto) {
        return mapper.dtoToEntity(dto);
    }

    @Override
    public PublishDetailDto toDto(PublishDetail entity) {
        return mapper.entityToDto(entity);
    }

    public List<EndUserDTO> publishMetaDataDetail(@NotNull Integer publishId) throws CustomCodeException {
        log.info("get all publish meta data by publish id: {}", publishId);
        try {
            List<EndUserDTO> result =  repository.publishMetaDataDetail(publishId);
            result.forEach(endUserDTO -> {
                // For each end user dto, if status is activated and publishResultMessage is null then set default message
                if (STATUS_FAIL.contains(PublishDetailStatus.valueOf(endUserDTO.getSmsStatus()))
                        && (Objects.isNull(endUserDTO.getPublishResultMessage()) || endUserDTO.getPublishResultMessage().isEmpty())) {
                    endUserDTO.setPublishResultMessage("Exception while publishing voucher");
                }
            });
            log.debug("result: {}", result);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

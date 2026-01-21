package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.dto.PublishDetailDto;
import com.evoucher.evoucherbe.entity.PublishDetail;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.PublishDetailMapper;
import com.evoucher.evoucherbe.repository.PublishDetailRepository;
import com.evoucher.evoucherbe.utils.ErrorCode;
import com.evoucher.evoucherbe.utils.MessageUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublishDetailBasicService extends EntityService<PublishDetail, Integer, PublishDetailDto>  {
    @Getter
    private final PublishDetailRepository repository;
    private final PublishDetailMapper mapper;
    private static final String notFoundKey = "evoucher.publish.detail.not.found";

    @Override
    public String getEntityType() {
        return "publish detail";
    }

    @Override
    public EntityNotFoundException getNotFoundException(Integer id) throws EntityNotFoundException {
        return new EntityNotFoundException(
                MessageUtils.getMessage(notFoundKey),
                ErrorCode.PUBLISH_NOT_FOUND);
    }

    @Override
    public PublishDetail toEntity(PublishDetailDto dto) {
        return mapper.dtoToEntity(dto);
    }

    @Override
    public PublishDetailDto toDto(PublishDetail entity) {
        return mapper.entityToDto(entity);
    }

    public void updateReceiveMobileNumber(Integer id, String newEndUser) throws EntityNotFoundException, CustomCodeException {
        log.info("update receive mobile number of {} to {}", id, newEndUser);
        try {
            PublishDetail entity = findById(id);

            entity.setReceiverMobileNo(newEndUser);

            repository.save(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

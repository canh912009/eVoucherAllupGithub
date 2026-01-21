package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.SMSType;
import com.evoucher.evoucherbe.dto.PublishDto;
import com.evoucher.evoucherbe.entity.Publish;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.PublishMapper;
import com.evoucher.evoucherbe.repository.PublishRepository;
import com.evoucher.evoucherbe.utils.ErrorCode;
import com.evoucher.evoucherbe.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.EnumSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublishBasicService extends EntityService<Publish, Integer, PublishDto>  {
    private static final EnumSet<SMSType> USE_CUSTOMER_NAME_FOR_TARGET_NAME = EnumSet.of(SMSType.DOWNLOAD);
    public static final String NOT_FOUND = "evoucher.publish.not.found";
    private final PublishRepository repository;
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
        return new EntityNotFoundException(MessageUtils.getMessage(NOT_FOUND), ErrorCode.PUBLISH_NOT_FOUND);
    }

    @Override
    public Publish toEntity(PublishDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public PublishDto toDto(Publish entity) {
        return mapper.toDto(entity);
    }
    public static boolean useCustomerNameInsteadOfUserNameBySmsType(SMSType smsType) {
        return USE_CUSTOMER_NAME_FOR_TARGET_NAME.contains(smsType);
    }
}

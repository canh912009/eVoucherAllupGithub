package com.evoucher.partner.service.service;

import com.evoucher.partner.service.bean.dtos.VnptProviderDto;
import com.evoucher.partner.service.bean.entity.VnptProvider;
import com.evoucher.partner.service.bean.enum_type.EnumValidYn;
import com.evoucher.partner.service.common.Common;
import com.evoucher.partner.service.exception.define_exception.CustomCodeException;
import com.evoucher.partner.service.exception.define_exception.VnptException;
import com.evoucher.partner.service.mapper.VnptProviderMapper;
import com.evoucher.partner.service.repository.VnptProviderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VnptProviderBasicService extends BasicService<VnptProvider, String, VnptProviderDto> {
    private static final VnptProviderMapper MAPPER = VnptProviderMapper.INSTANCE;

    private final VnptProviderRepository repository;
    private final ObjectMapper objectMapper;

    public static class ErrorCode {
        private ErrorCode() {}
        public static final int NOT_FOUND = 1096;
        public static final int INVALID = 1098;
        public static final int FACE_VALUE_IS_NOT_SUPPORTED = 1099;
    }

    private static final Map<Integer, String> ERROR_MSG_MAP = new HashMap<>();
    static {
        ERROR_MSG_MAP.put(ErrorCode.NOT_FOUND, "can not find vnpt provider by id");
        ERROR_MSG_MAP.put(ErrorCode.INVALID, "VNPT provider is invalid");
    }

    public static String getErrorMessageByCode(Integer code) {
        return ERROR_MSG_MAP.get(code);
    }
    @Override
    public JpaRepository<VnptProvider, String> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "vnpt provider";
    }

    @Override
    public EntityNotFoundException getNotFoundException(String id) {
        return new EntityNotFoundException(
                getErrorMessageByCode(ErrorCode.NOT_FOUND) + ": " + id
        );
    }

    @Override
    public VnptProvider toEntity(VnptProviderDto dto) {
        return MAPPER.toEntity(dto);
    }

    @Override
    public VnptProviderDto toDto(VnptProvider entity) {
        return MAPPER.toDto(entity);
    }

    public VnptProviderDto getValidProvider(String providerCode) throws CustomCodeException {
        if (StringUtils.isEmpty(providerCode)) {
            log.info("provider code is empty");
            throw new CustomCodeException(Common.SERVER_ERROR, "provider code is empty");
        }
        VnptProviderDto dto = findDtoById(providerCode);
        if (dto.getValidYn() != EnumValidYn.Y) {
            log.error("VNPT Provider : {} is invalid", providerCode);
            throw new CustomCodeException(ErrorCode.INVALID, "provider is invalid");
        }
        return dto;
    }
    public void validateFaceValue(VnptProviderDto dto, Long faceValue) throws VnptException, CustomCodeException {
        List<Long> supportedValues = new ArrayList<>();
        try {
            if (StringUtils.isEmpty(dto.getAllowedCardFaces())) {
                supportedValues = objectMapper.readValue(dto.getAllowedCardFaces(), new TypeReference<List<Long>>(){});
            }
            if (!supportedValues.contains(faceValue)) {
                log.info("provider [{}] supports : {} only ",dto.getProviderCd(), supportedValues);
                log.error("{} is not supported", faceValue);

                throw new CustomCodeException(
                        ErrorCode.FACE_VALUE_IS_NOT_SUPPORTED,
                        "Face value is not supported"
                );
            }
        } catch (JsonProcessingException e) {
            throw VnptException.vnptUnknownException(e);
        }
    }
}

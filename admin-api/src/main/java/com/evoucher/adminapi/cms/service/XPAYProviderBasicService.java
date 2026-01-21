package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.cms.dao.models.XPAYProvider;
import com.evoucher.adminapi.cms.mapper.XPAYProviderMapper;
import com.evoucher.adminapi.cms.service.models.XPAYProviderDto;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.service.EntityService;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class XPAYProviderBasicService extends EntityService<XPAYProvider, String, XPAYProviderDto> {
    private static final XPAYProviderMapper MAPPER = XPAYProviderMapper.INSTANCE;

    private final JpaRepository<XPAYProvider, String> repository;

    public static class ErrorCode {
        private ErrorCode() {}
        public static final int NOT_FOUND = 1096;
        public static final int INVALID = 1097;
        public static final int EXISTED = 1098;
    }

    private static final Map<Integer, String> ERROR_MSG_MAP = new HashMap<>();
    static {
        ERROR_MSG_MAP.put(ErrorCode.NOT_FOUND, "xpay.provider.not.found.by.id");
        ERROR_MSG_MAP.put(ErrorCode.INVALID, "xpay.provider.invalid");
        ERROR_MSG_MAP.put(ErrorCode.EXISTED, "xpay.provider.is.existed");
    }

    public static String getErrorMessageByCode(Integer code) {
        return ERROR_MSG_MAP.get(code);
    }
    @Override
    public JpaRepository<XPAYProvider, String> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "xpay provider";
    }

    @Override
    public EntityNotFoundException getNotFoundException(String id) {
        return new EntityNotFoundException(
                MessageUtils.getMessage(getErrorMessageByCode(ErrorCode.NOT_FOUND), id));
    }

    @Override
    public XPAYProvider toEntity(XPAYProviderDto dto) {
        return MAPPER.toEntity(dto);
    }

    @Override
    public XPAYProviderDto toDto(XPAYProvider entity) {
        return MAPPER.toDto(entity);
    }
}

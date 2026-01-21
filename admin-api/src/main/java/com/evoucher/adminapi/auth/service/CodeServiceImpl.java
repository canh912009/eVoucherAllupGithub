package com.evoucher.adminapi.auth.service;

import com.evoucher.adminapi.auth.dao.CodeGroupRepository;
import com.evoucher.adminapi.auth.dao.CodeRepository;
import com.evoucher.adminapi.auth.dao.models.Code;
import com.evoucher.adminapi.auth.mapper.CodeMapper;
import com.evoucher.adminapi.auth.service.models.*;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeServiceImpl implements CodeService {

    private final CodeRepository codeRepository;
    private final CodeGroupRepository codeGroupRepository;
    private final CodeMapper codeMapper;


    @Override
    public CodeDTO findById(String codeId, String codeGroupId) {
        log.info("Find Code by codeId: {} and codeGroupId: {}", codeId, codeGroupId);
        Code code = codeRepository.findByCodeIdAndCodeGroupIdAndValidYn(codeId, codeGroupId, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.code.not.found"),
                        HttpStatus.BAD_REQUEST));

        return codeMapper.toCodeDTO(code);
    }

    @Override
    public CodeDTO createCode(CodeRequest codeRequest) {
        // check validate codeRequest
        validateCodeRequest(codeRequest);

        // check CodeGroup had been existed
        checkCodeGroupExist(codeRequest);

        Code code = codeMapper.toCode(codeRequest);
        code.setValidYn(EnumValidYn.Y);

        log.info("Create Code with name: {}", codeRequest.getCodeName());
        code = codeRepository.save(code);
        return codeMapper.toCodeDTO(code);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CodeDTO editCode(String codeId, String codeGroupId, CodeRequest codeRequest) {
        log.info("Find Code with codeId: {} and codeGroupId: {}", codeId, codeGroupId);
        Code oldCode = codeRepository.findByCodeIdAndCodeGroupIdAndValidYn(codeId, codeGroupId, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.code.not.found"), HttpStatus.BAD_REQUEST));
        log.info("Delete Code old with codeId: {} and codeGroupId: {}", codeId, codeGroupId);
        codeRepository.delete(oldCode);

        // check validate codeRequest
        validateCodeRequest(codeRequest);

        // check codeGroupId is exits
        checkCodeGroupExist(codeRequest);

        Code code = codeMapper.toCode(codeRequest);
        code.setValidYn(EnumValidYn.Y);

        log.info("Update Code with id: {}", codeId);
        code = codeRepository.save(code);

        return codeMapper.toCodeDTO(code);
    }

    @Override
    public String deleteCode(String codeId, String codeGroupId) {
        log.info("Find Code with codeId: {} and codeGroupId: {}", codeId, codeGroupId);
        Code code = codeRepository.findByCodeIdAndCodeGroupIdAndValidYn(codeId, codeGroupId, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.code.not.found"),
                        HttpStatus.BAD_REQUEST));

        code.setValidYn(EnumValidYn.N);
        log.info("Delete Menu with codeId: {} and codeGroupId: {}", codeId, codeGroupId);
        codeRepository.save(code);

        return codeId;
    }

    @Override
    public Page<CodeDTO> searchCode(FilterSearchAuth filterSearchAuth) {
        int page = ObjectUtils.isEmpty(filterSearchAuth.getPage()) ? 0 : filterSearchAuth.getPage() - 1;
        int pageSize = ObjectUtils.isEmpty(filterSearchAuth.getPageSize()) ? 10 : filterSearchAuth.getPageSize();

        Pageable pageable = PageRequest.of(page, pageSize);
        List<CodeDTO> codeDTOS = codeRepository.searchCode(filterSearchAuth, pageable);
        long countMenu = 0L;
        if (!CollectionUtils.isEmpty(codeDTOS)) {
            countMenu = codeRepository.countCode(filterSearchAuth);
        }
        return new PageImpl<>(codeDTOS, pageable, countMenu);
    }

    private void validateCodeRequest(CodeRequest codeRequest) {
        log.info("Check Code is existed with codeId: {} and codeGroupId: {}", codeRequest.getCodeId(), codeRequest.getCodeGroupId());
        boolean isCodeExist = codeRepository.existsByCodeIdAndCodeGroupIdAndValidYn(
                codeRequest.getCodeId(), codeRequest.getCodeGroupId(), EnumValidYn.Y);
        if (isCodeExist) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.code.existed"),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private void checkCodeGroupExist(CodeRequest codeRequest) {
        log.info("Check CodeGroup is existed with  codeGroupId: {}", codeRequest.getCodeGroupId());
        boolean isCodeGroupExist = codeGroupRepository.existsByCodeGroupIdAndValidYn(
                codeRequest.getCodeGroupId(), EnumValidYn.Y);
        if (!isCodeGroupExist) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.code.group.not.found"),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}

package com.evoucher.adminapi.auth.service;

import com.evoucher.adminapi.auth.dao.CodeGroupRepository;
import com.evoucher.adminapi.auth.dao.CodeRepository;
import com.evoucher.adminapi.auth.dao.models.Code;
import com.evoucher.adminapi.auth.dao.models.CodeGroup;
import com.evoucher.adminapi.auth.mapper.CodeGroupMapper;
import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.CodeGroupDTO;
import com.evoucher.adminapi.auth.service.models.CodeGroupRequest;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeGroupServiceImpl implements CodeGroupService {

    private final CodeGroupRepository codeGroupRepository;
    private final CodeRepository codeRepository;

    private final CodeGroupMapper codeGroupMapper;


    @Override
    public CodeGroupDTO findById(String id) {
        log.info("Find CodeGroup by id: {}", id);
        CodeGroup codeGroup = codeGroupRepository.findByCodeGroupIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(
                        () -> new CustomCodeException(
                                MessageUtils.getMessage("evoucher.menu.group.not.found"),
                                HttpStatus.BAD_REQUEST)
                );

        log.info("Get List Code with codeGroupId: {}", id);
        List<Code> codes = codeRepository.findAllByCodeGroupIdAndValidYnOrderBySortOrder(id, EnumValidYn.Y);

        return codeGroupMapper.toCodeGroupDTO(codeGroup, codes);
    }

    @Override
    public CodeGroupDTO createCodeGroup(CodeGroupRequest codeGroupRequest) {
        // check code group existed
        checkCodeGroupExisted(codeGroupRequest);

        CodeGroup codeGroup = codeGroupMapper.toCodeGroup(codeGroupRequest);
        codeGroup.setValidYn(EnumValidYn.Y);

        log.info("Create CodeGroup with name: {}", codeGroupRequest.getCodeGroupName());
        codeGroup = codeGroupRepository.save(codeGroup);
        return codeGroupMapper.toCodeGroupDTO(codeGroup);
    }

    @Override
    public CodeGroupDTO editCodeGroup(String id, CodeGroupRequest codeGroupRequest) {
        log.info("Find CodeGroup with id: {}", id);
        Optional<CodeGroup> codeGroupOptional = codeGroupRepository.findByCodeGroupIdAndValidYn(id, EnumValidYn.Y);
        if (codeGroupOptional.isEmpty())
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.code.group.not.found"), HttpStatus.BAD_REQUEST);

        CodeGroup menuGroup = codeGroupMapper.toCodeGroup(codeGroupRequest);
        menuGroup.setCodeGroupId(id);
        menuGroup.setValidYn(EnumValidYn.Y);

        log.info("Update CodeGroup with id: {}", id);
        menuGroup = codeGroupRepository.save(menuGroup);

        return codeGroupMapper.toCodeGroupDTO(menuGroup);
    }

    private void checkCodeGroupExisted(CodeGroupRequest codeGroupRequest) {
        log.info("Check codeGroupId existed with codeGroupId: {}", codeGroupRequest.getCodeGroupId());
        boolean checkCodeGroupIdExisted = codeGroupRepository.existsByCodeGroupIdAndValidYn(codeGroupRequest.getCodeGroupId(), EnumValidYn.Y);
        if (checkCodeGroupIdExisted)
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.code.group.id.exist"), HttpStatus.BAD_REQUEST);
    }

    @Override
    public String deleteCodeGroup(String id) {
        log.info("Find CodeGroup with id: {}", id);
        CodeGroup menuGroup = codeGroupRepository.findByCodeGroupIdAndValidYn(id, EnumValidYn.Y).orElseThrow(
                () -> new CustomCodeException(MessageUtils.getMessage("evoucher.code.group.not.found"), HttpStatus.BAD_REQUEST)
        );

        menuGroup.setValidYn(EnumValidYn.N);
        log.info("Delete CodeGroup with id: {}", id);
        codeGroupRepository.save(menuGroup);

        log.info("Find all Code with codeGroupId: {}", id);
        List<Code> codes = codeRepository.findAllByCodeGroupIdAndValidYnOrderBySortOrder(id, EnumValidYn.Y);
        if (!CollectionUtils.isEmpty(codes)) {
            codes.forEach(code -> code.setValidYn(EnumValidYn.N));
            log.info("Delete all Code with codeGroupId: {}", id);
            codeRepository.saveAll(codes);
        }
        return id;
    }

    @Override
    public Page<CodeGroupDTO> searchCodeGroup(FilterSearchAuth filterSearchAuth) {
        int page = ObjectUtils.isEmpty(filterSearchAuth.getPage()) ? 0 : filterSearchAuth.getPage() - 1;
        int pageSize = ObjectUtils.isEmpty(filterSearchAuth.getPageSize()) ? 10 : filterSearchAuth.getPageSize();

        Pageable pageable = PageRequest.of(page, pageSize);
        List<CodeGroupDTO> menuDTOS = codeGroupRepository.searchCodeGroup(filterSearchAuth, pageable);
        long countCode = 0L;
        if (!CollectionUtils.isEmpty(menuDTOS)) {
            countCode = codeGroupRepository.countCodeGroup(filterSearchAuth);
        }
        return new PageImpl<>(menuDTOS, pageable, countCode);
    }
}

package com.evoucher.adminapi.auth.service;

import com.evoucher.adminapi.auth.dao.MenuGroupRepository;
import com.evoucher.adminapi.auth.dao.MenuRepository;
import com.evoucher.adminapi.auth.dao.models.Menu;
import com.evoucher.adminapi.auth.dao.models.MenuGroup;
import com.evoucher.adminapi.auth.mapper.MenuGroupMapper;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuGroupServiceImpl implements MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;

    private final MenuGroupMapper menuGroupMapper;


    @Override
    public MenuGroupDTO findById(Integer id) {
        log.info("Find MenuGroup by id: {}", id);
        MenuGroup menuGroup = menuGroupRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.menu.group.not.found"),
                        HttpStatus.BAD_REQUEST)
                );

        log.info("Find list Menu of MenuGroup with menuGroupId: {}", id);
        List<Menu> menus = menuRepository.findAllByMenuGroupIdAndValidYn(id, EnumValidYn.Y);

        return menuGroupMapper.toMenuGroupDTO(menuGroup, menus);
    }

    @Override
    public MenuGroupDTO createMenuGroup(MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupMapper.toMenuGroup(menuGroupRequest);
        menuGroup.setValidYn(EnumValidYn.Y);

        log.info("Create MenuGroup with name: {}", menuGroupRequest.getMenuGroupName());
        menuGroup = menuGroupRepository.save(menuGroup);
        return menuGroupMapper.toMenuGroupDTO(menuGroup);
    }

    @Override
    public MenuGroupDTO editMenuGroup(Integer id, MenuGroupRequest menuGroupRequest) {
        log.info("Find MenuGroup with id: {}", id);
        Optional<MenuGroup> menuGroupOptional = menuGroupRepository.findByIdAndValidYn(id, EnumValidYn.Y);
        if (menuGroupOptional.isEmpty())
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.menu.group.not.found"), HttpStatus.BAD_REQUEST);

        MenuGroup menuGroup = menuGroupMapper.toMenuGroup(menuGroupRequest);
        menuGroup.setId(id);
        menuGroup.setValidYn(EnumValidYn.Y);

        log.info("Update MenuGroup with id: {}", id);
        menuGroup = menuGroupRepository.save(menuGroup);

        return menuGroupMapper.toMenuGroupDTO(menuGroup);
    }

    @Override
    public Integer deleteMenuGroup(Integer id) {
        log.info("Find MenuGroup with id: {}", id);
        MenuGroup menuGroup = menuGroupRepository.findByIdAndValidYn(id, EnumValidYn.Y).orElseThrow(
                () -> new CustomCodeException(MessageUtils.getMessage("evoucher.menu.group.not.found"), HttpStatus.BAD_REQUEST)
        );

        menuGroup.setValidYn(EnumValidYn.N);
        log.info("Delete Menu with id: {}", id);
        menuGroupRepository.save(menuGroup);

        return id;
    }

    @Override
    public Page<MenuGroupDTO> searchMenuGroup(FilterSearchAuth filterSearchAuth) {
        int page = ObjectUtils.isEmpty(filterSearchAuth.getPage()) ? 0 : filterSearchAuth.getPage() - 1;
        int pageSize = ObjectUtils.isEmpty(filterSearchAuth.getPageSize()) ? 10 : filterSearchAuth.getPageSize();

        Pageable pageable = PageRequest.of(page, pageSize);
        List<MenuGroupDTO> menuDTOS = menuGroupRepository.searchMenuGroup(filterSearchAuth, pageable);
        long countMenu = 0L;
        if (!CollectionUtils.isEmpty(menuDTOS)) {
            countMenu = menuGroupRepository.countMenuGroup(filterSearchAuth);
        }
        return new PageImpl<>(menuDTOS, pageable, countMenu);
    }
}

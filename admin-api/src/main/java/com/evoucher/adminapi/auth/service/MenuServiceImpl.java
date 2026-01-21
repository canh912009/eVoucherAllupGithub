package com.evoucher.adminapi.auth.service;

import com.evoucher.adminapi.auth.dao.MenuGroupRepository;
import com.evoucher.adminapi.auth.dao.MenuRepository;
import com.evoucher.adminapi.auth.dao.models.Menu;
import com.evoucher.adminapi.auth.mapper.MenuMapper;
import com.evoucher.adminapi.auth.service.models.FilterSearchAuth;
import com.evoucher.adminapi.auth.service.models.MenuDTO;
import com.evoucher.adminapi.auth.service.models.MenuRequest;
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
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;

    private final MenuMapper menuMapper;


    @Override
    public MenuDTO findById(Integer id) {
        log.info("Find menu by id: {}", id);
        Optional<Menu> menu = menuRepository.findByIdAndValidYn(id, EnumValidYn.Y);

        return menu.map(menuMapper::toMenuDTO).orElseThrow(
                () -> new CustomCodeException(MessageUtils.getMessage("evoucher.menu.not.found"), HttpStatus.BAD_REQUEST)
        );
    }

    @Override
    public MenuDTO createMenu(MenuRequest menuRequest) {
        // check MenuGroup exist
        validateMenuGroupExist(menuRequest.getMenuGroupId());

        Menu menu = menuMapper.toMenu(menuRequest);
        menu.setValidYn(EnumValidYn.Y);

        log.info("Create menu with name: {}", menuRequest.getMenuName());
        menu = menuRepository.save(menu);
        return menuMapper.toMenuDTO(menu);
    }

    @Override
    public MenuDTO editMenu(Integer id, MenuRequest menuRequest) {
        log.info("Find Menu with id: {}", id);
        Optional<Menu> menuOptional = menuRepository.findByIdAndValidYn(id, EnumValidYn.Y);
        if (menuOptional.isEmpty())
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.menu.not.found"), HttpStatus.BAD_REQUEST);

        // check MenuGroup exist
        validateMenuGroupExist(menuRequest.getMenuGroupId());

        Menu menu = menuMapper.toMenu(menuRequest);
        menu.setId(id);
        menu.setValidYn(EnumValidYn.Y);

        log.info("Update Menu with id: {}", id);
        menu = menuRepository.save(menu);

        return menuMapper.toMenuDTO(menu);
    }

    @Override
    public Integer deleteMenu(Integer id) {
        log.info("Find Menu with id: {}", id);
        Menu menu = menuRepository.findByIdAndValidYn(id, EnumValidYn.Y).orElseThrow(
                () -> new CustomCodeException(MessageUtils.getMessage("evoucher.menu.not.found"), HttpStatus.BAD_REQUEST)
        );

        menu.setValidYn(EnumValidYn.N);
        log.info("Delete Menu with id: {}", id);
        menuRepository.save(menu);

        return id;
    }

    @Override
    public Page<MenuDTO> searchMenu(FilterSearchAuth filterSearchAuth) {
        int page = ObjectUtils.isEmpty(filterSearchAuth.getPage()) ? 0 : filterSearchAuth.getPage() - 1;
        int pageSize = ObjectUtils.isEmpty(filterSearchAuth.getPageSize()) ? 10 : filterSearchAuth.getPageSize();

        Pageable pageable = PageRequest.of(page, pageSize);
        List<MenuDTO> menuDTOS = menuRepository.searchMenu(filterSearchAuth, pageable);
        long countMenu = 0L;
        if (!CollectionUtils.isEmpty(menuDTOS)) {
            countMenu = menuRepository.countMenu(filterSearchAuth);
        }
        return new PageImpl<>(menuDTOS, pageable, countMenu);
    }

    private void validateMenuGroupExist(Integer menuGroupId) {
        log.info("Find MenuGroup with menuGroupId: {}", menuGroupId);
        menuGroupRepository.findByIdAndValidYn(menuGroupId, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.menu.group.not.found")
                        , HttpStatus.BAD_REQUEST));
    }
}

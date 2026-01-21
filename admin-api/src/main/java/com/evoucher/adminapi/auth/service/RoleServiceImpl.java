package com.evoucher.adminapi.auth.service;

import com.evoucher.adminapi.auth.dao.MenuGroupRepository;
import com.evoucher.adminapi.auth.dao.MenuRepository;
import com.evoucher.adminapi.auth.dao.RoleMenuRepository;
import com.evoucher.adminapi.auth.dao.RoleRepository;
import com.evoucher.adminapi.auth.dao.models.Menu;
import com.evoucher.adminapi.auth.dao.models.MenuGroup;
import com.evoucher.adminapi.auth.dao.models.Role;
import com.evoucher.adminapi.auth.dao.models.RoleMenu;
import com.evoucher.adminapi.auth.mapper.MenuGroupMapper;
import com.evoucher.adminapi.auth.mapper.MenuMapper;
import com.evoucher.adminapi.auth.mapper.RoleMapper;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;
    private final RoleMenuRepository roleMenuRepository;
    private final MenuGroupRepository menuGroupRepository;

    private final RoleMapper roleMapper;
    private final MenuGroupMapper menuGroupMapper;
    private final MenuMapper menuMapper;


    @Override
    public RoleDTO findById(String roleCode) {
        log.info("Find role with id: {}", roleCode);
        Role role = roleRepository.findByRoleCodeAndValidYn(roleCode, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.role.not.found"),
                        HttpStatus.BAD_REQUEST
                ));

        log.info("Find list Menu of Role with roleId: {}", roleCode);
        List<Menu> menus = menuRepository.findByRoleCodeAndValidYn(roleCode, EnumValidYn.Y.toString());

        Map<Integer, List<Menu>> menuGroupMap = menus.stream()
                .collect(Collectors.groupingBy(Menu::getMenuGroupId));

        List<MenuGroupDTO> menuGroups = new ArrayList<>();
        for (Map.Entry<Integer, List<Menu>> entry : menuGroupMap.entrySet()){
            Optional<MenuGroup> menuGroup = menuGroupRepository.findByIdAndValidYn(entry.getKey(), EnumValidYn.Y);
            if (menuGroup.isEmpty()) continue;

            MenuGroupDTO menuGroupDTO = menuGroupMapper.toMenuGroupDTO(menuGroup.get());
            menuGroupDTO.setMenus(menuMapper.toListMenuDTO(entry.getValue()));
            menuGroups.add(menuGroupDTO);
        }
        
        return roleMapper.toRoleDTOs(role, menuGroups);

    }

    @Override
    @Transactional
    public RoleDTO createRole(RoleRequest roleRequest) {
        boolean checkRoleExist = roleRepository.existsByRoleCodeAndValidYn(roleRequest.getRoleCode(), EnumValidYn.Y);
        if (checkRoleExist) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.role.exist"), HttpStatus.BAD_REQUEST);
        }

        Role role = roleMapper.toRole(roleRequest);
        role.setValidYn(EnumValidYn.Y);

        log.info("Save Role name: {}", roleRequest.getRoleName());
        role = roleRepository.save(role);

        if (!CollectionUtils.isEmpty(roleRequest.getMenus())) {
            List<Menu> menus = roleRequest.getMenus().stream().map(m -> {
                log.info("Check Menu exist with menuId: {}", m.getId());
                return menuRepository.findByIdAndValidYn(m.getId(), EnumValidYn.Y)
                        .orElseThrow(() -> new CustomCodeException(
                                MessageUtils.getMessage("evoucher.menu.not.found"),
                                HttpStatus.BAD_REQUEST
                        ));
            }).collect(Collectors.toList());

            // save list RoleMenu
            String roleCode = role.getRoleCode();
            saveListRoleMenuRelationship(roleCode, roleRequest.getMenus());

            return roleMapper.toRoleDTO(role, menus);
        }

        return roleMapper.toRoleDTO(role);
    }

    @Override
    @Transactional
    public RoleDTO updateRole(String roleCode, RoleRequest roleRequest) {
        log.info("Find Role with id: {}", roleCode);
        Optional<Role> roleOptional = roleRepository.findByRoleCodeAndValidYn(roleCode, EnumValidYn.Y);
        if (roleOptional.isEmpty()) throw new CustomCodeException(
                MessageUtils.getMessage("evoucher.role.not.found"),
                HttpStatus.BAD_REQUEST
        );

        Role role = roleMapper.toRole(roleRequest);
        role.setRoleCode(roleCode);
        role.setValidYn(EnumValidYn.Y);
        log.info("Update Role with id: {}", roleCode);
        role = roleRepository.save(role);

        if (!CollectionUtils.isEmpty(roleRequest.getMenus())) {
            // update RoleMenu
            List<Menu> menus = roleRequest.getMenus().stream().map(m -> {
                log.info("Check Menu exist with menuId: {}", m.getId());
                return menuRepository.findByIdAndValidYn(m.getId(), EnumValidYn.Y)
                        .orElseThrow(() -> new CustomCodeException(
                                MessageUtils.getMessage("evoucher.menu.not.found"),
                                HttpStatus.BAD_REQUEST
                        ));
            }).collect(Collectors.toList());

            log.info("Delete RoleMenu with roleId: {}", roleCode);
            roleMenuRepository.deleteAllByRoleCode(roleCode);

            log.info("Save list RoleMenu with roleCode: {}", roleCode);
            saveListRoleMenuRelationship(roleCode, roleRequest.getMenus());

            return roleMapper.toRoleDTO(role, menus);
        }
        return roleMapper.toRoleDTO(role);
    }

    private void saveListRoleMenuRelationship(String roleCode, List<MenuDTO> menuDTOS) {
        List<RoleMenu> roleMenus = menuDTOS.stream()
                .map(m -> RoleMenu.builder()
                        .roleCode(roleCode)
                        .menuId(m.getId())
                        .build()).collect(Collectors.toList());
        log.info("Save list RoleMenu for Role with roleId: {}", roleCode);
        roleMenuRepository.saveAll(roleMenus);
    }

    @Override
    public String deleteRole(String roleCode) {
        log.info("Find role with id: {}", roleCode);
        Role role = roleRepository.findByRoleCodeAndValidYn(roleCode, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.role.not.found"),
                        HttpStatus.BAD_REQUEST
                ));

        role.setValidYn(EnumValidYn.N);
        log.info("Delete role with id: {}", roleCode);
        roleRepository.save(role);
        return roleCode;
    }

    @Override
    public Page<RoleDTO> searchRole(FilterSearchAuth filterSearchAuth) {
        int page = ObjectUtils.isEmpty(filterSearchAuth.getPage()) ? 0 : filterSearchAuth.getPage() - 1; // pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchAuth.getPageSize()) ? 10 : filterSearchAuth.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        List<RoleDTO> roleDTOS = roleRepository.searchRole(filterSearchAuth, pageable);
        Long countRole = 0L;
        if (!CollectionUtils.isEmpty(roleDTOS)) {
            countRole = roleRepository.countRole(filterSearchAuth);
        }

        return new PageImpl<>(roleDTOS, pageable, countRole);
    }
}

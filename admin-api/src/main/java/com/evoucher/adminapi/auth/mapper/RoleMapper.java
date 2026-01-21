package com.evoucher.adminapi.auth.mapper;

import com.evoucher.adminapi.auth.dao.models.Menu;
import com.evoucher.adminapi.auth.dao.models.Role;
import com.evoucher.adminapi.auth.service.models.MenuGroupDTO;
import com.evoucher.adminapi.auth.service.models.RoleDTO;
import com.evoucher.adminapi.auth.service.models.RoleRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface RoleMapper {

    RoleMapper INSTANT = Mappers.getMapper(RoleMapper.class);

    Role toRole(RoleDTO roleDTO);
    Role toRole(RoleRequest roleRequest);
    RoleDTO toRoleDTO(Role role);
    @Mappings({
            @Mapping(source = "menus", target = "menus")
    })
    RoleDTO toRoleDTO(Role role, List<Menu> menus);
    @Mappings({
            @Mapping(source = "menuGroups", target = "menuGroups")
    })
    RoleDTO toRoleDTOs(Role role, List<MenuGroupDTO> menuGroups);
}

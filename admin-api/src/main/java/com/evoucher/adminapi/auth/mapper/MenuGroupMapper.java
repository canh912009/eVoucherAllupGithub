package com.evoucher.adminapi.auth.mapper;

import com.evoucher.adminapi.auth.dao.models.Menu;
import com.evoucher.adminapi.auth.dao.models.MenuGroup;
import com.evoucher.adminapi.auth.service.models.MenuGroupDTO;
import com.evoucher.adminapi.auth.service.models.MenuGroupRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface MenuGroupMapper {

    MenuGroupMapper INSTANT = Mappers.getMapper(MenuGroupMapper.class);

    MenuGroup toMenuGroup(MenuGroupDTO menuGroupDTO);

    MenuGroup toMenuGroup(MenuGroupRequest menuGroupRequest);

    MenuGroupDTO toMenuGroupDTO(MenuGroup menuGroup);

    @Mappings({
            @Mapping(source = "menus", target = "menus")
    })
    MenuGroupDTO toMenuGroupDTO(MenuGroup menuGroup, List<Menu> menus);
}

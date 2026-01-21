package com.evoucher.adminapi.auth.mapper;

import com.evoucher.adminapi.auth.dao.models.Menu;
import com.evoucher.adminapi.auth.service.models.MenuDTO;
import com.evoucher.adminapi.auth.service.models.MenuRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface MenuMapper {

    MenuMapper INSTANT = Mappers.getMapper(MenuMapper.class);

    Menu toMenu(MenuDTO menuDTO);

    Menu toMenu(MenuRequest menuRequest);

    MenuDTO toMenuDTO(Menu menu);

    List<MenuDTO> toListMenuDTO(List<Menu> menus);
}

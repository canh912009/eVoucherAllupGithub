package com.evoucher.adminapi.cms.mapper;

import com.evoucher.adminapi.cms.dao.models.EndUser;
import com.evoucher.adminapi.cms.service.models.EndUserDTO;
import com.evoucher.adminapi.cms.service.models.request.EndUserRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface EndUserMapper {

    EndUserMapper INSTANCE = Mappers.getMapper(EndUserMapper.class);

    EndUser toEntity(EndUserDTO endUserDTO);

    EndUser toEntity(EndUserRequest endUserRequest);

    EndUserDTO toDTO(EndUser user);

    List<EndUserDTO> toListDTO(List<EndUser> users);

    List<EndUser> toListEntity(List<EndUserDTO> endUserDTOs);
}
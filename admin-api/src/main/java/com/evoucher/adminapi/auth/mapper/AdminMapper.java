package com.evoucher.adminapi.auth.mapper;

import com.evoucher.adminapi.auth.dao.models.Admin;
import com.evoucher.adminapi.auth.service.models.AdminDTO;
import com.evoucher.adminapi.auth.service.models.AdminRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(builder = @Builder(disableBuilder = true))
public interface AdminMapper {

    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    Admin toAdmin(AdminDTO adminDTO);

    Admin toAdmin(AdminRequest adminRequest);

    AdminDTO toAdminDTO(Admin admin);
    @Named("mapAdminBasicInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "adminName", target = "adminName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "mobileNumber", target = "mobileNumber")
    AdminDTO toDtoWithBasicInfoOnly(Admin entity);
}

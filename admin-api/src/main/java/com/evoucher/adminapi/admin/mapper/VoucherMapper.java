package com.evoucher.adminapi.admin.mapper;

import com.evoucher.adminapi.admin.dao.models.EVoucher;
import com.evoucher.adminapi.admin.service.models.VoucherDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface VoucherMapper {

    @Named("toOperatorRequestInfo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "EV", target = "EV")
    @Mapping(source = "externalPinNo", target = "externalPinNo")
    @Mapping(source = "userMobileNumber", target = "userMobileNumber")
    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "publishId", target = "publishId")
    @Mapping(source = "expirationDate", target = "expirationDate")
    VoucherDto toOperatorRequestInfo(EVoucher entity);
    EVoucher toEntity(VoucherDto dto);
    VoucherDto toDto(EVoucher entity);
}

package com.castis.pos_api.mapper;

import com.castis.pos_api.dto.VoucherDto;
import com.castis.pos_api.dto.response.VoucherResponse;
import com.castis.pos_api.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {BrandMapper.class, GoodMapper.class})
public interface VoucherMapper {
    VoucherMapper INSTANCE = Mappers.getMapper(VoucherMapper.class);
    VoucherDto toDto(Voucher entity);
    Voucher toEntity(VoucherDto dto);
    @Mapping(target = "ev", source = "id")
    @Mapping(target = "userPhoneNo", source = "userMobileNumber")
    @Mapping(target = "expireDate", source = "expirationDate")
    VoucherResponse toResponse(VoucherDto dto);
}

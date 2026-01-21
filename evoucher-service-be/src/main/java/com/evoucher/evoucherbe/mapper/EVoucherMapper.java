package com.evoucher.evoucherbe.mapper;

import com.evoucher.evoucherbe.dto.VoucherDto;
import com.evoucher.evoucherbe.entity.EVoucher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EVoucherMapper {
    EVoucherMapper INSTANCE = Mappers.getMapper(EVoucherMapper.class);

    EVoucher copyVoucher(EVoucher eVoucher);
    EVoucher toEntity(VoucherDto dto);
    VoucherDto toDto(EVoucher entity);
}

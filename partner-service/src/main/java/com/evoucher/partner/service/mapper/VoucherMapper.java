package com.evoucher.partner.service.mapper;

import com.evoucher.partner.service.bean.dtos.GoodsDTO;
import com.evoucher.partner.service.bean.dtos.VoucherDto;
import com.evoucher.partner.service.bean.entity.Goods;
import com.evoucher.partner.service.bean.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VoucherMapper {
    VoucherMapper INSTANCE = Mappers.getMapper(VoucherMapper.class);
    Voucher toEntity(VoucherDto dto);
    VoucherDto toDTO(Voucher entity);
    GoodsDTO toGoodDto(Goods goods);
}

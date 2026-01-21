package com.evoucher.partner.service.mapper;

import com.evoucher.partner.service.bean.request.VnptPurchaseRequest;
import com.evoucher.partner.service.bean.request.VnptTopUpRequest;
import com.evoucher.partner.service.vnpt.bean.request.DownloadSoftPinRequestQuery;
import com.evoucher.partner.service.vnpt.bean.request.TopUpRequestQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VnptPurchaseMapper {
    VnptPurchaseMapper INSTANCE = Mappers.getMapper(VnptPurchaseMapper.class);

    @Mapping(target = "amount", source = "faceValue")
    DownloadSoftPinRequestQuery toVnptDownloadSoftPinRequest(VnptPurchaseRequest request);

    @Mapping(target = "amount", source = "faceValue")
    TopUpRequestQuery toTopUpRequest(VnptTopUpRequest request);
}

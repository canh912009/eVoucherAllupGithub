package com.castis.publishservice.mapper;

import com.castis.publishservice.dto.EvoucherDTO;
import com.castis.publishservice.dto.PurchaseChildRequest;
import com.castis.publishservice.dto.queue.VoucherRequest;
import com.castis.publishservice.dto.request.ChoiceChosenRequest;
import com.castis.publishservice.dto.request.CreateChoiceItemRequest;
import com.castis.publishservice.entity.Voucher;
import com.castis.publishservice.utils.Constants;
import com.castis.publishservice.utils.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {Utils.class}, uses = GoodsMapper.class)
public interface VoucherMapper {
    VoucherMapper INSTANCE = Mappers.getMapper(VoucherMapper.class);
    Voucher toEntity(EvoucherDTO dto);
    EvoucherDTO toDTO(Voucher entity);
    @Mapping(target = "voucherType", source = "voucherTypeCd")
    @Mapping(target = "voucherStatus", source = "voucherStatusCode")
    @Mapping(target = "transferStatus", source = "transferStatusCode")
    @Mapping(target = "createDate", source = "creationDate", dateFormat = Constants.fullDateTimeFormat)
    @Mapping(target = "expireDate", source = "expirationDate", dateFormat = Constants.fullDateTimeFormat)
    @Mapping(target = "publishDate", source = "publishDate", dateFormat = Constants.fullDateTimeFormat)
    @Mapping(target = "lastExchangeDate", source = "lastExchangeDate", dateFormat = Constants.fullDateTimeFormat)
    @Mapping(target = "disuseDate", source = "disuseDate", dateFormat = Constants.fullDateTimeFormat)
    @Mapping(target = "cancelDate", dateFormat = Constants.fullDateTimeFormat)
    @Mapping(target = "transferDate", dateFormat = Constants.fullDateTimeFormat)
    @Mapping(target = "publishDetailId", source = "publishDtlId")
    @Mapping(target = "transferMessage", source = "transferMsg")
    @Mapping(target = "originalVoucherId", source = "origEv")
    @Mapping(target = "extPinType", source = "externalPinType")
    @Mapping(target = "userEmail", source = "user.email")
    VoucherRequest entityToQueueRequest(Voucher entity);

    @Mapping(target = "parentId", source = "parentVoucherId")
    @Mapping(target = "token", source = "token")
    @Mapping(target = "products", source = "products")
    CreateChoiceItemRequest toCreateChoiceRequest(ChoiceChosenRequest uiInput);
    @Mapping(target = "parentId", source = "parentVoucherId")
    @Mapping(target = "products", source = "products")
    CreateChoiceItemRequest toCreateChoiceRequest(PurchaseChildRequest uiInput);
}

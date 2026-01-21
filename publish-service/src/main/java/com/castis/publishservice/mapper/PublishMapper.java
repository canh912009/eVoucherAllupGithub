package com.castis.publishservice.mapper;

import com.castis.publishservice.dto.PublishDTO;
import com.castis.publishservice.dto.queue.PublishQueueRequest;
import com.castis.publishservice.dto.response.BaseResponse;
import com.castis.publishservice.dto.response.VoucherResponse;
import com.castis.publishservice.entity.Publish;
import com.castis.publishservice.utils.Constants;
import com.castis.publishservice.utils.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {Utils.class}, uses = {CampaignMapper.class, CustomerMapper.class, GoodsMapper.class})
public interface PublishMapper {
    public static final PublishMapper INSTANCE = Mappers.getMapper(PublishMapper.class);
    Publish toEntity(PublishDTO dto);
    PublishDTO toDTO(Publish entity);
    @Mapping(target = "name", source = "publishName")
    @Mapping(target = "booking", expression = "java(Utils.mappingYesNo(entity.getBookingYn()))")
    @Mapping(target = "bookingDate", dateFormat = Constants.fullDateTimeFormat)
    @Mapping(target = "publishDate", dateFormat = Constants.fullDateTimeFormat)
    @Mapping(target = "cancelDate", dateFormat = Constants.fullDateTimeFormat)
    @Mapping(target = "testSend", expression = "java(Utils.mappingYesNo(entity.getTestSendYn()))")
    @Mapping(target = "receiverNoDuplicateAllowed", expression = "java(Utils.mappingYesNo(entity.getReceiverNoDuplicateAllowYn()))")
    PublishQueueRequest toRequest(Publish entity);

}

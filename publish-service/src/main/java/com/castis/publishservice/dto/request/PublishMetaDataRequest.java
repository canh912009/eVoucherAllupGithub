package com.castis.publishservice.dto.request;

import com.castis.publishservice.dto.EndUserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PublishMetaDataRequest extends PublishRequest {
    private Integer numberOfVouchers ;
}

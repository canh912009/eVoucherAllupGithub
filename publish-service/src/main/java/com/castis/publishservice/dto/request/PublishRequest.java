package com.castis.publishservice.dto.request;

import com.castis.publishservice.dto.EndUserDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PublishRequest {
    private Long publishId;
    List<EndUserDto> users;
}

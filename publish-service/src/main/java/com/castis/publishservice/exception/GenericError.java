package com.castis.publishservice.exception;

import com.castis.publishservice.dto.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericError extends BaseResponse {
  private int status;
  private String detailMessage;
  private Object listMessage;
}

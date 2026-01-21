package com.evoucher.evoucherbe.exception;

import com.evoucher.evoucherbe.message.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class GenericError extends BaseResponse {
  private int status;
  private String detailMessage;
  private Object listMessage;
}

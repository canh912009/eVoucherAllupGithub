package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.common.enums.UpdatingVoucherType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
public class UpdatingVoucherReq extends VoucherExchangeReq {
    UpdatingVoucherType updatingType;
}

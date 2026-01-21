package com.castis.publishservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Access;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherHandoverBERequest {
    private String evOld;
    private Long publishDetailId;
    private EndUserBERequest endUser;
    private String transferMessage;
}

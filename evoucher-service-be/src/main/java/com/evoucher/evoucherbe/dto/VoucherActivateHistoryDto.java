package com.evoucher.evoucherbe.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherActivateHistoryDto {
    Long id;
    String ev;
    String serialNumber;
    String userName;
    String phoneNumber;
    Date activationDate;
}

package com.evoucher.evoucherbe.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EndUserDto {
    Long id;
    String userMobileNum;
    String userNm;
    String gender;
    Date birthday;
    String address;
    String email;
}

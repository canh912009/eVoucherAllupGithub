package com.castis.publishservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndUserDto {
    private Long id;
    private String userMobileNum;
    private String userNm;
    private String gender;
    private Date birthday;
    private String address;
    private String email;
}

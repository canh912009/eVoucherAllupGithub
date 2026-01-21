package com.castis.publishservice.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndUserBERequest {
    private Long id;
    // Encrypted
    private String userMobileNum;
    // Encrypted
    private String userNm;
//    private EnumGender gender;
    private String birthday;
    private String address;
    private String email;
}

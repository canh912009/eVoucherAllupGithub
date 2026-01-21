package com.evoucher.adminapi.auth.service.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtDTO {

    private String token;
    private String roleCode;
    private String adminCorpId;
    private long expiredDate;
}

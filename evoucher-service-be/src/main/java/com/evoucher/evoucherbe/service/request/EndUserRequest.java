package com.evoucher.evoucherbe.service.request;

import com.evoucher.evoucherbe.common.enums.EnumGender;
import com.evoucher.evoucherbe.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndUserRequest {
    @NotBlank(message = "Id not blank")
    private Long id;
    private String userMobileNum;
    private String userNm;
    private EnumGender gender;
    @JsonFormat(pattern=Constant.Common.COMMON_DATE_FORMAT)
    private Date birthday;
    private String address;
    private String email;

}

package com.evoucher.evoucherbe.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class EVoucherActivateProcess {

    private String phoneNumber;
    private String userName;
    private String serialNumber;
    private String ev;
}

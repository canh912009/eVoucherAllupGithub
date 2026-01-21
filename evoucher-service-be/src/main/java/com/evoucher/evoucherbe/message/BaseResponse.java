package com.evoucher.evoucherbe.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse {
    private int code = 0;
    private String message = "OK";
    private String timestamp = LocalDateTime.now(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    @JsonIgnore
    public boolean isOk() {
        return code == 0;
    }

    @JsonIgnore
    public boolean isVnptProcessing() {
        return code == 99;
    }

    @JsonIgnore
    public boolean isXpayProcessing() {
        return Set.of(15, 80, 81, 99).contains(code);
    }
}

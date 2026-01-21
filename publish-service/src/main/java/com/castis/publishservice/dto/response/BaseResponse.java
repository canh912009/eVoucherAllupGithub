package com.castis.publishservice.dto.response;

import com.castis.publishservice.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse {
    private int code = Constants.SUCCESS_CODE;
    private String message = "OK";
    private String timestamp = LocalDateTime.now(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

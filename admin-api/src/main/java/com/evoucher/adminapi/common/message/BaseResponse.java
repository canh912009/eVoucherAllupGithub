package com.evoucher.adminapi.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {

    private String message = "OK";

    private String timestamp = LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    private String errorCode = "0";

    private Object data;

    private Long totalCount;

    public BaseResponse(Object data) {
        this.data = data;
    }
    public static BaseResponse newList(Collection data) {
        var result = new BaseResponse(data);
        result.setTotalCount((long) data.size());
        return result;

    }

    public static BaseResponse ok(Object data) {
        BaseResponse response = new BaseResponse();
        response.setData(data);
        return response;
    }
}

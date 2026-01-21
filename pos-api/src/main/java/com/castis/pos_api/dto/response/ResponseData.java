package com.castis.pos_api.dto.response;

import com.castis.pos_api.utils.CustomResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ResponseData
 *
 * @author by daont on 3/29/2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> {
    public static final int RESPONSE_SUCCESS = 0;
    private int result;
    private String message;
    private T data;

    public static ResponseData<Object> error(CustomResponse customResponse) {
        return new ResponseData<>(customResponse.getCode(), customResponse.getMessage(), null);
    }
    public static ResponseData<Object> error(int code, String message) {
        return new ResponseData<>(code, message, null);
    }

    public ResponseData(T data) {
        this.result = RESPONSE_SUCCESS;
        this.message = "";
        this.data = data;
    }

    public ResponseData(int result, String message) {
        this.result = result;
        this.message = message;
    }
}

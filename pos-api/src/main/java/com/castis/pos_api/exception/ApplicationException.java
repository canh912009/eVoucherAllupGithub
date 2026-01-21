package com.castis.pos_api.exception;

import com.castis.pos_api.utils.CustomResponse;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApplicationException extends RuntimeException implements Serializable {
    private int code;
    private String message;

    public ApplicationException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    public ApplicationException(CustomResponse response) {
        super(response.getMessage());
        this.code = response.getCode();
        this.message = response.getMessage();
    }

}

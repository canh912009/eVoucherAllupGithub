package asia.castis.web_hook.bean.dto.response;

import asia.castis.web_hook.utils.Common;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class BaseResponse {
    private int code = 0;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String message = "OK";
    public BaseResponse() {
        timestamp = LocalDateTime.now();
    }
    public BaseResponse(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public BaseResponse(Integer code) {
        this();
        this.code = code;
    }

    public String toJsonString() {
        try {
            return Common.OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return this.toString();
        }
    }
}

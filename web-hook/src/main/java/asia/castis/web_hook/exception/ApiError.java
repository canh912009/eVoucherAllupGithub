package asia.castis.web_hook.exception;

import asia.castis.web_hook.bean.dto.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
class ApiError extends BaseResponse {
    private String debugMessage;
    @Setter
    @Getter
    private List<ApiSubError> subErrors;

    private ApiError() {
        super();
    }

    ApiError(HttpStatus status) {
        this();
        this.setCode(status.value());
    }

    ApiError(HttpStatus status, Throwable ex) {
        super(status.value(), "Unexpected error");
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(HttpStatus status, String message, Throwable ex) {
        super(status.value(), message);
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(int status) {
        super(status);
    }

    ApiError(int status, Throwable ex) {
        super(status, "Unexpected error");
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(int status, String message, Throwable ex) {
        super(status, message);
        this.debugMessage = ex.getLocalizedMessage();
    }
}
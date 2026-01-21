package asia.castis.evoucher.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private int code;
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

}

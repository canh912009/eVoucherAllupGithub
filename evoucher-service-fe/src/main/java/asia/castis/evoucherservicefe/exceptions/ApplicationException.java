package asia.castis.evoucherservicefe.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApplicationException extends RuntimeException {
    private String message;
    private int code;

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}

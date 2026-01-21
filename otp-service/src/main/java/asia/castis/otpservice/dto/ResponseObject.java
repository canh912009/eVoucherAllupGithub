package asia.castis.otpservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject<T> {
    private String error;
    private String errorType;
    private String path;
    private T data;
}

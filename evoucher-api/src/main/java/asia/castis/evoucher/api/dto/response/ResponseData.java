package asia.castis.evoucher.api.dto.response;

import asia.castis.evoucher.api.common.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> {

    private int code;

    private String message;

    private T data;

    private static <T> ResponseData<T> restResult(T data, HttpStatus status, String message) {
        ResponseData<T> apiResult = new ResponseData<>();
        apiResult.setCode(status.value());
        apiResult.setMessage(message);
        apiResult.setData(data);
        return apiResult;
    }

    public static <T> ResponseData<T> ok(T data) {
        return restResult(data, HttpStatus.OK, Constant.SUCCESS);
    }

    public static <T> ResponseData<T> ok() {
        return restResult(null, HttpStatus.OK, Constant.SUCCESS);
    }

    public static <T> ResponseData<T> failed(HttpStatus status, String statusDesc) {
        return restResult(null, status, statusDesc);
    }

}

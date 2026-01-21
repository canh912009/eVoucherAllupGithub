package asia.castis.web_hook.bean.dto.response;

import lombok.Data;

@Data
public class ApiResponse <T> extends BaseResponse {
    T data;
    public ApiResponse(Integer code, String message) {
        super(code, message);
    }

}

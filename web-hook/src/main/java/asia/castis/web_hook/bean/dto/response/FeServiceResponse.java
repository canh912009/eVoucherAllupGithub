package asia.castis.web_hook.bean.dto.response;

import lombok.Data;

@Data
public class FeServiceResponse {
    private int code;
    private String message;
    private Object data;
}

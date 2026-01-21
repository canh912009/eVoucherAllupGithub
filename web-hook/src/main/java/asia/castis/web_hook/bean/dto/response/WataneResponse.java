package asia.castis.web_hook.bean.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WataneResponse {
    private String success;
    private String code;
    private String message;
    private Object data;
    @JsonProperty("response_time")
    private String responseTime;
}

package asia.castis.web_hook.bean.dto.response.ur_box;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@Data
@ToString
public class UrBoxResponse {
    private Integer done;
    private String msg;
    private String microtime;
    private Integer status;
}

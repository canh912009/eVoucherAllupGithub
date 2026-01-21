package asia.castis.web_hook.bean.dto.response.ur_box;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class UrBoxSingleResponse <T> extends UrBoxResponse{
    private T data;
}

package asia.castis.web_hook.bean.dto.response.ur_box;

import asia.castis.web_hook.bean.dto.response.ur_box.UrBoxResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class UrBoxListResponse<T> extends UrBoxResponse {
    private NestedData data;
    // use for brand
    private Integer brand_count;
    // use for good
    private String totalResult;
    @Data
    public class NestedData {
        private Integer totalPage = 0;
        @ToString.Exclude
        List<T> items = new ArrayList<>();
    }
}

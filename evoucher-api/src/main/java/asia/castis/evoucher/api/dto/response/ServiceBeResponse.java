package asia.castis.evoucher.api.dto.response;

import asia.castis.evoucher.api.common.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBeResponse {

    private String message = "OK";

    private String timestamp = DateUtils.getCurrentDateTimeString();

    private String errorCode = "0";

    private HashMap<Long, Integer> data;

    private Long totalCount;

}
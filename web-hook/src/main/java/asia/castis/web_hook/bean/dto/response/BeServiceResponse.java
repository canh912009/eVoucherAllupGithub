package asia.castis.web_hook.bean.dto.response;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true, chain = true)
public class BeServiceResponse extends BaseResponse {
    private int status;
    private Object listMessage;
}

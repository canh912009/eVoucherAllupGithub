package asia.castis.web_hook.exception;

import asia.castis.web_hook.bean.dto.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericError extends BaseResponse {
  private int status;
  private String detailMessage;
  private Object listMessage;
}

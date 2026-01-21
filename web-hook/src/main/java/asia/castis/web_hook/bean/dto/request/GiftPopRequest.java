package asia.castis.web_hook.bean.dto.request;

import asia.castis.web_hook.utils.Common;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GiftPopRequest {
    @NotNull
    @NotBlank
    String trId;
    String pinNo;
    String storeCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss")
    Date useDate;
    public String toJsonString() {
        try {
            return Common.OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return this.toString();
        }
    }
}

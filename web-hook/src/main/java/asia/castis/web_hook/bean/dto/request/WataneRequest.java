package asia.castis.web_hook.bean.dto.request;

import asia.castis.web_hook.utils.Common;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class WataneRequest {
    private String voucherSerial;
    private Integer voucherStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private Date redeemedAt;

    public String toJsonString() {
        try {
            return Common.OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return this.toString();
        }
    }
}

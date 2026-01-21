package asia.castis.otpservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Null;

@Data
@ToString
@AllArgsConstructor
public class CustomOTPRequestDTO {
    private String key;
    private Long ttl;
    private Integer length;
}

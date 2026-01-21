package asia.castis.evoucher.api.dto.otpservice.generate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpRequest {
    @NotEmpty(message = "Mobile number is required")
    @NotNull(message = "Mobile number is required")
    private String mobileNumber;
}

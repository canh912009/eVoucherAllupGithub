package asia.castis.otpservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@AllArgsConstructor
public class OtpDTO {
    @NotNull
    private String otp;
}

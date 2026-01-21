package asia.castis.evoucher.api.dto.request.activate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ActivateRequest {
    @NotNull(message = "phone number can not be null")
    @NotEmpty(message = "phone number can not be empty")
    private String phoneNumber;
    private String userName;
    @NotNull(message = "serial number can not be null")
    @NotEmpty(message = "serial number can not be empty")
    private String serialNumber;
    private String ev;
}

package asia.castis.evoucher.api.dto.request.choose;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ChosenRequestV2 extends ChosenRequest {
    @NotNull(message = "otp can not be null")
    @NotEmpty(message = "otp can not be empty")
    String otp;
}

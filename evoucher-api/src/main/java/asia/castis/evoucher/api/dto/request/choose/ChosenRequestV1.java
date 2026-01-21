package asia.castis.evoucher.api.dto.request.choose;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ChosenRequestV1 extends ChosenRequest {
    @NotNull(message = "token can not be null")
    private String token;
}

package asia.castis.evoucher.api.dto.request.choose;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceChosenRequestV1 extends ChoiceChosenRequest {
    @NotNull(message = "token can not be null")
    String token;
}

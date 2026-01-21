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
public class ChoiceChosenRequest {
    @NotNull(message = "choice voucher can not be null")
    String choiceVoucherId;
    @NotNull(message = "must choose at least one item")
    List<ChosenItem> choices;
}

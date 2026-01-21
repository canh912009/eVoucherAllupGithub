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
public class ChosenRequest {
    @NotNull(message = "parent voucher can not be null")
    @NotEmpty(message = "parent voucher can not be empty")
    protected String parentVoucherId;
    @NotNull(message = "must choose at least one item")
    protected List<ChosenItem> products;
    protected String type;
}

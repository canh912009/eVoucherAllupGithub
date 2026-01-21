package asia.castis.evoucher.api.dto.request.choose;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChosenItem {
    @NotNull(message = "Must choose at least one item")
    Integer goodsId;
    @Min(value = 0, message = "Quantity cannot be negative")
    @Max(value = 29, message = "Quantity must be less than 30")
    int quantity;
}


package asia.castis.evoucher.push.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IncomingMessage {
    private String phoneNumber;
    private String brandName;
    private String message;
}

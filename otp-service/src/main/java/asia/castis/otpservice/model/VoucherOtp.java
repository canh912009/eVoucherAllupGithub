package asia.castis.otpservice.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class VoucherOtp implements Serializable {
    private String uuid;
    private int counter;
    private int status;

}

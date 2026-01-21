package asia.castis.evoucher.push.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ZaloTemplateDataOld {
    private String customer_name;
    private String service_name;
    private String reg_date;
    private int customer_id;
}

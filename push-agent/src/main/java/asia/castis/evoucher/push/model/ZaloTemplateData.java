package asia.castis.evoucher.push.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ZaloTemplateData {
    // Dang The Doan
    private String customer_name;
    // Altimedia
    private String sender;
    // thank you for using our services
    private String message;
    // Tra Dao Cam Xa
    private String product_name;
    // 6/30/2023  11:59:59.PM
    private String expire_date;
    // 0966312666
    private String cta1;
    // production link
    private String cta2;
}

package asia.castis.evoucher.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserVoucherListRequest {
    private String mobileNumber;
    private int pageSize;
    private int pageNum;
    private String status;
    private String sortBy; // issueDate or expirationDate (default: issueDate)
    private String sortDirection;
    private String supplierName;
    private String brandName;
}

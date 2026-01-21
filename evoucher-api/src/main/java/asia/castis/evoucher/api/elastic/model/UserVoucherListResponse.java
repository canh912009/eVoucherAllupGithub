package asia.castis.evoucher.api.elastic.model;

import asia.castis.evoucher.api.elastic.response.ElasticVoucherResponse;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class UserVoucherListResponse {
    private int pageSize;
    private int pageNum;
    private String status;
    private int totalCount;
    private List<ElasticVoucherResponse> voucherList;
}

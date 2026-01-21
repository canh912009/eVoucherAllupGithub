package asia.castis.evoucher.api.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class LimitedCountHistoryResponse {
    private String storeName;
    private String transactionDate;
    private Integer remainingCount;
}

package asia.castis.evoucher.api.dto.response;

import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {
    private int pageSize;
    private int pageNum;
    private String status;
    private int totalCount;
    private List<T> pageData;
}

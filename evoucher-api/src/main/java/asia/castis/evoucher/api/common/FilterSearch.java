package asia.castis.evoucher.api.common;

import asia.castis.evoucher.api.common.enums.EnumValidYn;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class FilterSearch {
    private String voucherId;
    private String brandId;
    private String categoryId;
    private String id;
    private String name;
    private EnumValidYn validYn;
    private Integer page;
    private Integer pageSize;
}

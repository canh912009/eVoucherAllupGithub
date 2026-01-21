package asia.castis.evoucherservicefe.common.dto.publishreq.imcoming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Brand {
    private String id;
    private String name;
    private String description;
    private String imgUrl;
    private Supplier supplier;
    @JsonProperty("isPosLink")
    private Boolean isPosLink;
    private String system;
}

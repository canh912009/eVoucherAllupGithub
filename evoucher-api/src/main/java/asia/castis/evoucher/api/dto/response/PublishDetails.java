package asia.castis.evoucher.api.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class PublishDetails {
    private String name;
    private String imagePath;
}

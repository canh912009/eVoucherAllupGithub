package asia.castis.evoucher.api.dto.response;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private String id;
    private String name;
    private String imagePath;
    private String imageName;
}

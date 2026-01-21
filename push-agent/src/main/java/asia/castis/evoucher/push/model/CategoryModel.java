package asia.castis.evoucher.push.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CategoryModel {
    private Long id;
    private String name;
    private String description;
}

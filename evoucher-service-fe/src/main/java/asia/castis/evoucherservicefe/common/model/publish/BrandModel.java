package asia.castis.evoucherservicefe.common.model.publish;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@ToString
public class BrandModel {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Text)
    private String imgUrl;
    @Field(type = FieldType.Nested)
    private SupplierModel supplier;
    @Field(type = FieldType.Boolean)
    private Boolean isPosLink;
    @Field(type = FieldType.Text)
    private String system;
}

package asia.castis.evoucher.api.elastic.model.publish;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("isPosLink")
    private boolean isPosLink;
    private String system;
}

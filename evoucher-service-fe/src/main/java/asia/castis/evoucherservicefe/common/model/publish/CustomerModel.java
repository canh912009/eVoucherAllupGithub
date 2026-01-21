package asia.castis.evoucherservicefe.common.model.publish;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@ToString
public class CustomerModel {
    @Id
    private String id;
    @Field(type =FieldType.Text)
    private String name;
}

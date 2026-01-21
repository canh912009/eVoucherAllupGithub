package asia.castis.evoucher.push.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@ToString

@Document(indexName = "#{@environment.getProperty('elastic.index.check_publish')}")
public class CheckPublish {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    private Long publishId;
    @Field(type = FieldType.Boolean)
    private boolean done;
    /*
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime create_dt;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime update_dt;
    */
}
package asia.castis.evoucherservicefe.storerequest.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "#{@environment.getProperty('elastic.store.indexName')}")
public class StoreModel {
    @Id
    private String storeId;
    @Field(type = FieldType.Text)
    private String storeName;
    @Field(type = FieldType.Text)
    private String storeImagePath;
    @Field(type = FieldType.Text)
    private String storeImageName;
    @Field(type = FieldType.Text)
    private String supplierId;
    @Field(type = FieldType.Text)
    private String brandId;
    @Field(type = FieldType.Text)
    private String validYN;
    @Field(type = FieldType.Text)
    private String registerId;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date registerDate;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date updateDate;
    @Field(type = FieldType.Text)
    private String mapCode;
    @Field(type = FieldType.Text)
    private String mapInteractionType;
    @Field(type = FieldType.Text)
    private String region;
    @Field(type = FieldType.Text)
    private String storeType;
    @Field(type = FieldType.Text)
    private String fullAddress;
    @Field(type = FieldType.Text)
    private String tel;
    @Field(type = FieldType.Text)
    private String updateId;
}

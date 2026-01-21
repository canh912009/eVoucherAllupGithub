package asia.castis.evoucher.api.elastic.model;

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
    private String storeName;
    private String storeImagePath;
    private String storeImageName;
    private String supplierId;
    private String brandId;
    private String validYN;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date registerDate;
    private String registerId;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date updateDate;
    private String province;
    private String district;
    private String ward;
    private String tel;
    private String mapCode;
    private String mapInteractionType;
    private String fullAddress;
    private String updateId;
}

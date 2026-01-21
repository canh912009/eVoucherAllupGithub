package asia.castis.evoucher.push.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@ToString
public class GoodsModel {
    @Id
    private Long id;
    @Field(type = FieldType.Nested)
    private CategoryModel category;
    @Field(type = FieldType.Nested)
    private BrandModel brand;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Double)
    private Double listPrice;
    @Field(type = FieldType.Double)
    private Double sellPrice;
    @Field(type = FieldType.Double)
    private Double supplyDiscountCost;
    @Field(type = FieldType.Double)
    private Double supplyFeeRate;
    @Field(type = FieldType.Boolean)
    private boolean isSupplyVatInclude;
    @Field(type = FieldType.Text)
    private String supplyCalculateMethodCode;
    @Field(type = FieldType.Double)
    private Double sellDiscountRate;
    @Field(type = FieldType.Double)
    private Double sellDiscountCost;
    @Field(type = FieldType.Double)
    private Double sellFeeRate;
    @Field(type = FieldType.Text)
    private String sellCalculateMethod;
    @Field(type = FieldType.Double)
    private Double sendCost;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date sellStartDate;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date sellEndDate;
    @Field(type = FieldType.Boolean)
    private boolean isValid;
    @Field(type = FieldType.Text)
    private String sticker;
    @Field(type = FieldType.Text)
    private String exceptStoreIds;
}
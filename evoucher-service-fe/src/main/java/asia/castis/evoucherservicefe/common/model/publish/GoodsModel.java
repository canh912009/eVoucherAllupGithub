package asia.castis.evoucherservicefe.common.model.publish;

import asia.castis.evoucherservicefe.common.enums.EnumGoodsPeriodType;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Data
@ToString
public class GoodsModel {
    @Id
    private Long id;
    private List<CategoryModel> categories;
    @Field(type = FieldType.Nested)
    private BrandModel brand;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Double)
    private Double listPrice;
    @Field(type = FieldType.Double)
    private Double sellPrice;
    @Field(type = FieldType.Double)
    private Double supplyDiscountRate;
    @Field(type = FieldType.Double)
    private Double supplyDiscountAmount;
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
    private Date startDate;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date endDate;
    @Field(type = FieldType.Boolean)
    private boolean isValid;
    @Field(type = FieldType.Text)
    private String sticker;
    @Field(type = FieldType.Text)
    private List<String> exceptStoreIds;
    @Field(type = FieldType.Text)
    private String imagePath;
    @Field(type = FieldType.Text)
    private String imageName;
    @Field(type = FieldType.Text)
    private EnumGoodsPeriodType periodType; // FIXED_TERM/FIXED_DT
    @Field(type = FieldType.Double)
    private Double periodTerm;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private Date periodExpireDate;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Text)
    private String system;
    @Field(type = FieldType.Text)
    String type;
//    @Field(type = FieldType.Nested)
    List<GoodsModel> choices;
    @Field(type = FieldType.Integer)
    private Integer remainingCount;
}

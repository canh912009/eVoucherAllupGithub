package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.cms.service.models.vnptEPay.response.PinSearchDTO;
import com.evoucher.adminapi.common.enums.ExternalPinStatus;
import com.evoucher.adminapi.common.enums.ExternalPinType;
import com.evoucher.adminapi.common.enums.PinDisplayType;
import com.evoucher.adminapi.common.models.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@SqlResultSetMapping(
        name = "vnpt_search_pin_mapping",
        classes = @ConstructorResult(
                targetClass = PinSearchDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "giftTitle", type = String.class),
                        @ColumnResult(name = "brandName", type = String.class),
                        @ColumnResult(name = "price", type = Long.class),
                        @ColumnResult(name = "quantity", type = Integer.class),
                        @ColumnResult(name = "createDate", type = Date.class)
                }
        )
)

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_EXT_PIN")
public class ExternalPin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "EXT_PIN_NO")
    private String externalPinNo;

    @Column(name = "GOODS_ID")
    private Integer goodsId;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ExternalPinStatus status;

    @Column(name = "EXT_PIN_TYPE")
    @Enumerated(EnumType.STRING)
    private ExternalPinType externalPinType;

    @JoinColumn(name = "UPLOAD_ID")
    @ManyToOne
    private ExternalPinUpload externalPinUpload;

    @Column(name = "EXPIRE_TIME")
    private Date expireTime;

    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "display_type")
    @Enumerated(EnumType.STRING)
    private PinDisplayType displayType;
}

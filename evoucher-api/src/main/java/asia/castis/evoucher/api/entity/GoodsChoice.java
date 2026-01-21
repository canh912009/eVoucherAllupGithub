package asia.castis.evoucher.api.entity;

import asia.castis.evoucher.api.common.enums.EnumValidYn;
import lombok.*;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "TB_GOODS_CHOICES")
@IdClass(GoodsChoiceId.class)
public class GoodsChoice extends BaseEntity {
    @Id
    @Column(name = "PARENT_GOODS_ID")
    private Integer parentGoodsId;
    @Id
    @Column(name = "GOODS_ID")
    private Integer goodsId;

    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

    @Column(name = "display_idx")
    private int displayIdx;

}

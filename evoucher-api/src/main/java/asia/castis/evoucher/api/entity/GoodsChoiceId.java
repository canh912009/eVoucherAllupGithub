package asia.castis.evoucher.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsChoiceId implements Serializable {

    @Column(name = "PARENT_GOODS_ID")
    private Integer parentGoodsId;

    @Column(name = "GOODS_ID")
    private Integer goodsId;
}

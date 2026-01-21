package asia.castis.evoucher.api.entity;

import asia.castis.evoucher.api.common.enums.EnumValidYn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "tb_bulk_goods")
public class BulkGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bulkGoodsId;
    @OneToOne
    @JoinColumn(name = "goods_id")
    private Goods goods;
    @Column(name = "display_idx")
    private int displayIdx;
    @Column(name = "valid_yn")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;
    @Column(name = "reg_dt")
    private Date regDt;
    @Column(name = "reg_id")
    private String regId;
    @Column(name = "updt_dt")
    private Date updtDt;
    @Column(name = "updt_id")
    private String updtId;
    @Column(name = "bulk_brand_id")
    private Long bulkBrandId;
}

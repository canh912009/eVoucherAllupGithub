package asia.castis.evoucher.api.entity;

import asia.castis.evoucher.api.common.enums.EnumValidYn;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "tb_bulk_ctgr")
public class BulkCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulk_ctgr_id")
    private Long bulkCtgrId;

    @Column(name = "goods_id")
    private Integer goodsId;

    @OneToOne
    @JoinColumn(name = "ctgr_cd")
    private Category category;

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

//    @OneToMany(mappedBy = "bulkCategory")
//    private Set<BulkBrand> bulkBrands;

    // Getters and setters
}

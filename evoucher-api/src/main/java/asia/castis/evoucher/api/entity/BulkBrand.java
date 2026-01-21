package asia.castis.evoucher.api.entity;

import asia.castis.evoucher.api.common.enums.EnumValidYn;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_bulk_brand")
@Setter
@Getter
public class BulkBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulk_brand_id")
    private Long bulkBrandId;

    @Column(name = "bulk_ctgr_id")
    private Long bulkCtgrId;

    @OneToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

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
}

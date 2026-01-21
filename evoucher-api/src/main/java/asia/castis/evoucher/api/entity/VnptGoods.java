package asia.castis.evoucher.api.entity;

import asia.castis.evoucher.api.common.enums.EnumValidYn;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_goods_vnpt")
@Data
public class VnptGoods extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_goods_id", nullable = false)
    private Integer parentGoodsId;

    @Column(name = "provider_cd", length = 50)
    private String providerCode;

    @OneToOne
    @JoinColumn(name = "provider_cd", referencedColumnName = "provider_cd", insertable = false, updatable = false)
    @JsonManagedReference
    private VnptProvider vnptProvider;

    @Column(name = "face_value", nullable = false)
    private Double faceValue;

    @Column(name = "valid_yn", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

    @Column(name = "description", length = 500)
    private String description;
}

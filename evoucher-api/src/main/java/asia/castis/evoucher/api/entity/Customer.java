package asia.castis.evoucher.api.entity;

import asia.castis.evoucher.api.common.enums.CustomerType;
import asia.castis.evoucher.api.common.enums.EnumValidYn;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tb_customer")
public class Customer extends BaseEntity {
    @Id
    @Column(name = "customer_id")
    private String id;

    @Column(name = "customer_nm")
    private String customerName;

    @Column(name = "valid_yn")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

    @Column(name = "customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType customerTypeCode;
}

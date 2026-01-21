package asia.castis.evoucher.api.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tb_user")
@ToString
public class EndUser {
    @Id
    private Long id;

    @Column(name = "user_mobile_num")
    private String userMobileNum;

    @Column(name = "user_nm")
    private String userNm;
}

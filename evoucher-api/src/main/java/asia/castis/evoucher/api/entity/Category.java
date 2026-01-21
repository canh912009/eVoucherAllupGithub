package asia.castis.evoucher.api.entity;

import asia.castis.evoucher.api.common.enums.EnumValidYn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tb_category")
public class Category extends BaseEntity {
    @Id
    @Column(name = "ctgr_cd")
    private String categoryCode;

    @Column(name = "ctgr_nm")
    private String categoryName;

    @Column(name = "ctgr_img_path")
    private String imagePath;

    @Column(name = "ctgr_img_nm")
    private String imageName;

    @Column(name = "valid_yn")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;
}

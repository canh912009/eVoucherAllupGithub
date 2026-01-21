package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.common.enums.ExternalPinUploadStatus;
import com.evoucher.adminapi.common.models.BaseEntity;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_EXT_PIN_UPLOAD")
public class ExternalPinUpload extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UPLOAD_ID")
    private Integer id;

    @Column(name = "UPLOAD_NM")
    private String uploadName;

    @Column(name = "GOODS_ID")
    private Integer goodsId;

    @Column(name = "UPLOAD_FILE_PATH")
    private String uploadFilePath;

    @Column(name = "UPLOAD_FILE_NM")
    private String uploadFileName;

    @Column(name = "ROW_CNT")
    private Integer rowCount;

    @Column(name = "MEMO")
    private String memo;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ExternalPinUploadStatus status;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(
            mappedBy = "externalPinUpload",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ExternalPin> pins;
}

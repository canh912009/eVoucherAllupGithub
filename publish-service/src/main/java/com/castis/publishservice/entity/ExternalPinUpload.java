package com.castis.publishservice.entity;

import com.castis.publishservice.utils.status.ExternalPinUploadStatus;
import lombok.*;

import javax.persistence.*;

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
    private Long id;

    @Column(name = "UPLOAD_NM")
    private String uploadName;

    @Column(name = "GOODS_ID")
    private Long goodsId;

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
}

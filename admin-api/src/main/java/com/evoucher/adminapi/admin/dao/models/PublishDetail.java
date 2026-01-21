package com.evoucher.adminapi.admin.dao.models;

import com.evoucher.adminapi.cms.service.models.EndUserDTO;
import com.evoucher.adminapi.common.config.PropertyConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;


@NamedNativeQuery(
        name = "PublishDetail.publishMetaDataDetail",
        query = "select u.user_mobile_num as userMobileNum" +
                "    , u.user_nm as userNm" +
                "    , u.gender as gender" +
                "    , u.birthday as birthday" +
                "    , u.address as address" +
                "    , u.email as email " +
                "    , d.publish_dtl_status_cd as smsStatus" +
                "    , v.ext_pin_no as externalPinNo" +
                "    , d.publish_rslt_msg as publishResultMessage" +
                "    , v.ev as ev" +
                "    from tb_publish_detail d " +
                "left join tb_user u on d.user_id = u.id " +
                "left join tb_voucher v on d.publish_dtl_id = v.publish_dtl_id " +
                "where d.publish_id = ?1 and v.orig_ev is null and v.parent_voucher_ev is null",
        resultSetMapping = "publishMetaDataDetailResultMapping"

)

@SqlResultSetMapping(
        name="publishMetaDataDetailResultMapping",
        classes={
                @ConstructorResult(
                        targetClass= EndUserDTO.class,
                        columns={
                                @ColumnResult(name="userMobileNum", type = String.class),
                                @ColumnResult(name="userNm", type = String.class),
                                @ColumnResult(name="gender", type = String.class),
                                @ColumnResult(name="birthday", type = Date.class),
                                @ColumnResult(name="address", type = String.class),
                                @ColumnResult(name="email", type = String.class),
                                @ColumnResult(name="smsStatus", type = String.class),
                                @ColumnResult(name="externalPinNo", type = String.class),
                                @ColumnResult(name="publishResultMessage", type = String.class),
                                @ColumnResult(name="ev", type = String.class),
                        }
                )
        }
)

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_publish_detail")
@ToString
public class PublishDetail {
    @Id
    @Column(name = "publish_dtl_id")
    private Integer id;

    @Column(name = "publish_id")
    private Integer publishId;

    @Column(name = "receiver_mobile_no")
    @Convert(converter = PropertyConverter.class)
    private String receiverMobileNo;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "publish_dtl_status_cd")
    private String publishDetailStatusCode;
    @Column(name = "EXT_PIN_ID")
    private Integer externalPinId;
    @Column(name = "publish_rslt_msg")
    private String publishResultMessage;
}

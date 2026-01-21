package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.common.config.PropertyConverter;
import com.evoucher.adminapi.common.enums.EnumGender;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tb_user")
//@IdClass(EndUserPrimaryKey.class)
@ToString
public class EndUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_mobile_num")
    @Convert(converter = PropertyConverter.class)
    private String userMobileNum;

    @Column(name = "user_nm")
    @Convert(converter = PropertyConverter.class)
    private String userNm;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private EnumGender gender;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

}

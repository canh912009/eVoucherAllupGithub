package com.evoucher.evoucherbe.entity;

import com.evoucher.evoucherbe.config.PropertyConverter;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@Table(name = "TB_USER")
public class EndUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_MOBILE_NUM")
    @Convert(converter = PropertyConverter.class)
    private String userMobileNum;

    @Column(name = "USER_NM")
    @Convert(converter = PropertyConverter.class)
    private String userNm;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "BIRTHDAY")
    private Date birthday;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "email")
    private String email;

}

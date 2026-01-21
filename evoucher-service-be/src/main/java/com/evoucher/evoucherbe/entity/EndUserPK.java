package com.evoucher.evoucherbe.entity;

import com.evoucher.evoucherbe.config.PropertyConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndUserPK implements Serializable {
    @Column(name = "USER_MOBILE_NUM")
    @Convert(converter = PropertyConverter.class)
    private String userMobileNum;
}

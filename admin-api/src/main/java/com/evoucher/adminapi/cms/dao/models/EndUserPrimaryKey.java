package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.common.config.PropertyConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndUserPrimaryKey implements Serializable {
    @Column(name = "user_mobile_num")
    @Convert(converter = PropertyConverter.class)
    private String userMobileNum;
}

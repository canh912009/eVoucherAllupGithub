package com.evoucher.adminapi.common.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class BaseDTO implements Serializable {
    private String regId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date regDt;
    private String updtId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updtDt;
}

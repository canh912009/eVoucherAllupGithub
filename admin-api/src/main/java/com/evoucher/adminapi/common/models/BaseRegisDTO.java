package com.evoucher.adminapi.common.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
public class BaseRegisDTO {
    private String regId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date regDt;
}

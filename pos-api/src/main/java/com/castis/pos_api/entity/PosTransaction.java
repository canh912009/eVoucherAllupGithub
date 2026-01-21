package com.castis.pos_api.entity;

import com.castis.pos_api.enum_constant.PosKeyType;
import com.castis.pos_api.enum_constant.PosRequestType;
import com.castis.pos_api.enum_constant.ProcessResult;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_pos_transaction")
@Data
public class PosTransaction {

    @Id
    @Column(name = "transaction_id", length = 50)
    private String transactionId;

    @Column(name = "pos_key", length = 100)
    private String key;

    @Column(name = "app_id", length = 50, nullable = false)
    private String appId;

    @Column(name = "key_type")
    private Integer keyType;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "user_phone_no", length = 50)
    private String userPhoneNo;

    @Column(name = "pos_cd", length = 50)
    private String posCd;

    @Column(name = "brand_id", length = 20)
    private String brandId;

    @Column(name = "ev", length = 40)
    private String ev;

    @Column(name = "req_dt")
    private Date requestDate;

    @Column(name = "res_dt")
    private Date responseDate;

    @Column(name = "result", length = 20)
    @Enumerated(EnumType.STRING)
    private ProcessResult result;

    @Column(name = "req_type", length = 10)
    @Enumerated(EnumType.STRING)
    private PosRequestType requestType;

    @Column(name = "prepaid_amount")
    private Double prepaidAmount;

    @Column(name = "res_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "request_body", columnDefinition = "TEXT")
    private String requestBody;
}

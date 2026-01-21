package com.castis.publishservice.entity;

import com.castis.publishservice.utils.enum_template.ThirdRequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_3rd_party_req")
public class ThirdPartyCallAPIHistory {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "system")
    private String system;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "goods_id")
    private Long goodsId;

    @Column(name = "action")
    private String action;

    @Column(name = "request_url")
    private String requestUrl;

    @Column(name = "request_time")
    private Date requestTime;

    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "response_time")
    private Date responseTime;

    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "result")
    private String result;

    @Column(name = "description")
    private String description;

    @Column(name = "uploadId")
    private Long uploadId;
    @Column(name = "request_type")
    @Enumerated(EnumType.STRING)
    private ThirdRequestType requestType;
}

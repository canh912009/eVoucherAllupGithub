package com.evoucher.evoucherbe.entity;

import com.evoucher.evoucherbe.common.enums.SystemType;
import com.evoucher.evoucherbe.common.enums.VoucherChoiceHistoryStatus;
import com.evoucher.evoucherbe.service.request.ChildVoucherRequest;
import com.evoucher.evoucherbe.service.request.VoucherChoiceRequest;
import com.evoucher.evoucherbe.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_parent_voucher_history")
public class VoucherChoiceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "CHOICE_EV")
    private String choiceEv;

    @Column(name = "PUBLISH_ID")
    private Integer publishId;

    @Column(name = "GOODS_CHOICE_LIST")
    private String goodsChoiceList;

    @Column(name = "USER_MOBILE_NUM")
    private String userMobileNumber;

    @Column(name = "USER_NM")
    private String userName;

    @CreatedDate
    @Column(name = "PURCHASE_DT", updatable = false)
    private Date purchaseDate;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private VoucherChoiceHistoryStatus status;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "system", nullable = false)
    @Enumerated(EnumType.STRING)
    private SystemType system;

    public VoucherChoiceHistory(VoucherChoiceRequest voucherChoiceRequest) {
        this.choiceEv = voucherChoiceRequest.getParentId();
        this.publishId = voucherChoiceRequest.getPublishId();
        this.goodsChoiceList = Constant.gson.toJson(voucherChoiceRequest.getProducts());
        this.system = voucherChoiceRequest.getType();
    }
    public VoucherChoiceHistory(ChildVoucherRequest voucherChoiceRequest) {
        this.choiceEv = voucherChoiceRequest.getParentId();
        this.publishId = voucherChoiceRequest.getPublishId();
        this.goodsChoiceList = Constant.gson.toJson(voucherChoiceRequest.getProducts());
        this.system = voucherChoiceRequest.getType();
    }
}

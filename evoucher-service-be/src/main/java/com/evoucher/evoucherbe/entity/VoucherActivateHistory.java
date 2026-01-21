package com.evoucher.evoucherbe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "TB_ACTIVATE_HISTORY")
public class VoucherActivateHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private Long id;
    @Column(name = "EV")
    private String ev;
    @Column(name = "SERIAL_NO")
    private String serialNumber;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "ACTIVATION_DT")
    private Date activationDate;
}

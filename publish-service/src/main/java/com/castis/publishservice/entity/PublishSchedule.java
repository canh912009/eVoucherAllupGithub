package com.castis.publishservice.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "publish_schedule")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PublishSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "create_date")
    @CreatedDate
    private Date createDate;
    @Basic
    @Column(name = "update_date")
    @LastModifiedDate
    private Date updateDate;
    @Basic
    @Column(name = "start_at")
    private Date startAt;
    @Basic
    @Column(name = "publish_id")
    private Long publishId;
    @Basic
    @Column(name = "status")
    private Integer status;
}

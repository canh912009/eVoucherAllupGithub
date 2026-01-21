package com.evoucher.adminapi.admin.dao.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_message_template")
public class MessageTemplate {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "message_string")
    private String messageString;
    @Basic
    @Column(name = "template_detail")
    private String templateDetail;
    @Basic
    @Column(name = "create_date")
    private Timestamp createDate;
    @Basic
    @Column(name = "update_date")
    private Timestamp updateDate;
    @Basic
    @Column(name = "creator")
    private String creator;
    @Basic
    @Column(name = "modifier")
    private String modifier;

    @Column(name = "system")
    private String system;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageTemplate that = (MessageTemplate) o;
        return id == that.id && Objects.equals(messageString, that.messageString) && Objects.equals(name, that.name) && Objects.equals(templateDetail, that.templateDetail) && Objects.equals(createDate, that.createDate) && Objects.equals(updateDate, that.updateDate) && Objects.equals(creator, that.creator) && Objects.equals(modifier, that.modifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, messageString, templateDetail, createDate, updateDate, creator, modifier);
    }
}

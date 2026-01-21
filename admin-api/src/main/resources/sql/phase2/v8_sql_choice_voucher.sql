create table tb_goods_choices
(
    parent_goods_id bigint      not null,
    goods_id        bigint      not null,
    valid_yn        char        not null,
    reg_id          varchar(20) not null,
    reg_dt          datetime    not null,
    updt_id         varchar(20) null,
    updt_dt         datetime    null,
    primary key (parent_goods_id, goods_id),
    constraint tb_goods_choices_tb_goods_goods_id_fk
        foreign key (parent_goods_id) references tb_goods (goods_id),
    constraint tb_goods_choices_tb_goods_goods_id_fk2
        foreign key (goods_id) references tb_goods (goods_id)
);

alter table tb_voucher
    add choice_voucher_ev varchar(40) null;
alter table tb_voucher
    add choice_token varchar(10) null;

create table tb_voucher_choices_history
(
    id                  bigint auto_increment primary key,
    choice_ev           varchar(40)      not null,
    publish_id          bigint           not null,
    goods_choice_list   varchar(2000)    not null,
    user_mobile_num     varchar(150)     not null,
    user_nm             varchar(400)     null,
    purchase_dt         datetime         null,
    status              varchar(20)      null,
    description         varchar(1000) charset utf8mb3    null
);

alter table tb_settlement_log
    add choice_ev varchar(40) null;

INSERT INTO e_voucher.tb_code (cd_id, reg_dt, reg_id, updt_dt, updt_id, cd_grp_id, cd_nm, sort_order, valid_yn)
VALUES ('CHOICE', '2023-07-04 17:45:32', '1', '2023-07-04 17:45:32', '1', 'SYSTEM', 'Choice', 1, 'Y');

alter table tb_message_template
    add system varchar(50) null;

UPDATE e_voucher.tb_message_template t
SET t.`system` = 'NORMAL'
WHERE t.`system` is null;

INSERT INTO e_voucher.tb_message_template (name, message_string, template_detail, create_date, update_date, creator, modifier, `system`)
VALUES ('Template choice voucher', '[aQua VN] {sender} gui ban mot mon qua lua chon. Vui long truy cap vao link de nhan qua: {shortLink}. Ma OTP cua ban la {OTP}', '{"template":"","language":"EN","data":[{"key":"customerName","type":"CUSTOM","encrypted":true,"defaultValue":null,"maxLength":30},
{"key":"OTP","type":"CUSTOM","encrypted":false,"defaultValue":null,"maxLength":300},{"key":"sender","type":"CUSTOM","encrypted":false,"defaultValue":null,"maxLength":30},{"key":"message","type":"DISABLED","encrypted":false,"defaultValue":null,"maxLength":30},{"key":"productName","type":"DISABLED","encrypted":false,"defaultValue":null,"maxLength":30},{"key":"expireDate","type":"DISABLED","encrypted":false,"defaultValue":null,"maxLength":30},{"key":"transferMessage","type":"DISABLED","encrypted":false,"defaultValue":null,"maxLength":30},{"key":"shortLink","type":"CUSTOM","encrypted":false,"defaultValue":null,"maxLength":50},{"key":"cta2","type":"DISABLED","encrypted":false,"defaultValue":null,"maxLength":30}]}',
        '2023-12-27 11:18:25', '2023-12-27 11:18:25', null, null, 'CHOICE');


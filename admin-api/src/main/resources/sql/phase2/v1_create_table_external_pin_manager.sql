create table tb_ext_pin_upload
(
    upload_id        bigint auto_increment,
    upload_nm        VARCHAR(200) charset utf8mb3 null,
    goods_id         bigint                       null,
    upload_file_path VARCHAR(500) charset utf8mb3 null,
    upload_file_nm   VARCHAR(100) charset utf8mb3 null,
    row_cnt          int                          null,
    memo             VARCHAR(500) charset utf8mb3 null,
    status           VARCHAR(20)                  null,
    reg_id           VARCHAR(20)                  null,
    reg_dt           datetime                     null,
    updt_id          VARCHAR(20)                  null,
    updt_dt          datetime                     null,
    constraint tb_ext_pin_upload_pk
        primary key (upload_id)
);

create table tb_ext_pin
(
    id           bigint auto_increment
        primary key,
    ext_pin_no   varchar(100) null,
    goods_id     bigint       null,
    upload_id    bigint       null,
    status       varchar(20)  null,
    ext_pin_type varchar(20)  null,
    reg_id       varchar(20)  null,
    reg_dt       datetime     null,
    updt_id      varchar(20)  null,
    updt_dt      datetime     null,
    constraint tb_ext_pin_tb_ext_pin_upload_upload_id_fk
        foreign key (upload_id) references tb_ext_pin_upload (upload_id)
);

alter table tb_goods
    add display_type varchar(50) null;

INSERT INTO e_voucher.tb_code_group (cd_grp_id, reg_dt, reg_id, updt_dt, updt_id, cd_grp_nm, valid_yn)
VALUES ('EXT_PIN_DISPLAY_TYPE', '2023-09-04 17:43:21', '1', '2023-09-04 17:43:21', '1', 'External pin display type','Y');

INSERT INTO e_voucher.tb_code (cd_id, reg_dt, reg_id, updt_dt, updt_id, cd_grp_id, cd_nm, sort_order, valid_yn)
VALUES ('NORMAL', '2023-07-04 17:45:32', '1', '2023-07-04 17:45:32', '1', 'EXT_PIN_DISPLAY_TYPE', 'Normal', 1, 'Y');

INSERT INTO e_voucher.tb_code (cd_id, reg_dt, reg_id, updt_dt, updt_id, cd_grp_id, cd_nm, sort_order, valid_yn)
VALUES ('BARCODE', '2023-07-04 17:45:32', '1', '2023-07-04 17:45:32', '1', 'EXT_PIN_DISPLAY_TYPE', 'Barcode', 2, 'Y');

-- add code group
INSERT INTO e_voucher.tb_code_group (cd_grp_id, reg_dt, reg_id, updt_dt, updt_id, cd_grp_nm, valid_yn)
VALUES ('CUSTOMER_TYPE', '2023-10-31 09:53:32', '1', '2023-10-31 09:53:32', '1', 'Customer type', 'Y');

INSERT INTO e_voucher.tb_code (cd_id, reg_dt, reg_id, updt_dt, updt_id, cd_grp_id, cd_nm, sort_order, valid_yn)
VALUES ('B2B', '2023-10-31 09:45:32', '1', '2023-10-31 09:45:32', '1', 'CUSTOMER_TYPE', 'B2B', 1, 'Y');

INSERT INTO e_voucher.tb_code (cd_id, reg_dt, reg_id, updt_dt, updt_id, cd_grp_id, cd_nm, sort_order, valid_yn)
VALUES ('CHANNEL', '2023-10-31 09:45:32', '1', '2023-10-31 09:45:32', '1', 'CUSTOMER_TYPE', 'Channel', 2, 'Y');

-- add field customer_type in tb_customer
alter table tb_customer
    add customer_type varchar(50) null;

-- update customer_type = 'B2B' for all customer already exist
UPDATE tb_customer
SET customer_type = 'B2B'
WHERE customer_type is null;

-- add field expire_time to tb_ext_pin
alter table tb_ext_pin
    add expire_time datetime null;

-- add content_link to tb_voucher
alter table tb_voucher
    add content_link varchar(1000) null;

-- add table tb_external_publish
create table tb_external_publish
(
    transaction_id     varchar(100)  not null
        primary key,
    sha                varchar(255)  null,
    request_time       datetime      null,
    user_id            varchar(100)  null,
    is_send_sms        tinyint(1)    null,
    sms_scheduled      datetime      null,
    subject            varchar(64)   null,
    content_text       varchar(1000) null,
    content_image_path varchar(500)  null,
    content_image_name varchar(500)  null,
    content_link       varchar(500)  null,
    orders             text          null,
    customer_id        varchar(20)   null
);

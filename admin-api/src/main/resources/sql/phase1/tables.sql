create database e_voucher;
use e_voucher;

CREATE TABLE `publish_schedule` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `create_date` datetime DEFAULT NULL,
    `publish_id` bigint(20) DEFAULT NULL,
    `start_at` datetime DEFAULT NULL,
    `status` int(11) DEFAULT NULL,
    `update_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

create table tb_code_group
(
    cd_grp_id varchar(20)  not null
        primary key,
    reg_dt    datetime     not null,
    reg_id    varchar(20)  not null,
    updt_dt   datetime     null,
    updt_id   varchar(20)  null,
    cd_grp_nm varchar(100) collate utf8mb4_unicode_ci not null,
    valid_yn  char         not null
);

create table tb_code
(
    cd_id      varchar(20)  not null,
    reg_dt     datetime     not null,
    reg_id     varchar(20)  not null,
    updt_dt    datetime     null,
    updt_id    varchar(20)  null,
    cd_grp_id  varchar(20)  not null,
    cd_nm      varchar(100) collate utf8mb4_unicode_ci not null,
    sort_order int          not null,
    valid_yn   char         not null,
    primary key (cd_id, cd_grp_id),
    constraint tb_code_tb_code_group_cd_grp_id_fk
        foreign key (cd_grp_id) references tb_code_group (cd_grp_id)
);

create table tb_menu_group
(
    menu_grp_id bigint auto_increment
        primary key,
    reg_dt      datetime     not null,
    reg_id      varchar(20)  not null,
    updt_dt     datetime     null,
    updt_id     varchar(20)  null,
    menu_grp_nm varchar(100) collate utf8mb4_unicode_ci not null,
    sort_order  int          not null,
    valid_yn    char         not null
);

create table tb_menu
(
    menu_id     bigint auto_increment
        primary key,
    reg_dt      datetime     not null,
    reg_id      varchar(20)  not null,
    updt_dt     datetime     null,
    updt_id     varchar(20)  null,
    menu_grp_id bigint       null,
    menu_nm     varchar(100) collate utf8mb4_unicode_ci not null,
    menu_url    varchar(200) not null,
    sort_order  int          not null,
    valid_yn    char         not null,
    constraint tb_menu_tb_menu_group_menu_grp_id_fk
        foreign key (menu_grp_id) references tb_menu_group (menu_grp_id)
);

create table tb_role
(
    role_code  varchar(20)  not null
        primary key,
    reg_dt     datetime     null,
    reg_id     varchar(20)  null,
    updt_dt    datetime     not null,
    updt_id    varchar(20)  not null,
    role_nm    varchar(100) null,
    sort_order int          null,
    valid_yn   char         null
);

create table tb_role_menu_rel
(
    menu_id   bigint      not null,
    role_code varchar(20) not null,
    reg_dt    datetime    not null,
    reg_id    varchar(20) not null,
    primary key (menu_id, role_code),
    constraint tb_role_menu_rel_tb_menu_menu_id_fk
        foreign key (menu_id) references tb_menu (menu_id),
    constraint tb_role_menu_rel_tb_role_role_code_fk
        foreign key (role_code) references tb_role (role_code)
);

create table tb_admin
(
    admin_id       varchar(20)      not null
        primary key,
    reg_dt         datetime         not null,
    reg_id         varchar(20)      not null,
    updt_dt        datetime         null,
    updt_id        varchar(20)      null,
    admin_corp_id  varchar(20)      null,
    admin_nm       varchar(50) collate utf8mb4_unicode_ci      not null,
    role_cd        varchar(20)      not null,
    email          varchar(50) collate utf8mb4_unicode_ci      null,
    last_login_dt  datetime         null,
    login_fail_cnt int              default 0   not null,
    mobile_no      varchar(20)      not null,
    passwd         varchar(300)     not null,
    passwd_init_yn char default 'N' not null,
    passwd_updt_dt datetime         null,
    tel            varchar(20)      null,
    valid_yn       char             not null,
    constraint tb_admin_tb_role_role_code_fk
        foreign key (role_cd) references tb_role (role_code)
);

create table tb_supplier
(
    supplier_id               varchar(15)                  not null
        primary key,
    reg_dt                    datetime                     not null,
    reg_id                    varchar(20)                  not null,
    updt_dt                   datetime                     null,
    updt_id                   varchar(20)                  null,
    account_num               varchar(50)                  null,
    account_nm                varchar(100)                 null collate utf8mb4_unicode_ci,
    bank_nm                   varchar(100)                 null collate utf8mb4_unicode_ci,
    supplier_nm               varchar(100)                 null collate utf8mb4_unicode_ci,
    taxcode                   varchar(15)                  not null,
    mngr_email                varchar(50)                  not null,
    mngr_mobile_no            varchar(20)                  null,
    mngr_nm                   varchar(50)                  not null collate utf8mb4_unicode_ci,
    primary_contact_email     varchar(50)                  not null,
    primary_contact_mobile_no varchar(20)                  not null,
    primary_contact_nm        varchar(50)                  not null collate utf8mb4_unicode_ci,
    supply_dc_rate            decimal(8, 2)                null,
    supply_commission_rate    decimal(8, 2)                null,
    settlement_method_cd      varchar(20)                  null,
    vat_inc_yn                char                         null,
    valid_yn                  char                         not null,
    apprv_status_cd           varchar(20)                  null,
    apprver_id                varchar(20)                  null
);

create table tb_supplier_apprv_history
(
    apprv_history_id bigint auto_increment
        primary key,
    reg_dt           datetime                      not null,
    reg_id           varchar(20)                   not null,
    apprv_status_cd  varchar(10)                   not null,
    supplier_id      varchar(15)                   not null,
    rejct_reason     varchar(1000)                 null collate utf8mb4_unicode_ci,
    constraint tb_supplier_apprv_history_tb_supplier_supplier_id_fk
        foreign key (supplier_id) references tb_supplier (supplier_id)
);

create table tb_brand
(
    brand_id         varchar(20)                   not null
        primary key,
    reg_dt           datetime                      null,
    reg_id           varchar(20)                   not null,
    updt_dt          datetime                      null,
    updt_id          varchar(20)                   null,
    brand_img_nm     varchar(100)                  null collate utf8mb4_unicode_ci,
    brand_img_path   varchar(500)                  null,
    brand_nm         varchar(100)                  not null collate utf8mb4_unicode_ci,
    `desc`           varchar(1000)                 null collate utf8mb4_unicode_ci,
    supplier_id      varchar(15)                   not null,
    valid_yn         char                          not null,
    default_brand_yn char                          null,
    constraint tb_brand_tb_supplier_supplier_id_fk
        foreign key (supplier_id) references tb_supplier (supplier_id)
);

create table tb_store
(
    store_id            varchar(25)                  not null
        primary key,
    reg_dt              datetime                     not null,
    reg_id              varchar(20)                  not null,
    updt_dt             datetime                     null,
    updt_id             varchar(20)                  null,
    full_address        varchar(200)                 null collate utf8mb4_unicode_ci,
    brand_id            varchar(20)                  null,
    map_cd              varchar(20)                  null,
    map_interation_type varchar(20)                  null,
    region              varchar(50)                  null collate utf8mb4_unicode_ci,
    store_type          varchar(50)                  null collate utf8mb4_unicode_ci,
    store_img_nm        varchar(100)                 null collate utf8mb4_unicode_ci,
    store_img_path      varchar(500)                 null,
    store_nm            varchar(100)                 not null collate utf8mb4_unicode_ci,
    valid_yn            char                         not null,
    tel                 varchar(20)                  null,
    constraint tb_store_tb_brand_brand_id_fk
        foreign key (brand_id) references tb_brand (brand_id)
);

create table tb_customer
(
    customer_id               varchar(19)                             not null
        primary key,
    customer_nm               varchar(100) collate utf8mb3_unicode_ci not null,
    taxcode                   varchar(20)                             not null,
    bank_nm                   varchar(100) collate utf8mb4_unicode_ci null,
    account_num               varchar(50)                             null,
    account_nm                varchar(100) collate utf8mb4_unicode_ci null,
    sell_dc_rate              decimal(8, 2)                           null,
    sell_commission_rate      decimal(8, 2)                           null,
    vat_inc_yn                char                                    null,
    settlement_method_cd      varchar(20)                             null,
    send_cost                 decimal(12, 2)                          null,
    mngr_nm                   varchar(50) collate utf8mb4_unicode_ci  null,
    mngr_email                varchar(50) collate utf8mb4_unicode_ci  null,
    mngr_mobile_no            varchar(20)                             null,
    primary_contact_nm        varchar(50) collate utf8mb4_unicode_ci  null,
    primary_contact_email     varchar(50) collate utf8mb4_unicode_ci  null,
    primary_contact_mobile_no varchar(20)                             not null,
    valid_yn                  char                                    not null,
    reg_dt                    datetime                                not null,
    reg_id                    varchar(20)                             not null,
    updt_dt                   datetime                                null,
    updt_id                   varchar(20)                             null,
    apprv_status_cd           varchar(10)                             null,
    apprver_id                varchar(20)                             null
);

create table tb_customer_apprv_history
(
    apprv_history_id int auto_increment
        primary key,
    customer_id      varchar(19)                              not null,
    apprv_status_cd  varchar(10)                              null,
    rejct_reason     varchar(1000) collate utf8mb4_unicode_ci null,
    reg_id           varchar(20)                              not null,
    reg_dt           datetime                                 not null,
    updt_dt          datetime                                 null,
    updt_id          varchar(20)                              null,
    constraint tb_customer_apprv_history_tb_customer_customer_id_fk
        foreign key (customer_id) references tb_customer (customer_id)
);

create table tb_customer_contract
(
    customer_contract_id      bigint auto_increment
        primary key,
    reg_dt                    datetime                      null,
    reg_id                    varchar(20)                   null,
    updt_dt                   datetime                      null,
    updt_id                   varchar(20)                   null,
    apprv_dt                  datetime                      null,
    apprver_id                varchar(20)                   null,
    apprv_req_dt              datetime                      null,
    apprv_req_id              varchar(20)                   null,
    apprv_status_cd           varchar(10)                   null,
    contract_file_nm          varchar(100)                  null collate utf8mb4_unicode_ci,
    contract_file_path        varchar(500)                  null,
    contract_nm               varchar(200)                  not null collate utf8mb4_unicode_ci,
    customer_id               varchar(19)                   null,
    ed_dt                     datetime                      not null,
    rejct_dt                  datetime                      null,
    rejcter_id                varchar(20)                   null,
    rejct_reason              varchar(1000)                 null collate utf8mb4_unicode_ci,
    sell_settlement_method_cd varchar(20)                   null,
    sell_dc_amount            decimal(12, 2)                null,
    sell_dc_rate              decimal(8, 2)                 null,
    sell_commission_rate      decimal(8, 2)                 null,
    sell_vat_inc_yn           char                          null,
    st_dt                     datetime                      not null,
    valid_yn                  char                          null
);

create table tb_customer_contract_apprv_history
(
    apprv_history_id     bigint auto_increment
        primary key,
    customer_contract_id bigint                        not null,
    apprv_status_cd      varchar(10)                   not null,
    rejct_reason         varchar(1000)                 null collate utf8mb4_unicode_ci,
    reg_id               varchar(20)                   not null,
    reg_dt               datetime                      not null,
    constraint tb_contract_apprv_history_tb_contract_contract_id_fk
        foreign key (customer_contract_id) references tb_customer_contract (customer_contract_id)
);

create table tb_supplier_contract
(
    supplier_contract_id        bigint auto_increment
        primary key,
    contract_nm                 varchar(200)                not null collate utf8mb4_unicode_ci,
    supplier_id                 varchar(15)                 not null,
    st_dt                       datetime                    not null,
    ed_dt                       datetime                    not null,
    supply_dc_amount            decimal(12, 2)              null,
    supply_dc_rate              decimal(8, 2)               null,
    supply_commission_rate      decimal(8, 2)               null,
    supply_vat_inc_yn           char                        null,
    supply_settlement_method_cd varchar(20)                 null,
    apprv_status_cd             varchar(10)                 null,
    apprv_req_id                varchar(20)                 null,
    apprv_req_dt                datetime                    null,
    apprv_dt                    datetime                    null,
    apprver_id                  varchar(20)                 null,
    contract_file_nm            varchar(100)                null collate utf8mb4_unicode_ci,
    contract_file_path          varchar(500)                null collate utf8mb4_unicode_ci,
    rejct_dt                    datetime                    null,
    rejcter_id                  varchar(20)                 null,
    rejct_reason                varchar(1000)               null,
    reg_dt                      datetime                    null,
    reg_id                      varchar(20)                 null,
    updt_dt                     datetime                    null,
    updt_id                     varchar(20)                 null,
    valid_yn                    char                        null
);

create table tb_supplier_contract_apprv_history
(
    apprv_history_id     bigint auto_increment
        primary key,
    supplier_contract_id bigint                        not null,
    apprv_status_cd      varchar(10)                   not null,
    rejct_reason         varchar(1000)                 null collate utf8mb4_unicode_ci,
    reg_id               varchar(20)                   not null,
    reg_dt               datetime                      not null,
    constraint tb_s_contract_apprv_his_tb_s_contract_supplier_contract_id_fk
        foreign key (supplier_contract_id) references tb_supplier_contract (supplier_contract_id)
);

create table tb_campaign
(
    campaign_id          bigint auto_increment
        primary key,
    reg_dt               datetime                      null,
    reg_id               varchar(20)                   null,
    updt_dt              datetime                      null,
    updt_id              varchar(20)                   null,
    campaign_nm          varchar(200)                  not null collate utf8mb4_unicode_ci,
    customer_contract_id bigint                        null,
    customer_id          varchar(19)                   not null,
    ed_dt                datetime                      not null,
    msg_calling_num      varchar(20)                   null,
    msg_content          varchar(1000)                 null collate utf8mb4_unicode_ci,
    msg_subject          varchar(64)                   null collate utf8mb4_unicode_ci,
    st_dt                datetime                      not null,
    valid_yn             char                          null,
    apprv_status_cd      varchar(20)                   null,
    apprv_req_id         varchar(20)                   null,
    apprv_req_dt         datetime                      null,
    apprver_id           varchar(20)                   null,
    apprv_dt             datetime                      null,
    constraint tb_campaign_tb_contract_contract_id_fk
        foreign key (customer_contract_id) references tb_customer_contract (customer_contract_id),
    constraint tb_campaign_tb_customer_customer_id_fk
        foreign key (customer_id) references tb_customer (customer_id)
);

create table tb_campaign_apprv_history
(
    apprv_history_id bigint auto_increment
        primary key,
    campaign_id      bigint                        not null,
    apprv_status_cd  varchar(20)                   not null,
    rejct_reason     varchar(1000)                 null collate utf8mb4_unicode_ci,
    reg_id           varchar(20)                   not null,
    reg_dt           datetime                      not null,
    constraint tb_campaign_apprv_history_tb_campaign_campaign_id_fk
        foreign key (campaign_id) references tb_campaign (campaign_id)
);

create table tb_goods
(
    goods_id               bigint auto_increment
        primary key,
    goods_nm               varchar(100) collate utf8mb4_unicode_ci  null,
    goods_desc             varchar(4000) collate utf8mb4_unicode_ci null,
    goods_status_cd        varchar(20)                              null,
    goods_type             varchar(20)                              null,
    use_info               varchar(4000) collate utf8mb4_unicode_ci null,
    supplier_goods_id      varchar(20)                              null,
    supplier_contract_id   bigint                                   null,
    supplier_id            varchar(15)                              not null,
    brand_id               varchar(20)                              null,
    goods_img_path         varchar(500) collate utf8mb4_unicode_ci  null,
    goods_img_nm           varchar(100) collate utf8mb4_unicode_ci  null,
    strt_dt                datetime                                 null,
    end_dt                 datetime                                 null,
    sell_price             decimal(12, 2)                           not null,
    list_price             decimal(12, 2)                           not null,
    settlement_method_cd   varchar(20)                              not null,
    supply_commission_rate decimal(8, 2)                            not null,
    supply_dc_amount       decimal(12, 2)                           not null,
    supply_dc_rate         decimal(8, 2)                            null,
    vat_inc_yn             char                                     not null,
    valid_yn               char                                     not null,
    reg_id                 varchar(20)                              not null,
    reg_dt                 datetime                                 not null,
    updt_id                varchar(20)                              null,
    updt_dt                datetime                                 null,
    except_store_ids       varchar(1000)                            null,
    period_type            varchar(20)                              null,
    period_term            decimal(12, 2)                           null,
    period_expire_date     varchar(10)                              null,
    constraint tb_goods_tb_supplier_contract_supplier_contract_id_fk
        foreign key (supplier_contract_id) references tb_supplier_contract (supplier_contract_id)
);

create table tb_campaign_goods_rel
(
    campaign_id            bigint         not null,
    goods_id               bigint         not null,
    reg_dt                 datetime       not null,
    reg_id                 varchar(20)    not null,
    updt_dt                datetime       null,
    updt_id                varchar(20)    null,
    settlement_method_cd   varchar(20)    not null,
    supply_dc_rate         decimal(8, 2)  null,
    supply_dc_amount       decimal(12, 2) not null,
    supply_commission_rate decimal(8, 2)  not null,
    send_cost              decimal(12, 2) null,
    valid_yn               char           not null,
    vat_inc_yn             char           not null,
    primary key (campaign_id, goods_id),
    constraint tb_campaign_goods_rel_tb_campaign_campaign_id_fk
        foreign key (campaign_id) references tb_campaign (campaign_id),
    constraint tb_campaign_goods_rel_tb_goods_goods_id_fk
        foreign key (goods_id) references tb_goods (goods_id)
);

create table tb_category
(
    ctgr_cd  varchar(10)                             not null
        primary key,
    ctgr_nm  varchar(100) collate utf8mb4_unicode_ci null,
    valid_yn varchar(1)                              null,
    reg_dt   datetime                                null,
    reg_id   varchar(20)                             null,
    updt_dt  datetime                                null,
    updt_id  varchar(20)                             null
);

create table tb_category_goods_rel
(
    ctgr_cd  varchar(10) not null,
    goods_id bigint      not null,
    reg_dt   datetime    not null,
    reg_id   varchar(20) not null,
    updt_dt  datetime    null,
    updt_id  varchar(20) null,
    primary key (goods_id, ctgr_cd),
    constraint tb_category_goods_rel_tb_category_ctgr_cd_fk
        foreign key (ctgr_cd) references tb_category (ctgr_cd),
    constraint tb_category_goods_rel_tb_goods_goods_id_fk
        foreign key (goods_id) references tb_goods (goods_id)
);

create table tb_publish
(
    publish_id                bigint auto_increment
        primary key,
    reg_dt                    datetime                      not null,
    updt_dt                   datetime                      null,
    apprv_req_dt              datetime                      null,
    apprv_req_id              varchar(20)                   null,
    apprv_status_cd           varchar(20)                   null,
    apprver_dt                datetime                      null,
    apprver_id                varchar(20)                   null,
    booking_dt                datetime                      null,
    booking_yn                char                          not null,
    campaign_id               bigint                        not null,
    cancel_dt                 datetime                      null,
    customer_id               varchar(19)                   null,
    goods_id                  bigint                        null,
    msg_calling_num           varchar(20)                   null,
    msg_content               varchar(1000)                 not null collate utf8mb4_unicode_ci,
    msg_subject               varchar(64)                   not null collate utf8mb4_unicode_ci,
    publish_dt                datetime                      null,
    publish_nm                varchar(200)                  not null collate utf8mb4_unicode_ci,
    publish_status_cd         varchar(20)                   null,
    receiver_no_dupl_allow_yn char                          not null,
    rejct_reason              varchar(1000)                 null collate utf8mb4_unicode_ci,
    rejcter_id                varchar(20)                   null,
    sell_dc_rate              decimal(8, 2)                 null,
    sell_list_price           decimal(12, 2)                null,
    sell_price                decimal(12, 2)                null,
    sell_vat_inc_yn           char                          null,
    send_cost                 decimal(12, 2)                null,
    sms_type                  varchar(20)                   null,
    supplier_id               varchar(15)                   null,
    test_send_yn              char                          null,
    upload_file_nm            varchar(100)                  null,
    upload_file_path          varchar(500)                  null,
    upload_text               text                          null collate utf8mb4_unicode_ci,
    upload_type               varchar(20)                   not null,
    rejct_dt                  datetime                      null,
    sell_commission_rate      decimal(8, 2)                 null,
    sell_settlement_method_cd varchar(20)                   null,
    sell_dc_amount            decimal(12, 2)                null,
    constraint tb_publish_tb_campaign_campaign_id_fk
        foreign key (campaign_id) references tb_campaign (campaign_id),
    constraint tb_publish_tb_goods_goods_id_fk
        foreign key (goods_id) references tb_goods (goods_id)
);

create index tb_publish_tb_campaign_campaign_id_fk
    on tb_publish (campaign_id);

create table tb_publish_apprv_history
(
    publish_apprv_history_id bigint auto_increment
        primary key,
    publish_id               bigint                        not null,
    apprv_status_cd          varchar(20)                   not null,
    rejct_reason             varchar(1000)                 null collate utf8mb4_unicode_ci,
    reg_id                   varchar(20)                   not null,
    reg_dt                   datetime                      not null
);

create table tb_publish_detail
(
    publish_dtl_id        bigint auto_increment
        primary key,
    reg_dt                datetime                     not null,
    publish_id            bigint                       not null,
    receiver_mobile_no    varchar(150)                 not null,
    publish_dtl_status_cd varchar(20)                  not null,
    publish_rslt_msg      varchar(500)                 null collate utf8mb4_unicode_ci,
    updt_dt               datetime                     null,
    sms_id                varchar(30)                  null,
    sms_type              varchar(20)                  null,
    sms_send_dt           datetime                     null,
    sms_send_rslt_dt      datetime                     null,
    constraint tb_publish_detail_tb_publish_publish_id_fk
        foreign key (publish_id) references tb_publish (publish_id)
);

create table tb_user
(
    user_mobile_num varchar(150)                            not null
        primary key,
    user_nm         varchar(400) collate utf8mb4_unicode_ci null,
    gender          varchar(10)                             null comment 'MEN/WOMEN/KIDS/MISCELL',
    birthday        varchar(8)                              null comment 'ex: 20230401',
    address         varchar(200) collate utf8mb4_unicode_ci null,
    mngr_email      varchar(50)                             null
);

create table tb_voucher
(
    ev                 varchar(40)                  not null
        primary key,
    balance            decimal(12, 2)               null,
    campaign_id        bigint                       not null,
    cancel_dt          datetime                     null,
    content            varchar(1000)                not null collate utf8mb4_unicode_ci,
    creation_dt        datetime                     not null,
    dc_limit_price     decimal(12, 2)               null,
    dc_rate            decimal(8, 2)                null,
    disuse_dt          datetime                     null,
    expiration_dt      datetime                     not null,
    goods_id           bigint                       not null,
    img_url            varchar(500)                 null,
    init_amount        decimal(12, 2)               null,
    last_exchange_dt   datetime                     null,
    publish_dt         datetime                     null,
    publish_dtl_id     bigint                       not null,
    publish_id         bigint                       not null,
    reg_dt             datetime                     not null,
    short_link         varchar(500)                 not null collate utf8mb4_unicode_ci,
    sticker            varchar(100)                 null collate utf8mb4_unicode_ci,
    subject            varchar(200)                 not null collate utf8mb4_unicode_ci,
    test_yn            char                         null,
    transfer_dt        datetime                     null,
    transfer_status_cd varchar(20)                  null,
    useinfo            varchar(4000)                null collate utf8mb4_unicode_ci,
    user_mobile_num    varchar(150)                 not null,
    user_nm            varchar(400)                 null,
    voucher_price      decimal(12, 2)               null,
    voucher_status_cd  varchar(10)                  not null,
    voucher_type_cd    varchar(10)                  not null,
    orig_ev            varchar(40)                  null,
    transfer_msg       varchar(200)                 null collate utf8mb4_unicode_ci,
    constraint tb_voucher_tb_goods_goods_id_fk
        foreign key (goods_id) references tb_goods (goods_id),
    constraint tb_voucher_tb_publish_detail_publish_dtl_id_fk
        foreign key (publish_dtl_id) references tb_publish_detail (publish_dtl_id),
    constraint tb_voucher_tb_publish_publish_id_fk
        foreign key (publish_id) references tb_publish (publish_id)
);

create table tb_exchange_history
(
    transaction_id   bigint         not null
        primary key,
    exchange_type    varchar(20)    not null,
    transaction_dt   datetime       not null,
    store_id         varchar(25)    not null,
    ev               varchar(40)    not null,
    voucher_type_cd  varchar(20)    not null,
    goods_id         bigint         not null,
    goods_nm         varchar(100)   null,
    list_price       decimal(12, 2) null,
    dc_rate          decimal(8, 2)  not null,
    dc_amount        decimal(12, 2) not null,
    exchange_amount  decimal(12, 2) not null,
    user_mobile_num  varchar(150)   not null,
    staff_mobile_num varchar(150)   not null,
    constraint tb_exchange_history_tb_voucher_ev_fk
        foreign key (ev) references tb_voucher (ev)
);

create table tb_settlement_log
(
    log_id                      bigint auto_increment
        primary key,
    ev                          varchar(40)    null,
    settlement_log_type         varchar(20)    null,
    publish_id                  bigint         null,
    publish_dtl_id              bigint         null,
    transaction_id              bigint         null,
    transaction_dt              datetime       null,
    log_create_dt               datetime       null,
    voucher_type_cd             varchar(20)    null,
    goods_id                    bigint         null,
    customer_id                 varchar(19)    null,
    supplier_id                 varchar(15)    null,
    brand_id                    varchar(20)    null,
    store_id                    varchar(25)    null,
    user_mobile_num             varchar(150)   null,
    staff_mobile_num            varchar(150)   null,
    settlement_complete_yn      char           null,
    settlement_complete_dt      datetime       null,
    settlement_target           varchar(20)    null,
    settlement_method_cd        varchar(20)    null,
    list_price                  decimal(12, 2) null,
    sales_price                 decimal(12, 2) null,
    dc_rate                     decimal(8, 2)  null,
    dc_amount                   decimal(12, 2) null,
    dc_applied_amount           decimal(12, 2) null,
    settlement_amount           decimal(12, 2) null,
    vat_inc_yn                  char           null,
    vat_amount                  decimal(12, 2) null,
    commission_rate             decimal(8, 2)  null,
    commission_amount           decimal(12, 2) null,
    send_cost                   decimal(12, 2) null,
    settlement_except_reason_cd varchar(20)    null,
    settlement_except_reason    varchar(500)   null collate utf8mb4_unicode_ci,
    remain_balance              decimal(12, 2) null
);

create table tb_transfer_history
(
    transaction_id          bigint auto_increment
        primary key,
    transfer_status_cd      varchar(20)    not null,
    transaction_dt          datetime       not null,
    receipt_confirm_dt      datetime       null,
    return_dt               datetime       null,
    from_voucher_short_link varchar(500)   not null,
    from_ev                 varchar(40)    not null,
    `from`                  varchar(20)    not null,
    to_voucher_short_link   varchar(500)   not null,
    to_ev                   varchar(40)    not null,
    `to`                    varchar(20)    not null,
    voucher_type_cd         varchar(20)    not null,
    init_amount             decimal(12, 2) null,
    transfer_amount         decimal(12, 2) not null
);
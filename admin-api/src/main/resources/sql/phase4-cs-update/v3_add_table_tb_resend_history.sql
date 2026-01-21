create table tb_resend_history
(
    id                bigint(20)  auto_increment  not null,
    ev                varchar(255)                    null,
    publish_id        bigint(20)                      null,
    publish_dtl_id    bigint(20)                      null,
    prev_sms_id       varchar(255)                    null,
    memo              text collate utf8mb4_unicode_ci null,
    be_prev_publish_dtl_status_cd   varchar(255)      null,
    be_update_result  varchar(255)                    null,
    fe_prev_publish_dtl_status_cd   varchar(255)      null,
    fe_update_result  varchar(255)                    null,
    reg_id            varchar(255)                    null,
    reg_dt            datetime                        null,
    primary key (id)
);
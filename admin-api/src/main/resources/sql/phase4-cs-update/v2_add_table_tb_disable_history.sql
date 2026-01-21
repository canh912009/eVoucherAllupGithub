create table tb_disable_history
(
    id                bigint(20)  auto_increment  not null,
    ev                varchar(255)                    null,
    memo              text collate utf8mb4_unicode_ci null,
    be_prev_status_cd varchar(255)                    null,
    be_update_result  varchar(255)                    null,
    fe_prev_status_cd varchar(255)                    null,
    fe_update_result  varchar(255)                    null,
    reg_id            varchar(255)                    null,
    reg_dt            datetime                        null,
    primary key (id)
);
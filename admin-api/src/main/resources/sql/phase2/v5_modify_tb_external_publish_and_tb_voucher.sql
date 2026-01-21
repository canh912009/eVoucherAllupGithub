alter table tb_external_publish
    modify content_image_path varchar(500) charset utf8mb3 null;

alter table tb_external_publish
    modify content_image_name varchar(500) charset utf8mb3 null;

alter table tb_voucher
    add content_image_path varchar(500) charset utf8mb3 null;

alter table tb_voucher
    add content_image_name varchar(500) charset utf8mb3 null;


alter table tb_ext_pin
    add password varchar(100) null;

alter table tb_voucher
    add ext_pin_password varchar(100) null;


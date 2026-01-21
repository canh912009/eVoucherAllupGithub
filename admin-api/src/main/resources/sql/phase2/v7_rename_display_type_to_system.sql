ALTER TABLE tb_brand
    ADD `system` VARCHAR(20) COMMENT 'INTERNAL/EXTERNAL' NOT NULL DEFAULT 'INTERNAL';

UPDATE tb_brand
SET `system` = 'INTERNAL'
WHERE `system` IS NULL;


ALTER TABLE tb_goods
    RENAME COLUMN display_type TO `system`;

UPDATE tb_goods
SET `system` = 'INTERNAL'
WHERE `system` IS NULL OR `system`= 'NORMAL';

UPDATE tb_goods
SET `system` = 'EXTERNAL'
WHERE `system`= 'BARCODE';

ALTER TABLE tb_voucher
    RENAME COLUMN ext_pin_display_type TO `system`;

UPDATE tb_voucher
SET `system` = 'INTERNAL'
WHERE `system` IS NULL OR system= 'NORMAL';

UPDATE tb_voucher
SET `system` = 'EXTERNAL'
WHERE `system`= 'BARCODE';

INSERT INTO e_voucher.tb_code_group (cd_grp_id, reg_dt, reg_id, updt_dt, updt_id, cd_grp_nm, valid_yn)
VALUES ('SYSTEM', '2023-09-04 17:43:21', '1', '2023-09-04 17:43:21', '1', 'System type', 'Y');

INSERT INTO e_voucher.tb_code (cd_id, reg_dt, reg_id, updt_dt, updt_id, cd_grp_id, cd_nm, sort_order, valid_yn)
VALUES ('INTERNAL', '2023-07-04 17:45:32', '1', '2023-07-04 17:45:32', '1', 'SYSTEM', 'Internal', 2, 'Y');

INSERT INTO e_voucher.tb_code (cd_id, reg_dt, reg_id, updt_dt, updt_id, cd_grp_id, cd_nm, sort_order, valid_yn)
VALUES ('EXTERNAL', '2023-07-04 17:45:32', '1', '2023-07-04 17:45:32', '1', 'SYSTEM', 'External', 1, 'Y');
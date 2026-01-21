-- insert new admin
INSERT INTO tb_admin (admin_id, reg_dt, reg_id, updt_dt, updt_id, admin_corp_id, admin_nm, role_cd,
                      email, last_login_dt, login_fail_cnt, mobile_no, passwd, passwd_init_yn,
                      passwd_updt_dt, tel, valid_yn)
VALUES ('1', '2024-08-15 10:48:41', '1', null, null, null, 'admin', 'ROLE_ADMIN', null, null, 0, '0988000001',
        '$2a$11$3Nk9CCe020qAAxxxjdMAF.4HOpp8i/Bh0WeE5na0HuqicJXHJkR8m', 'Y', null, null, 'Y');

-- insert new internal voucher
INSERT INTO tb_voucher (ev, balance, campaign_id, cancel_dt, content, creation_dt, dc_limit_price,
                        dc_rate, disuse_dt, expiration_dt, goods_id, img_url, init_amount,
                        last_exchange_dt, publish_dt, publish_dtl_id, publish_id, reg_dt, short_link,
                        sticker, subject, test_yn, transfer_dt, transfer_status_cd, useinfo,
                        user_mobile_num, user_nm, voucher_price, voucher_status_cd, voucher_type_cd,
                        orig_ev, transfer_msg, ext_pin_id, ext_pin_no, ext_pin_type, `system`,
                        activation_url, serial_no, activation_dt,
                        activation_id, parent_voucher_ev, parent_voucher_token)
VALUES ('16758daf-4516-477a-bd2b-233da3fdb5be', 50000.00, 1, null, 'test', now(), 50000.00, null, null,
        DATEADD('DAY', 1, CURRENT_TIMESTAMP), 1, null,
        50000.00, null, now(), 1, 1, now(), 'http://192.168.0.125:80/ytfwy', null, 'test', null, null, null, null, 'gQS9Pe5iRePxjieaxsmauw==\r\n', 'barfAZaxjvzw+Qg4jI20MQ==', 50000.00, 'NORMAL', 'SI', null, null, null, null, 'BARCODE', 'INTERNAL',
          null, null, null, null, null, null);

-- new requested operator request
insert into tb_operator_request (req_id, ev, publish_id, goods_id, req_status, requester, req_dt, memo, appr_dt,
                                 appr_memo, approver)
values (1, '16758daf-4516-477a-bd2b-233da3fdb5be', 1, 1, 'REQUESTED', '1', now(), 'test-memo', null, null,
        null);
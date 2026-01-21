# AQUA ADMIN API
v0.0.8.QR2 / 2023-12-12
========================
**Config changes**
- no charge

**DB changes**
- All sql in file src/main/resources/sql/phase2/v7_rename_display_type_to_system.sql
1. tb_brand: add field system
2. tb_goods
   - Rename field display_type to system
   - Update data for field system
3. tb_voucher
   - Rename field ext_pin_display_type to system
   - Update data for field system
4. tb_code_group
   - add data SYSTEM
5. tb_code
   - Add data INTERNAL, EXTERNAL

v0.2.0 / 2024-02-20 Gift pop implementation
========================
**Config changes**
- gift-pop.authentication-key
- gift-pop.url

**DB changes in file**
- src/main/resources/sql/phase3-giftpop/v1_add_column_brand_cd_in_tb_brand_table.sql
- src/main/resources/sql/phase3-giftpop/v2_insert_data.sql
1. tb_brand: add field brand_code
2. tb_code_group
   - add data for giftpop
3. tb_code
   - Add data for giftpop

v0.2.1 / 2024-02-28
========================
**Config changes**
- servers.evoucherServiceBe

**DB changes in file**
- src/main/resources/sql/phase4-cs-update/v1_add_column_sender_name_in_tb_publish_table.sql
- src/main/resources/sql/phase4-cs-update/v2_add_table_tb_disable_history.sql
- src/main/resources/sql/phase4-cs-update/v3_add_table_tb_resend_history.sql

v1.2.2 / 2024-04-23
========================

**Config Changes**
ur-box:
  app-secret: f8876e0e88b69b1aa1b4411c71931adf
  app-id: 500000282
  url: https://sandapi.urbox.dev
  path:
    get-brands: /4.0/gift/brand
    get-goods: /4.0/gift/lists
    get-good: /4.0/gift/detail


v1.3.2.QR1 / 2024-05-31
========================

1. fix: validate valid good & valid brand when publish
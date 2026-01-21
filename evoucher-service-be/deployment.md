# AQUA ADMIN API
v0.0.3.QR2 / 2023-12-12
========================
**Config changes**
   - no charge
**DB changes**
   - no charge

v0.0.4 / 2024-02-06
========================
**Config changes**
- no charge
  **DB changes**
- no charge

v0.0.5 / 2024-02-28
========================
**Config changes**
- queues.disable_voucher.queue: DISABLE_VOUCHER
- queues.disable_voucher.routingKey: disable_voucher
- queues.disable_voucher_result.queue: DISABLE_VOUCHER_RESULT
- queues.disable_voucher_result.routingKey: disable_voucher_result

**DB changes in file**
- no charge

v1.1.7 + 1.1.8 / 2024-06-07
========================
**Config**

1. Changes
```
redis:
  host: 192.168.0.125
  port: 6379
  isStandAlone: true
  key:
    buy-choice:
      prefix: CHOOSE_CHOICE_
```

v1.2.0 / 2024-06-24
========================
**DB changes**
  ```sql
    rename table tb_voucher_choices_history to tb_parent_voucher_history;
    alter table tb_parent_voucher_history
        add `system` varchar(20) not null;
    UPDATE tb_parent_voucher_history t SET t.`system` = 'CHOICE' WHERE 0=0;
  ```

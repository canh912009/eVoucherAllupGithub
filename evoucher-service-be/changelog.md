# AQUA E-VOUCHER SERVICE BE

v0.0.2 / 2023-10-30
========================
**Feature**
- implement vnpt epay integrated


v0.0.2.QR1 / 2023-11-07
========================
**Feature**
- decrypt vnpt pin of purchase result

v0.0.2.QR2 / 2023-11-07
========================
**Feature**
- return vnpt error status to admin api

v0.0.4 / 2024-02-06
========================
**Feature**
- hot fix remove set choice information to new voucher when transfer voucher

v0.0.5 / 2024-02-28
========================
**Feature**
1. Update CS management
2. Add api disable voucher and resend voucher

v1.1.2 / 2024-05-08
========================
**Feature**
1. add controller for updating used voucher status
2. QR1: fix bug can not publish urbox publish

v1.1.4 / 2024-05-14
========================
**Feature**
1. feat: add buy urbox pin for choice voucher

v1.1.5 / 2024-05-22
========================
**Feature**
1. feat: sync voucher status with ur box

v1.1.6 / 2024-05-30
========================
**Feature**
1. feat: create settlement log when purchase choice voucher [jira issue](https://vsourcing.atlassian.net/browse/EV-34?atlOrigin=eyJpIjoiMzM2NDkxYzZlOTdkNDU0MDg4NjI0NTVkMjk3ZDI3ZDciLCJwIjoiaiJ9)

v1.1.7 / 2024-06-06
========================
**Feature**
1. fix: use redis as distributed lock

v1.1.8 / 2024-06-07
========================
**Feature**
1. fix: lock multiple choice voucher purchasing processing

v1.1.8.QR5 / 2024-06-10
========================
**Feature**
1. fix: wrong external pin id type
2. [QR2]: fix pin status change to reserved after fail generating choice voucher due to missing pins
3. [QR3] [EV-44#2](https://vsourcing.atlassian.net/browse/EV-44?atlOrigin=eyJpIjoiY2E2MWYwYzI0ZjkyNDhkZTk4OWFmM2U1ZjY4MWI1NmEiLCJwIjoiaiJ9): fix call ur box api for getting giftpop pin
4. [QR4] [EV-44#1](https://vsourcing.atlassian.net/browse/EV-44?atlOrigin=eyJpIjoiY2E2MWYwYzI0ZjkyNDhkZTk4OWFmM2U1ZjY4MWI1NmEiLCJwIjoiaiJ9): fix Parameter value element did not match expected type
5. [QR5]: fix show giftpop error message when purchase urbox voucher

v1.1.9 / 2024-06-10
========================
**Feature**
1. feat: unify response format to
    ```json
    {
        "code": "int",
        "message": "string",
        "data": "string"
    }
    ```
2. feat: change getting voucher quantity path from /quality to /quantity
3. add more error code:
   ```
      GOOD_NOT_FOUND = 1092;
      PUBLISH_NOT_FOUND = 5001;
      CAN_NOT_FIND_USER = 1089;
      CUS_CONTRACT_NOT_FOUND = 1094;
      SUPP_CONTRACT_NOT_FOUND = 1094;
   ```

v1.2.0 / 2024-06-24
========================
**Feature**
1. change table `tb_voucher_choices_history` name to `tb_parent_voucher_history`
2. add more `system` to `tb_parent_voucher_history`
3. feat: alter table tb_voucher: `CHOICE_VOUCHER_EV` changed to `parent_voucher_ev`
4. feat: alter table tb_voucher: `CHOICE_TOKEN` changed to `parent_voucher_token`
5. feat: update create choice voucher request payload field name

v1.2.1 / 2024-06-26
========================
**Feature**
1. feat: update voucher last exchange date of choice type when purchase child voucher
2. [QR2] fix: add otp when creating bulk voucher, and change to secure random for safer
3. [QR3] fix: [Jira](https://vsourcing.atlassian.net/browse/EV-60?atlOrigin=eyJpIjoiZjhjOWIzZjZhMjM4NDgyMjkxYzU3MzE0ZTVmNWZjNmMiLCJwIjoiaiJ9) Prevent Choice/Bulk voucher purchases the EXPIRED good
4. [QR4] fix: add more error code when call publish service to create external pin

v1.2.2 / 2024-07-19
========================
**Feature**
1. feat: using vnpt voucher api
2. [QR1] ref: remove unused old vnpt calling client
3. [QR1] fix:  can not parse partner service response
4. [QR2] fix: user mobile is null while topup
5. [QR3] feat: update vnpt exchange type card_code to cardcode
6. [QR4] fix: bug Sai chu ky
7. [QR5] fix: can not topup
8. [QR6] fix: use voucher user number for voucher exchange history

v1.2.3 / 2024-07-22
========================
**Feature**
1. fix: multiple username for one DOWNLOAD campaign
2. [QR1] ev-57 feat: update customer table
3. [QR2] ev-57 feat: update search customer result field
4. [QR3] fix: error message isn't clear when purchasing vnpt voucher return error

v1.2.4 / 2024-08-05
========================
**Feature**
1. feat: integrate with pos-api (update exchange voucher api)
2. [QR1] fix: can't topup
3. [QR2] fix: voucher's target name of choice is wrong after create several voucher of different customer
4. [QR3] ref: use java.util.Date instead of LocalDateTime and LocalDate

v1.2.5 / 2024-08-28
========================
**Feature**
[EV-72](https://vsourcing.atlassian.net/browse/EV-76?atlOrigin=eyJpIjoiZGZhNWZhYmIwZTNjNDM4ODk0ZmMyYThjMjI4OTI2YjQiLCJwIjoiaiJ9)

1. feat: add more good type limited count LC

v1.2.6 / 2024-08-29
========================
**Feature**

1. feat: use api instead of queue for receiving, activating voucher

v1.2.7 / 2024-09-19
========================
**Feature**
EV-99

1. fix: transferred voucher publish date resemble with original voucher
2. fix: missing transfer history receipt_confirm_dt, return dt


v1.2.8 / 2024-10-01
========================
**Feature**
EV-105

1. fix: missing customer name for exchange settlement log of supplier
2. fix: wrong brand name, supplier name of exchange settlement

v1.2.10 / 2024-11-05
========================
**Feature**
- Support mail typed voucher
- Transfer with new flow
- Handle receiving voucher new flow
 
v1.3.0 / 2024-11-20
========================
**Feature**
- Support Watane product
---
Remove activation link for PAPER typed voucher
---
Choice remove token

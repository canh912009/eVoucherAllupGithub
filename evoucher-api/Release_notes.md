# E-voucher API change log document

v0.0.2 2023-08-16
==================

1. add more checking transfer-mobile number is same with old one
2. fix bug: wrong result when search storeModel

v0.0.3 2023-08-17
=================

1. add processing find storeModel by id

v0.0.4 2023-08-18
=================

1. exchange voucher with transaction

v0.0.5 2023-08-23
=================

1. add more is pos link field

v0.0.5.QR1 2023-08-29
=================

1. fix bug error when exchange voucher

v0.0.6 2023-09-25
=================

1. integrate external pin

v0.0.6.QR1 2023-09-27
=================

1. fix bug: not limit result when searching storeModel

v0.0.6.QR2 2023-10-06
=================

1. fix bug: missing voucher status when exchange voucher

v0.0.7 2023-10-09
=================

1. update ui

v0.0.7.QR1 - 2023-10-01
=================

1. missing brand url when exchange voucher, error when transfer voucher

v0.0.7.QR3 - 2023-10-12
=================

1. fix bug: not change voucher status when send payment history to rabbit mq
2. missing balance when send to queue

v0.0.8 2023-11-10
==================

1. add more voucher's fields: contentLink, contentImagePath, contentImageName

v0.0.9/ 2023-11-10
==================

1. add more voucher's fields: externalPinPassword

v0.0.10/ 2023-11-28
==================

1. change good, voucher field displayType to system

v0.1.0/ 2023-12-04
==================

1. merge with e voucher viewer (add more api: get voucher by short link)

v0.1.1/ 2023-12-05
==================

1. choice voucher
2. remove evoucher - viewer component


v0.1.2/ 2023-12-13
==================

1. choose choice item
2. api get choice voucher children

v0.1.3/ 2023-12-14
==================

1. get remaining ext pin amount


v0.1.4/ 2024-02-02
==================
1. Encrypt user mobile number

v0.1.5/ 2024-02-04
==================
1. stop transferring choice voucher
2. stop disabled voucher purchase choice voucher

v0.1.6/ 2024-02-04
==================
1. encrypt user number before send to queue

v0.1.4/ 2024-02-05
==================

1. implementation send voucher otp

v0.1.5/ 2024-02-28
==================

1. Update voucher list: add sort voucher by createDate

v1.0.0/ 2024-04-12
==================

1. Support download-typed voucher

v1.0.1 / 2024-05-14
==================

1. feat: add extPinType to voucher for showing urbox qr/barcode
2. QR1: fix: exception when search storeModel not exist

v1.2.0 / 2024-06-16
==================

1. Migrate from Elasticsearch to Mysql

v1.2.1 / 2024-06-17
==================

1. Fix bug error code issue when get OTP

v1.3.0 / 2024-06-24
==================

1. Support bulk voucher

v1.4.0 / 2024-07-18
==================

1. Support VNPT typed vouchers
2. Change the way store list is returned
3. Fix issue display stores

v1.4.4 / 2024-08-02
==================

1. OTP voucher list change from 3 -> 30 days
2. Add OTP expireDate in OTP response
3. Improve Bulk voucher response

v1.4.5 / 2024-08-02
==================

1. Order choice vouchers
2. [QR1] fix: can not re-generate otp after 3 minutes
3. [QR2] fix: missing sender of voucher detail
4. [QR3] fix: missing expire date while get otp

v1.4.6 / 2024-08-12
==================

1. Fix store list doesn't show for URBOX

v1.4.7 / 2024-08-14
==================

1. Change all date to what received from db

v1.4.8 / 2024-08-20
==================

1. Change Localdate to Date

v1.4.10 / 2024-08-28
==================

1. Limited count voucher support
2. Change exchange request to synchronous
3. Fix bug datetime with store
4. Fix issue missing LC voucher type

v1.4.11 / 2024-09-09
==================

1. change queue message to api call

v1.4.12 / 2024-09-17
==================

1. Fix bug missing store verification

v1.5.0 / 2024-09-24
==================

1. Add `/preCheck` API
2. Add voucher detail API v2
3. Add choice/bulk api v2
4. Add get voucher list v2
5. Add create OTP v2
6. Add VNPT epay purchase for v2
7. Precheck add otpExists & otpExprireDt
8. Excluded store can not purchase

v1.6.0 / 2024-11-20
==================
- Support watane product
---
Update check for voucher version

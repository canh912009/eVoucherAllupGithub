# AQUA ADMIN API

v1.1.0 / 2024-04-24
========================
**Feature**
- AquaVN-22: support activation flow

v1.0.0 / 2024-04-09
========================
**Feature**
- Support download-typed voucher


v0.0.2 / 2023-09-15
========================
**Feature**
- implement getting all message templates
- add message template to campaign (get, create, update)
- add error string when message template id is null, and when can not find message template by id


v0.0.3 / 2023-09-23
========================
**Feature**
- add getting charts data APIS

v0.0.3.QR1 / 2023-09-25
========================
**Feature**
- fix bug error when get goods chart data

v0.0.4 / 2023-09-26
========================
**Feature**
- automatic get id param from token example, get supplier Id from token of logged in supplier user

v0.0.5 / 2023-10-25
========================
**Feature**
- cs management implement

v0.0.5.QR1 / 2023-10-25
========================
**Feature**
- change cs management request field name from startFrom, endAt to startDate, endDate
- change cs management request field format from yyyy/mm/dd to yyyy-mm-dd
- get from voucher table when searching cs management instead of settlement log table

v0.0.6 / 2023-10-29
========================
**Feature**
- get pin detail data api

v0.0.6.QR1 / 2023-10-31
========================
**Feature**
- fix bug can not find management by target name

v0.0.6.QR2 / 2023-10-31
========================
**Feature**
- fix bug null campaign info and good info when search cs which haven't settlement log

v0.0.6.QR3 / 2023-11-1
========================
**Feature**
- remove id in cs search response object
- remove settlement log in search query

v0.0.7 / 2023-11-02
========================
**Feature**
- vnpt ePay integrate
1. search vnpt brand api
2. search vnpt gift api
3. get balance api
4. purchase api
5. get all brand
6. get all good

v0.0.7.QR1 / 2023-11-04
========================
**Feature**
1. fix bug, return empty data when search purchase gift

v0.0.7.QR2 / 2023-11-04
========================
**Feature**
1. change purchase search date format

v0.0.8 / 2023-11-06
========================
**Feature**
1. add more giftTitle param to search gift api

v0.0.8.QR1 / 2023-11-06
========================
**Feature**
1. fix bug when get gift list in purchase screen

v0.0.8.QR2 / 2023-11-16
========================
**Feature**
1. cs management: no result when search start date, end date with same date

v0.0.9 / 2023-12-12
========================
**Feature**
1. Rename display type to system

v0.1.0 / 2023-12-12
========================
**Feature**
1. Add flow for Choice voucher

v0.1.2 / 2023-12-19
========================
**Feature**
1. Add content link, content image when create campaign and publish

v0.2.0 / 2024-02-20
========================
**Feature**
1. Add gifpop flow

v0.2.1 / 2024-02-28
========================
**Feature**
1. Update CS management
2. Add api disable voucher and resend voucher

v1.2.2 / 2024-04-23
========================
**Feature**
1. UrBox retrieve brands and goods
2. QR1: error when create ur box brand
3. QR2: error when get ur box goods

v1.2.3 / 2024-05-13
========================
**Feature**
1. add display type to external pin

v1.3.1 / 2024-05-13
========================
**Feature**
1. feat: create stores when create new brand
2. feat: get and set excepted store for ur box brand when creating new
3. QR1: fix: create good with vietnamese good name
4. QR2: fix: save urbox display type as string in db
5. QR3: refactor: change type of voucher external_pin_type field to PinDisplayType

v1.3.2 / 2024-05-15
========================
**Feature**
1. fix: can not create new giftpop good

v1.3.3 / 2024-06-04
========================
**Feature**
1. feat: create store when regis gift pop brand

v1.4.0 / 2024-06-19
========================
**Feature**
1. feat: category CRUD, search by category code/name like
2. [QR3] fix: missing condition when search category
3. [QR4] fix: set validYn = N for bulk when it was deleted from goods

v1.4.1 / 2024-06-27
========================
**Feature**
1. ref: update response field name:
   `choiceToken` -> `parentVoucherToken`
   `choiceVoucherEv` -> `parentVoucherEv`
   `choiceParentEv` -> `parentVoucherEv`

v1.4.3 / 2024-07-03
========================
**Feature**
1. feat: add more `parentSystem` when get pin detail data (cs management / detail)
2. [QR1] feat: allow to create choice & bulk item in same voucher
3. [QR1] feat: allow to search message template by system in list system
4. [QR2] fix: wrong sort order of bulk good
5. [QR3] feat: add more cs search filter : `productName`, `productId`, `voucherExpireBefore`
6. [QR4] fix: 500 error when search brand by brand Name
7. [QR5] fix: duplicate brand when search by valid with different page
8. [QR6] fix: 500 error when search good by brandId, categoryCode, validYn
9. [QR7] fix: search by brand id like instead start with
10. [QR8] feat: add more supplier name to search brand response

v1.4.4 / 2024-07-03
========================
**Feature**

1. [EV-30](https://vsourcing.atlassian.net/browse/EV-50?atlOrigin=eyJpIjoiMDMyODc0NjBlOWU2NGUwMzhiYWU0YjY0NmE5NzI4MjIiLCJwIjoiaiJ9) feat:
   - Cs Management: [+] `productId`, [+] `expireDate`
   - Raw Settlement: [-] `transactionId`, [+] `pin`
2. [QR1] fix: error when create normal campaign with normal message template
3. [QR2] fix: don't validate available pin before publish with good not is external
4. [QR3] fix: don't save store updated information
5. [QR4] feat: more detail urbox error response


v1.4.6 / 2024-07-05
========================
**Feature**

1. feat: allow to search brand by systems
2. feat: update message when create new category with exist code
3. [QR1] fix: [Jira](https://vsourcing.atlassian.net/browse/EV-60?atlOrigin=eyJpIjoiZjhjOWIzZjZhMjM4NDgyMjkxYzU3MzE0ZTVmNWZjNmMiLCJwIjoiaiJ9) Prevent Choice/Bulk voucher purchases the EXPIRED good

v1.5.0/ 2024-07-11
========================
**Feature**
[vnpt integration](https://vsourcing.atlassian.net/browse/EV-55?atlOrigin=eyJpIjoiMTMyZTMzZWY5MTkzNDJkZjhkZDhjYWIzZDg2ZDBmODQiLCJwIjoiaiJ9)

1. feat: search vnpt provider api
2. feat: update vnpt provider api
3. [QR1] fix: can't create vnpt brand
4. [QR3] feat: create vnpt good
5. [QR4] feat: get vnpt balance api
6. [QR5] feat: sort code list by sortOrder when get code group
7. [QR6] feat: update deleted vnpt good valid to N
8. [QR7] feat: add more vnpt provider to vnpt good 
9. [QR8] fix: can not update bulk good
10. [QR9] fix: vnpt epay missing period type when creat and update

v1.5.1/ 2024-07-19
========================
**Feature**
[vnpt integration](https://vsourcing.atlassian.net/browse/EV-62?atlOrigin=eyJpIjoiZGE5MjBmNjcxYjM5NGYwMTlhODAzMWZlNWM1NzE0MTgiLCJwIjoiaiJ9)

1. feat: add more store query type to good
2. [QR1] feat: set default value for include, exclude store good type. keep old include store ids for include type
3. [QR2] feat: update customer table
4. [QR3] fix: [EV-68] fail publishing while publish edited publish
5. [QR4] fix: search brand return null while search invalid brand by brand id
6. [QR5] feat: add more customer manager name after store name in raw settlement page

v1.5.2/ 2024-07-24
========================
**Feature**
[sync stores](https://vsourcing.atlassian.net/browse/EV-62?atlOrigin=eyJpIjoiNjk5OTMwNTNkYWIwNDI0Yjg5ZTNjZTJkNGUzMTQ2MWMiLCJwIjoiaiJ9)

1. feat: sync store for gift pop, urbox
2. [QR1] fix: error when search bulk good
3. [QR2] feat: add admin search filter role codes to search admin with multiple roles
4. [QR3] fix: validate available pins before approve
5. [QR4] fix: store synchronization not work

v1.5.3/ 2024-07-25
========================
**Feature**
[AQUAVN-28](https://vsourcing.atlassian.net/browse/EV-29?atlOrigin=eyJpIjoiMmEwM2U2Y2I0NjY1NGI0NThmNzExMzI1NjYwZTkyMWIiLCJwIjoiaiJ9)
1. feat: change choice voucher order
2. [QR1] feat: EV67-AQUAVN64 add more `senderName`, `showPopupYn` to `campaign`, `showPopupYn` to `publish`
3. [QR2] feat: add more system field to cs management detail
4. [QR3] feat: search operator request api
5. [QR4] feat: add operator request count, underProcess to cs management detail response
6. [QR5] fix: operator search response return unused fields
7. [QR6] fix: add get all operator request by ev

v1.5.4/ 2024-08-05
========================
**Feature**
1. feat: generate brand auth code and encrypt key when create new brand
2. [QR1] fix: can not search bulk brand
3. [QR2] fix: can not topup after update vnpt provider
4. [QR3] fix: error when sync ur-box store
5. [QR4] ref: get only good basic info for sync store
6. [QR5] fix: change store query type of gift pop good to exclude store id
7. [QR6] fix: operator request list return only requested request, cs management by ev request count approved requests
8. [QR7] fix: operator request list filter add more field request type
9. [QR8] feat: set approver, approved date manually
10. [QR9] fix: only return 1 result when search operator request\
11. [QR10] feat: add more voucher expire date to operator request detail response
12. [QR11] feat: validate voucher expire date before create extend request
13. [QR12] fix: extend voucher expire date both approve and reject cases
14. [QR13] fix: allow to extend expire date for expired voucher
15. [QR14] fix: operator request search function return null when search by target name, target number, request type
16. [QR15] ref: update LocalDate to Date

v1.5.5/ 2024-08-26
========================
**Feature**
[AQUAVN-72](https://vsourcing.atlassian.net/browse/EV-76?atlOrigin=eyJpIjoiOTEzZjE1MjVlZDlkNDFhNjk3YjNjMzQyMGVkMGFmODEiLCJwIjoiaiJ9)
1. feat: add voucher type limited count (LC).
2. [QR1] fix: can not find good by category & brand after add LC type

v1.5.6/ 2024-08-29
========================
**Feature**
1. feat: remove calling to fe for synchronizing store



v1.5.8/ 2024-09-16
========================
**Feature**
[EV-82](https://vsourcing.atlassian.net/browse/EV-82?atlOrigin=eyJpIjoiNmQxNmVmZDQ2OGZiNGI1NWFkNGY1ZTE5OWYyMjA4MzAiLCJwIjoiaiJ9)
1. feat: add transaction id in cs list
2. feat: add more field into cs search list: status, requestId,otp, remainingCount, remainingBalance, password , provider, faceValue, cardSerial, cardPin, topupNumber, 
3. [QR1] feat: add more fields added to cs search into settlement all data
4. [QR2] feat: allow supplier to access settlement page
5. [QR3] feat: allow supplier to search publish, customer, campaign. limit result only show row which belong to logged in supplier"
6. [QR4] feat: filter cs search result for supplier role---


v1.6.0/ 2024-10-15
========================
**Feature**

1. Allow SUPPLIER role to query campaigns and publishes
2. Change from publish status -> publish detail status in cs management detail
3. Fix sorting and change remainBalance -> remainAmount, add initAmount
4. Fix sorting for pin, mobile number, manager name
5. Fix sorting for remain amount and init amount


v1.7.0/ 2024-11-05
========================
**Feature**
- Publish detail user name decrypt
- Generate user when create publish for PAPER type
- Voucher detail cs add missing field
- Support mail type

**Bug**
- Fix can not find brand due to special characters
- Fix except store Id
- Fix filter by target phone number
- Fix issue can not create download type

v1.8.0/ 2024-11-20
---
- support watane products
---
Customer add new fields to support CHANNEL type
---
Remove constraints for tax code
---
Add check for brand by appId, appId is unique
---
Fix for pos api
---
Fix supplier taxcode
---
Fix supplier taxcode
---
Fix for supplier and customer id seperate from taxcode
---
Fix issue create brandId
---
Fix issue cancel publish issue
---
Fix message template apply for all types
---
v1.8.1/ 2025-01-07
---
- AQUAVN-94: Request to create Barcode 39
- https://altimedia.atlassian.net/browse/VETEV-35(Revert for Download type to previous flow)
- https://altimedia.atlassian.net/browse/VETEV-36 : Display Supplier Logo for each Brand
- testbed DB : 192.168.0.125 to 192.168.105.149
---
---
v1.9.0/ 2025-04-15
---
- https://altimedia.atlassian.net/browse/VETEV-59: Integration with XPAY 
- https://altimedia.atlassian.net/browse/VETEV-61 / 54 / 112 : Stock Management (External system)
- edit to fix always reload page CMS client
---

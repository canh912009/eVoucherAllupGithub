
v0.0.0 / 2023-05-01
========================
**Feature**
- make base project

v0.0.1 / 2023-05-31 (dao-nt)
========================
**Feature**
- create publish voucher api
- process create message result
- process send message result

v0.0.1.QR1 / 2023-06-12
=======================
**Feature**

- update max log file size to 100MB

v0.0.1.QR2 / 2023-06-13
=======================
**Feature**

- validate publish status before publish campaign

v0.0.1.QR3 / 2023-06-28
=======================
**Feature**

- add more field original voucher id into publish voucher queue input
- add more image path and image name into publish voucher queue input

v0.0.1.QR4 / 2023-06-28
=======================
**Feature**

- log queue message as pretty json

v0.0.1.QR5 / 2023-07-11
=======================
**Feature**

- update publish entity

v0.0.2 / 2023-07-28
=======================
**Feature**

- update some publish, good fields

v0.0.3 / 2023-07-31
=======================
**Feature**

- rollback voucher when get error

v0.0.4 / 2023-08-03
=======================
**Feature**

1. update publish detail field name
2. update customer id type to string

v0.0.5 / 2023-08-07
=======================
**Feature**

1. encrypt and decrypt when working with publish detail

v0.0.6 / 2023-08-23
=======================
**Feature**

1. add more isPosLinkField into Brand Model and Brand Entity

v0.0.7 / 2023-09-18
=======================
**Feature**

1. add message template selector
> note: 
> e-voucher service fe: at least version 0.6.8
> admin api: at least version:  0.0.2

v0.0.8 / 2023-09-18
=======================
**Feature**

1. update publish date when publish campaign

v0.0.9 / 2023-09-26
=======================
**Feature**

1. integrate external pin

v0.0.9.QR1 / 2023-10-06
=======================
**Feature**

1. change external pin integration rule: only add pin when has at least one was integrated with goods


v0.0.10 / 2023-10-09
=======================
**Feature**

1. new ui

v0.0.10.QR1 / 2023-10-13
=======================
**Feature**

1. missing brand image url
2. missing isPosLink
3. skip when update message fail

v0.0.11/ 2023-11-04
=======================
**Feature**

1. add more expire date condition when combine external pin with publish detail
2. add more contentLink into voucher object of publish queue

v0.0.11.QR1/ 2023-11-04
=======================
**Feature**

1. get external pin had expire date after publish date

v0.0.11.QR2/ 2023-11-09
=======================
**Feature**

1. fix bug wrong when parse future date

v0.0.12/ 2023-11-09
=======================
**Feature**

1. add more 2 fields into voucher queue request: contentImagePath & contentImageName

v0.0.13/ 2023-11-10
=======================
**Feature**

1. add api to cancel publish: change reserved pin status to available

v0.0.13.QR2/ 2023-11-10
=======================
**Feature**

1. cancel publishing return bad request
2. can not find any detail when cancel publish

v0.0.14/ 2023-11-14
=======================
**Feature**

1. add more field externalPinPassword to voucher request for cgv external pin type

v0.0.15/ 2023-11-26
=======================
**Feature**

1. change good display type to system, display type value to internal and external

v0.0.16/ 2023-12-05
=======================
**Feature**

1. create publish detail for choice campaign

v0.0.17 / 2023-12-13
=======================
**Feature**

1. choose choice item

v0.0.18 / 2024-02-04
=======================
**Feature**

1. hot fix: do not update new user mobile no of new publish detail when transfer voucher

v0.0.18.QR1 / 2024-02-04
=======================
**Config**

- Add more 2:
  system:
    decrypt:
        vector: TotalRandoVector
        key: decrypt key
- 
**Feature**

1. hot fix: can not transfer voucher

v0.0.19 / 2024-02-20
========================
**Feature**
1. Add gifpop flow

v0.0.20 / 2024-02-28
========================
**Feature**
1. Update CS management
2. Add api disable voucher and resend voucher

v1.0.0 / 2024-04-09
========================
**Feature**
1. Support download-typed voucher

v1.0.1 / 2024-05-04
========================
**Config**
1. Add:
   - ur-box:
     key-store:
       path: /Users/daont/tools/java_jdk/jdk-11.0.23.jdk/Contents/Home/bin/urboxStore.p12
       password: castis
       alias: urboxsignkey
       key:
         password: castis
     app-secret: f8876e0e88b69b1aa1b4411c71931adf
     app-id: 500000282
     campaign_code: UG723322
     transaction_info:
       phone: 0999999999
       email: abc@gmail.com
       full_name: AQUA E VOUCHER
       site_user_id: AQUA_E_VOUCHER
       url: https://sandapi.urbox.dev
     path:
       buy-gift: /2.0/cart/cartPayVoucher

**File**
1. /docs/keys/urbox/pubkey.pem: public key for urbox verify
2. /docs/keys/urbox/urboxStore.p12: private key to generate urbox signature

**Feature**
1. integrate with urbox
2. QR1: save ur box serial as voucher password

v1.0.2 / 2024-05-08
========================
**Config**
**Features**
1. store dto: remove province; district; ward; replace by region

v1.0.3 / 2024-05-08
========================
**Config**
**Features**
1. save gift pop pin transaction id

v1.0.4 / 2024-05-08
========================
**Config**
**Features**
1. wrong pin expire date

v1.0.6 / 2024-05-08
========================
**Config**
**Features**
1. save ur box transaction id to external pin
2. QR1: update: update third party log

v1.0.7 / 2024-05-14
========================
**Config**
**Features**
1. save ur box response when buy urbox voucher
2. QR1: add urbox display type, and urbox voucher display type
3. QR2: refactor: change external pin type field name of voucher request to extPinType
4. QR3: fix: don't buy new external pin when remaining is enough

v1.0.8 / 2024-05-14
========================
**Config**
**Features**
1. Add ExtPinService tests and perform batch ordering for UrBox

v1.0.9 / 2024-05-16
========================
**Config**
**Features**
1. Fix missing smsType
2. [QR1] refactor: change mapping urbox good display for text type
3. [QR2] feat: log when get error while create new urbox voucher

v1.0.10 / 2024-05-20
=====================
**Config**
1. Changes:
   - gift-pop:
       authentication-key: QVFVQTpRbXQyVERkMmRIbHZWMloyYkVF
       decrypt-pin-key: RBV5NWKTTRNTOO2L

**Features**
1. up update gift pop authKey, decrypt key
2. validate expire date before create choice voucher
3. QR2: fix: error when get all good by parent id
4. [QR3] fix: no bean name CHOICE: available


v1.0.11.QR1 / 2024-05-29
==================
**Config**
1. fix: fix bug duplicate pins for 2 voucher. [jira_issue](https://vsourcing.atlassian.net/browse/EV-13?atlOrigin=eyJpIjoiMDUxZmU3ZmMyYzJkNDMwNjk1ZjM0MThmYzJhODA5MzciLCJwIjoiaiJ9)
2. fix: can buy inactive good | good of inactive brand [EV-35](https://vsourcing.atlassian.net/browse/EV-35?atlOrigin=eyJpIjoiZjZmMTJlMWJiNmZkNGY3YmJkMjcyZTBiMGNkMjUwNDUiLCJwIjoiaiJ9)
3. [QR4] fix: can not find external pin for choice

v1.0.12.QR1 / 2024-06-04
=====================
**Features**
1. fix: use reserved pins for booking publish
2. [QR1] fix: release pin after processing
3. [QR2] fix: not allow to create booking job when not enough available pins

v1.0.13 / 2024-06-05
=====================
**Features**
1. use redis as a distributed lock

v1.0.13.QR1 / 2024-06-11
=====================
**Features**
1. use redis as a distributed lock
2. [QR1] fix: show giftpop error message when get creating urbox voucher fail

v1.0.14 / 2024-06-05
=====================
**Features**
1. feat [jira-EV41](https://vsourcing.atlassian.net/browse/EV-41?atlOrigin=eyJpIjoiNDRhMjUzMTQ2MTRjNDMxYWI2M2YxNjBkMTNlZWRjYjkiLCJwIjoiaiJ9): unify response format to:
    ```json
    {
        "code": "int",
        "message": "string",
        "data": "string"
    }
    ```
2. [QR1] ref: warning when logged-in username is null instead of error log
3. ref: add more 3 error code for choice voucher purchasing
    ```
        10011 : INACTIVE_BRAND
        10012 : INACTIVE_GOOD
        10013 : CHOICE_GOOD_ID_NULL
   ```
4. [QR4]: fix: can not send message when transferring voucher

v1.1.0 / 2024-06-25
=====================
**Features**
1. change choice voucher request payload
2. alter voucher table: 
   - change `choice_voucher_ev` to `parent_voucher_ev`
   - change `choice_token` to `parent_voucher_token`
3. [QR2] fix: 400 because of invalid type when call choose-choice api

v1.1.2 / 2024-07-03
=====================
**Features**
1. [EV-58](https://vsourcing.atlassian.net/browse/EV-58) fix: gift pop error invalid quantity
2. [QR1] fix: if there are remaining pins in db, publishing URBOX/GIFTPOP will be fail
3. [QR5] feat: add more gift field in urbox response when pin purchasing fail
4. [QR6] fix: [Jira](https://vsourcing.atlassian.net/browse/EV-60?atlOrigin=eyJpIjoiZjhjOWIzZjZhMjM4NDgyMjkxYzU3MzE0ZTVmNWZjNmMiLCJwIjoiaiJ9) Prevent Choice/Bulk voucher purchases the EXPIRED good
5. [QR7] fix: show error code when service be return error code

v1.1.3 / 2024-07-16
=====================
**Features**
1. feat: add more system type: `VNPT_EPAY`
2. [QR1] feat: update customer table
3. [QR2] fix: use Date instead of LocalDateTime and LocalDate

v1.1.4 / 2024-08-24
========================
**Feature**
AQUA-72
1. feat: add new good type limited count LC

v1.1.5 / 2024-08-29
========================
**Feature**
AQUA-72
1. feat: use api instead of queue consumer for transfer handling

v1.1.6 / 2024-09-19
========================
**Feature**
EV-87
1. fix: incorrect resend message upon sending child voucher

v1.1.7 / 2024-10-01
========================
**Feature**
EV-87
1. feat: push resend request to publish queue


v1.2.0 / 2024-11-01
========================
**Feature**
- Transfer using userId
- Update activation new flow
- Support mail

v1.3.0 / 2024-11-20
========================
**Feature**
- Purchase watane product
---
Improve loging for publish flow

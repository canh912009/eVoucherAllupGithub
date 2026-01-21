
# E-Voucher Service FE Release Notes
***Author** Nguyen Thanh Tai*
#  
#  

# v1.2.0 / 2024-10-07

**Detail**
1. Support new flow
2. Optional user name


# v1.1.3 / 2024-09-17

**Detail**
1. Fix bug: Error transferring voucher


# v1.1.2 / 2024-08-26

**Detail**
1. Remove /voucher/use & /voucher/update-status

# v1.1.1 / 2024-08-21

**Detail**
1. Change Local date to Date

# v1.1.0 / 2024-06-25

**Detail**
1. Support BULK voucher

# v1.0.5 / 2024-06-17

**Detail**
1. Handle error messages

---
# v1.0.4 / 2024-05-31

**Detail**
1. Fix bug publish status = GENERATING for PAPER typed
2. Fix bug publish detail status = GENERATE failed when transfer
3. Code refactor

# v1.0.3 / 2024-05-16

**Detail**
1. feat: sync voucher status with urbox

# v1.0.2 / 2024-05-16

**Detail**
1. handle using voucher


### Version
1.0.2

## Detail
1. feat: add more extPinType field to voucher

### Version
1.0.1

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- AquaVN-22: activate voucher flow

### Added functions

### Updated functions
1. store model: remove province; district; ward;
2. add region

### Configuration update
*none*

=========================

### Version
1.0.0

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- AquaVN-22: activate voucher flow

### Added functions
- AquaVN-22: activate voucher flow

### Updated functions
*none*

### Configuration update
*none*

## E-Voucher-Service-Fe-0.8.0

---

### Version
0.8.0

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Support download-typed voucher

### Added functions
- Support download-typed voucher

### Updated functions
*none*

### Configuration update
*none*

## E-Voucher-Service-Fe-0.7.0

---

### Version
0.7.0

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Disable voucher flow
- Resend voucher flow

### Added functions
- Disable voucher flow
- Resend voucher flow

### Updated functions
*none*

### Configuration update
```
  ### Disable voucher
  rabbitmq.in.exchange.disable=direct_exchange
  rabbitmq.in.routingKey.disable=disable_voucher
  rabbitmq.in.durable.disable=true
  rabbitmq.in.queue.disable=DISABLE_VOUCHER
  
  ### Disable voucher result
  rabbitmq.out.exchange.disableResult=direct_exchange
  rabbitmq.out.routingKey.disableResult=disable_voucher_result
  rabbitmq.out.durable.disableResult=true
  rabbitmq.out.queue.disableResult=DISABLE_VOUCHER_RESULT
  
  ### Resend voucher
  rabbitmq.in.exchange.resend=direct_exchange
  rabbitmq.in.routingKey.resend=resend_voucher
  rabbitmq.in.durable.resend=true
  rabbitmq.in.queue.resend=RESEND_VOUCHER
  
```


## E-Voucher-Service-Fe-0.6.15

---

### Version
0.6.15

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9
 
### Summary
- Send otp sms

### Added functions
- Send otp sms

### Updated functions
*none*

### Configuration update
```
  message.otp.template=[aQua VN] AQUA RETAIL gui ban mot mon qua lua chon. Vui long truy cap vao link de nhan qua: https://ev.aqua.gift. Ma OTP cua ban la %s
  ### Write messages to SMS OTP
  rabbitmq.out.exchange.otp=direct_exchange
  rabbitmq.out.routingKey.otp=send_otp
  rabbitmq.out.durable.otp=true
  rabbitmq.out.queue.otp=SEND_OTP
```


#  
#  

---

## E-Voucher-Service-Fe-0.6.14

---

### Version
0.6.14.QR1

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9### Summary

### Added functions
- add choice token to sms message
- change choice token to OTP in message template

### Updated functions
*none*

### Configuration update
*none*

#  
#  

---

### Version
0.6.13

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9### Summary

### Added functions
- choose choice item

### Updated functions
*none*

### Configuration update
*none*

#  
#  

---

### Version
0.6.12

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9### Summary

### Added functions
- add more choice voucher
- add more choiceToken, choiceVoucherEv fields into voucher incoming
- add more choices into good incoming

### Updated functions
*none*

### Configuration update
*none*

#  
#  

### Version
0.6.11

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9### Summary

### Added functions
- change good, voucher's field: displayType to system

### Updated functions
*none*

### Configuration update
*none*

#  
#  

### Version
0.6.10

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Add 1 new field in voucher `externalPinPassword`

### Added functions
- Add 1 new field in voucher `externalPinPassword`

### Updated functions
*none*

### Configuration update
*none*

#  
#  

### Version
0.6.9.QR5

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Add 2 new fields in voucher `contentImageName`, `contentImagePath`
- Remove `contentImage`

### Added functions
- Add 2 new fields in voucher `contentImageName`, `contentImagePath`
- Remove `contentImage`

### Updated functions
*none*

### Configuration update
*none*

#  
#  



## E-Voucher-Service-Fe-0.6.9.QR4

---

### Version
0.6.9.QR4

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Add 2 new fields in voucher `contentImage`, `contentLink`

### Added functions
- Add 2 new fields in voucher `contentImage`, `contentLink`
- Do not send message if the smsType is null or empty

### Updated functions
*none*

### Configuration update
*none*

#  
#  


## E-Voucher-Service-Fe-0.6.9.QR3

---

### Version
0.6.9.QR3

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Fix bug isPosLink error

### Added functions
*none*

### Updated functions
- Fix bug `isPosLink` error

### Configuration update
*none*

#  
#  


### Version
0.6.9.QR2

- update new ui
- update rollback flow when have error during transfer voucher

## E-Voucher-Service-Fe-0.6.8

---

### Version
0.6.8

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Handle `external PIN`

### Added functions
- Handle `external PIN`
- Adding 3 more fields for `voucher`
  - `extPinId`
  - `extPinDisplayType`: `BARCODE/NORMAL`
  - `extPinNo`

### Updated functions
*none*

### Configuration update
*none*

#  
#  


## E-Voucher-Service-Fe-0.6.7

---

### Version
0.6.7

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Handle isPosLink for brand
- Expire job retrieves all vouchers that are not `expired` and not `disabled`

### Added functions
- Handle isPosLink for brand
- Expire job retrieves all vouchers that are not `expired` and not `disabled`

### Updated functions
*none*

### Configuration update
*none*

#  
#  


## E-Voucher-Service-Fe-0.6.6

---

### Version
0.6.6

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Handle exception cases for publish voucher flow

### Added functions
- Handle exception cases for publish voucher flow

### Updated functions
*none*

### Configuration update
*none*

#  
#  



## E-Voucher-Service-Fe-0.6.5.RC1

---

### Version
0.6.5.RC1

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Fix bug can not parse date field to String for Store `registerDate`
- Remove redundant config `general.maxRetryCount`

### Added functions
*none*

### Updated functions
- Fix bug can not parse date field to String for Store `registerDate`
  ```json
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  ```
- Remove redundant config `general.maxRetryCount`

### Configuration update
~~`general.maxRetryCount`~~

#  
#  


## E-Voucher-Service-Fe-0.6.5

---

### Version
0.6.5

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Update new template file.

### Added functions
*None*

### Updated functions
- Update customerId from Long to String

### Configuration update
- Add 2 new `json` file for template message
`message_template_en.json` & `transfer_message_template_en.json`
```json
{
  "template": "aQua gui toi Quy khach {customerName} ma QR tai duong link {shortLink} Vui long quet ma va lam theo huong dan.",
  "language": "EN",
  "data": [
    {
      "key": "customerName",
      "type": "CUSTOM",
      "encrypted": true,
      "defaultValue": null,
      "maxLength": 30
    },
    {
      "key": "sender",
      "type": "DISABLED",
      "encrypted": false,
      "defaultValue": null,
      "maxLength": 30
    },
    {
      "key": "message",
      "type": "DISABLED",
      "encrypted": false,
      "defaultValue": null,
      "maxLength": 30
    },
    {
      "key": "productName",
      "type": "DISABLED",
      "encrypted": false,
      "defaultValue": null,
      "maxLength": 30
    },
    {
      "key": "expireDate",
      "type": "DISABLED",
      "encrypted": false,
      "defaultValue": null,
      "maxLength": 30
    },
    {
      "key": "transferMessage",
      "type": "DISABLED",
      "encrypted": false,
      "defaultValue": null,
      "maxLength": 30
    },
    {
      "key": "shortLink",
      "type": "CUSTOM",
      "encrypted": false,
      "defaultValue": null,
      "maxLength": 50
    },
    {
      "key": "cta2",
      "type": "DISABLED",
      "encrypted": false,
      "defaultValue": null,
      "maxLength": 30
    }
  ]
}
```
**Note**  
`data.type`: `DISABLED/CUSTOM/FIXED`  
`CUSTOM`: apply the external value  
`FIXED`: apply the `defaultValue`  
`DISABLED`: do not use in template

#  
#  




## E-Voucher-Service-Fe-0.6.4.QR3

---

### Version
0.6.4.QR3

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Update customerId from Long to String
```
http://192.168.0.125:9200/publish/_update_by_query
{
  "script": {
    "source": "ctx._source.customer.id = Integer.toString(ctx._source.customer.id);",
    "lang": "painless"
  },
  "query": {
    "match_all": {}
  }
}
```

### Added functions
*None*

### Updated functions
- Update customerId from Long to String

### Configuration update
*None*
#  
#  






## E-Voucher-Service-Fe-0.6.4.QR2

---

### Version
0.6.4.QR2

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Update `periodExpireDate` format from `YYYY-MM-dd HH:mm:ss` to `YYYY-MM-dd`. `periodExpireDate`
by default will be used as the end of the day `23:59:59`

### Added functions
*None*

### Updated functions
- Update `periodExpireDate` format from `YYYY-MM-dd HH:mm:ss` to `YYYY-MM-dd`.

### Configuration update
*None*
#  
#  




## E-Voucher-Service-Fe-0.6.4.QR1

---

### Version
0.6.4.QR1

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Remove check when the store validYN = N

### Added functions
*None*

### Updated functions
- Remove check when the store validYN = N

### Configuration update
*None*
#  
#  



## E-Voucher-Service-Fe-0.6.4

---

### Version
0.6.4

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Change except store format from string to array

### Added functions
*None*

### Updated functions
- Change except store format from string to array

### Configuration update
*None*
#  
#  



## E-Voucher-Service-Fe-0.6.3

---

### Version
0.6.3

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Update for `incoming` goods from `BE`

### Added functions
*None*

### Updated functions
- Update for `incoming` goods from `BE`
- Remove from `goods`
  + ~~supplyDiscountCost~~
  + ~~sellStartDate~~
  + ~~sellEndDate~~
- Add to `goods`
  - supplyDiscountRate
  - supplyDiscountAmount
  - startDate
  - endDate
  - periodType: FIXED_TERM/FIXED_DT
  - periodTerm: only available when periodType = FIXED_TERM
  - periodExpireDate: only available when periodType = FIXED_DT

### Configuration update
*None*
#  
#  



## E-Voucher-Service-Fe-0.6.2.QR1

---

### Version
0.6.2.QR1

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Fix Can not update toVoucherId

### Added functions
- Fix Can not update toVoucherId

### Updated functions
*none*

### Configuration update
*None*
#  
#  



## E-Voucher-Service-Fe-0.6.1

---

### Version
0.6.1

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Fix issue transfer incorrect name

### Added functions
*none*

### Updated functions
- Fix issue transfer incorrect name

### Configuration update
*None*

#  
#  

## E-Voucher-Service-Fe-0.6.0

---

### Version
0.6.0

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Handle transfer case

### Added functions
- Handle transfer case

### Updated functions
*none*

### Configuration update
*None*

#  
#  

## E-Voucher-Service-Fe-0.5.0

---

### Version
0.5.0

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Add retry function

### Added functions
- Add retry function

### Updated functions
*none*

### Configuration update
*None*

#  
#  

## E-Voucher-Service-Fe-0.4.0

---

### Version
0.4.0

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Report generate status to back end

### Added functions
- Report generate status to back end

### Updated functions
*none*

### Configuration update

| Field                            | Type    | Description                                     |
|----------------------------------|---------|-------------------------------------------------|
| `rabbitmq.out.queue.result`      | String  | Outgoing generate result queue to Backend       | 
| `rabbitmq.out.exchange.result`   | String  | Outgoing generate result exchange to Backend    |
| `rabbitmq.out.routingKey.result` | String  | Outgoing generate result routing key to Backend |
| `rabbitmq.out.durable.result`    | Boolean | Outgoing generate result durable to Backend     |

#  
#  

## E-Voucher-Service-Fe-0.3.0

---

### Version
0.3.0

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Handle update store flow

### Added functions
- Handle update store flow

### Updated functions
*none*

### Configuration update

| Field                              | Type    | Description                                 |
|------------------------------------|---------|---------------------------------------------|
| `rabbitmq.out.queue.store`         | String  | Outgoing store queue name to Backend        |
| `rabbitmq.out.exchange.store`      | String  | Outgoing store queue exchange to Backend    |
| `rabbitmq.out.routingKey.store`    | String  | Outgoing store queue routing key to Backend |
| `rabbitmq.out.durable.store`       | Boolean | Outgoing store queue durable to Backend     | 
| `elastic.store.indexName`          | String  | Elasticsearch Store index name              | 

#  
#  

## E-Voucher-Service-Fe-0.2.0

---

### Version
0.2.0

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Handle expire vouchers job
- Handle restore vouchers job
- Handle transfer history

### Added functions
- Handle expire vouchers job
- Handle restore vouchers job
- Handle transfer history

### Updated functions
*none*

### Configuration update

| Field                                             | Type    | Description                                    |
|---------------------------------------------------|---------|------------------------------------------------|
| `rabbitmq.out.queue.jobResult`                    | String  | Outgoing job result name to Backend            |
| `rabbitmq.out.exchange.jobResult`                 | String  | Outgoing job result exchange to Backend        |
| `rabbitmq.out.routingKey.jobResult`               | String  | Outgoing job result routing key to Backend     |
| `rabbitmq.out.durable.jobResult`                  | Boolean | Outgoing job result durable to Backend         | 
| `quartz.expireVoucher.enabled`                    | Boolean | Quartz expire voucher enable or not            | 
| `quartz.expireVoucher.cron`                       | String  | Quartz expire voucher cron                     | 
| `quartz.expireVoucher.retryCount`                 | String  | Quartz expire voucher retry count              | 
| `quartz.expireVoucher.voucherDurationInDays`      | Number  | Quartz expire voucher voucher duration in day  | 
| `quartz.restoreVoucher.enabled`                   | Boolean | Quartz restore voucher enable or not           | 
| `quartz.restoreVoucher.cron`                      | String  | Quartz restore voucher cron                    | 
| `quartz.restoreVoucher.retryCount`                | String  | Quartz restore voucher retry count             | 
| `quartz.restoreVoucher.confirmWaitDurationInDays` | Number  | Quartz restore voucher voucher duration in day | 
| `spring.quartz.job-store-type`                    | String  | Quartz job store type                          | 
| `spring.quartz.jdbc.initialize-schema`            | String  | JDBC initialize schema strategy                | 
| `spring.datasource.jdbc-url`                      | String  | Spring JDBC-url                                | 
| `spring.datasource.driverClassName`               | String  | Spring JDBC driver class name                  | 
| `spring.datasource.username`                      | String  | Spring JDBC user name                          | 
| `spring.datasource.password`                      | String  | Spring JDBC password                           | 

#  
#  

## E-Voucher-Service-Fe-0.1.1

---

### Version
0.1.1

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Bind queue correctly and update message template

### Added functions
*none*

### Updated functions
- Bind queue correctly and update message template

### Configuration update
*none*

#  
#  
## E-Voucher-Service-Fe-0.1.0

---

### Version
0.1.0

### Restriction
- Java v11
- RabbitMQ 3.10.19
- Elasticsearch 7.17.9

### Summary
- Build core structure
- Save voucher from MQ to ES
- Handle request as a list of queue

### Added functions
- Build core structure
- Save voucher from MQ to ES
- Handle request as a list of queue

### Updated functions
*none*

### Configuration update

| Field                                                    | Type    | Description                                             |
|----------------------------------------------------------|---------|---------------------------------------------------------|
| `server.port`                                            | number  | E-Voucher Service FE listen port                        |
| `spring.rabbitmq.host`                                   | String  | Rabbit MQ host                                          |
| `spring.rabbitmq.port`                                   | String  | Rabbit MQ port                                          |
| `spring.rabbitmq.username`                               | String  | Rabbit MQ user name                                     |
| `spring.rabbitmq.password`                               | String  | Rabbit MQ password                                      |
| `general.maxRetryCount`                                  | String  | Rabbit MQ password                                      |
| `spring.rabbitmq.listener.simple.retry.enabled`          | Boolean | Listener retry enable or not                            |
| `spring.rabbitmq.listener.simple.retry.max-attempts`     | Number  | Listener retry max attempts                             |
| `spring.rabbitmq.listener.simple.retry.initial-interval` | String  | Listener retry initial interval. Ex: 1000ms             |
| `spring.rabbitmq.listener.simple.retry.multiplier`       | String  | Listener retry multiplier. Ex: 2.0                      |
| `rabbitmq.in.queue.publish`                              | String  | Incoming publish queue name from Publish Service        |
| `rabbitmq.in.exchange.publish`                           | String  | Incoming publish queue exchange from Publish Service    |
| `rabbitmq.in.routingKey.publish`                         | String  | Incoming publish queue routing key from Publish Service |
| `rabbitmq.in.durable.publish`                            | Boolean | Incoming publish queue durable from Publish Service     |
| `rabbitmq.out.queue.publish`                             | String  | Outgoing publish queue name to Push Agent               |
| `rabbitmq.out.exchange.publish`                          | String  | Outgoing publish queue exchange to Push Agent           |
| `rabbitmq.out.routingKey.publish`                        | String  | Outgoing publish queue routing key to Push Agent        |
| `rabbitmq.out.durable.publish`                           | Boolean | Outgoing publish queue durable to Push Agent            | 
| `elastic.host`                                           | String  | Elasticsearch host                                      | 
| `elastic.port`                                           | Number  | Elasticsearch port                                      | 
| `elastic.basicAuth`                                      | Boolean | Elasticsearch enable basic authentication or not        | 
| `elastic.username`                                       | String  | Elasticsearch username                                  | 
| `elastic.password`                                       | String  | Elasticsearch password                                  | 
| `elastic.voucher.indexName`                              | String  | Elasticsearch password                                  | 
| `elastic.publish.indexName`                              | String  | Elasticsearch password                                  | 

#  

# v1.2.0 / 2024-11-05

**Detail**
- Support mail type
- Restore generation SMS body for DOWNLOAD & PAPER
---
Change the email subject

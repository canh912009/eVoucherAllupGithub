v0.6.12 - 2023-11-28
==================
### Config changes

- no changes

### DB changes

- no changes

v0.6.14. - 2023-12-28
==================
### Config changes

1. Added
- castis.component.publish-service.url: publish-service component url

### DB changes

- no changes

v0.6.15. - 2024-02-05
==================
### Config changes
1. Added
```
    message.otp.template=[aQua VN] AQUA RETAIL gui ban mot mon qua lua chon. Vui long truy cap vao link de nhan qua: https://ev.aqua.gift. Ma OTP cua ban la %s
    ### Write messages to SMS OTP
    rabbitmq.out.exchange.otp=direct_exchange
    rabbitmq.out.routingKey.otp=send_otp
    rabbitmq.out.durable.otp=true
    rabbitmq.out.queue.otp=SEND_OTP
```

### DB changes
- no changes

v0.7.0 - 2024-02-27
==================
### Config changes
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


### DB changes
- no changes

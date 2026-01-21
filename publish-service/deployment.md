v0.0.16/ 2023-12-05
============================
### Config changes
- no changes

### DB changes
- no changes

v0.0.18.QR1 / 2024-02-04
==========================
### Config changes:
- add more 2:
  system:
  decrypt:
  vector: TotalRandoVector
  key: decrypt key

v0.0.19/ 2024-02-20
============================
### Config changes
- no changes

### DB changes
- rsa.evoucher.private-key
- rsa.evoucher.public-key
- rsa.gift-pop.public-key
- gift-pop.authentication-key
- gift-pop.decrypt-pin-key
- gift-pop.url

v0.0.20 / 2024-02-28
========================
**Config changes**
- rabbitmq.queue.resend-voucher: RESEND_VOUCHER
- rabbitmq.routing-key.resend-voucher: resend_voucher

**DB changes in file**
- no changes

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

v1.0.10 / 2024-05-20
==================
**Config**
1. Changes:
    - gift-pop:
      authentication-key: QVFVQTpRbXQyVERkMmRIbHZWMloyYkVF
      decrypt-pin-key: RBV5NWKTTRNTOO2L


v1.0.13 / 2024-06-05
==================
**Config**
1. Changes:
```
redis:
  host: 192.168.0.125
  port: 6379
  isStandAlone: true
```
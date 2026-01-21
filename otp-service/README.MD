# OTP Service
_Author_: Nguyen Thanh Tai

Generate One time password (OTP)
OTP lasts for 10 minutes or config in `otp.timeToLiveInSec`  

OTP can be only used once, the next time calls OTP will be invalid. (`USED`)
Disable this by setting `system.oneTimeUse` to `false`  

OTP can be restored by calling `/otp/restore/{otpCode}`. Back to `NORMAL`

## Mechanism
Using a MIT library: [OTP Java ](https://github.com/BastiaanJansen/otp-java)

HMAC-based one-time password (HOTP, RFC 4226) generation based on counter and OTPAuth URI.  
Password length: 8 or config in `otp.passwordLength`  
Algorithm: `SHA256`  
Counter: Random from 0 -> Integer(MAX_LENGTH)
## OTP statuses

- __NORMAL__: OTP created
- __USED__: OTP used, can not be reused. This status only available if `system.oneTimeUse` = `true`

## Build project
```
mvn clean package
```

## Docker compose

### Build
```
docker-compose up
```

# AQUA E-VOUCHER PARTNER SERVICE

v1.1.0 / 2024-07
========================

1. feat: init commit
2. feat: integrate with vnpt
3. feat: create query balance, topup apis


v1.1.1 / 2024-07-19
========================
1. feat: integrate with service be for using vnpt voucher
2. [QR1] fix: purchase history null after topup successfully
3. [QR2] fix: bug sai chu ky khi topup
4. [QR3] fix: vnpt voucher save exchange type as string instead of origin
5. [QR4] fix: can not topup 
6. [QR5] feat: validate receiver number before topup
7. [QR6] fix: get error when topup with target number contain region code
8. [QR7] fix: wrong vnpt sign while topup with target number contain region code
9. [QR8] fix: handle 0084 mobile number
10. [QR9] fix: save vnpt request response body to third party history
11. [QR10] fix: validate face value before purchase vnpt voucher
12. [QR11] fix: lost 0 when topup phone number container 0 characters
13. [QR12] fix: set jackson timezone to utc+7



v1.1.2 / 2024-10-07
========================
1. fix: mark vnpt 99 code (pending) as success result

v1.2.0 / 2024-11-20
========================
1. Support watane products


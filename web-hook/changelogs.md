### AQUA WEB HOOK


## CONFIG CHANGES TABLE

| VERSION    | KEY                         | ACTION | VALUE  | DETAIL                  |
|------------|-----------------------------|--------|--------|-------------------------|
| 1.0.0      | spring.application.name | ADD    | string | web_hook                |
|1.0.0|server.servlet.context-path|ADD| string | /                       |
|1.0.0|server.port|ADD| int    | 8089                    |
|1.0.0|spring.datasource.driver-class-name|ADD| string | org.mariadb.jdbc.Driver |
|1.0.0|spring.datasource.url|ADD|    string    |    ${SECRETS_DB_URL:jdbc:mariadb://192.168.0.125:3306/e_voucher}                     |
|1.0.0|spring.datasource.username|ADD|   string     |           ${SECRETS_DB_USERNAME:castis}              |
|1.0.0|spring.datasource.password|ADD|    string    |${SECRETS_DB_PASSWORD:castis}                         |
|1.0.0|spring.jpa.hibernate.ddl-auto|ADD|  string      |none                         |
|1.0.0|spring.jpa.properties.hibernate.show_sql|ADD|   string     |true                         |
|1.0.0|spring.jpa.properties.hibernate.format_sql|ADD|  string      |true                         |
|1.0.0|log.home|ADD|     string   |                         |
|1.0.0|components.service-be.url|ADD|     string   |${SERVICE_BE_URL:http://192.168.0.125:8083/evouchers}                         |

v1.0.0 / 2023-05-08
========================
**Feature**
1. init project
2. integrate with giftpop (update used voucher status)

v1.0.1 / 2023-05-20
========================
**Feature**
1. update voucher status in elasticsearch

v1.0.2 / 2023-05-22
========================
**Feature**
1. feat: sync urbox voucher status

v1.1.0 / 2023-06-26
========================
**Feature**
1. feat: add more voucher type bulk
2. fix: missing urbox voucher using time
3. [QR1] ref: refactor be client request

v1.1.1 / 2023-10-07
========================
**Feature**
1. fix: error while sync transferred voucher status

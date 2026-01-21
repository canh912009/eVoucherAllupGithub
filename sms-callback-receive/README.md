# Sms Callback Receive

Use to receive deliver report from sms fpt.
This is apart from Push module of EVoucher system.
Implement base on EVoucher design system and sms fpt api document.
## Generate jar file

```
mvn install
```
Jar file will generate  in target folder.
## Deploy

To run this service, your system need java 8.

```
java -jar sms-0.0.1-SNAPSHOT.jar
```
Want to change configuration, change it from application.properties file. For Example, change server port, change url elasticsearch...

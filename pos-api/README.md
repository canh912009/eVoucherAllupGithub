

# Run

* mvn clean install                      => unit testing + reporting
* mvn clean install -P integration test  => integration testing + reporting
* mvn clean install -P test-all          => unit AND integration testing + (combined) reporting !

#Skipping Tests After the Nth Failure or Error
*-Dmaven.test.failure.ignore=true

#Configure a code style scheme
Press Ctrl+Alt+S to open the IDE settings and select Editor.
   1. click Code style -> click java -> import code-style.xml
      ```
      using `Ctrl + Alt + L` to Reformat Code
      ```
   2. click Code templates -> click includes and add template below:
      ```
      /**
        * ${NAME}
        * @project ${PROJECT_NAME}
        * @author by daont on ${DAY}/${MONTH}/${YEAR}
        */
      ```

How to use communicationRestTemplate </br>
```
    private final CommunicationRestTemplate restTemplate;
    public AccessTokenDTO Register() {
        Map<Object, Object> body = new HashMap<>();
        body.put("username", "0907445554");
        body.put("password", "Travinh12345A#");
        body.put("phoneNumber", "0907445554");
        body.put("fullName", "0972806455");
        String url = "https://ekyc.castis.com/user-profile-service/admin/v1/register";
        ResponseEntity<AccessTokenDTO> useDto = restTemplate.makeRequestRedis(HttpMethod.POST, RedisConstants.AFC_KEY, body, url, AccessTokenDTO.class);
        return useDto.getBody();
    }
```

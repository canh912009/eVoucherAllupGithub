package asia.castis.web_hook.bean.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WataneHeaders {
    private String username;
    private String credential;
    private String signature;
}
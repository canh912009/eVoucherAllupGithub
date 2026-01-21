package asia.castis.web_hook.bean.entity;

import asia.castis.web_hook.common.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_3rd_party_req")
public class ThirdPartyCallingHistory {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "system")
    private String system;

    @Column(name = "request_url")
    private String requestUrl;

    @Column(name = "request_time")
    private Date requestTime;

    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "response_time")
    private Date responseTime;

    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "result")
    private String result;

    @Column(name = "description")
    private String description;

    @Column(name = "uploadId")
    private Long uploadId;

    @Column(name = "request_type")
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    public void setFail() {
        this.result = "Fail";
    }

    public void setOk() {
        this.result = "OK";
    }
}

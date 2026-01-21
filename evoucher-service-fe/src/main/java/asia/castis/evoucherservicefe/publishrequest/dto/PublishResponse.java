package asia.castis.evoucherservicefe.publishrequest.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class PublishResponse<T> {
    private String message = "OK";

    private String timestamp = LocalDateTime.now(ZoneId.systemDefault()).toString();

    private String errorCode = "0";

    private Long totalCount;
    private T data;
}

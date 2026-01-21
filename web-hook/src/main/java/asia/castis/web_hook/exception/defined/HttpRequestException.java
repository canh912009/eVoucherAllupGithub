package asia.castis.web_hook.exception.defined;

import org.springframework.web.client.HttpServerErrorException;

public class HttpRequestException extends RuntimeException {
    public HttpRequestException(String msg, RuntimeException e) {
        super(msg, e);
    }
}

package asia.castis.web_hook.exception.defined;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException{
    Integer code;
    public EntityNotFoundException(String errorMessage) {
        super(errorMessage);
    }
    public EntityNotFoundException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
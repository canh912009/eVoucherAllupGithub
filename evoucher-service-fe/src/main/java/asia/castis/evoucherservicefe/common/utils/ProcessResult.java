package asia.castis.evoucherservicefe.common.utils;

import asia.castis.evoucherservicefe.common.enums.EnumProcessResult;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProcessResult<T> {
    private EnumProcessResult result;
    private T output;
    private String message;

    public void fail(String message) {
        this.setResult(EnumProcessResult.FAILED);
        this.setMessage(message);
    }
    public void invalid(String message) {
        this.setResult(EnumProcessResult.INVALID);
        this.setMessage(message);
    }
    public void success(T data) {
        this.setResult(EnumProcessResult.SUCCESS);
        this.setMessage("Success");
        this.setOutput(data);
    }
}


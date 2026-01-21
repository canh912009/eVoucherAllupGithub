package asia.castis.evoucher.api.common;

import asia.castis.evoucher.api.elastic.enums.EnumProcessResult;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProcessResult<T> {

    private EnumProcessResult result;
    private T data;
    private String message;
}


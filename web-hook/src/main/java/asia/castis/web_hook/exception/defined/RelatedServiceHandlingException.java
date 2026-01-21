package asia.castis.web_hook.exception.defined;

import java.rmi.ServerException;

public class RelatedServiceHandlingException extends ServerException {
    public RelatedServiceHandlingException(String s) {
        super(s);
    }

    public RelatedServiceHandlingException(String s, Exception ex) {
        super(s, ex);
    }
}

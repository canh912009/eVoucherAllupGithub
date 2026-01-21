package asia.castis.otpservice.dto;

import asia.castis.otpservice.common.Constants;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

public class ResponseBuilder<T> {
    public ResponseObject exception(Throwable e, WebRequest request) {
        ResponseObject response = new ResponseObject();
        response.setErrorType(e.getClass().getName());
        response.setError(e.getMessage());
        response.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        return response;
    }
    public ResponseObject<T> success(T data, HttpServletRequest request) {
        ResponseObject<T> response = new ResponseObject<>();
        response.setErrorType("");
        response.setError("");
        response.setPath(request.getRequestURI());
        response.setData(data);
        return response;
    }
}

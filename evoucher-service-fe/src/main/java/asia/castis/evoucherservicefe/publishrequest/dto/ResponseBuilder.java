package asia.castis.evoucherservicefe.publishrequest.dto;

import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

public class ResponseBuilder<T> {
    public ResponseObject exception(Throwable e, HttpServletRequest request) {
        ResponseObject response = new ResponseObject();
        response.setErrorType(e.getClass().getName());
        response.setError(e.getMessage());
        response.setPath(request.getRequestURI());
        return response;
    }
    public ResponseObject exception(String errorType, String message, HttpServletRequest request) {
        ResponseObject response = new ResponseObject();
        response.setErrorType(errorType);
        response.setError(message);
        response.setPath(request.getRequestURI());
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

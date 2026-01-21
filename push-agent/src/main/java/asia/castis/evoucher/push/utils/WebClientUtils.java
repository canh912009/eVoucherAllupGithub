package asia.castis.evoucher.push.utils;

import asia.castis.evoucher.push.exception.SendingSmsMessage4xxException;
import asia.castis.evoucher.push.exception.SendingSmsMessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

@Slf4j
public class WebClientUtils {
    /*
    public static ExchangeFilterFunction smsErrorHandlingFilter() {
        log.info("smsErrorhandlingfilter");
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            if (response.statusCode() != null && (response.statusCode().is3xxRedirection() || response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError())) {
                return response.bodyToMono(String.class)
                        .defaultIfEmpty(response.statusCode().getReasonPhrase())
                        .flatMap(body -> {
                            log.debug("Body is {}", body);
                            return Mono.error(new SendingSmsMessageException(body, String.valueOf(response.rawStatusCode())));
                        });
            } else {
                return Mono.just(response);
            }
        });
    }
    */

    public static ExchangeFilterFunction smsErrorHandlingFilter() {
        log.info("smsErrorhandlingfilter");
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if(clientResponse.statusCode()!=null ) {
                if(clientResponse.statusCode().is4xxClientError()) {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("clientResponse 4xx errorBody:{}", errorBody);
                                return Mono.empty();
                                //return Mono.error(new SendingSmsMessage4xxException(errorBody,clientResponse.statusCode().toString()));
                            });
                } else {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("clientResponse errorBody:{}", errorBody);
                                return Mono.error(new SendingSmsMessageException(errorBody,clientResponse.statusCode().toString()));
                            });
                }
            }else {
                log.info("clientResponse statusCode null");
                return Mono.just(clientResponse);
            }
        });
    }

    public static ExchangeFilterFunction zaloErrorHandlingFilter() {
        log.info("smsErrorHandlingFilter");
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if(clientResponse.statusCode()!=null && (clientResponse.statusCode().is5xxServerError() || clientResponse.statusCode().is4xxClientError()) ) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            return Mono.error(new SendingSmsMessageException(errorBody,clientResponse.statusCode().toString()));
                        });
            }else {
                return Mono.just(clientResponse);
            }
        });
    }
}

package asia.castis.evoucher.push.service;

import asia.castis.evoucher.push.common.utils.DateUtils;
import asia.castis.evoucher.push.components.PushCipher;
import asia.castis.evoucher.push.constant.Constant;
import asia.castis.evoucher.push.exception.*;
import asia.castis.evoucher.push.model.*;
import asia.castis.evoucher.push.repositories.PublishRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("smsMessageService")
@Slf4j
public class SmsMessageService {
    @Value("${smsFPT.client-id}")
    private String clientId;
    @Value("${smsFPT.client-secret}")
    private String clientSecret;
    @Value("${smsFPT.session-id}")
    private String sessionId;
    @Value("${smsFPT.authorization-uri}")
    private String tokenUrl;
    @Value("${smsFPT.sms-uri-domestic}")
    private String smsSendingUrlDomestic;
    @Value("${smsFPT.sms-uri-international}")
    private String smsSendingUrlInternational;
    @Value("${smsFPT.scope}")
    private String scope;
    @Value("${smsFPT.grant_type}")
    private String grantType;
    private String smsToken;

    @Autowired
    private WebClient webClientSMS;
    @Autowired
    private PushCipher pushCipher;
    private PublishRepository publishRepository;

    public void requestSmsToken() {
        log.info("requestSmsToken");
        String bodyData =
                "{ \"client_id\": \"" + clientId
                        + "\", \"client_secret\": \"" + clientSecret
                        + "\", \"session_id\": \"" + sessionId
                        + "\", \"scope\": \"" + scope
                        + "\", \"grant_type\": \"" + grantType
                        + "\"}";
        try {
            String response = webClientSMS.post()
                    .uri(tokenUrl)
                    .body(BodyInserters.fromValue(bodyData))
                    .retrieve()
                    .onStatus(HttpStatus::is3xxRedirection, clientResponse -> {
                        return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody->{
                            log.info("requestSmsToken: clientResponse 3xx errorBody:{}", errorBody);
                            return Mono.error(new RequestSmsToken3xxException(errorBody, clientResponse.statusCode().toString()));
                        });
                    })
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                        return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("requestSmsToken: clientResponse 4xx errorBody:{}", errorBody);
                                return Mono.error(new RequestSmsToken4xxException(errorBody, clientResponse.statusCode().toString()));
                            });
                    })
                    .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                        return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("sendingSmsMessage: clientResponse 5xx errorBody:{}", errorBody);
                                return Mono.error(new SendingSmsMessageException(errorBody, clientResponse.statusCode().toString()));
                            });
                    })
                    .bodyToMono(String.class)
                    .block();
            if (response != null) {
                log.info("requestSmsToken: response:{}", response);
                Gson gson = new Gson();
                SmsTokenResponse smsTokenResponse = gson.fromJson(response, SmsTokenResponse.class);
                this.setSmsToken(smsTokenResponse.getAccess_token());
            }
        } catch (RequestSmsToken4xxException requestSmsToken4xxException) {
            log.info("requestSmsToken 4xx Exception");
        } catch (RequestSmsToken5xxException requestSmsToken5xxException) {
            log.info("requestSmsToken 5xx Exception");
        } catch (RequestSmsToken3xxException requestSmsToken3xxException) {
            log.info("requestSmsToken 3xx Exception");
        } catch (Exception e) {
            log.info("requestSmsToken Exception: {}", e.getMessage());
        }
    }
    public void sendingSmsMessage(IncomingPublishMessage incomingPublishMessage,
                                  IncomingPublish incomingPublish,
                                  List<PublishDetailModel> publishDetailList,
                                  List<SendingPublishDetail> sendingPublishDetailList,
                                  SendingPublishDetail sendingPublishDetail) throws Exception {
        log.debug("sendingSmsMessage: accessToken:{}", this.getSmsToken());
        log.info("Incoming msg: {}", incomingPublishMessage);
        log.info("Incoming pulbish: {}", incomingPublish);
        for (PublishDetailModel publishDetailModel : publishDetailList) {
            log.info("publish detail: {}", publishDetailModel);
        }
        for (SendingPublishDetail publishDetail : sendingPublishDetailList) {
            log.info("publish detail send: {}", publishDetail);
        }
        String phoneNumber = pushCipher.cipherDecrypt(incomingPublishMessage.getReceiverMobileNumber());
        String incomMessage = incomingPublishMessage.getMessage();
        byte[] incomMessageBytes = incomMessage.getBytes(StandardCharsets.UTF_8);
        String incomMessageBase64 = Base64.getEncoder().encodeToString(incomMessageBytes);
        String bodyData =
                "{ \"access_token\": \"" + this.getSmsToken()
                        + "\", \"session_id\": \"" + sessionId
                        + "\", \"BrandName\": \"" + incomingPublish.getBrandName()
                        + "\", \"Phone\": \"" + phoneNumber
                        + "\", \"Message\": \"" + incomMessageBase64
                        + "\" }";
        log.debug("sendingSmsMessage: bodyData: {}", bodyData);
        try {
            // check phone number is domestic or international return url send sms corresponding
            String smsSendingUrl = isDomesticPhoneNumber(phoneNumber) ? smsSendingUrlDomestic : smsSendingUrlInternational;
            String smsSendingResponse = webClientSMS.post()
                .uri(smsSendingUrl)
                .body(BodyInserters.fromValue(bodyData))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("sendingSmsMessage: clientResponse 4xx errorBody:{}", errorBody);
                            return Mono.error(new SendingSmsMessage4xxException(errorBody,clientResponse.statusCode().toString()));
                            //return Mono.empty();
                        });
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("sendingSmsMessage: clientResponse 5xx errorBody:{}", errorBody);
                            return Mono.error(new SendingSmsMessageException(errorBody, clientResponse.statusCode().toString()));
                        });
                })
                .bodyToMono(String.class)
                .block();
            if (smsSendingResponse != null) {
                log.debug("sendingSmsMessage: smsMessageResponse: {}", smsSendingResponse);
                Gson gson = new Gson();
                SmsMessageResponse smsMessageResponseObject = gson.fromJson(smsSendingResponse, SmsMessageResponse.class);
                PublishDetailModel curDetailModel = publishDetailList.stream().filter(detail -> incomingPublishMessage.getPublishDetailId().equals(detail.getId()))
                                .findFirst().orElseThrow(() -> new Exception(String.format("Can not find publish detail %d", incomingPublishMessage.getPublishDetailId())));
                curDetailModel.setPublishStatusCode(EnumPublishDetailStatus.RESULT_PENDING);
                curDetailModel.setPublishResultMessage(smsMessageResponseObject.getMessage());
                curDetailModel.setMessageId(smsMessageResponseObject.getMessageId());

                sendingPublishDetail.setReceivedTime(DateUtils.getStrDate(Calendar.getInstance().getTime()));
                sendingPublishDetail.setMessageId(smsMessageResponseObject.getMessageId());
                sendingPublishDetail.setMessage(smsMessageResponseObject.getMessage());
                sendingPublishDetail.setResult(Constant.PUBLIC_DETAIL_STATUS_CODE.RESULT_PENDING);
                sendingPublishDetailList.add(sendingPublishDetail);
            }
        } catch (SendingSmsMessage4xxException sendingSmsMessage4xxException) {
            log.info("sendingSmsMessage: 4xx Exception: errorBody:{}", sendingSmsMessage4xxException.getErrorBody());
            Gson gson = new Gson();
            SmsSendingErrorResponse smsSendingErrorResponse = gson.fromJson(sendingSmsMessage4xxException.getErrorBody(), SmsSendingErrorResponse.class);
            if (smsSendingErrorResponse.getError() == Constant.SMS_ERROR_CODE.ACCESS_TOKEN_EXPIRED) {
                log.info("case sending expire time");
                log.info("sendingSmsMessage smsToken:{}", this.getSmsToken());
                this.requestSmsToken();
                //
                this.sendingSmsMessage(incomingPublishMessage, incomingPublish, publishDetailList, sendingPublishDetailList, sendingPublishDetail);
            } else if (smsSendingErrorResponse.getError() == Constant.SMS_ERROR_CODE.ACCESS_TOKEN_EMPTY) {
                log.info("sendingSmsMessage have token but response is Empty");
                //
            } else {
                // other case is mean fail
                log.info("sendingSmsMessage update sendingPublishDetailList");
                sendingPublishDetail.setResult(Constant.PUBLIC_DETAIL_STATUS_CODE.RESULT_FAIL);
                sendingPublishDetailList.add(sendingPublishDetail);
                PublishDetailModel curDetailModel = publishDetailList.stream().filter(detail -> incomingPublishMessage.getPublishDetailId().equals(detail.getId()))
                        .findFirst().orElseThrow(() -> new Exception(String.format("Can not find publish detail %d", incomingPublishMessage.getPublishDetailId())));
                curDetailModel.setPublishStatusCode(EnumPublishDetailStatus.FAIL_SND_MSG);
                curDetailModel.setPublishResultMessage(String.valueOf(smsSendingErrorResponse.getError()));
                curDetailModel.setId(incomingPublishMessage.getPublishDetailId());
            }
        } catch (SendingSmsMessageException sendingSmsMessageException) {
            log.error("sendingSmsMessage: Exception: errorBody:{}", sendingSmsMessageException.getErrorBody());
            log.error("Internal sms gateway error.");
        } catch (Exception e) {
            log.error("sendingSmsMessage: Exception: {}", e.getMessage());
        }
    }

    public void sendingSmsMessage(IncomingMessage incomingMessage) {
        String phoneNumberEncrypt = incomingMessage.getPhoneNumber();
        String message = incomingMessage.getMessage();
        String brandName = incomingMessage.getBrandName();
        log.info("Send Message to phone number: {} with message: {}", phoneNumberEncrypt, message);

        byte[] incomMessageBytes = message.getBytes(StandardCharsets.UTF_8);
        String incomMessageBase64 = Base64.getEncoder().encodeToString(incomMessageBytes);


        log.info("decypted phone number:{}", phoneNumberEncrypt);
        String phoneNumber = pushCipher.cipherDecrypt(phoneNumberEncrypt);

        String bodyData =
                "{ \"access_token\": \"" + this.getSmsToken()
                        + "\", \"session_id\": \"" + sessionId
                        + "\", \"BrandName\": \"" + brandName
                        + "\", \"Phone\": \"" + phoneNumber
                        + "\", \"Message\": \"" + incomMessageBase64
                        + "\" }";
        log.info("Send sms message payload: {}", bodyData);

        String smsSendingUrl = isDomesticPhoneNumber(phoneNumber) ? smsSendingUrlDomestic : smsSendingUrlInternational;
        String smsSendingResponse = webClientSMS.post()
                .uri(smsSendingUrl)
                .body(BodyInserters.fromValue(bodyData))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("Send sms message error 4xx with errorBody:{}", errorBody);
                                return Mono.error(
                                        new SendingSmsMessage4xxException(
                                                errorBody,
                                                clientResponse.statusCode().toString()));
                            });
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("Send sms message error 5xx with errorBody:{}", errorBody);
                                return Mono.error(
                                        new SendingSmsMessageException(
                                                errorBody,
                                                clientResponse.statusCode().toString()));
                            });
                })
                .bodyToMono(String.class)
                .block();

        log.info("Send Sms response: {}", smsSendingResponse);
    }

    public void setSmsToken(String smsToken) {
        this.smsToken = smsToken;
    }
    public String getSmsToken() {
        return this.smsToken;
    }

    private boolean isDomesticPhoneNumber(String phoneNumber) {
        // check phone start with '0' or '84' -> is a domestic phone number (VietNam)
        String regex = "^(0|84)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.find();
    }
}

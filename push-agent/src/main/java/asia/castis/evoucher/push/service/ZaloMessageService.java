package asia.castis.evoucher.push.service;

import asia.castis.evoucher.push.common.utils.DateUtils;
import asia.castis.evoucher.push.components.PushCipher;
import asia.castis.evoucher.push.constant.Constant;
import asia.castis.evoucher.push.exception.SendingZaloMessage4xxException;
import asia.castis.evoucher.push.exception.SendingZaloMessageException;
import asia.castis.evoucher.push.model.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class ZaloMessageService {
    @Value("${fnsFPT.app-id}")
    private String fns_app_id;
    @Value("${fnsFPT.secret-key}")
    private String fns_secret_key;
    @Value("${fnsFPT.lang}")
    private String fns_lang;
    @Value("${fnsFPT.sending_url}")
    private String fnsSendingUrl;
    @Value("${fnsFPT.callback_url}")
    private String zaloCallBackUrl;
    @Autowired
    private WebClient webClientZalo;
    @Autowired
    private PushCipher pushCipher;

    public void sendingZaloMessage(IncomingPublishMessage incomingPublishMessage, List<PublishDetailModel> publishDetailList, List<SendingPublishDetail> sendingPublishDetailList, SendingPublishDetail sendingPublishDetail) {
        log.info("sendingZaloMessage");

        String bodyData =
                "{ \"phone\": \"" + pushCipher.cipherDecrypt(incomingPublishMessage.getReceiverMobileNumber())
                        + "\", \"template_id\": \"" + incomingPublishMessage.getTemplateId()
                        + "\", \"template_data\": { "
                        + "\"customer_name\": \"" + incomingPublishMessage.getTemplateData().getCustomer_name()
                        + "\", \"service_name\": \"" + incomingPublishMessage.getTemplateData().getService_name()
                        + "\", \"reg_date\": \"" + incomingPublishMessage.getTemplateData().getReg_date()
                        + "\", \"customer_id\": \"" + incomingPublishMessage.getTemplateData().getCustomer_id()
                        + "\" }"
                        + ", \"sms_msg\": \"" + incomingPublishMessage.getMessage()
                        + "\", \"failover\": \"1"
                        + "\", \"callback_url\": \"" + zaloCallBackUrl
                        + "\" }";

        log.info("bodyData: {}", bodyData);
        try {
            String zaloSendingResponse = webClientZalo.post()
                    .uri(fnsSendingUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("app-id", fns_app_id)
                    .header("secret-key", fns_secret_key)
                    .header("lang", fns_lang)
                    .body(BodyInserters.fromValue(bodyData))
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("sendingZaloMessage: clientResponse 4xx errorBody:{}", errorBody);
                                    return Mono.error(new SendingZaloMessage4xxException(errorBody, clientResponse.statusCode().toString()));
                                });
                    })
                    .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("sendingZaloMessage: clientResponse 5xx errorBody:{}", errorBody);
                                    return Mono.error(new SendingZaloMessageException(errorBody, clientResponse.statusCode().toString()));
                                });
                    })
                    .bodyToMono(String.class)
                    .block();
            if (zaloSendingResponse != null) {
                Gson gson = new Gson();
                log.info("zaloSendingResponse: {}", zaloSendingResponse);
                ZaloMessageResponse zaloMessageResponse = gson.fromJson(zaloSendingResponse, ZaloMessageResponse.class);
                log.info("zaloMessageResponse.code: {}", zaloMessageResponse.getCode());
                log.info("zaloMessageResponse.message: {}", zaloMessageResponse.getMessage());
                log.info("zaloMessageResponse.data.message: {}", zaloMessageResponse.getData().getMessage_id());
                // message to elastic search
                PublishDetailModel publishDetailModel = new PublishDetailModel();
                publishDetailModel.setPublishStatusCode(EnumPublishDetailStatus.RESULT_PENDING);
                publishDetailModel.setPublishResultMessage(zaloMessageResponse.getMessage());
                publishDetailModel.setMessageId(zaloMessageResponse.getData().getMessage_id());
                publishDetailModel.setId(incomingPublishMessage.getPublishDetailId());
                publishDetailList.add(publishDetailModel);

                sendingPublishDetail.setReceivedTime(DateUtils.getStrDate(Calendar.getInstance().getTime()));
                sendingPublishDetail.setResult(Constant.PUBLIC_DETAIL_STATUS_CODE.RESULT_PENDING);
                sendingPublishDetail.setMessageId(zaloMessageResponse.getData().getMessage_id());
                sendingPublishDetailList.add(sendingPublishDetail);
            }

        } catch (SendingZaloMessage4xxException sendingZaloMessage4xxException) {
            log.info("sendingZaloMessage 4xx Exception");

            Gson gson = new Gson();
            ZaloMessageInvalidResponse zaloMessageInvalidResponse = gson.fromJson(sendingZaloMessage4xxException.getErrorBody(), ZaloMessageInvalidResponse.class);
            sendingPublishDetail.setResult(Constant.PUBLIC_DETAIL_STATUS_CODE.FAIL_SND_MSG);
            sendingPublishDetailList.add(sendingPublishDetail);

            PublishDetailModel publishDetailModel = new PublishDetailModel();
            publishDetailModel.setPublishStatusCode(EnumPublishDetailStatus.FAIL_SND_MSG);
            publishDetailModel.setPublishResultMessage(zaloMessageInvalidResponse.getCode() + " : " + zaloMessageInvalidResponse.getMessage());
            publishDetailModel.setId(incomingPublishMessage.getPublishDetailId());
            publishDetailList.add(publishDetailModel);
        } catch (SendingZaloMessageException sendingZaloMessageException) {
            log.info("sendingZaloMessage: Exception: errorBody:{}", sendingZaloMessageException.getErrorBody());
            sendingPublishDetail.setResult(Constant.PUBLIC_DETAIL_STATUS_CODE.FAIL_SND_MSG);
            sendingPublishDetailList.add(sendingPublishDetail);
            PublishDetailModel publishDetailModel = new PublishDetailModel();
            publishDetailModel.setPublishStatusCode(EnumPublishDetailStatus.FAIL_SND_MSG);
            publishDetailModel.setPublishResultMessage(sendingZaloMessageException.getErrorBody());
            publishDetailModel.setId(incomingPublishMessage.getPublishDetailId());
            publishDetailList.add(publishDetailModel);
        }
    }
}

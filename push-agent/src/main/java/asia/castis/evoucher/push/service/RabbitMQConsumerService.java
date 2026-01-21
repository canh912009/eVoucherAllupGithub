package asia.castis.evoucher.push.service;

import asia.castis.evoucher.push.common.utils.DateUtils;
import asia.castis.evoucher.push.components.PushCipher;
import asia.castis.evoucher.push.constant.Constant;
import asia.castis.evoucher.push.exception.SendingSmsMessage4xxException;
import asia.castis.evoucher.push.model.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RabbitMQConsumerService {

    private static final Gson gson = new Gson();
    private final RabbitMQProducerService rabbitMQProducerService;
    private final SmsMessageService smsMessageService;
    private final ZaloMessageService zaloMessageService;
    private final PublishService publishService;
    private final CheckPublishService checkPublishService;
    private final PushCipher pushCipher;

    @Autowired
    public RabbitMQConsumerService(RabbitMQProducerService rabbitMQProducerService, ZaloMessageService zaloMessageService,
                                   SmsMessageService smsMessageService, PublishService publishService,
                                   CheckPublishService checkPublishService, PushCipher pushCipher) {
        this.rabbitMQProducerService = rabbitMQProducerService;
        this.zaloMessageService = zaloMessageService;
        this.smsMessageService = smsMessageService;
        this.publishService = publishService;
        this.checkPublishService = checkPublishService;
        this.pushCipher = pushCipher;
    }

    @RabbitListener(queues = "${queue.in.name}")
    public void sendMessageQueue(IncomingPublish incomingPublish) {
        try {
            log.info("consume inQueue: incomingPublish:{}", gson.toJson(incomingPublish));
            log.info("consume inQueue: incomingPublish size: {}", incomingPublish.getTotalCount());
            Long publishId = incomingPublish.getPublishId();
            validationIncomingPublish(incomingPublish);

            List<IncomingPublishMessage> publishMessageList = incomingPublish.getPublishMessageList();
            List<Long> incomingPublishDetailIdList = publishMessageList.stream()
                    .map(IncomingPublishMessage::getPublishDetailId)
                    .collect(Collectors.toList());

            log.info("Get Publish info with publishId: {}", publishId);
            PublishModel existPublishModel = publishService.findById(publishId);
            log.info("Publish info: {}", existPublishModel);

            if (existPublishModel == null) {
                throw new Exception("Oop! can't find publish model "+publishId+ ".");
            }
            log.info("Exist publishModel with publishId {}", incomingPublish.getPublishId());
            log.info("Saved publish id {} for check later", publishId);

            existPublishModel.setPublishStatusCode(EnumPublishStatus.SENDING);

            List<PublishDetailModel> listPublishDetail = existPublishModel.getPublishDetails().stream()
                    .map(
                       publishDetailModel -> {
                          if (incomingPublishDetailIdList.contains(publishDetailModel.getId())) {
                                publishDetailModel.setPublishStatusCode(EnumPublishDetailStatus.STRT_SND_MSG);
                          }
                          return publishDetailModel;})
                    .collect(Collectors.toList());
            existPublishModel.setPublishDetails(listPublishDetail);

            log.info("Update publish with publish info: {}", existPublishModel);
            PublishModel savedPublishModel = publishService.save(existPublishModel);

            if (savedPublishModel == null) {
                throw new Exception("Oop! can't update publishStatusCode publish model "+publishId+ ".");
            }

            log.info("Create or Update check publish with publishId: {}", publishId);
            createOrUpdateCheckPublish(publishId);

            //start SENDING status in mq
            SendingPublish sendingPublishSTRT = new SendingPublish();
            sendingPublishSTRT.setPublishId(publishId);
            sendingPublishSTRT.setPublishStatus(Constant.PUBLIC_STATUS_CODE.SENDING);

            List<SendingPublishDetail> publishSendingDetailListSTRT = new ArrayList<>();
            List<IncomingPublishMessage> publishMessageListSTRT = incomingPublish.getPublishMessageList();
            for (IncomingPublishMessage ipmSTRT : publishMessageListSTRT) {
                SendingPublishDetail sendingPublishDetailSTRT = new SendingPublishDetail();
                sendingPublishDetailSTRT.setPublishDetailId(ipmSTRT.getPublishDetailId());
                sendingPublishDetailSTRT.setSmsType(incomingPublish.getSmsType());
                sendingPublishDetailSTRT.setResult(Constant.PUBLIC_DETAIL_STATUS_CODE.STRT_SND_MSG);
                publishSendingDetailListSTRT.add(sendingPublishDetailSTRT);
            }
            sendingPublishSTRT.setDetailStatus(publishSendingDetailListSTRT);

            List<SendingPublish> sendingPublishListSTRT = new ArrayList<>();
            sendingPublishListSTRT.add(sendingPublishSTRT);
            rabbitMQProducerService.sendingPublishList(sendingPublishListSTRT);
            //end SENDING status im mq

            SendingPublish sendingPublish = new SendingPublish();
            sendingPublish.setPublishId(incomingPublish.getPublishId());
            sendingPublish.setPublishStatus(Constant.PUBLIC_STATUS_CODE.WAIT_FOR_SEND_RESULT);
            log.info("Update status SENDING for publishId {} success", publishId);
            List<PublishDetailModel> incomingPublishDetailList = savedPublishModel.getPublishDetails();
            List<SendingPublishDetail> publishSendingDetailList = new ArrayList<>();
            if (incomingPublish.getSmsType().equals(Constant.MSG_TYPE.SMS)) {
                // process sms message type
                for (IncomingPublishMessage ipm : publishMessageList) {
                    String decyptedPhoneNumber = pushCipher.cipherDecrypt(ipm.getReceiverMobileNumber());
                    log.info("decypted phone number:{}", decyptedPhoneNumber);
                    if (StringUtils.isEmpty(smsMessageService.getSmsToken())) {
                        log.info("1.consume smsToken is null");
                        smsMessageService.requestSmsToken();
                        log.info("2. request smsToken when is null. smsToken: {}", smsMessageService.getSmsToken());
                    }

                    SendingPublishDetail sendingPublishDetail = new SendingPublishDetail();
                    sendingPublishDetail.setPublishDetailId(ipm.getPublishDetailId());
                    sendingPublishDetail.setSmsType(incomingPublish.getSmsType());

                    if (!StringUtils.isEmpty(smsMessageService.getSmsToken())) {
                        log.info("3. sending message with smsToken: {}", smsMessageService.getSmsToken());
                        sendingPublishDetail.setSendDatetime(DateUtils.getStrDate(Calendar.getInstance().getTime()));
                        smsMessageService.sendingSmsMessage(ipm, incomingPublish, incomingPublishDetailList, publishSendingDetailList, sendingPublishDetail);
                        log.info("4. Sms message is sended.");
                    } else {
                        log.info("Oop! Can't get sms token");
                        log.info("Sending message with publicDetailId {} fail.", ipm.getPublishDetailId());
                        sendingPublishDetail.setResult(Constant.PUBLIC_DETAIL_STATUS_CODE.FAIL_SND_MSG);
                        publishSendingDetailList.add(sendingPublishDetail);
                    }
                }
            } else if (incomingPublish.getSmsType().equals(Constant.MSG_TYPE.ZALO)) {
                // process zalo message type
                for (IncomingPublishMessage ipm : publishMessageList) {
                    String decyptedPhoneNumber = pushCipher.cipherDecrypt(ipm.getReceiverMobileNumber());
                    log.info("Zalo PublishMessage phone:{}", decyptedPhoneNumber);
                    log.info("Zalo PublishMessage message:{}", ipm.getMessage());
                    SendingPublishDetail sendingPublishDetail = new SendingPublishDetail();
                    sendingPublishDetail.setPublishDetailId(ipm.getPublishDetailId());
                    sendingPublishDetail.setSmsType(incomingPublish.getSmsType());
                    //sendingPublishDetail.setPhoneNumber(ipm.getReceiverMobileNumber());
                    sendingPublishDetail.setSendDatetime(DateUtils.getStrDate(Calendar.getInstance().getTime()));
                    zaloMessageService.sendingZaloMessage(ipm, incomingPublishDetailList, publishSendingDetailList, sendingPublishDetail);
                }
            }

            savedPublishModel.setPublishStatusCode(EnumPublishStatus.WAIT_FOR_SEND_RESULT);

            log.info("IncomingPublishDetailList info: {}", gson.toJson(incomingPublishDetailList));
            Map<Long, PublishDetailModel> incomingPublishDetailMap = incomingPublishDetailList.stream()
                    .collect(Collectors.toMap(PublishDetailModel::getId, publishDetailModel -> publishDetailModel));

            log.info("Map IncomingPublishDetailList to ListPublishDetail with listPublishDetail: {}", gson.toJson(listPublishDetail));
            List<PublishDetailModel> result = listPublishDetail.stream()
                    .map(
                       publishDetailModel ->
                          incomingPublishDetailMap.getOrDefault(publishDetailModel.getId(), publishDetailModel))
                    .collect(Collectors.toList());
            savedPublishModel.setPublishDetails(result);

            log.info("Update Publish detail result: {}", savedPublishModel);
            PublishModel publishModelUpdate02 = publishService.save(savedPublishModel);
            if (publishModelUpdate02 != null) {
                //update WAIT_FOR_SENDING_RESULT success
                log.info("After sending all message, Update publishStatusCode WAIT_FOR_SEND_RESULT for {} success.", savedPublishModel.getId());
            } else {
                log.info("After sending all message, Update publishStatusCode WAIT_FOR_SEND_RESULT for {} fail.", savedPublishModel.getId());
            }
            log.info("publishSendingDetailList size: {}", publishSendingDetailList.size());
            sendingPublish.setDetailStatus(publishSendingDetailList);
            List<SendingPublish> sendingPublishList = new ArrayList<>();
            sendingPublishList.add(sendingPublish);
            log.info("sendingPublishList object: {}", sendingPublishList);
            rabbitMQProducerService.sendingPublishList(sendingPublishList);
        } catch (Exception e) {
           log.error(e.getMessage(), e);
        }
    }
    // validaation IncomingPublish
    private void validationIncomingPublish(IncomingPublish incomingPublish) throws Exception {
        if(incomingPublish == null) {
            throw new Exception("incomingPublish is null");
        }
        if(incomingPublish.getPublishId() == null) {
            throw new Exception("publishId of incomingPublish null");
        }
        if(incomingPublish.getSmsType() == null) {
            throw new Exception("incomingPublish need message type SMS or ZALO");
        } else if(!incomingPublish.getSmsType().equals(Constant.MSG_TYPE.SMS) && !incomingPublish.getSmsType().equals(Constant.MSG_TYPE.ZALO)) {
            throw new Exception("incomingPublish support only message type SMS or ZALO");
        }
        if(incomingPublish.getPublishMessageList() == null) {
            throw new Exception("list publishMessage of incommingPublish is null");
        } else if (incomingPublish.getPublishMessageList().size() == 0) {
            throw new Exception("list publishMessage of incommingPublish is empty");
        } else {
            // publishMessageList has size > 0
            List<IncomingPublishMessage> incomingPublishMessageList = incomingPublish.getPublishMessageList();
            // loop through and validation PublishMessage
            int icpmIndex = 1;
            for(IncomingPublishMessage icpm : incomingPublishMessageList) {
                log.info("Validation IncomingPublishMessage {} of IncomingPublish", icpmIndex);
                validateIncomingPublishMessage(icpm);
                icpmIndex++;
            }
        }
        if(incomingPublish.getBrandName() == null || incomingPublish.getBrandName() =="") {
            if(incomingPublish.getSmsType() == Constant.MSG_TYPE.SMS) {
                throw new Exception("incomingPublish need brandName to sending sms message");
            }
        }
        if(incomingPublish.getTemplateId() == null || incomingPublish.getTemplateId() == "") {
            if(incomingPublish.getSmsType() == Constant.MSG_TYPE.ZALO) {
                throw new Exception("incomingPublish need temmplateId to sending zalo message");
            }
        }
    }
    private void validateIncomingPublishMessage(IncomingPublishMessage incomingPublishMessage) throws Exception {
        if(incomingPublishMessage == null) {
            throw new Exception("incomingPublishMessage null");
        }
        if(incomingPublishMessage.getPublishDetailId() == null) {
            throw new Exception("publishDetailId of incomingPublishMessage null");
        }

        if(incomingPublishMessage.getReceiverMobileNumber() == null || incomingPublishMessage.getReceiverMobileNumber() == "") {
            throw new Exception("receiverMobileNumber of incomingPublishMessage null or empty");
        }

        if(incomingPublishMessage.getMessage() == null || incomingPublishMessage.getMessage() == "") {
            throw new Exception("message of incomingPublishMessage null or empty");
        }
    }

    @RabbitListener(queues = "${queue.send.otp.name}")
    public void sendOtpConsume(IncomingMessage incomingMessage) {
        try {
            log.info("Send OTP request: {}", gson.toJson(incomingMessage));
            if (StringUtils.isBlank(incomingMessage.getPhoneNumber())) {
                throw new RuntimeException("Phone number is empty");
            }
            if (StringUtils.isBlank(incomingMessage.getMessage())) {
                throw new RuntimeException("Message is empty");
            }
            if (StringUtils.isBlank(incomingMessage.getBrandName())) {
                throw new RuntimeException("Brand name is empty");
            }

            if (StringUtils.isEmpty(smsMessageService.getSmsToken())) {
                log.info("Request smsToken");
                smsMessageService.requestSmsToken();
                log.info("SmsToken: {}", smsMessageService.getSmsToken());
            }

            smsMessageService.sendingSmsMessage(incomingMessage);
        } catch (SendingSmsMessage4xxException error) {
            log.info("sendingSmsMessage: 4xx Exception: errorBody:{}", error.getErrorBody());
            SmsSendingErrorResponse smsSendingErrorResponse = gson.fromJson(
                    error.getErrorBody(), SmsSendingErrorResponse.class);

            if (smsSendingErrorResponse.getError() == Constant.SMS_ERROR_CODE.ACCESS_TOKEN_EXPIRED) {
                log.info("Re Request smsToken");
                smsMessageService.requestSmsToken();

                log.info("Re send Sms message");
                smsMessageService.sendingSmsMessage(incomingMessage);
            }
        } catch (Exception e) {
            log.error("Error send OTP request: {}", e.getMessage(), e);
        }
    }

    private void createOrUpdateCheckPublish(Long publishId) throws Exception {
        log.info("Find CheckPublish with publishId: {}", publishId);
        CheckPublish checkPublish = checkPublishService.findByPublishId(publishId);
        if (ObjectUtils.isEmpty(checkPublish)) {
            checkPublish = new CheckPublish();
            checkPublish.setPublishId(publishId);
        }
        checkPublish.setDone(false);
        CheckPublish savedCheckPublish = checkPublishService.save(checkPublish);
        if (ObjectUtils.isEmpty(savedCheckPublish)) {
            throw new Exception("Can't save publish id "+ publishId +" for check later ");
        }
    }
}
package asia.castis.evoucher.push.scheduler;

import asia.castis.evoucher.push.common.utils.DateUtils;
import asia.castis.evoucher.push.constant.Constant;
import asia.castis.evoucher.push.model.*;
import asia.castis.evoucher.push.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.google.gson.Gson;
import org.springframework.util.CollectionUtils;

@Component
@Slf4j
public class PushAgentScheduler {
    //private PAMessageService service;
    private PublishService publishService;
    private CheckPublishService checkPublishService;
    private PAMessageCallBackSmsService paMessageCallBackSmsService;
    private PAMessageCallBackZaloService paMessageCallBackZaloService;
    //private static List<Long> listPublishId = new ArrayList<Long>();
    //private List<Long> listPublicIdSendingCompleted = new ArrayList<>();

    /*public static List<Long> getListPublishId() {
        return listPublishId;
    }
    */
    @Value("${queue.out.exchange}")
    private String exchange;
    @Value("${queue.out.routingKey}")
    private String routingKey;

    private RabbitMQProducerService sendingPublishService;

    public PushAgentScheduler(PublishService publishService, CheckPublishService checkPublishService, PAMessageCallBackSmsService paMessageCallBackSmsService, PAMessageCallBackZaloService paMessageCallBackZaloService, RabbitMQProducerService sendingInquiryService) {
        //this.service = service;
        this.publishService = publishService;
        this.paMessageCallBackSmsService = paMessageCallBackSmsService;
        this.paMessageCallBackZaloService = paMessageCallBackZaloService;
        this.sendingPublishService = sendingInquiryService;
        this.checkPublishService = checkPublishService;
    }

    @Scheduled(cron = "${schedule.cron}")
    public void sendingInquiryTask() {
        // To assert that the lock is held (prevents misconfiguration errors)
        List<CheckPublish> listCheckPublishNotDone = checkPublishService.getListCheckPublishNotDone();
        if (listCheckPublishNotDone != null) {
            log.info("sendingInquiryTask publish size: {}", listCheckPublishNotDone.size());
            List<SendingPublish> sendingPublishList = new ArrayList<>();
            // find by condition
            for (int i = 0; i < listCheckPublishNotDone.size(); i++) {
                CheckPublish checkPublish = listCheckPublishNotDone.get(i);
                Long currPublishId = checkPublish.getPublishId();
                log.info("schedule processing for publishId {}", currPublishId);
                PublishModel publishModel = publishService.findById(currPublishId);
                if (publishModel != null) {
                    List<PublishDetailModel> publishDetailModelList = publishModel.getPublishDetails();
                    SendingPublish sendingPublish = new SendingPublish();
                    sendingPublish.setPublishId(currPublishId);
                    sendingPublish.setPublishStatus(Constant.PUBLIC_STATUS_CODE.WAIT_FOR_SEND_RESULT);
                    List<SendingPublishDetail> sendingPublishDetailList = new ArrayList<>();
                    boolean dlrDone = true;
                    if(publishDetailModelList.size()<1) {
                        continue;
                    }
                    for (PublishDetailModel pdm : publishDetailModelList) {
                        log.info("schedule processing for PublishDetail {} of publishId {}",pdm.getId(), currPublishId);
                        if(pdm.getPublishStatusCode() == EnumPublishDetailStatus.RESULT_PENDING) {
                            log.info("publish id {} still have publish detail model status RESULT_PENDING, so dlrDone false", currPublishId);
                            dlrDone = false;
                        }

                        if (pdm.getMessageId() != null) {
                            log.info("publishDetailModel messageId {}", pdm.getMessageId());
                            SendingPublishDetail sendingPublishDetail = new SendingPublishDetail();
                            sendingPublishDetail.setPublishDetailId(pdm.getId());
                            sendingPublishDetail.setSmsType(publishModel.getSmsType());
                            sendingPublishDetail.setResult(pdm.getPublishStatusCode().getValue());

                            //
                            if (publishModel.getSmsType().equals(Constant.MSG_TYPE.SMS)) {
                                PAMessageCallBackSms paMessageCallBackSms = paMessageCallBackSmsService.findByMessageId(pdm.getMessageId());
                                if (paMessageCallBackSms != null) {
                                    // it exist so update it status
                                    if (paMessageCallBackSms.getStatus() == Constant.MSG_SENDING_STATUS.FAIL) {
                                        pdm.setPublishStatusCode(EnumPublishDetailStatus.RESULT_FAIL);
                                        sendingPublishDetail.setResult(Constant.PUBLIC_DETAIL_STATUS_CODE.RESULT_FAIL);
                                        sendingPublishDetail.setSendResultDatetime(DateUtils.getStrDate(Calendar.getInstance().getTime()));
                                    } else if (paMessageCallBackSms.getStatus() == Constant.MSG_SENDING_STATUS.SUCCESS) {
                                        pdm.setPublishStatusCode(EnumPublishDetailStatus.RESULT_SUCCESS);
                                        sendingPublishDetail.setResult(Constant.PUBLIC_DETAIL_STATUS_CODE.RESULT_SUCCESS);
                                        sendingPublishDetail.setSendResultDatetime(DateUtils.getStrDate(Calendar.getInstance().getTime()));
                                    }
                                }
                            } else if (publishModel.getSmsType().equals(Constant.MSG_TYPE.ZALO)) {
                                PAMessageCallBackZalo paMessageCallBackZalo = paMessageCallBackZaloService.findByMessageId(pdm.getMessageId());
                                if (paMessageCallBackZalo != null) {
                                    if (paMessageCallBackZalo.getStatus() == Constant.MSG_SENDING_STATUS.ZALO_FAIL) {
                                        pdm.setPublishStatusCode(EnumPublishDetailStatus.RESULT_FAIL);
                                        sendingPublishDetail.setResult(Constant.PUBLIC_DETAIL_STATUS_CODE.RESULT_FAIL);
                                        sendingPublishDetail.setSendResultDatetime(DateUtils.getStrDate(Calendar.getInstance().getTime()));
                                    } else if (paMessageCallBackZalo.getStatus() == Constant.MSG_SENDING_STATUS.SUCCESS) {
                                        pdm.setPublishStatusCode(EnumPublishDetailStatus.RESULT_SUCCESS);
                                        sendingPublishDetail.setResult(Constant.PUBLIC_DETAIL_STATUS_CODE.RESULT_SUCCESS);
                                        sendingPublishDetail.setSendResultDatetime(DateUtils.getStrDate(Calendar.getInstance().getTime()));
                                    }
                                }
                            } else {
                                log.info("Op we currently only handle 2 type sms and zalo.");
                            }
                            sendingPublishDetailList.add(sendingPublishDetail);
                        } else {
                            // may schedule check before message sending to gateway ?
                            // how about dlr fail to get dlr result
                            log.info("getMessageId null because of 2 main reason:");
                            log.info("1. dlr fail to get dlr result");
                            log.info("1. dlr come later");
                        }
                    }
                    if(dlrDone == true) {
                        log.info("Publish with id {} completed", currPublishId);
                        sendingPublish.setPublishStatus(Constant.PUBLIC_STATUS_CODE.FINISHED);
                        publishModel.setPublishStatusCode(EnumPublishStatus.FINISHED);
                        checkPublish.setDone(true);
                        CheckPublish updatedCheckPublish = checkPublishService.save(checkPublish);
                        if(updatedCheckPublish!=null) {
                            log.info("Update CheckPublish for publishId {} successful", checkPublish.getPublishId());
                        } else {
                            log.info("Update CheckPublish for publishId {} fail", checkPublish.getPublishId());
                        }
                    }
                    log.info("size of sendingPublishDetailList is {}", sendingPublishDetailList.size());
                    sendingPublish.setDetailStatus(sendingPublishDetailList);
                    publishModel.setPublishDetails(publishDetailModelList);
                    PublishModel updatedPublishModel = publishService.save(publishModel);
                    if (updatedPublishModel != null) {
                        //
                        log.info("Shedule update publish model id {} success.", updatedPublishModel.getId());
                    } else {

                        log.info("Shedule update publish model id {} fail.", updatedPublishModel.getId());
                    }
                    // sending List sendingPublish all at once
                    sendingPublishList.add(sendingPublish);
                } else {
                    //??
                    // need to throw exception because we need update before doing other thing
                    log.info("Oop! Can find publish with id {}", currPublishId);
                }
            }
            Gson gson = new Gson();
            log.info("SendingPublishList: {}",gson.toJson(sendingPublishList));
            if(!CollectionUtils.isEmpty(sendingPublishList)) {
                sendingPublishService.sendingPublishList(sendingPublishList);
            }
        } else {
            log.info("There is no publish need to check.");
        }

    }

}

package asia.castis.evoucher.push.callback_receive.zalo.controller;

import asia.castis.evoucher.push.callback_receive.zalo.constant.Constant;
import asia.castis.evoucher.push.callback_receive.zalo.model.PAMessage;
import asia.castis.evoucher.push.callback_receive.zalo.model.PAMessageCallBackZalo;
import asia.castis.evoucher.push.callback_receive.zalo.model.ZaloReceive;
import asia.castis.evoucher.push.callback_receive.zalo.model.ZaloResponse;
import asia.castis.evoucher.push.callback_receive.zalo.service.PAMessageCallBackZaloService;
import asia.castis.evoucher.push.callback_receive.zalo.service.PAMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ZaloCallbackReceiveController {

    private PAMessageCallBackZaloService paMessageCallBackZaloService;
    public ZaloCallbackReceiveController(PAMessageCallBackZaloService paMessageCallBackZaloService) {
        this.paMessageCallBackZaloService = paMessageCallBackZaloService;
    }

    @PostMapping("/callback_receive")
    public ZaloResponse callbackReceive(@RequestBody ZaloReceive zaloReceive) {
        log.info("callbackReceive:ZaloReceive:{}", zaloReceive);
        ZaloResponse zaloResponse = new ZaloResponse();
        zaloResponse.setStatus(1);
        if(zaloReceive == null) {
            log.info("Oop! request body null");
            zaloResponse.setStatus(0);
            return zaloResponse;
        }else {
            log.info("ZaloReceive: sms_id: {}, status: {}, type:{}", zaloReceive.getMsg_id(), zaloReceive.getStatus(), zaloReceive.getType());
            if(zaloReceive.getMsg_id() != null) {
                // update PAMessageCallBackZalo if exist
                PAMessageCallBackZalo existPAMessageCallBackZalo = paMessageCallBackZaloService.findByMessageId(zaloReceive.getMsg_id());

                if(existPAMessageCallBackZalo == null) {
                    // create new if first time
                    PAMessageCallBackZalo newPAMessageCallBackZalo = new PAMessageCallBackZalo();
                    newPAMessageCallBackZalo.setMessageId(zaloReceive.getMsg_id());
                    newPAMessageCallBackZalo.setStatus(zaloReceive.getStatus());
                    newPAMessageCallBackZalo.setSentTime(zaloReceive.getSent_time());
                    newPAMessageCallBackZalo.setReceiveTime(zaloReceive.getReceived_time());
                    newPAMessageCallBackZalo.setError(zaloReceive.getError());
                    newPAMessageCallBackZalo.setErrorInfo(zaloReceive.getError_info());
                    newPAMessageCallBackZalo.setType(zaloReceive.getType());
                    PAMessageCallBackZalo savedNewPAMessageCallBackZalo = paMessageCallBackZaloService.save(newPAMessageCallBackZalo);
                    if(savedNewPAMessageCallBackZalo!=null) {
                        // save success
                        log.info("Save new Callback Zalo message with message id {} success.", zaloReceive.getMsg_id());
                    }else {
                        // save not success, what to do next
                        log.info("Save new Callback Zalo message with message id {} fail.", zaloReceive.getMsg_id());
                    }
                }else {// exist so we will update PAMessageCallBackZalo
                    existPAMessageCallBackZalo.setStatus(zaloReceive.getStatus());
                    existPAMessageCallBackZalo.setReceiveTime(zaloReceive.getReceived_time());
                    existPAMessageCallBackZalo.setSentTime(zaloReceive.getSent_time());
                    existPAMessageCallBackZalo.setError(zaloReceive.getError());
                    existPAMessageCallBackZalo.setErrorInfo(zaloReceive.getError_info());
                    PAMessageCallBackZalo savedExistMessageCallBackZalo = paMessageCallBackZaloService.save(existPAMessageCallBackZalo);
                    if(savedExistMessageCallBackZalo!=null) {
                        log.info("update existing PAMessageCallBackZalo with message id {} success.", zaloReceive.getMsg_id());
                    }else {
                        //update exist PAMessageCallBackZalo fail
                        log.info("update existing PAMessageCallBackZalo with message id {} fail.", zaloReceive.getMsg_id());
                    }
                }
            } else {
                log.info("Oop, ZaloReceive has no msg_id, is invalid");
            }
        }
        return zaloResponse;
    }
}

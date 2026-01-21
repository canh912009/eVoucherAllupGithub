package asia.castis.evoucherservicefe.publishrequest.service.impl;

import asia.castis.evoucherservicefe.common.dto.createmessageresult.DetailResult;
import asia.castis.evoucherservicefe.common.dto.createmessageresult.OutgoingPublishResult;
import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.PublishDetail;
import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.RequestFromBE;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.PublishMessage;
import asia.castis.evoucherservicefe.common.enums.EnumPublishDetailStatus;
import asia.castis.evoucherservicefe.common.enums.EnumPublishStatus;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.common.model.publish.PublishDetailModel;
import asia.castis.evoucherservicefe.common.model.publish.PublishModel;
import asia.castis.evoucherservicefe.common.utils.DateUtils;
import asia.castis.evoucherservicefe.exceptions.SendMessageToQueueException;
import asia.castis.evoucherservicefe.publishrequest.sender.PublishRequestSender;
import asia.castis.evoucherservicefe.publishrequest.service.MessageResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageResultServiceImpl implements MessageResultService {
    public static final String SUCCESS_MESSAGE = "SUCCESS";

    private final PublishRequestSender publishRequestSender;


    @Override
    public void sendAllFailMessages(RequestFromBE incomingPublish, Date startProcessingTime) {
        try {
            log.info("Sending FAIL_GENERATING result for the whole publish");
            OutgoingPublishResult failResult = makeFailResult(incomingPublish, startProcessingTime);
            reportResult(failResult);
        } catch (Exception e) {
            log.error("Handle exception faced exception. Msg={}", e.getMessage());
        }
    }

    @Override
    public void sendStartGenMessage(RequestFromBE request, List<VoucherModel> parseVouchers,
                                    Date startProcessingTime, EnumPublishStatus publishStatus) throws SendMessageToQueueException {
        // Prepare detail results list
        List<DetailResult> detailResults =
                request.getPublishDetails().stream().map(
                        publishDetail -> DetailResult.builder()
                                .publishDetailId(publishDetail.getId())
                                .smsType(request.getSmsType())
                                .receivedTime(DateUtils.toDateTimeString(startProcessingTime))
                                .sendDatetime(DateUtils.getCurrentDateTimeString())
                                .sendResultDatetime(DateUtils.getCurrentDateTimeString())
                                .phoneNumber(getUserMobileNumber(parseVouchers, publishDetail))
                                .result(EnumPublishDetailStatus.STRT_GEN_MSG)
                                .build()
                ).collect(Collectors.toList());

        // Make outgoing Publish result
        OutgoingPublishResult startGenMsg = OutgoingPublishResult.builder()
                .publishId(request.getId())
                .publishStatus(publishStatus)
                .detailStatus(detailResults)
                .build();

        reportResult(startGenMsg);
    }

    private static String getUserMobileNumber(List<VoucherModel> parseVouchers, PublishDetail publishDetail) {
        VoucherModel voucherModel = parseVouchers.stream().filter(v -> v.getPublishDetailId().equals(publishDetail.getId())).findFirst().orElse(null);
        return Objects.isNull(voucherModel) ? "" : voucherModel.getUserMobileNumber();
    }

    private static String getUserMobileNumber(List<VoucherModel> parseVouchers, PublishDetailModel publishDetail) {
        VoucherModel voucherModel = parseVouchers.stream().filter(v -> v.getPublishDetailId().equals(publishDetail.getId())).findFirst().orElse(null);
        return Objects.isNull(voucherModel) ? "" : voucherModel.getUserMobileNumber();
    }

    @Override
    public void sendFinishResult(PublishModel publishModel, List<VoucherModel> parseVouchers,
                                 Date startProcessingTime) throws SendMessageToQueueException {
        // Prepare detail results list
        List<DetailResult> detailResults =
                publishModel.getPublishDetails().stream().map(
                        publishDetail -> DetailResult.builder()
                                .publishDetailId(publishDetail.getId())
                                .smsType(publishModel.getSmsType().getValue())
                                .receivedTime(DateUtils.toDateTimeString(startProcessingTime))
                                .sendDatetime(DateUtils.getCurrentDateTimeString())
                                .sendResultDatetime(DateUtils.getCurrentDateTimeString())
                                .phoneNumber(getUserMobileNumber(parseVouchers, publishDetail))
                                .result(EnumPublishDetailStatus.RESULT_SUCCESS)
                                .build()
                ).collect(Collectors.toList());

        // Make outgoing Publish result
        OutgoingPublishResult startGenMsg = OutgoingPublishResult.builder()
                .publishId(publishModel.getId())
                .publishStatus(publishModel.getPublishStatusCode())
                .detailStatus(detailResults)
                .build();

        reportResult(startGenMsg);
    }

    @Override
    public void sendEndGenResult(RequestFromBE incomingPublish, List<VoucherModel> parsedVouchers, Date startProcessingTime, Map<VoucherModel, PublishMessage> publishMessageMap, PublishModel processingPublishModel) throws SendMessageToQueueException {
        OutgoingPublishResult endGenerateResult = makeEndGenResult(incomingPublish,
                parsedVouchers,
                publishMessageMap,
                startProcessingTime,
                processingPublishModel.getPublishStatusCode());
        reportResult(endGenerateResult);
    }

    @Override
    public void sendEmailResult(RequestFromBE request, Map<VoucherModel, String> emailSendingResult, Date startProcessingTime) throws SendMessageToQueueException {
        OutgoingPublishResult sendMailResult = makeSendMailResult(request, emailSendingResult, startProcessingTime);
        reportResult(sendMailResult);
    }


    private void reportResult(OutgoingPublishResult result) throws SendMessageToQueueException {
        log.info("Start sending create message result, publishId={}", result.getPublishId());
        publishRequestSender.sendCreateMessageResult(result);
        log.info("Finish sending create message result");
    }

    private OutgoingPublishResult makeSendMailResult(RequestFromBE request,
                                                     Map<VoucherModel, String> sendMailResultMap,
                                                     Date startProcessingTime) {
        // Prepare detail results list
        List<DetailResult> detailResults = new ArrayList<>();
        for (Map.Entry<VoucherModel, String> entry : sendMailResultMap.entrySet()) {
            VoucherModel voucher = entry.getKey();
            String resultMessage = entry.getValue();

            DetailResult detailResult = new DetailResult();
            detailResult.setPublishDetailId(voucher.getPublishDetailId());
            detailResult.setSmsType(request.getSmsType());
            detailResult.setReceivedTime(DateUtils.toDateTimeString(startProcessingTime));
            detailResult.setSendDatetime(DateUtils.getCurrentDateTimeString());
            detailResult.setSendResultDatetime(DateUtils.getCurrentDateTimeString());
            detailResult.setEmail(voucher.getUserEmail());

            boolean success = Objects.nonNull(resultMessage)
                    && resultMessage.equalsIgnoreCase(SUCCESS_MESSAGE);
            if (success) {
                detailResult.setResult(EnumPublishDetailStatus.RESULT_SUCCESS);
            } else {
                detailResult.setResult(EnumPublishDetailStatus.RESULT_FAIL);
                detailResult.setMessage(resultMessage);
            }
            detailResults.add(detailResult);
        }

        // Make outgoing Publish result
        return OutgoingPublishResult.builder()
                .publishId(request.getId())
                .publishStatus(EnumPublishStatus.FINISHED)
                .detailStatus(detailResults)
                .build();
    }

    private OutgoingPublishResult makeEndGenResult(RequestFromBE request,
                                                   List<VoucherModel> parsedVouchers,
                                                   Map<VoucherModel, PublishMessage> msgCreationMap,
                                                   Date startProcessingTime,
                                                   EnumPublishStatus publishStatus) {
        // Prepare detail results list
        List<DetailResult> detailResults = new ArrayList<>();
        for (VoucherModel voucher : parsedVouchers) {
            DetailResult detailResult = new DetailResult();
            detailResult.setPublishDetailId(voucher.getPublishDetailId());
            detailResult.setSmsType(request.getSmsType());
            detailResult.setReceivedTime(DateUtils.toDateTimeString(startProcessingTime));
            detailResult.setSendDatetime(DateUtils.getCurrentDateTimeString());
            detailResult.setSendResultDatetime(DateUtils.getCurrentDateTimeString());

            boolean success = msgCreationMap.keySet().stream().anyMatch(successVoucher -> successVoucher.getId().equals(voucher.getId()));
            if (success) {
                detailResult.setResult(EnumPublishDetailStatus.END_GEN_MSG);
                detailResult.setPhoneNumber(voucher.getUserMobileNumber());
            } else {
                detailResult.setResult(EnumPublishDetailStatus.FAIL_GEN_MSG);
            }
            detailResults.add(detailResult);
        }

        // Make outgoing Publish result
        return OutgoingPublishResult.builder()
                .publishId(request.getId())
                .publishStatus(publishStatus)
                .detailStatus(detailResults)
                .build();
    }

    private OutgoingPublishResult makeFailResult(RequestFromBE incomingPublish, Date startProcessingTime) {
        List<DetailResult> detailResults = new ArrayList<>();
        for (PublishDetail publishDetail : incomingPublish.getPublishDetails()) {
            DetailResult result = new DetailResult();
            result.setPublishDetailId(publishDetail.getId());
            result.setResult(EnumPublishDetailStatus.FAIL_GEN_MSG);
            result.setPhoneNumber(publishDetail.getVoucher().getUserMobileNumber());
            result.setSmsType(publishDetail.getSmsType());

            result.setSendResultDatetime(DateUtils.getCurrentDateTimeString());
            result.setSendDatetime(DateUtils.getCurrentDateTimeString());
            result.setReceivedTime(DateUtils.toDateTimeString(startProcessingTime));
            detailResults.add(result);
        }

        return OutgoingPublishResult.builder()
                .publishId(incomingPublish.getId())
                .publishStatus(EnumPublishStatus.FAIL_GENERATING)
                .detailStatus(detailResults)
                .build();
    }
}

package asia.castis.evoucherservicefe.publishrequest.service.impl;

import asia.castis.evoucherservicefe.common.dto.messagetemplate.MessageTemplate;
import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.PublishDetail;
import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.RequestFromBE;
import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.Voucher;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.PublishMessage;
import asia.castis.evoucherservicefe.common.dto.publishreq.outgoing.RequestToPushAgent;
import asia.castis.evoucherservicefe.common.enums.EnumMessageType;
import asia.castis.evoucherservicefe.common.enums.EnumPublishDetailStatus;
import asia.castis.evoucherservicefe.common.enums.EnumPublishStatus;
import asia.castis.evoucherservicefe.common.enums.EnumPublishType;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.common.model.publish.PublishDetailModel;
import asia.castis.evoucherservicefe.common.model.publish.PublishModel;
import asia.castis.evoucherservicefe.common.service.EsPublishService;
import asia.castis.evoucherservicefe.common.utils.CryptoUtils;
import asia.castis.evoucherservicefe.common.utils.DateUtils;
import asia.castis.evoucherservicefe.common.utils.ValidateResult;
import asia.castis.evoucherservicefe.exceptions.InvalidException;
import asia.castis.evoucherservicefe.exceptions.NotFoundException;
import asia.castis.evoucherservicefe.exceptions.ParseRequestException;
import asia.castis.evoucherservicefe.exceptions.SendMessageToQueueException;
import asia.castis.evoucherservicefe.publishrequest.sender.PublishRequestSender;
import asia.castis.evoucherservicefe.publishrequest.service.MessageResultService;
import asia.castis.evoucherservicefe.publishrequest.service.MessageService;
import asia.castis.evoucherservicefe.publishrequest.service.PublishRequestService;
import asia.castis.evoucherservicefe.publishrequest.utils.PublishValidator;
import asia.castis.evoucherservicefe.voucherhandler.service.VoucherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static asia.castis.evoucherservicefe.publishrequest.service.impl.MessageResultServiceImpl.SUCCESS_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublishRequestServiceImpl implements PublishRequestService {

    @Value("${push.zalo.templateId}")
    public String templateId;
    @Value("${push.brandName}")
    public String pushBrandName;

    @Value("${email.template}")
    public String emailTemplate;

    private final VoucherService voucherService;
    private final EsPublishService esPublishService;
    private final MessageService smsMessageService;
    private final PublishRequestSender publishRequestSender;
    private final ModelMapper modelMapper;
    private final PublishValidator validator;
    private final EmailService emailService;
    private final MessageResultService messageResultService;

    @Override
    public void incomingPublishHandling(RequestFromBE incomingPublish, Date startProcessingTime) {
        if (Objects.isNull(incomingPublish) || Objects.isNull(incomingPublish.getId())) {
            log.error("Null publish data received, can not process any further");
            return;
        }
        ValidateResult validateResult = validator.validate(incomingPublish);
        if (!validateResult.isValid()) {
            log.error("Invalid publish info received, drop this publish message, msg={}", validateResult.getValidationMessage());
            messageResultService.sendAllFailMessages(incomingPublish, startProcessingTime);
            return;
        }
        PublishModel publishModelFromQueue;
        List<VoucherModel> parsedVouchers;
        try {
            publishModelFromQueue = makePublishModel(incomingPublish);
            parsedVouchers = parseVouchers(incomingPublish);
        } catch (ParseRequestException e) {
            log.error(e.getMessage(), e);
            handleParseException(incomingPublish, startProcessingTime, e);
            return;
        }

        if (EnumSet.of(EnumPublishType.NORMAL, EnumPublishType.RESEND).contains(publishModelFromQueue.getType())) {
            publishVoucherNormally(incomingPublish, publishModelFromQueue, parsedVouchers, startProcessingTime);
        } else if (publishModelFromQueue.getType() == EnumPublishType.TRANSFER) {
            transferVoucher(incomingPublish, publishModelFromQueue, parsedVouchers, startProcessingTime);
        }
    }

    private void transferVoucher(RequestFromBE incomingPublish,
                                 PublishModel processingPublishModel,
                                 List<VoucherModel> parsedVouchers,
                                 Date startProcessingTime) {
        log.info("Start transfer publish={}", incomingPublish.getId());
        PublishModel origPublish = null;
        try {
            origPublish = getOrigPublish(processingPublishModel.getId());
            if (Objects.nonNull(origPublish)) { // Need to combine the transferred publish detail with the existing publish detail
                processingPublishModel.getPublishDetails().addAll(origPublish.getPublishDetails());
                processingPublishModel.setActivationUrl(origPublish.getActivationUrl());
                processingPublishModel.setActivationId(origPublish.getActivationId());
                log.info("Added {} old detail to publish detail list", origPublish.getPublishDetails());
            }
            processingPublishModel = savePublishModel(processingPublishModel);
            voucherService.saveVouchers(parsedVouchers);

            messageResultService.sendStartGenMessage(incomingPublish, parsedVouchers, startProcessingTime, processingPublishModel.getPublishStatusCode());
            Map<VoucherModel, PublishMessage> publishMessageMap = createPublishMessages(incomingPublish.getCampaign().getMessageTemplate(),
                    processingPublishModel,
                    parsedVouchers);
            savePushAgentRequest(publishMessageMap);

            updatePublishStatusForTransfer(processingPublishModel, publishMessageMap);
            messageResultService.sendEndGenResult(incomingPublish, parsedVouchers, startProcessingTime, publishMessageMap, processingPublishModel);

            // 4. Send message to Push Agent
            RequestToPushAgent requestToPushAgent = makeRequestToPushAgentObj(incomingPublish, new ArrayList<>(publishMessageMap.values()));
            sendToPushAgent(requestToPushAgent);
            log.info("Finish transfer publish={}", incomingPublish.getId());
        } catch (Exception e) {
            log.error("Transfer voucher exception", e);
            messageResultService.sendAllFailMessages(incomingPublish, startProcessingTime);
            try {
                log.info("Start rolling back for transfer publishId={} and related vouchers", incomingPublish.getId());
                esPublishService.rollback(incomingPublish.getId(), origPublish);
                Set<String> tobeDeleteVouchers = incomingPublish.getPublishDetails().stream().map(detail -> detail.getVoucher().getId()).collect(Collectors.toSet());
                voucherService.delete(tobeDeleteVouchers);
            } catch (Exception ex) {
                log.error("Exception while rolling back", ex);
            }
        }
    }

    private void publishVoucherNormally(RequestFromBE incomingPublish,
                                        PublishModel publishModelFromQueue,
                                        List<VoucherModel> parsedVouchers,
                                        Date startProcessingTime) {
        log.info("Start handling normal publish={}", incomingPublish.getId());
        try {
            PublishModel processingPublishModel = savePublishModel(publishModelFromQueue);
            voucherService.saveVouchers(parsedVouchers);

            messageResultService.sendStartGenMessage(incomingPublish, parsedVouchers, startProcessingTime, processingPublishModel.getPublishStatusCode());

            EnumMessageType requestMsgType = EnumMessageType.getEnum(incomingPublish.getSmsType());
            Map<VoucherModel, PublishMessage> publishMessageMap = createPublishMessages(incomingPublish.getCampaign().getMessageTemplate(),
                    processingPublishModel,
                    parsedVouchers);
            savePushAgentRequest(publishMessageMap);
            if (EnumSet.of(EnumMessageType.DOWNLOAD, EnumMessageType.PAPER).contains(requestMsgType)) {
                log.info("SMS Type = {}. No SMS sending phase, just sync data between FE & BE. Start sending result back", requestMsgType);
                // Update publish status
                processingPublishModel.setPublishStatusCode(EnumPublishStatus.FINISHED);
                savePublishModel(processingPublishModel);
                messageResultService.sendFinishResult(publishModelFromQueue, parsedVouchers, startProcessingTime);
                return;
            } else if (EnumMessageType.SMS.equals(requestMsgType)) {
                updatePublishStatus(processingPublishModel, publishMessageMap);

                messageResultService.sendEndGenResult(incomingPublish, parsedVouchers, startProcessingTime,
                        publishMessageMap, processingPublishModel);
                // 4. Send message to Push Agent
                RequestToPushAgent requestToPushAgent = makeRequestToPushAgentObj(incomingPublish, new ArrayList<>(publishMessageMap.values()));
                sendToPushAgent(requestToPushAgent);
            } else if (EnumMessageType.EMAIL.equals(requestMsgType)) {
                Map<VoucherModel, String> emailSendingResult = sendMail(parsedVouchers);
                updateSendMailStatus(processingPublishModel, emailSendingResult);
                messageResultService.sendEmailResult(incomingPublish, emailSendingResult, startProcessingTime);
            } else {
                throw new InvalidException("Invalid SMS Type " + incomingPublish.getSmsType());
            }
            log.info("Finish handling normal publish={}", incomingPublish.getId());
        } catch (Exception e) {
            log.error("Handle normal voucher exception", e);
            messageResultService.sendAllFailMessages(incomingPublish, startProcessingTime);
            log.info("Start rolling back for publishId={} and related vouchers", incomingPublish.getId());
            try {
                esPublishService.delete(incomingPublish.getId());
                Set<String> tobeDeleteVouchers = incomingPublish.getPublishDetails().stream().map(detail -> detail.getVoucher().getId()).collect(Collectors.toSet());
                voucherService.delete(tobeDeleteVouchers);
            } catch (Exception ex) {
                log.error("Exception while rolling back", ex);
            }
        }
    }

    private Map<VoucherModel, String> sendMail(List<VoucherModel> vouchers) throws NotFoundException {
        Map<VoucherModel, String> finalResult = new HashMap<>();
        if (vouchers.isEmpty()) {
            throw new NotFoundException("No voucher found in incoming publish");
        }
        for (VoucherModel voucher : vouchers) {
            try {
                String expireMsg = makeExpireMessage(voucher.getExpireDate());
                emailService.sendEmail(voucher.getUserEmail(), emailTemplate, voucher.getShortLink(),
                        voucher.getSerialNo(), expireMsg);
                finalResult.put(voucher, SUCCESS_MESSAGE);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                finalResult.put(voucher, e.getMessage());
            }
        }
        return finalResult;
    }

    private String makeExpireMessage(Date expireDate) throws InvalidException {
        // given 2025-04-18 23:59:59 change to dd/MM/yyyy format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (Objects.isNull(expireDate)) {
            throw new InvalidException("Invalid expire date format");
        }
        String expireDateString = dateFormat.format(expireDate);
        return "E-voucher có giá trị sử dụng đến hết ngày " + expireDateString;
    }

    /***
     * Can be used later for sending messages
     * Activate a Paper voucher -> send message use this data
     * @param msgCreationMap
     */
    private void savePushAgentRequest(Map<VoucherModel, PublishMessage> msgCreationMap) {
        ObjectMapper mapper = new JsonMapper();
        msgCreationMap.forEach((key, value) -> {
            try {
                VoucherModel voucherModel = voucherService.findById(key.getId());
                voucherModel.setOutgoingRequest(mapper.writeValueAsString(value));
                voucherService.saveOne(voucherModel);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    private PublishModel getOrigPublish(Long publishId) {
        try {
            return esPublishService.getPublish(publishId);
        } catch (NotFoundException e) {
            log.warn("Msg={}. Save as new publish={}", e.getMessage(), publishId);
            return null;
        }
    }

    private void handleParseException(RequestFromBE incomingPublish, Date startProcessingTime, ParseRequestException e) {
        log.error("Invalid publish info received, drop this publish message", e);
        messageResultService.sendAllFailMessages(incomingPublish, startProcessingTime);
    }

    private RequestToPushAgent makeRequestToPushAgentObj(RequestFromBE incomingPublish, List<PublishMessage> publishMessages) {
        RequestToPushAgent requestToPushAgent = modelMapper.map(incomingPublish, RequestToPushAgent.class);
        if (Objects.isNull(requestToPushAgent.getSmsType())
                || EnumSet.of(EnumMessageType.PAPER, EnumMessageType.DOWNLOAD, EnumMessageType.EMAIL).contains(EnumMessageType.getEnum(requestToPushAgent.getSmsType()))) {
            requestToPushAgent.setSmsType(EnumMessageType.SMS.getValue());
        }
        requestToPushAgent.setTemplateId(templateId);
        requestToPushAgent.setBrandName(pushBrandName);
        requestToPushAgent.setPublishMessageList(publishMessages);
        requestToPushAgent.setTotalCount(publishMessages.size());
        return requestToPushAgent;
    }

    private PublishModel savePublishModel(PublishModel publishModel) {
        log.info("Start saving publish={}", publishModel);
        PublishModel dbPublishModel = esPublishService.save(publishModel);
        log.info("Finish saving publish");
        return dbPublishModel;
    }

    private void sendToPushAgent(RequestToPushAgent publish) throws SendMessageToQueueException {
        log.info("Start sending request to PushAgent={}", publish.getPublishId());
        publishRequestSender.sendRequestToPushAgent(publish);
        log.info("Finish sending request to PushAgent");
    }

    private List<VoucherModel> parseVouchers(RequestFromBE publish) throws ParseRequestException {
        List<PublishDetail> details = publish.getPublishDetails();
        log.info("Start parsing vouchers, count={}", details.size());
        List<VoucherModel> result = new ArrayList<>();
        for (PublishDetail publishDetail : details) {
            log.info("Parsing voucher of publishDetailId={}", publishDetail.getId());
            String voucherType = publish.getSmsType();
            EnumMessageType enumMessageType = Objects.isNull(voucherType) || voucherType.isEmpty()
                    ? EnumMessageType.SMS : EnumMessageType.getEnum(voucherType);
            VoucherModel parsedVoucher = voucherService.parseVoucher(publishDetail.getVoucher(), publishDetail.getId(), enumMessageType);
            result.add(parsedVoucher);
        }

        return result;
    }

    public static List<String> getExceptStoreList(Voucher incomingVoucher) {
        String exceptStoreIds = incomingVoucher.getGoods().getExceptStoreIds();
        if (Objects.isNull(exceptStoreIds) || exceptStoreIds.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return List.of(exceptStoreIds.split(","));
    }

    private Map<VoucherModel, PublishMessage> createPublishMessages(MessageTemplate messageTemplate,
                                                                    PublishModel publish,
                                                                    List<VoucherModel> vouchers) {
        Map<VoucherModel, PublishMessage> publishMessageMap = new HashMap<>();
        log.info("Start creating publish messages");
        for (VoucherModel voucher : vouchers) {
            try {
                PublishMessage publishMessage = new PublishMessage();
                publishMessage.setPublishDetailId(voucher.getPublishDetailId());
                // 3.1 Create message either for zalo or SMS
                if (EnumSet.of(EnumMessageType.SMS, EnumMessageType.DOWNLOAD, EnumMessageType.PAPER, EnumMessageType.EMAIL).contains(publish.getSmsType())) {
                    // 3.1.1. Create SMS message
                    publishMessage = smsMessageService.createMessage(messageTemplate, voucher, publish);
                } else {
                    log.warn("Unsupported SMS type: {}", publish.getSmsType());
                    continue;
                }
                publishMessageMap.put(voucher, publishMessage);
            } catch (Exception e) {
                log.error("Exception while creating publish message. Ev={}, Msg={}", voucher.getId(), e.getMessage());
            }
        }
        log.info("Publish messages {}/{}", publishMessageMap.size(), vouchers.size());
        return publishMessageMap;
    }

    private PublishModel makePublishModel(RequestFromBE publish) throws ParseRequestException {
        try {
            PublishModel publishModel = modelMapper.map(publish, PublishModel.class);
            // Model mapper can not map Local datetime
            publishModel.setBookingDate(DateUtils.toDateTime(publish.getBookingDate()));
            publishModel.setPublishDate(DateUtils.toDateTime(publish.getPublishDate()));
            publishModel.setCancelDate(DateUtils.toDateTime(publish.getCancelDate()));
            publishModel.getCampaign().setStartDate(DateUtils.toDateTime(publish.getCampaign().getStartDate()));
            publishModel.getCampaign().setEndDate(DateUtils.toDateTime(publish.getCampaign().getEndDate()));
            if (Objects.nonNull(publish.getPublishDetails()) && !publish.getPublishDetails().isEmpty()) {
                Optional<PublishDetail> detailsWithActivationIdOption = publish.getPublishDetails().stream()
                        .filter(publishDetail -> Objects.nonNull(publishDetail.getVoucher().getActivationId())).findFirst();
                publishModel.setActivationId(detailsWithActivationIdOption.map(publishDetail -> publishDetail.getVoucher().getActivationId()).orElse(null));
                Optional<PublishDetail> detailsWithActivationUrlOption = publish.getPublishDetails().stream()
                        .filter(publishDetail -> Objects.nonNull(publishDetail.getVoucher().getActivationUrl())).findFirst();
                publishModel.setActivationUrl(detailsWithActivationUrlOption.map(publishDetail -> publishDetail.getVoucher().getActivationUrl()).orElse(null));
            }
            // Set detail status
            for (PublishDetailModel detailModel : publishModel.getPublishDetails()) {
                detailModel.setPublishStatusCode(EnumPublishDetailStatus.STRT_GEN_MSG);
            }
            // set publish status
            publishModel.setPublishStatusCode(EnumPublishStatus.GENERATING);
            return publishModel;
        } catch (Exception e) {
            throw new ParseRequestException("Exception while parsing publish", e);
        }
    }

    private void updatePublishStatus(PublishModel publish, Map<VoucherModel, PublishMessage> msgCreationMap) {
        publish.getPublishDetails()
                .forEach(detail -> {
                    boolean msgCreateSuccessfully = msgCreationMap
                            .entrySet()
                            .stream()
                            .anyMatch(entry -> entry.getKey().getPublishDetailId().equals(detail.getId()));
                    if (!msgCreateSuccessfully) {
                        detail.setPublishStatusCode(EnumPublishDetailStatus.FAIL_GEN_MSG);
                    } else {
                        detail.setPublishStatusCode(EnumPublishDetailStatus.END_GEN_MSG);
                    }
                });
        if (msgCreationMap.isEmpty()) {
            publish.setPublishStatusCode(EnumPublishStatus.FAIL_GENERATING);
        } else {
            publish.setPublishStatusCode(EnumPublishStatus.SENDING);
        }
        savePublishModel(publish);
    }

    private void updateSendMailStatus(PublishModel publish, Map<VoucherModel, String> sendMailResult) {
        publish.getPublishDetails()
                .forEach(detail -> {
                    // get voucher from sendmailResult by detailId
                    Map.Entry<VoucherModel, String> sendResult = sendMailResult
                            .entrySet()
                            .stream()
                            .filter(entry -> entry.getKey().getPublishDetailId().equals(detail.getId()))
                            .findFirst().orElse(null);
                    if (Objects.nonNull(sendResult) && Objects.nonNull(sendResult.getValue())
                            && sendResult.getValue().equalsIgnoreCase(SUCCESS_MESSAGE)) {
                        detail.setPublishStatusCode(EnumPublishDetailStatus.RESULT_SUCCESS);
                    } else {
                        detail.setPublishStatusCode(EnumPublishDetailStatus.RESULT_FAIL);
                    }
                });
        publish.setPublishStatusCode(EnumPublishStatus.FINISHED);
        savePublishModel(publish);
    }

    private void updatePublishStatusForTransfer(PublishModel publish, Map<VoucherModel, PublishMessage> msgCreationMap) {

        publish.getPublishDetails()
                .forEach(detail -> {
                    boolean msgCreateSuccessfully = msgCreationMap
                            .entrySet()
                            .stream()
                            .anyMatch(entry -> entry.getKey().getPublishDetailId().equals(detail.getId()));
                    if (msgCreateSuccessfully) {
                        detail.setPublishStatusCode(EnumPublishDetailStatus.END_GEN_MSG);
                    }
                });
        if (msgCreationMap.isEmpty()) {
            publish.setPublishStatusCode(EnumPublishStatus.FAIL_GENERATING);
        } else {
            publish.setPublishStatusCode(EnumPublishStatus.SENDING);
        }
        savePublishModel(publish);
    }
}

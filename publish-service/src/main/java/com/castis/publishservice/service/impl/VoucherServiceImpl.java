package com.castis.publishservice.service.impl;

import com.castis.publishservice.dto.PublishDetailDTO;
import com.castis.publishservice.dto.queue.PublishDetailQueueRequest;
import com.castis.publishservice.dto.queue.PublishQueueRequest;
import com.castis.publishservice.dto.queue.VoucherRequest;
import com.castis.publishservice.dto.queue.VoucherResendProcessResponse;
import com.castis.publishservice.dto.request.SendMessageStatusRequest;
import com.castis.publishservice.dto.request.StatusQueue;
import com.castis.publishservice.dto.request.VoucherResendRequest;
import com.castis.publishservice.entity.*;
import com.castis.publishservice.exception.defineException.BadRequestException;
import com.castis.publishservice.exception.defineException.NotFoundException;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import com.castis.publishservice.mapper.VoucherMapper;
import com.castis.publishservice.producer.RabbitMQProducer;
import com.castis.publishservice.repository.*;
import com.castis.publishservice.service.*;
import com.castis.publishservice.utils.Utils;
import com.castis.publishservice.utils.enum_template.SmsType;
import com.castis.publishservice.utils.status.CompletedStatusCode;
import com.castis.publishservice.utils.status.GenerateMessageType;
import com.castis.publishservice.utils.status.PublishDetailStatus;
import com.castis.publishservice.utils.status.PublishStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final PublishService publishService;
    private final CustomerService customerService;
    private final PublishDetailService detailService;
    private final RabbitMQProducer rabbitMQProducer;
    private final VoucherRepository voucherRepository;
    private final PublishRepository publishRepository;
    private final PublishDetailRepository publishDetailRepository;
    private final VoucherResendHistoryRepository voucherResendHistoryRepository;
    private final MessageTemplateRepository messageTemplateRepository;
    private static final VoucherMapper mapper = VoucherMapper.INSTANCE;

    @Value("${message.transfer.template-id}")
    private Integer transferTemplateId;

    @Override
    @Transactional
    public List<VoucherRequest> getVoucherRequestByVoucherId(List<String> requestIds) throws ServerRuntimeException {
        log.info("Get voucher requests by voucher ids={}", requestIds);
        if (Objects.isNull(requestIds) || requestIds.isEmpty()) {
            throw new ServerRuntimeException("Request is null or empty");
        }
        List<Voucher> vouchers = voucherRepository.findAllByIdIn(requestIds);
        if (Objects.isNull(vouchers) || vouchers.isEmpty()) {
            log.warn("Can not find any voucher with ids={}", requestIds);
            throw new ServerRuntimeException("Can not find any voucher with ids=" + requestIds);
        }
        log.info("Found vouchers={}", Utils.toJson(vouchers.stream().map(Voucher::getId).collect(Collectors.toList())));
        return vouchers.stream().map(mapper::entityToQueueRequest).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updatePublishStatus(StatusQueue status) {
        log.info("Receive request to update publish status {}", Utils.toJson(status));
        try {
            publishService.updatePublishStatus(status.getPublishId(), status.getPublishStatus());

            Map<Long, SendMessageStatusRequest> resultMap = new HashMap<>();
            status.getDetailStatus().forEach(o -> resultMap.put(o.getPublishDetailId(), o));
            List<Long> detailIds = status.getDetailStatus().stream().map(SendMessageStatusRequest::getPublishDetailId).collect(Collectors.toList());

            //update publish detail status
            List<PublishDetailDTO> details = detailService.getAllByIds(detailIds);
            details.forEach(o -> {
                Long publishDetailId = o.getPublishDtlId();
                SendMessageStatusRequest request = resultMap.get(publishDetailId);
                if (Objects.nonNull(request.getMessageId())) {
                    o.setSmsId(request.getMessageId());
                }
                if (Objects.nonNull(request.getSendDatetime())) {
                    o.setSmsSendDt(request.getSendDatetime());
                }
                if (Objects.nonNull(request.getSendResultDatetime())) {
                    o.setSmsSendResultDate(request.getSendResultDatetime());
                }
                if (Objects.nonNull(request.getReceivedTime())) {
                    o.setSmsSendResultDate(request.getReceivedTime());
                }
                if (Objects.nonNull(request.getMessage())) {
                    o.setPublishResultMessage(request.getMessage());
                }

                PublishDetailStatus publishDetailStatus = request.getResult();

                o.setPublishStatusCd(publishDetailStatus);
                o.setUpdateDate(new Date());

                if (ObjectUtils.isNotEmpty(request.getResend())) {
                    log.info("Update log for resend voucher with publishDetailId: {}", publishDetailId);
                    updateResendVoucherResult(request.getResend(), publishDetailId);
                }
            });
            log.info("Update status for publish detail={}", details);
            detailService.saveAll(details);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            //todo: save to log table
        }
    }

    @Override
    public void updateSendMessageStatus(List<StatusQueue> statusQueues) {
        log.info("Update send message status {}", Utils.toJson(statusQueues));
        if (Objects.isNull(statusQueues) || statusQueues.isEmpty()) {
            log.warn("Received empty status queue list");
            return;
        }
        statusQueues.forEach(this::updatePublishStatus);
    }

    @Override
    public Long getContractIdByPublishId(Long publishId) throws ServerRuntimeException {
        if (Objects.isNull(publishId)) {
            throw new ServerRuntimeException("Publish id can not be null when get contract id by publish id");
        }
        return voucherRepository.getContractIdByPublishId(publishId);
    }

    @Transactional
    public void resendVoucher(VoucherResendRequest voucherResendRequest) {
        log.info("Resend voucher request {}", Utils.toJson(voucherResendRequest));
        try {
            String ev = voucherResendRequest.getEv();
            Voucher voucher = voucherRepository.findById(ev)
                    .orElseThrow(() -> NotFoundException.voucher(ev));

            Long publishDetailId = voucher.getPublishDtlId();
            PublishDetail publishDetail = publishDetailRepository.findById(publishDetailId)
                    .orElseThrow(() -> NotFoundException.publishDetail(publishDetailId));

            Long publishId = publishDetail.getPublishId();
            Publish publish = publishRepository.findById(publishId)
                    .orElseThrow(() -> NotFoundException.publish(publishId));

            if (PublishDetailStatus.canNotBeResend()
                    .contains(PublishDetailStatus.valueOf(publishDetail.getPublishStatusCd()))) {
                throw new BadRequestException(String.format(
                        "Publish detail=%d status=%s can not be resend voucher",
                        publishDetail.getPublishDtlId(), publishDetail.getPublishStatusCd()));
            }

            Integer voucherResendHistoryId = saveLogResendHistory(voucherResendRequest, publishDetail);

            publishDetail = updatePublishDetailStatusToStartPublish(publishDetail);

            publish = updatePublishStatusToPublishing(publish);

            sendToQueueResendVoucher(publish, publishDetail, voucher, voucherResendHistoryId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage());
        }
    }

    private Integer saveLogResendHistory(
            VoucherResendRequest voucherResendRequest,
            PublishDetail publishDetail) {
        VoucherResendHistory voucherResendHistory = VoucherResendHistory.builder()
                .ev(voucherResendRequest.getEv())
                .publishDetailId(publishDetail.getPublishDtlId())
                .publishId(publishDetail.getPublishId())
                .memo(voucherResendRequest.getReason())
                .previousSmsId(publishDetail.getSmsId())
                .backEndPreviousPublishDetailStatusCode(publishDetail.getPublishStatusCd())
                .backEndUpdateResult(CompletedStatusCode.SUCCESS)
                .build();

        log.info("Save VoucherResendHistory with ev: {}", voucherResendRequest.getEv());
        voucherResendHistory = voucherResendHistoryRepository.save(voucherResendHistory);
        log.info("resend history saved: {}", voucherResendHistory.getId());
        return voucherResendHistory.getId();
    }

    private PublishDetail updatePublishDetailStatusToStartPublish(PublishDetail publishDetail) {
        log.info("Update publish details={} status->STRT_PUB", publishDetail.getPublishDtlId());
        publishDetail.setPublishStatusCd(PublishDetailStatus.STRT_PUB.name());
        publishDetail.setUpdateDate(new Date());
        return publishDetailRepository.save(publishDetail);
    }

    private Publish updatePublishStatusToPublishing(Publish publish) {
        log.info("Update Publish={} status->PUBLISHING", publish.getId());
        publish.setPublishStatusCode(PublishStatus.PUBLISHING);
        publish.setUpdtDt(new Date());
        return publishRepository.save(publish);
    }

    private void sendToQueueResendVoucher(Publish publish,
                                          PublishDetail publishDetail,
                                          Voucher voucher,
                                          Integer voucherResendHistoryId) throws JsonProcessingException {
        log.info("Send to queue Resend voucher={}, historyId={}", voucher.getId(), voucherResendHistoryId);

        PublishQueueRequest publishRequest = publishService.findRequestById(publish.getId());
        publishRequest.setType(GenerateMessageType.RESEND);
        publishRequest.setVoucherResendHistoryId(voucherResendHistoryId);
        publishRequest.setCustomer(customerService.findRequestById(publishRequest.getCustomerId()));

        PublishDetailQueueRequest detail = PublishDetailQueueRequest.builder()
                .id(publishDetail.getPublishDtlId())
                .publishId(publishDetail.getPublishId())
                .smsType(publishDetail.getSmsType())
                .voucher(mapper.entityToQueueRequest(voucher))
                .build();


        if (StringUtils.isNotBlank(voucher.getOrigEv())) {
            log.info("Is transferred voucher from {}, send with transfer template", voucher.getOrigEv());
            MessageTemplate messageTemplate = messageTemplateRepository.findById(transferTemplateId)
                    .orElseThrow(
                            () ->
                                    new IllegalArgumentException("Message template for Transfer voucher not found"));
            publishRequest.getCampaign().setMessageTemplate(MessageTemplateService.toRequest(messageTemplate));
            publishRequest.setTransferMessage(voucher.getTransferMsg());
            detail.setSmsType(SmsType.SMS.name());
            publishRequest.setSmsType(SmsType.SMS.name());
        }

        List<PublishDetailQueueRequest> detailRequest = List.of(detail);

        publishRequest.setPublishDetails(detailRequest);

        rabbitMQProducer.publishEvoucher(publishRequest);
    }

    private void updateResendVoucherResult(VoucherResendProcessResponse response, Long publishDetailId) {
        try {
            updateResultToVoucherResendHistory(response, publishDetailId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void updateResultToVoucherResendHistory(VoucherResendProcessResponse response, Long publishDetailId) {
        log.info("Update result to voucher resend history response={}", Utils.toJson(response));
        Integer voucherResendHistoryId = response.getVoucherResendHistoryId();
        log.info("Find VoucherResendHistory with id={}", voucherResendHistoryId);
        VoucherResendHistory voucherResendHistory =
                voucherResendHistoryRepository.findById(voucherResendHistoryId)
                        .orElseThrow(() -> NotFoundException.voucherResendHistory(voucherResendHistoryId));

        if (!publishDetailId.equals(voucherResendHistory.getPublishDetailId())) {
            String errorMsg = String.format("PublishDetailId=%d not equal PublishDetailId=%d of VoucherResendHistory",
                    publishDetailId, voucherResendHistory.getPublishDetailId());
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        voucherResendHistory.setFrontEndUpdateResult(response.getFrontEndUpdateResult());
        voucherResendHistory.setFrontEndPreviousPublishDetailStatusCode(
                response.getFrontEndPreviousPublishDetailStatusCode());

        log.info("Update VoucherResendHistory with Id={}", voucherResendHistoryId);
        voucherResendHistoryRepository.save(voucherResendHistory);
    }
}

package asia.castis.web_hook.serivce;

import asia.castis.web_hook.bean.dto.ExtVoucherUsingInfo;
import asia.castis.web_hook.bean.dto.request.GiftPopRequest;
import asia.castis.web_hook.bean.dto.request.UpdatingVoucherReq;
import asia.castis.web_hook.bean.dto.request.WataneHeaders;
import asia.castis.web_hook.bean.dto.request.WataneRequest;
import asia.castis.web_hook.bean.dto.response.BaseResponse;
import asia.castis.web_hook.bean.dto.response.WataneResponse;
import asia.castis.web_hook.bean.entity.ExtPin;
import asia.castis.web_hook.bean.entity.ThirdPartyCallingHistory;
import asia.castis.web_hook.bean.entity.Voucher;
import asia.castis.web_hook.client.BeServiceClient;
import asia.castis.web_hook.common.*;
import asia.castis.web_hook.exception.InvalidCredentialException;
import asia.castis.web_hook.exception.InvalidSignatureException;
import asia.castis.web_hook.exception.defined.BadRequestException;
import asia.castis.web_hook.exception.defined.EntityNotFoundException;
import asia.castis.web_hook.exception.defined.RelatedServiceHandlingException;
import asia.castis.web_hook.exception.defined.ServerRuntimeException;
import asia.castis.web_hook.serivce.external_sys.ExternalSystemFactory;
import asia.castis.web_hook.serivce.external_sys.WataneService;
import asia.castis.web_hook.utils.Common;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherStatusService {
    private final VoucherService voucherService;
    private final ExtPinService extPinService;
    private final ThirdPartyCallingService thirdPartyCallingService;
    private final ExternalSystemFactory extFactory;

    private final WataneService wataneService;

    private final BeServiceClient beClient;

    @Scheduled(cron = "${system.update.voucher.status.cron.ur_box}")
    public void updateVoucherStatus() {
        updateUrBoxVoucherStatus();
    }
    private final List<VoucherStatusCode> usableStatus = List.of(VoucherStatusCode.NORMAL, VoucherStatusCode.PART_USED);

    public BaseResponse processUsingGiftPopVoucher(@Valid GiftPopRequest request, String requestPath)
            throws EntityNotFoundException,
            BadRequestException,
            ServerRuntimeException,
            RelatedServiceHandlingException {
        log.info("receive gift pop updating voucher status: {}", request);
        var requestLog = thirdPartyCallingService.makeNewHistory(RequestType.INBOUND)
                .requestUrl(requestPath)
                .requestBody(request.toJsonString())
                .system(SystemType.GIFTPOP.name())
                .build();
        try {
            requestLog = thirdPartyCallingService.save(requestLog);
            ExtPin pin = extPinService.findByTransactionId(request.getTrId());
            log.info("got pin: {}", pin);

            // get voucher with pin id = pin id, order by publish date desc and limit 1 (the latest voucher is bind with pin)
            Voucher voucher = voucherService.getLastUseGiftPopVoucherByTransactionId(pin.getId(), usableStatus);
            log.info("got voucher: {}", voucher);

//            validateVoucherStatus(voucher, pin.getTransactionId());

            //call service be for update status
            var relatedServicePayload = makeUseRequestPayload(voucher, request.getUseDate());

            //call service to update voucher status both be and fe sides
            callRelatedService(relatedServicePayload);

            var response = new BaseResponse(200, "Success");
            requestLog.setResponseBody(response.toJsonString());
            requestLog.setResponseTime(new Date());
            requestLog.setResult("OK");

            return response;

        } catch (EntityNotFoundException | BadRequestException | RelatedServiceHandlingException e) {
            setFailResponse(
                    requestLog,
                    e.getMessage(),
                    e.getMessage()
            );
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage());
        } finally {
            log.info("update log: {}",requestLog);
            thirdPartyCallingService.save(requestLog);
        }
    }

    public BaseResponse updateUrBoxVouchersStatus(List<String> vouchers) {
        try {
            log.info("start checking and updating ur box voucher status: {}", vouchers);
            List<Voucher> voucherList = voucherService.getAllByIdIn(List.of(VoucherStatusCode.NORMAL, VoucherStatusCode.PART_USED)
                    , SystemType.UR_BOX, vouchers);
            if (CollectionUtils.isEmpty(voucherList)) {
                log.warn("could not find voucher had status NORMAL, PART_USED and system = {}", SystemType.UR_BOX);
                return new BaseResponse(HttpStatus.OK.value(), HttpStatus.OK.toString());
            }

            Map<String, ExtVoucherUsingInfo> extStatus =
                    extFactory.getServiceByType(SystemType.UR_BOX).synchronizeStatus(voucherList);

            updateVoucherStatus(voucherList, extStatus);
            return new BaseResponse(HttpStatus.OK.value(), HttpStatus.OK.toString());
        } catch (EntityNotFoundException e) {
            log.warn(e.getMessage());
            return new BaseResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }

    private void updateVoucherStatus(List<Voucher> voucherList, Map<String, ExtVoucherUsingInfo> extStatus) throws RelatedServiceHandlingException{
        log.info("ext status: {}", Common.toJsonBody(extStatus));
        if (!extStatus.isEmpty()) {
            List<UpdatingVoucherReq> relatedRequest = new ArrayList<>();

            voucherList.stream()
                    .filter(o -> extStatus.containsKey(o.getExtPinNo())).forEach(o -> {
                        ExtVoucherUsingInfo info = extStatus.get(o.getExtPinNo());
                        relatedRequest.add(makeUpdatingReqByStatus(o, info.getUsingTime(), info.getStatus()));
                    });

            log.info("update status payload: {}", relatedRequest);
            var beResponse = beClient.updateStatus(relatedRequest);
            handleBeServiceResponse(beResponse);
        } else {
            log.warn("has no expired or used voucher");
        }

    }

    private void updateUrBoxVoucherStatus() {
        log.info("start checking and updating urbox voucher status");
        try {
            List<Voucher> existingVoucher = voucherService.getAllByStatusInAndSystem(
                    List.of(VoucherStatusCode.NORMAL, VoucherStatusCode.PART_USED)
                    , SystemType.UR_BOX
            );
            if (CollectionUtils.isEmpty(existingVoucher)) {
                log.warn("could not find voucher had status NORMAL, PART_USED and system = {}", SystemType.UR_BOX);
                return;
            }
            log.info("got voucher: {}", existingVoucher.stream().map(Voucher::getId).collect(Collectors.toList()));

            Map<String, ExtVoucherUsingInfo> extStatus = extFactory.getServiceByType(SystemType.UR_BOX).synchronizeStatus(existingVoucher);
            log.info("ext status: {}", Common.toJsonBody(extStatus));

            updateVoucherStatus(existingVoucher, extStatus);

        } catch (EntityNotFoundException e) {
            log.warn(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    private void callRelatedService(UpdatingVoucherReq request) throws RelatedServiceHandlingException {

        try {
            log.info("call service be to update voucher using status: {}", request);
            var beResponse = beClient.usingVoucher(request);

            handleBeServiceResponse(beResponse);

        } catch (RelatedServiceHandlingException e) {
            throw e;
        } catch (Exception e) {
            log.error("server exception : {}", e.getMessage());
            log.error(e.getMessage(), e);
            throw new RelatedServiceHandlingException(e.getMessage(), e);
        }
    }

    private void handleBeServiceResponse(BaseResponse beResponse) throws RelatedServiceHandlingException {
        if (beResponse.getCode() == Common.SUCCESS) {
            log.info("update voucher status success");
        } else {
            log.error("service be return fail result: {}", beResponse);
            throw new RelatedServiceHandlingException(beResponse.getMessage());
        }
    }

    private void setFailResponse(ThirdPartyCallingHistory requestLog, Object responseBody, String exception) {
        requestLog.setResult("Fail");
        requestLog.setResponseBody(Common.toJsonBody(responseBody));
        requestLog.setResponseTime(new Date());
        requestLog.setDescription(exception);
    }

    private void validateVoucherStatus(Voucher voucher, String transactionId) throws BadRequestException {
        if (voucher.getVoucherStatusCode() == VoucherStatusCode.USED) {
            log.error("voucher is used");
            throw new BadRequestException("Voucher with trid "+transactionId+" is already used");
        }
        if (!this.usableStatus.contains(voucher.getVoucherStatusCode())) {
            log.error("voucher status: {} is unusable", voucher.getVoucherStatusCode());
            throw new BadRequestException("voucher is unusable");
        }
    }

    private void setVoucherUsingReqPayload(UpdatingVoucherReq.UpdatingVoucherReqBuilder builder,
                                           Voucher voucher, Date transactionDate) {
        builder
                .voucherId(voucher.getId())
                .transactionDate(transactionDate)
                .exchangeAmount(voucher.getBalance())
                .updatingType(UpdatingVoucherType.USING)
                .build();
    }

    private UpdatingVoucherReq makeUseRequestPayload(Voucher voucher, Date transactionDate) {
        log.info("make request to service be for updating voucher status and making settlement log");
        UpdatingVoucherReq.UpdatingVoucherReqBuilder builder = UpdatingVoucherReq.builder()
                .updatingType(UpdatingVoucherType.USING);

        setVoucherUsingReqPayload(builder, voucher, transactionDate);
        return builder.build();
    }

    private UpdatingVoucherReq makeExpireRequestPayload(Voucher voucher, Date transactionDate) {
        log.info("make request to service be for updating voucher status and making settlement log");
        UpdatingVoucherReq.UpdatingVoucherReqBuilder builder = UpdatingVoucherReq.builder()
                .updatingType(UpdatingVoucherType.EXPIRED);

        setVoucherUsingReqPayload(builder, voucher, transactionDate);
        return builder.build();
    }

    private UpdatingVoucherReq makeUpdatingReqByStatus(Voucher voucher, Date transactionDate, VoucherStatusCode status)
    throws ServerRuntimeException {
        switch (status) {
            case USED:
                return makeUseRequestPayload(voucher, transactionDate);
            case EXPIRE:
                return makeExpireRequestPayload(voucher, transactionDate);
            default:
                log.error("status : {} is not supported for using", status);
                throw new ServerRuntimeException("voucher status is invalid");
        }
    }

    public ResponseEntity<WataneResponse> processWataneRequest(WataneRequest request,
                                                               WataneHeaders headers,
                                                               String requestPath) {
        log.info("Processing Watane request: headers={}, request={}", headers, request);

        ThirdPartyCallingHistory requestLog = null;

        try {
            wataneService.validate(headers, request);

            requestLog = thirdPartyCallingService.makeNewHistory(RequestType.INBOUND)
                    .requestUrl(requestPath)
                    .requestBody(request.toJsonString())
                    .system(SystemType.WATANE.name())
                    .build();

            return handleWataneBusiness(request, requestLog);

        } catch (InvalidCredentialException e) {
            return buildWataneErrorResponse(WataneErrorCode.INVALID_USERNAME_OR_CREDENTIAL);
        } catch (InvalidSignatureException e) {
            return buildWataneErrorResponse(WataneErrorCode.INVALID_SIGNATURE);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return buildWataneErrorResponse(WataneErrorCode.COMMON_UNKNOWN);
        } finally {
            if (requestLog != null) {
                thirdPartyCallingService.save(requestLog);
                log.info("Saved request log: {}", requestLog);
            }
        }
    }

    private ResponseEntity<WataneResponse> handleWataneBusiness(WataneRequest request,
                                                                ThirdPartyCallingHistory requestLog) throws Exception {
        try {
            requestLog = thirdPartyCallingService.save(requestLog);

            ExtPin pin = extPinService.findByTrackingId(request.getVoucherSerial());
            log.info("Found PIN: {}", pin);

            Voucher voucher = voucherService.getLastUseWataneVoucherByTrackingId(pin.getId(), usableStatus);
            log.info("Found Voucher: {}", voucher);

            var payload = makeUseRequestPayload(voucher, request.getRedeemedAt());
            callRelatedService(payload);

            var response = new WataneResponse(
                    "true", null, null,
                    Map.of("result", "ok"),
                    Instant.now().toString()
            );

            String signature = wataneService.sign(response);
            return ResponseEntity.ok().header("signature", signature).body(response);

        } catch (EntityNotFoundException | BadRequestException | RelatedServiceHandlingException e) {
            setFailResponse(requestLog, e.getMessage(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Business error: {}", e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage());
        }
    }

    private ResponseEntity<WataneResponse> buildWataneErrorResponse(WataneErrorCode errorCode) {
        var response = new WataneResponse(
                "false",
                errorCode.getCode(),
                errorCode.getMessage(),
                null,
                Instant.now().toString()
        );
        return ResponseEntity.ok(response);
    }
}

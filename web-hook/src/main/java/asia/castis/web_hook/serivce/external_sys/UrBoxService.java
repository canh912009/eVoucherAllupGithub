package asia.castis.web_hook.serivce.external_sys;

import asia.castis.web_hook.bean.dto.ExtVoucherUsingInfo;
import asia.castis.web_hook.bean.dto.FailHandling;
import asia.castis.web_hook.bean.dto.request.UrBoxBaseRequest;
import asia.castis.web_hook.bean.dto.request.UrBoxVoucherListReq;
import asia.castis.web_hook.bean.dto.response.ur_box.UrBoxSingleResponse;
import asia.castis.web_hook.bean.dto.response.ur_box.UrBoxVoucherList;
import asia.castis.web_hook.bean.entity.ThirdPartyCallingHistory;
import asia.castis.web_hook.bean.entity.Voucher;
import asia.castis.web_hook.client.UrBoxClient;
import asia.castis.web_hook.common.RequestType;
import asia.castis.web_hook.common.SystemType;
import asia.castis.web_hook.common.VoucherStatusCode;
import asia.castis.web_hook.exception.defined.HttpRequestException;
import asia.castis.web_hook.exception.defined.ServerRuntimeException;
import asia.castis.web_hook.serivce.ThirdPartyCallingService;
import asia.castis.web_hook.utils.Common;
import asia.castis.web_hook.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Service(value = "UR_BOX")
@Slf4j
@RequiredArgsConstructor
public class UrBoxService implements ExternalSystemService {
    @Value("${systems.ur_box.app_secret}")
    private String appSecret;
    @Value("${systems.ur_box.app_id}")
    private Integer appId;
    @Value("${systems.ur_box.url}")
    private String urBoxUrl;
    @Value("${systems.ur_box.paths.get-vouchers}")
    private String getVouByTransId;

    private final ThirdPartyCallingService thirdPartyService;
    private final UrBoxClient client;

    private static final int MAX_TRANSACTION_PER_REQUEST = 50;
    private static final int UR_BOX_DONE = 1;
    private static final List<VoucherStatusCode> needToUpdateStatus =
            List.of(VoucherStatusCode.USED, VoucherStatusCode.EXPIRE);

    private void setUrBoxBaseParam(UrBoxBaseRequest request) {
        request.setApp_id(appId)
                .setApp_secret(appSecret);
    }
    @Override
    public Map<String, ExtVoucherUsingInfo> synchronizeStatus(List<Voucher> voucherList) {
        //filter all empty external pin no, transaction id
        voucherList = voucherList.stream().filter( o ->
                Optional.ofNullable(o)
                        .map(Voucher::getExtPin)
                        .map( p -> p.getExtPinNo() != null && p.getTransactionId() != null)
                        .orElse(false)
        ).collect(Collectors.toList());

        List<String> transactionsId = voucherList.stream().map(o -> o.getExtPin().getTransactionId())
                .distinct()
                .collect(Collectors.toList());
        List<String> extPins = voucherList.stream().map(Voucher::getExtPinNo).collect(Collectors.toList());
        log.info("get urbox pin status by transaction id: {},\n external pin no: {}", transactionsId, extPins);
        return synchronize(transactionsId, extPins);
    }

    public Map<String, ExtVoucherUsingInfo> synchronize(List<String> transactionsId, List<String> extPins)
            throws ServerRuntimeException, HttpRequestException {
        Map<String, ExtVoucherUsingInfo> result = new HashMap<>();


        // urbox only handle maximum 50 transaction per 1 request
        // save 3rd party calling for each request

        // get max 50 first transaction
        while (!transactionsId.isEmpty()) {
            // only process for 50 transactions for 1 request to urbox
            List<String> subList = transactionsId.subList(0,
                    Math.min(MAX_TRANSACTION_PER_REQUEST, transactionsId.size())
            );
            List<String> first50 = new ArrayList<>(subList);
            //remove processed transaction from list
            subList.clear();

            log.info("get pin status by transaction id: {}", first50);
            try {
                result.putAll(getPinStatusMapPartly(first50, extPins));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return result;

    }




    private Map<String, ExtVoucherUsingInfo> getPinStatusMapPartly(@Valid @NotNull List<String> transactionsId, List<String> extPins) {

        var result = new HashMap<String, ExtVoucherUsingInfo>();
        if (transactionsId.isEmpty()) {
            log.warn("transaction is empty");
            return result;
        }
        UrBoxBaseRequest request = createGettingVoucherStatusReq(transactionsId);
        // build third party calling log
        ThirdPartyCallingHistory history = thirdPartyService.makeNewHistory(RequestType.OUTBOUND)
                .system(SystemType.UR_BOX.name())
                .requestBody(Common.toJsonBody(request))
                .requestUrl(urBoxUrl.concat(getVouByTransId))
                .build();
        history = thirdPartyService.save(history);
        try {
            // build urbox api request
            //calling to urbox
            log.info("call to urbox to getting voucher status with body: \n {}", request);
            UrBoxSingleResponse<List<UrBoxVoucherList>> response = client.getVoucherByTransactionsId(request);
            log.info("urbox return: \n {}", response);
            history.setResponseBody(Common.toJsonBody(response));
            history.setResponseTime(new Date());

            // check if response is null or response status is not success
            var responseStatus  =
                    Optional.ofNullable(response)
                            .map(UrBoxSingleResponse::getDone)
                            .orElseThrow(() -> {
                                log.error("urbox return null");
                                return new ServerRuntimeException("urbox return null");
                            });

            List<FailHandling> failCases = new ArrayList<>();
            List<String> notUse = new ArrayList<>();
            if (responseStatus == UR_BOX_DONE) {
                List<UrBoxVoucherList> responseData = Optional.ofNullable(response.getData()).orElseThrow(() -> {
                    log.warn("urbox return success status but null data");
                    return new ServerRuntimeException("Urbox return success status but data is null");
                });

                for (UrBoxVoucherList voucherList : responseData) {
                    convertEachUrBoxStatus(extPins, voucherList, result, notUse, failCases);
                }
            } else {
                log.error("urbox return error: {}", response.getMsg());
                throw new ServerRuntimeException(Objects.toString(
                        response.getMsg(),
                        "Urbox return error with null message")
                );
            }

            history.setDescription(
                    Optional.ofNullable(history.getDescription()).orElse("")
                            .concat(String.format("success cases: %s, fail cases: %s, not use: %s",
                                    Common.toJsonBody(result),
                                    Common.toJsonBody(failCases),
                                    Common.toJsonBody(notUse))
                            )
            );
            log.info("success({}): \n{}, fails({}):\n{}, not use({}): \n {}",result.size(), Common.toJsonBody(result), failCases.size(),  Common.toJsonBody(failCases), notUse.size(), Common.toJsonBody(notUse));
            history.setOk();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            history.setFail();
            history.setDescription(Optional.ofNullable(history.getDescription()).orElse("").concat(String.format("Exception : %s", e.getMessage())));
        } finally {
            thirdPartyService.save(history);
        }
        return result;
    }

    private void convertEachUrBoxStatus(List<String> extPins, UrBoxVoucherList voucherList, HashMap<String, ExtVoucherUsingInfo> result, List<String> notUse, List<FailHandling> failCases) {
        try {
            result.putAll(convertUrBoxStatusForEachTransaction(voucherList, extPins, notUse, failCases));
        } catch (ServerRuntimeException e) {
            //catch exception and continue
            log.error(e.getMessage());
            // log fail cases
            failCases.add(FailHandling.builder()
                    .key(voucherList.getId())
                    .detail(e.getMessage())
                    .type("transaction")
                    .build());
        }
    }

    private HashMap<String, ExtVoucherUsingInfo> convertUrBoxStatusForEachTransaction(UrBoxVoucherList transaction,
                                                                                      List<String> extPins,
                                                                                      List<String> notUse,
                                                                                      List<FailHandling> failCases) {
        log.info("convert ur box status for each transaction: {}", transaction.getTransaction_id());
        List<UrBoxVoucherList.Detail> details = Optional.ofNullable(transaction.getDetail()).orElseThrow(() -> {
            log.error("transaction: {} has empty detail list", transaction.getTransaction_id());
            return new ServerRuntimeException(
                    String.format("transaction %s has empty detail list",
                            Objects.toString(transaction.getTransaction_id(),
                                    "transaction Id is null")));
        });
        log.info("with {} voucher", transaction.getDetail().size());
        HashMap<String, ExtVoucherUsingInfo> result = new HashMap<>();
        for (UrBoxVoucherList.Detail detail : details) {
            // check if external pin no not null, usage status code not null and external is checking code
            if (Optional.ofNullable(detail)
                    .map(o -> o.getCode() != null
                            && o.getDeliveryCode() != null
                    )
                    .orElse(false) && extPins.contains(detail.getCode())) {
                try {
                    VoucherStatusCode voucherStatus = fromUrBoxStatus(detail.getDeliveryCode());
                    if (needToUpdateStatus.contains(voucherStatus)) {
                        var usingInfo = ExtVoucherUsingInfo.builder()
                                .status(voucherStatus)
                                .usingTime(
                                        voucherStatus == VoucherStatusCode.USED ?
                                                DateUtils.slashDMY.parse(detail.getUsing_time()) :
                                                null)
                                .build();
                        result.put(detail.getCode(), usingInfo);
                    } else {
                        notUse.add(detail.getCode());
                    }
                } catch (Exception e) {
                    log.error("exception when convert urbox voucher status");
                    log.error(e.getMessage(), e);
                    failCases.add(FailHandling.builder()
                                    .key(detail.getCode())
                                    .cause(e.getMessage())
                                    .type("voucher")
                            .build());
                }
            }
        }
        if (!result.isEmpty()) {
            log.info("used | expired voucher : {}", result);
        }
        return result;
    }

    VoucherStatusCode fromUrBoxStatus(Integer urBoxStatus) {
        switch (urBoxStatus) {
            case 1:
                return VoucherStatusCode.NORMAL;
            case 2:
                return VoucherStatusCode.USED;
            case 4:
                return VoucherStatusCode.EXPIRE;
            default:
                throw new ServerRuntimeException("urbox status wasn't defined");
        }
    }

    private UrBoxBaseRequest createGettingVoucherStatusReq(List<String> transactionsId) {
        return new UrBoxVoucherListReq()
                .setTransaction_id(String.join(",", transactionsId))
                .setApp_id(appId)
                .setApp_secret(appSecret);
    }

}

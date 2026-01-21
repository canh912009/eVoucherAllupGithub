package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.*;
import com.evoucher.evoucherbe.config.PropertyConverter;
import com.evoucher.evoucherbe.dto.*;
import com.evoucher.evoucherbe.dto.partner_service.request.McpTransactionResult;
import com.evoucher.evoucherbe.entity.*;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.EndUserMapper;
import com.evoucher.evoucherbe.mapper.RequestMapper;
import com.evoucher.evoucherbe.message.BaseResponse;
import com.evoucher.evoucherbe.repository.*;
import com.evoucher.evoucherbe.service.typed.AbstractGoodService;
import com.evoucher.evoucherbe.service.typed.LockingService;
import com.evoucher.evoucherbe.service.typed.ServiceFactory;
import com.evoucher.evoucherbe.utils.Constant;
import com.evoucher.evoucherbe.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EVoucherProcessServiceImpl implements EVoucherProcessService {

    private final EVoucherRepository eVoucherRepository;
    private final SettlementLogRepository settlementLogRepository;
    private final EndUserRepository endUserRepository;
    private final VoucherTransferHistoryRepository transferHistoryRepository;
    private final VoucherActivateHistoryRepository activateHistoryRepository;
    private final VoucherDisableHistoryRepository voucherDisableHistoryRepository;
    private final EndUserMapper endUserMapper;
    private final PublishDetailRepository publishDetailRepository;
    private final ServiceFactory goodServiceFactory;

    private final ObjectMapper objectMapper;
    private final RequestMapper requestMapper;

    private final GoodBasicService goodService;
    private final LockingService lockingService;
    private final VoucherBasicService voucherService;
    private final PublishBasicService publishService;
    private final ExchangeHistoryService exchangeHistoryService;
    private final SettlementLogService settlementLogService;
    private final CustomerContractService customerContractService;
    private final SupplierContractService supplierContractService;
    private final CampaignService campaignService;
    private final EndUserBasicService endUserService;
    private final PublishDetailBasicService publishDetailBasicService;
    private final ActivationHistoryService activationHistoryService;
    private final PropertyConverter propertyConverter;

    @Transactional
    public BaseResponse processExchangeVouchers(List<VoucherExchangeReq> exchangeReq) throws CustomCodeException {
        try {
            log.info("use vouchers: {}", exchangeReq);
            for (VoucherExchangeReq voucherExchangeReq : exchangeReq) {
                processExchangeVoucher(voucherExchangeReq);
            }
            return new BaseResponse();
        } catch (CustomCodeException e) {
            throw e;
        } catch (Exception e) {
            log.info("======>>> PROCESS USED VOUCHERS");
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public BaseResponse processExchangeVoucher(VoucherExchangeReq exchangeReq) throws CustomCodeException {
        try {
            log.info("use voucher: {}", exchangeReq);
            lockingService.lockPurchaseChoiceVoucherProcessor(exchangeReq.getVoucherId());
            log.info("exchange voucher: {}", exchangeReq);

            VoucherDto voucher = voucherService.findDtoById(exchangeReq.getVoucherId());
            // validate voucher balance
            goodServiceFactory.getGoodTypeServiceByType(voucher.getVoucherTypeCode())
                    .getPayingService().validateBeforePay(voucher, exchangeReq.getExchangeAmount());

            PublishDto publish = publishService.findDtoById(voucher.getPublishId());
            GoodDto good = goodService.findDtoById(voucher.getGoodsId());

            log.info("call {} for paying ", voucher.getSystem());
            AbstractGoodService abstractGoodService = goodServiceFactory.getGoodServiceByType(voucher.getSystem());
            BaseResponse response = abstractGoodService
                    .processUsingVoucher(exchangeReq, voucher, publish.getGoods());

            if (response.isOk()) {
                log.info("process using voucher success, update voucher status, balance");
                voucher = voucherService.deduceBalance(voucher, exchangeReq.getExchangeAmount().longValue());
                voucher = voucherService.setUsedStatus(voucher);

                log.info("Create Exchange History for voucher: {}", exchangeReq.getVoucherId());
                var exchangeHistory = exchangeHistoryService.createExchangeHistory(exchangeReq, good, voucher);
                exchangeHistory = exchangeHistoryService.saveDto(exchangeHistory);

                createAndSaveSettlementLogForExchange(voucher, publish, exchangeHistory, good, SettlementLogType.PER_EXCHANGE);
            } else if (isVoucherProcessing(voucher.getSystem(), response)) {
                Double exchangeAmount = exchangeReq.getExchangeAmount();
                if (voucher.getSystem().equals(SystemType.VNPT_EPAY) && !exchangeReq.getExchangeAmount().equals(voucher.getBalance())) {
                    log.warn("VNPT EPay exchange amount {} should be equal to voucher balance {}", exchangeReq.getExchangeAmount(), voucher.getBalance());
                    exchangeAmount = voucher.getBalance();
                }
                voucherService.deduceBalance(voucher, exchangeAmount.longValue());
            } else {
                log.error("[{}], {}", response.getCode(), response.getMessage());
                throw new CustomCodeException(response.getMessage(), response.getCode());
            }

            return response;
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (CustomCodeException e) {
            throw e;
        } catch (Exception e) {
            log.info("======>>> PROCESS USED VOUCHER ERROR WITH EV: {}", exchangeReq.getVoucherId());
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            lockingService.unlockPurchaseChoiceVoucherProcessor(exchangeReq.getVoucherId());
        }
    }

    private static boolean isVoucherProcessing(SystemType systemType, BaseResponse response) {
        if (systemType.equals(SystemType.VNPT_EPAY) && response.isVnptProcessing()) {
            return true;
        }
        return systemType.equals(SystemType.XPAY) && response.isXpayProcessing();
    }

    @Override
    public BaseResponse processCancelingExchange(VoucherExchangeReq exchangeReq) throws CustomCodeException {
        try {
            log.info("cancel exchange voucher: {}", exchangeReq);
            VoucherDto voucher = voucherService.findDtoById(exchangeReq.getVoucherId());
            PublishDto publish = publishService.findDtoById(voucher.getPublishId());
            GoodDto good = goodService.findDtoById(voucher.getGoodsId());

            log.info("process canceling voucher success, update voucher status, balance");
            voucher = voucherService.updateExchangeCanceledVoucherStatus(voucher, exchangeReq.getExchangeAmount().longValue());

            log.info("Save cancel exchange history for ev: {}", exchangeReq.getVoucherId());
            var exchangeHistory = exchangeHistoryService.createExchangeHistoryForCanceling(exchangeReq, good, voucher);

            exchangeHistory = exchangeHistoryService.saveDto(exchangeHistory);

            createAndSaveSettlementLogForExchange(voucher, publish, exchangeHistory, good, SettlementLogType.PER_EXCHANGE_CANCEL);
            return new BaseResponse();
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (CustomCodeException e) {
            throw e;
        } catch (Exception e) {
            log.info("======>>> PROCESS Cancel VOUCHER ERROR WITH EV: {}", exchangeReq.getVoucherId());
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void createAndSaveSettlementLogForExchange(VoucherDto voucher,
                                                       PublishDto publish,
                                                       VoucherExchangeHistoryDto exchangeHistory,
                                                       GoodDto good,
                                                       SettlementLogType type) {
        CampaignDto campaign = campaignService.findDtoById(voucher.getCampaignId());
        CustomerContractDto customerContract = customerContractService.findValidById(campaign.getCustomerContractId());
        SupplierContractDto supplierContractDto = supplierContractService.findValidById(good.getSupplierContractId());
        List<SettlementLogDto> settlementLogs = SettlementLogService.createSettlementLogListForExchangeAndCancel(
                voucher,
                publish,
                exchangeHistory,
                good,
                customerContract,
                supplierContractDto
        );

        settlementLogs.forEach(o -> o.setSettlementLogType(type));
        if (!CollectionUtils.isEmpty(settlementLogs)) {
            settlementLogService.saveAllDto(settlementLogs);
            log.info("settlement saved");
        } else {
            log.warn("settlement list is empty");
        }
    }

    @Override
    @Transactional
    public BaseResponse processUsedVoucher(EVoucherHistoryProcess eVoucherHistoryProcess) throws CustomCodeException {
        try {
            log.info("======>>> START PROCESS USED VOUCHER: {}", eVoucherHistoryProcess);

            VoucherExchangeReq exchangeReq = requestMapper.toExchangeRequest(eVoucherHistoryProcess);
            // use object mapper to parse from configured timezone to system timezone
//            Date transactionDate = objectMapper.convertValue(eVoucherHistoryProcess.getTransactionDate(), Date.class);


            Date transactionDate = Constant.COMMON_DATE_FORMATTER.parse(eVoucherHistoryProcess.getTransactionDate());

            exchangeReq.setTransactionDate(transactionDate);

            switch (eVoucherHistoryProcess.getExchangeType()) {
                case USE:
                    return processExchangeVoucher(exchangeReq);
                case CANCEL:
                    return processCancelingExchange(exchangeReq);
                default:
                    log.error("exchange type is not supported: {}", eVoucherHistoryProcess.getExchangeType());
                    throw new CustomCodeException("exchange type " + eVoucherHistoryProcess.getExchangeType() + " is not supported", HttpStatus.BAD_REQUEST);
            }
            // partner service have to update balance and voucher status code
//            lockingService.lockPurchaseChoiceVoucherProcessor(eVoucherHistoryProcess.getEv());
//
//            log.info("Get eVoucher info with eVoucherId: {}", eVoucherHistoryProcess.getEv());
//            EVoucher eVoucher = eVoucherRepository.findById(eVoucherHistoryProcess.getEv())
//                    .orElseThrow(() -> new CustomCodeException(
//                            MessageUtils.getMessage("evoucher.voucher.not.found"),
//                            HttpStatus.BAD_REQUEST
//                    ));
//
//            ExchangeType exchangeType = eVoucherHistoryProcess.getExchangeType();
//
//            Publish publish = publishRepository.findById(eVoucher.getPublishId())
//                    .orElseThrow(() -> new CustomCodeException(
//                            MessageUtils.getMessage("evoucher.publish.not.found"),
//                            HttpStatus.BAD_REQUEST));
//            GoodDto goods = goodService.toDto(publish.getGoods());
//
//
//            // update evoucher in database
//            eVoucher.setLastExchangeDate(transactionDate);
//
//            if (ServiceFactory.isSupported(eVoucher.getSystem())) {
//
//                eVoucherHistoryProcess.setExchangeAmount(goods.getSellPrice());
//                eVoucherHistoryProcess.setListPrice(goods.getListPrice());
//            } else {
//                eVoucher.setBalance(eVoucher.getBalance() - eVoucherHistoryProcess.getBalance());
//                eVoucher.setVoucherStatusCode(eVoucherHistoryProcess.getVoucherStatusCode());
//            }
//
//            log.info("Save exchange history with ev: {}", eVoucherHistoryProcess.getEv());
//            var exchangeHistory = exchangeHistoryRepository.save(VoucherExchangeHistory.builder()
//                    .exchangeType(exchangeType)
//                    .transactionDate(transactionDate)
//                    .storeId(eVoucherHistoryProcess.getStoreId())
//                    .ev(eVoucher.getEV())
//                    .voucherTypeCode(eVoucher.getVoucherTypeCode())
//                    .goodsId(eVoucherHistoryProcess.getGoodsId())
//                    .goodsName(eVoucherHistoryProcess.getGoodsName())
//                    .listPrice(eVoucherHistoryProcess.getListPrice())
//                    .discountRate(eVoucherHistoryProcess.getDiscountRate())
//                    .discountAmount(eVoucherHistoryProcess.getDiscountAmount())
//                    .exchangeAmount(eVoucherHistoryProcess.getExchangeAmount())
//                    .userMobileNumber(eVoucher.getUserMobileNumber())
//                    .staffMobileNumber(eVoucherHistoryProcess.getStaffMobileNumber())
//                    .build());
//
//            log.info("Get Publish info with publishId: {}", eVoucher.getPublishId());
//
//            Campaign campaign = publish.getCampaign();
//
//            List<SettlementLog> settlementLogs = new ArrayList<>();
//            SettlementLogType settlementLogType = getSettlementLogType(exchangeType);
//            // check and save SettlementMethodCode: EXCHANGE / PER_USE_AMOUNT for Customer
//            if (SettlementMethodCode.PER_EXCHANGE.equals(publish.getSellSettlementMethodCode())
//                    || SettlementMethodCode.PER_USE_AMOUNT.equals(publish.getSellSettlementMethodCode())) {
//                log.info("Create SettlementLog for Customer with ev: {}", eVoucher.getEV());
//
//                settlementLogs.add(SettlementLogService.makeCustomerExchangeLog(
//                        eVoucher,
//                        settlementLogType,
//                        publish,
//                        goods,
//                        exchangeHistory,
//                        eVoucherHistoryProcess,
//                        eVoucher.getUserMobileNumber(),
//                        campaign.getId(),
//                        SettlementTarget.CUSTOMER
//                ));
//            }
//            // check and save SettlementMethodCode: EXCHANGE / PER_USE_AMOUNT for Supplier
//            if (SettlementMethodCode.PER_EXCHANGE.equals(goods.getSettlementMethodCode())
//                    || SettlementMethodCode.PER_USE_AMOUNT.equals(goods.getSettlementMethodCode())) {
//                log.info("Create SettlementLog for Customer with ev: {}", eVoucher.getEV());
//
//                settlementLogs.add(SettlementLogService.makeSupplierExchangeLog(
//                        eVoucher,
//                        settlementLogType,
//                        publish,
//                        exchangeHistory,
//                        goods,
//                        eVoucherHistoryProcess,
//                        eVoucher.getUserMobileNumber(),
//                        campaign.getId(),
//                        SettlementTarget.SUPPLIER
//                ));
//            }
//            if (!CollectionUtils.isEmpty(settlementLogs)) {
//                log.info("Save List settlementLogs with ev: {}", eVoucher.getEV());
//                settlementLogRepository.saveAll(settlementLogs);
//            }
//
//            BaseResponse response = new BaseResponse();
//            if (ServiceFactory.isSupported(eVoucher.getSystem())) {
//                // partner service have to update balance and voucher status code
//                lockingService.lockPurchaseChoiceVoucherProcessor(eVoucher.getEV());
//                try {
//                    response = goodServiceFactory.getGoodServiceByType(eVoucher.getSystem())
//                            .processUsingVoucher(eVoucherHistoryProcess, eVoucher, goods);
//                    if (!response.isOk()) {
//                        log.error("[{}], {}", response.getCode(), response.getMessage());
//                        throw new CustomCodeException(response.getMessage(), response.getCode());
//                    }
//                } finally {
//                    lockingService.unlockPurchaseChoiceVoucherProcessor(eVoucher.getEV());
//                }
//            }
//            log.info("process using voucher success, update voucher status, balance");
//            eVoucherRepository.save(eVoucher);
//            return response;
        } catch (CustomCodeException e) {
            throw e;
        } catch (Exception e) {
            log.info("======>>> PROCESS USED VOUCHER ERROR WITH EV: {}", eVoucherHistoryProcess.getEv());
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public BaseResponse updateVoucherStatus(List<UpdatingVoucherReq> request) {
        try {
            List<String> expiredIds = request.stream().filter(o -> o.getUpdatingType() == UpdatingVoucherType.EXPIRED)
                    .map(UpdatingVoucherReq::getVoucherId).collect(Collectors.toList());

            VoucherJobResultDTO expiredDto = new VoucherJobResultDTO();
            expiredDto.setVoucherIds(expiredIds);
            expiredDto.setRequestDate("");
            expiredDto.setRequestType(EnumVoucherJobType.EXPIRE.name());

            log.info("update voucher : {} status to expired", expiredIds);
            processUpdateVoucher(expiredDto);

            request.stream().filter(o -> o.getUpdatingType() == UpdatingVoucherType.USING).forEach(o -> {
                log.info("process use voucher: {}", o);
                processExchangeVoucher(o);
            });
            return new BaseResponse();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handleMcpTransactionResultReceived(McpTransactionResult vnptTransactionResult) {
        VoucherDto voucher = voucherService.findDtoById(vnptTransactionResult.getEv());
        if (McpTransactionResultEnum.SUCCESS == vnptTransactionResult.getResult()) { // Success
            voucherService.setUsedStatus(voucher);
        } else { // failed
            voucherService.topupBalance(voucher, voucher.getInitAmount().longValue());
        }
    }

    @Override
    @Transactional
    public BaseResponse receiveVoucher(EVoucherTransferProcess voucherTransferProcess) throws CustomCodeException {
        log.info("Start receiving voucher: {}", voucherTransferProcess.getToEv());

        String evOld = voucherTransferProcess.getFromEv();
        String evNew = voucherTransferProcess.getToEv();
        try {
            log.info("Get eVoucher Old info with eVoucherId: {}", evOld);
            EVoucher eVoucherOld = eVoucherRepository.findById(evOld)
                    .orElseThrow(() -> new CustomCodeException(
                            MessageUtils.getMessage("evoucher.voucher.not.found"),
                            HttpStatus.BAD_REQUEST
                    ));

            log.info("Get eVoucher New info with eVoucherId: {}", evNew);
            EVoucher eVoucherNew = eVoucherRepository.findById(evNew)
                    .orElseThrow(() -> new CustomCodeException(
                            MessageUtils.getMessage("evoucher.voucher.not.found"),
                            HttpStatus.BAD_REQUEST
                    ));

            TransferStatusCode transferStatusCode = TransferStatusCode.valueOf(voucherTransferProcess.getTransferStatusCode());
            switch (transferStatusCode) {
                // nếu là đồng ý
                case RECPTED: {
                    log.info("Update endUser receipt voucher");
                    EndUser endUser = endUserMapper.toEntity(voucherTransferProcess.getEndUser());
                    endUserRepository.save(endUser);

                    eVoucherNew.setVoucherStatusCode(VoucherStatusCode.NORMAL);
                    eVoucherNew.setTransferStatusCode(TransferStatusCode.RECPTED);
                    eVoucherNew.setUserName(endUser.getUserNm());
                    log.info("Update status NORMAL to new voucher with ev: {}", evNew);
                    eVoucherRepository.save(eVoucherNew);

                    eVoucherOld.setTransferDate(new Date());
                    log.info("Update old voucher with ev: {}", evOld);
                    eVoucherRepository.save(eVoucherOld);
                    break;
                }
                // nếu không đồng ý hoặc hết hạn
                case RETURN: {
                    eVoucherOld.setVoucherStatusCode(VoucherStatusCode.NORMAL);
                    eVoucherOld.setTransferStatusCode(null);
                    log.info("Update status NORMAL to old voucher with ev: {}", evOld);
                    eVoucherRepository.save(eVoucherOld);

                    eVoucherNew.setVoucherStatusCode(VoucherStatusCode.DISABLED);
                    eVoucherNew.setTransferStatusCode(TransferStatusCode.RETURN);
                    log.info("Update status DISABLED to new voucher with ev: {}", evNew);
                    eVoucherRepository.save(eVoucherNew);
                    break;
                }
                default:
                    throw new CustomCodeException(MessageUtils.getMessage("evoucher.voucher.transfer.status.invalid"),
                            HttpStatus.BAD_REQUEST);
            }

            log.info("Create transfer history with fromEv: {} and toEv: {}", evOld, evNew);
            Date transactionDate = new Date();

            VoucherTransferHistory history = VoucherTransferHistory.builder()
                    .transferStatusCode(transferStatusCode)
                    .transactionDate(transactionDate)
                    .fromVoucherShortLink(eVoucherOld.getShortLink())
                    .fromEv(evOld)
                    .fromUserId(eVoucherOld.getUserId())
                    .fromMobileNumber(eVoucherOld.getUserMobileNumber())
                    .toVoucherShortLink(eVoucherOld.getShortLink())
                    .toEv(evNew)
                    .toMobileNumber(eVoucherNew.getUserMobileNumber())
                    .toUserId(eVoucherNew.getUserId())
                    .voucherTypeCode(VoucherTypeCode.valueOf(voucherTransferProcess.getVoucherTypeCode()))
                    .initAmount(voucherTransferProcess.getInitAmount())
                    .transferAmount(voucherTransferProcess.getTransferAmount())
                    .build();

            if (transferStatusCode == TransferStatusCode.RECPTED) {
                history.setReceiptConfirmDatetime(transactionDate);
            } else {
                history.setReturnDate(transactionDate);
            }

            transferHistoryRepository.save(history);

            return new BaseResponse();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * activation version 2 is temporary for email type only
     *
     * @param request: activation request
     * @throws CustomCodeException when
     */
    @Override
    @Transactional
    public BaseResponse processActivateVoucher(EVoucherActivateProcess request) {
        log.info("activate voucher: {}", request.toString());

        try {
            log.info("Get voucher info with ev={}", request.getEv());
            EVoucher dbVoucher = voucherService.findById(request.getEv());
            PublishDetail dbPublishDetail = publishDetailBasicService.findById(dbVoucher.getPublishDetailId());

            // End user create
            log.info("Get EndUser info with phoneNumber: {}", request.getPhoneNumber());
            EndUser endUser;
            if (dbVoucher.getUserId() == null) {
                log.info("Can not find user from db, create new User mobileNo={}", request.getPhoneNumber());
                endUser = EndUser.builder()
                        .userMobileNum(request.getPhoneNumber())
                        .userNm(request.getUserName())
                        .build();
                endUser = endUserService.save(endUser);
                dbVoucher.setUserId(endUser.getId());
                dbPublishDetail.setUserId(endUser.getId());
                log.info("Create new user. name={},phone={}", request.getUserName(), request.getPhoneNumber());
            } else {
                endUser = endUserService.findById(dbVoucher.getUserId());
                log.info("update user {} phone number from {} to {}", endUser.getId(), endUser.getUserMobileNum(), request.getPhoneNumber());
                endUser.setUserMobileNum(request.getPhoneNumber());
                endUser.setUserNm(request.getUserName());

                endUserService.save(endUser);
            }

            // Associate voucher with endUser
            Date activationDate = new Date();
            dbVoucher.setActivationDate(activationDate);
            dbVoucher.setUserName(request.getUserName());
            dbVoucher.setUserMobileNumber(request.getPhoneNumber());
            dbVoucher.setActivationDate(activationDate);
            log.info("Update voucher owner: ev={},user={}", dbVoucher.getEV(), request.getUserName());
            eVoucherRepository.save(dbVoucher);


            dbPublishDetail.setReceiverMobileNo(request.getPhoneNumber());
            publishDetailRepository.save(dbPublishDetail);

            // Save activate history
            log.info("Create activation history with ev={} and serial={}", request.getEv(), request.getSerialNumber());
            activateHistoryRepository.save(VoucherActivateHistory.builder()
                    .ev(request.getEv())
                    .serialNumber(request.getSerialNumber())
                    .userName(request.getUserName())
                    .phoneNumber(request.getPhoneNumber())
                    .build());

            // Save settlement history
            List<SettlementLog> logs = settlementLogRepository.getSettlementLogByEv(request.getEv());
            List<SettlementLog> publishLogs = logs.stream()
                    .filter(log -> SettlementMethodCode.PER_PUBLISH.equals(log.getSettlementMethodCode())).collect(Collectors.toList());
            log.info("Update {} settlement logs for PER_PUBLISH ev={}", publishLogs.size(), request.getEv());
            publishLogs.forEach(log -> {
                log.setActivationDate(activationDate);
                log.setUserMobileNumber(request.getPhoneNumber());
            });
            settlementLogRepository.saveAll(publishLogs);
            return new BaseResponse();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
//            throw new HandleMessageQueueException("======>>> PROCESS ACTIVATE VOUCHER ERROR WITH ev=" + request.getEv()
//                    + ", phone=" + request.getPhoneNumber());
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * activation version 2 is temporary for email type only
     *
     * @param request: activation request
     * @throws CustomCodeException when
     */
    @Override
    @Transactional
    public void processActivateVoucher(ActivationRequest request) throws EntityNotFoundException, CustomCodeException {
        log.info("activate voucher: {}", request);

        try {
            Date activationDate = new Date();
            // update voucher activation info and get
            VoucherDto dbVoucher = voucherService.updateActivationAndGet(request, activationDate);

            // update end user phone number
            EndUserDto endUser = endUserService.updatePhoneNumberAndGet(dbVoucher.getUserId(), request.getPhoneNumber());

            // update publish detail user mobile number
            publishDetailBasicService.updateReceiveMobileNumber(dbVoucher.getPublishDetailId(), request.getPhoneNumber());

            // Save activate history
            log.info("Create activation history with ev={} and serial={}", request.getEv(), request.getSerialNumber());
            activationHistoryService.createNewHistory(request, endUser);

            // update activation date of per_publish type settlement log
            settlementLogService.updateActivationInfoForPublishLogByEv(request.getEv(), activationDate, request.getPhoneNumber());
        } catch (EntityNotFoundException | CustomCodeException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void processUpdateVoucher(VoucherJobResultDTO voucherJobResultDTO) {
        List<String> expiredVoucherIds = voucherJobResultDTO.getVoucherIds();
        log.info("List voucher expired: {}", String.join(",", expiredVoucherIds));

        if (CollectionUtils.isEmpty(voucherJobResultDTO.getVoucherIds())) {
            log.error("List voucher Ids is empty!");
            return;
        }

        EnumVoucherJobType requestType = EnumVoucherJobType.valueOf(voucherJobResultDTO.getRequestType());
        if (!EnumVoucherJobType.EXPIRE.equals(requestType)) {
            log.error("Voucher job type is not expire: {}", requestType);
            return;
        }

        List<EVoucher> vouchers = expiredVoucherIds.stream()
                .filter(voucherId -> {
                    boolean isExists = eVoucherRepository.existsById(voucherId);
                    if (!isExists) {
                        log.error("Voucher not found with voucherId: {}", voucherId);
                    }
                    return isExists;
                })
                .map(voucherId -> {
                    EVoucher voucher = eVoucherRepository.findById(voucherId).get();
                    voucher.setVoucherStatusCode(VoucherStatusCode.EXPIRE);
                    return voucher;
                })
                .collect(Collectors.toList());

        log.info("Update status list voucher Expired");
        eVoucherRepository.saveAll(vouchers);
    }

    @Override
    public void processDisableVoucherResult(VoucherDisableProcessResponse response) {
        try {
            VoucherDisableHistory voucherDisableHistory =
                    updateResultToVoucherDisableHistory(response);

            if (CompletedStatusCode.FAILED.equals(response.getFrontEndUpdateResult())) {
                log.info("Backup previous voucher status with front end response: {}", response);
                backupVoucherStatusWhenFrontEndDisableFailed(response, voucherDisableHistory);
            }
        } catch (Exception e) {
            log.error("Error update Disable Voucher result: {}", e.getMessage(), e);
        }
    }

    private void backupVoucherStatusWhenFrontEndDisableFailed(
            VoucherDisableProcessResponse response,
            VoucherDisableHistory voucherDisableHistory) {
        String ev = response.getEv();
        log.info("Get eVoucher info with eVoucherId: {}", ev);
        EVoucher eVoucher = eVoucherRepository.findById(ev)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Evoucher not found with ev: " + ev));

        if (confirmConditionsToBackupPreviousStatusVoucher(voucherDisableHistory, eVoucher)) {
            log.info("Can not backup previous status voucher with ev: {}", ev);
            return;
        }

        log.info("Update voucher status is disable with ev: {}", ev);
        eVoucher.setVoucherStatusCode(voucherDisableHistory.getBackEndPreviousStatusCode());
        eVoucherRepository.save(eVoucher);
    }

    private boolean confirmConditionsToBackupPreviousStatusVoucher(
            VoucherDisableHistory voucherDisableHistory,
            EVoucher eVoucher) {
        if (CompletedStatusCode.FAILED.equals(voucherDisableHistory.getBackEndUpdateResult())) {
            log.info("Back end update result is FAILED");
            return true;
        }
        if (!VoucherStatusCode.DISABLED.equals(eVoucher.getVoucherStatusCode())) {
            log.info("Current voucher status is not DISABLE");
            return true;
        }
        return false;
    }

    private VoucherDisableHistory updateResultToVoucherDisableHistory(VoucherDisableProcessResponse response) {
        Integer voucherDisableHistoryId = response.getVoucherDisableHistoryId();
        log.info("Find VoucherDisableHistory with Id: {}", voucherDisableHistoryId);
        VoucherDisableHistory voucherDisableHistory =
                voucherDisableHistoryRepository.findById(voucherDisableHistoryId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "VoucherDisableHistory is empty with id: " + voucherDisableHistoryId));

        if (!response.getEv().equals(voucherDisableHistory.getEv())) {
            throw new IllegalArgumentException(
                    "Result ev: "
                            + response.getEv()
                            + "not equal VoucherDisableHistory: "
                            + voucherDisableHistory.getEv());
        }

        voucherDisableHistory.setFrontEndUpdateResult(response.getFrontEndUpdateResult());
        voucherDisableHistory.setFrontEndPreviousStatusCode(response.getFrontEndPreviousStatusCode());

        log.info("Update VoucherDisableHistory with Id: {}", voucherDisableHistoryId);
        voucherDisableHistory = voucherDisableHistoryRepository.save(voucherDisableHistory);

        return voucherDisableHistory;
    }
}

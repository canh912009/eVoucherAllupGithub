package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.*;
import com.evoucher.evoucherbe.config.PropertyConverter;
import com.evoucher.evoucherbe.dto.*;
import com.evoucher.evoucherbe.entity.*;
import com.evoucher.evoucherbe.exception.ChoiceVoucherProcessException;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.EVoucherMapper;
import com.evoucher.evoucherbe.repository.*;
import com.evoucher.evoucherbe.service.remote.ShortURLService;
import com.evoucher.evoucherbe.service.request.*;
import com.evoucher.evoucherbe.service.typed.LockingService;
import com.evoucher.evoucherbe.service.typed.ServiceFactory;
import com.evoucher.evoucherbe.utils.*;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EVoucherServiceImpl implements EVoucherService {

    private final EndUserRepository endUserRepository;
    private final Gson gson;
    @Value("${queues.disable_voucher.queue}")
    private String queueDisableVoucher;

    private final EVoucherRepository eVoucherRepository;
    private final CustomerContractRepository customerContractRepository;
    private final SupplierContractRepository supplierContractRepository;
    private final PublishRepository publishRepository;
    private final ExternalPinRepository externalPinRepository;
    private final SettlementLogRepository settlementLogRepository;
    private final VoucherTransferHistoryRepository transferHistoryRepository;
    private final PublishDetailRepository publishDetailRepository;
    private final GoodsRepository goodsRepository;
    private final VoucherDisableHistoryRepository voucherDisableHistoryRepository;
    private final VoucherChoiceHistoryService voucherChoiceHistoryService;
    private final ShortURLService shortURLService;
    private final VoucherServiceCommon voucherServiceCommon;
    private final RabbitTemplate rabbitTemplate;

    private final PublishBasicService publishBasicService;
    private final ServiceFactory serviceFactory;
    private final LockingService lockingService;
    private final GoodBasicService goodService;
    private final CustomerBasicService customerBasicService;
    private final EndUserBasicService endUserBasicService;
    private final VoucherBasicService voucherBasicService;
    public final PublishDetailBasicService publishDetailBasicService;
    private final BrandRepository brandRepository;

    private final EVoucherMapper eVoucherMapper;
    private final PropertyConverter converter;

    @Override
    @Transactional
    public List<String> generateEachVoucher(PublishRequest publishRequest) {
        log.info("Start generate voucher with publishDetailId: {}", publishRequest.getPublishId());

        Integer publishId = publishRequest.getPublishId();
        List<PublishDetailRequest> publishDetails = publishRequest.getPublishDetails();

        log.info("Get Publish info with publishId: {}", publishId);
        Publish publish = publishBasicService.findById(publishId);

        if (!PublishStatusCode.PUBLISHING.name().equals(publish.getPublishStatusCode())) {
            log.error("status: {} is not allowed to create voucher ", publish.getPublishStatusCode());
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.publish.status.not.create.voucher"),
                    HttpStatus.BAD_REQUEST);
        }

        Campaign campaign = publish.getCampaign();
        validateCampaign(campaign);

        GoodDto goods = goodService.toDto(publish.getGoods());
        validateGoods(goods);

        log.info("Get customer contract info with contractId: {}", campaign.getCustomerContractId());
        CustomerContract customerContract = customerContractRepository.findByIdAndValidYn(campaign.getCustomerContractId(), EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.customerContract.not.found"),
                        HttpStatus.BAD_REQUEST));

        log.info("Get supplier contract info with contractId: {}", goods.getSupplierContractId());
        SupplierContract supplierContract = supplierContractRepository.findByIdAndValidYn(goods.getSupplierContractId(), EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage("evoucher.supplierContract.not.found"),
                        HttpStatus.BAD_REQUEST));

        VoucherGenerateInfo voucherGenerateInfo = new VoucherGenerateInfo();
        try {
            List<PublishDetailRequest> publishDetailsCopy = new ArrayList<>(publishDetails);

            // Activation URL all the same for each publish

            // chia 3 luồng mỗi luồng chạy 1 publishDetails => tạo voucher cho từng số điện thoại
            while (!CollectionUtils.isEmpty(publishDetailsCopy) && publishDetailsCopy.size() >= 3) {
                CompletableFuture<Boolean> thread1 = createVoucher(campaign, publish, customerContract, supplierContract,
                        goods, getLastPublishDetailAndDelete(publishDetailsCopy), voucherGenerateInfo);
                CompletableFuture<Boolean> thread2 = createVoucher(campaign, publish, customerContract, supplierContract,
                        goods, getLastPublishDetailAndDelete(publishDetailsCopy), voucherGenerateInfo);
                CompletableFuture<Boolean> thread3 = createVoucher(campaign, publish, customerContract, supplierContract,
                        goods, getLastPublishDetailAndDelete(publishDetailsCopy), voucherGenerateInfo);

                CompletableFuture.allOf(thread1, thread2, thread3).join();
            }

            // nếu danh sách số điện thoại hết hoặc nhỏ hơn 2 thì tạo từng cái
            for (PublishDetailRequest publishDetail : publishDetailsCopy) {
                CompletableFuture<Boolean> createVoucher = createVoucher(campaign, publish, customerContract, supplierContract,
                        goods, publishDetail, voucherGenerateInfo);
                createVoucher.get();
            }

            // get list voucher
            List<EVoucher> voucherResult = voucherGenerateInfo.getEVouchers();

            // save voucher
            log.info("Save List EVoucher with publishId: {}", publishId);
            voucherResult = eVoucherRepository.saveAll(voucherResult);

            log.info("Save list SettlementLog with publishId: {}", publishId);
            settlementLogRepository.saveAll(voucherGenerateInfo.getSettlementLogs());

            return voucherResult.stream().map(EVoucher::getEV).collect(Collectors.toList());
        } catch (Exception e) {
            log.info("Error generate voucher with publishId: {}", publishId);
            log.error(e.getMessage(), e);

            log.info("Delete list url shortLink");
            deleteListUrlShortLink(voucherGenerateInfo.getUrlShortLinks());

            // Clean up whatever needs to be handled before interrupting */
            Thread.currentThread().interrupt();

            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.voucher.create.error"),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Override
    @Transactional
    public void revertListVoucher(List<String> evs) {
        // delete list voucher
        log.info("Find list eVoucher");
        List<EVoucher> eVoucherList = eVoucherRepository.findAllByEVIn(evs);
        log.info("List EVoucher found size: {} and data: {}", eVoucherList.size(), Constant.gson.toJson(eVoucherList));
        if (eVoucherList.isEmpty()) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.voucher.not.found"),
                    HttpStatus.BAD_REQUEST);
        }

        // delete list short URL link
        List<String> shortUrlLinkList = eVoucherList.stream()
                .map(EVoucher::getShortLink).collect(Collectors.toList());
        log.info("Delete list url shortLink");
        deleteListUrlShortLink(shortUrlLinkList);

        // delete settlement log
        log.info("Delete list settlement log");
        settlementLogRepository.deleteAllByEvIn(evs);

        // Delete list voucher
        log.info("Delete list voucher");
        eVoucherRepository.deleteAll(eVoucherList);
    }

    @Override
    @Transactional
    public String voucherHandover(VoucherHandoverRequest voucherHandoverRequest) throws EntityNotFoundException, CustomCodeException {
        String evOld = voucherHandoverRequest.getEvOld();
        String evNew = UUID.randomUUID().toString();
        String shortLink = null;

        // find voucher old
        log.info("Find old voucher with ev: {}", evOld);
        EVoucher voucherOld = voucherBasicService.findById(evOld);
        PublishDetailDto publishDetail =
                publishDetailBasicService.findDtoById(voucherHandoverRequest.getPublishDetailId());

        PublishDto publish = publishBasicService.findDtoById(publishDetail.getPublishId());

        // validate voucher status(voucher còn hạn không)
        if (!VoucherStatusCode.NORMAL.equals(voucherOld.getVoucherStatusCode())
                && !VoucherStatusCode.PART_USED.equals(voucherOld.getVoucherStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.voucher.status.not.suitable.for.transfer"),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            // disable old voucher
            voucherOld.setVoucherStatusCode(VoucherStatusCode.DISABLED);
            voucherOld.setTransferStatusCode(TransferStatusCode.TRANSFER);

            log.info("Update voucher old with ev: {}", evOld);
            voucherOld = eVoucherRepository.save(voucherOld);

            // create endUser new
//            EndUserRequest endUserRequest = voucherHandoverRequest.getEndUser();
//            endUserBasicService.saveUserByUserRequest(endUserRequest);
            EndUser newEndUser = endUserRepository.findById(publishDetail.getUserId())
                    .orElseThrow(() -> new CustomCodeException(String.format("Can not find voucher by end user id=%s", publishDetail.getUserId())
                            , HttpStatus.INTERNAL_SERVER_ERROR));

            log.info("Found new end user: {}", gson.toJson(newEndUser));
            // create new voucher
            EVoucher voucherNew = eVoucherMapper.copyVoucher(voucherOld);
            shortLink = shortURLService.createShortURLForEVoucher(evNew);


            voucherNew.setEV(evNew);
            voucherNew.setShortLink(shortLink);
            voucherNew.setUserMobileNumber(newEndUser.getUserMobileNum());
            voucherNew.setUserName(newEndUser.getUserNm());
            voucherNew.setUserId(publishDetail.getUserId());
            voucherNew.setPublishDetailId(voucherHandoverRequest.getPublishDetailId());
            voucherNew.setVoucherStatusCode(VoucherStatusCode.DISABLED);
            voucherNew.setTransferStatusCode(TransferStatusCode.RECPT_WAIT);
            voucherNew.setOriginalEv(evOld);
            voucherNew.setTransferMessage(voucherHandoverRequest.getTransferMessage());
            voucherNew.setParentVoucherEv(null);
            voucherNew.setParentVoucherToken(null);
            voucherNew.setPublishDate(new Date());
            voucherNew.setVoucherVersion(SMSType.DOWNLOAD.equals(publish.getSmsType()) ? Constant.Common.VERSION_1 : Constant.Common.VERSION_2);

            log.info("Create voucher new with ev: {}, version: {}", evNew, voucherNew.getVoucherVersion());
            voucherNew = eVoucherRepository.save(voucherNew);

            // save transfer history
            log.info("Create transfer history with fromEv: {} and toEv: {}", evOld, evNew);
            transferHistoryRepository.save(VoucherTransferHistory.builder()
                    .transferStatusCode(TransferStatusCode.RECPT_WAIT)
                    .transactionDate(new Date())
                    .fromVoucherShortLink(voucherOld.getShortLink())
                    .fromEv(evOld)
                    .fromMobileNumber(voucherOld.getUserMobileNumber())
                    .fromUserId(voucherOld.getUserId())
                    .toVoucherShortLink(voucherNew.getShortLink())
                    .toEv(evNew)
                    .toMobileNumber(newEndUser.getUserMobileNum())
                    .toUserId(voucherNew.getUserId())
                    .voucherTypeCode(voucherOld.getVoucherTypeCode())
                    .initAmount(voucherOld.getInitAmount())
                    .transferAmount(!Objects.isNull(voucherOld.getBalance()) ? voucherOld.getBalance() : 0)
                    .build());

            return evNew;
        } catch (Exception e) {
            if (StringUtils.isNotBlank(shortLink)) {
                log.info("Delete url shortLink: {}", shortLink);
                shortURLService.deleteUrlShortLink(shortLink);
            }
            log.error("Handover voucher error with ev: {}", evOld, e);
            throw new CustomCodeException("Handover voucher error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<String> processCreateChoiceVoucher(VoucherChoiceRequest voucherChoiceRequest) {
        // lock voucher purchasing
        lockingService.lockPurchaseChoiceVoucherProcessor(voucherChoiceRequest.getParentId());

        VoucherChoiceHistory choiceHistory = new VoucherChoiceHistory(voucherChoiceRequest);
        VoucherGenerateInfo voucherGenerateInfo = new VoucherGenerateInfo();
        List<ExternalPin> listExternalPinSelected = new ArrayList<>();
        HashMap<Long, List<Long>> processingPins = new HashMap<>();

        try {
            log.info("Find Evoucher by ev={}", voucherChoiceRequest.getParentId());
            EVoucher voucherParent = voucherBasicService.findById(voucherChoiceRequest.getParentId());
            //update voucher last exchange date

            if (!voucherChoiceRequest.getToken().equals(voucherParent.getParentVoucherToken())) {
                log.info("Choice token is incorrect");
                throw new ChoiceVoucherProcessException(
                        MessageUtils.getMessage("evoucher.voucher.choice.token.invalid"),
                        ErrorCode.INVALID_TOKEN);
            }

            var goodsChoiceList = voucherChoiceRequest.getProducts();

            double balance = voucherParent.getBalance();
            Map<Long, GoodDto> goodMap = goodService.getGoodMapByIdIn(
                    goodsChoiceList.stream()
                            .map(VoucherChoiceRequest.VoucherGoodsChoiceRequest::getGoodsId)
                            .collect(Collectors.toList()
                            )
            );
            double totalBalanceRequest = goodsChoiceList.stream()
                    .mapToDouble(goodsChoice -> {
                        log.info("Find goods choice with goodsId={}", goodsChoice.getGoodsId());
                        GoodDto goods = Optional.ofNullable(goodMap.get(goodsChoice.getGoodsId()))
                                .orElseThrow(() -> new CustomCodeException(
                                        MessageUtils.getMessage("evoucher.goods.not.found"),
                                        ErrorCode.GOOD_NOT_FOUND));
                        return DataUtils.roundingNumber(goodsChoice.getQuantity() * goods.getSellPrice());
                    })
                    .sum();


            if (totalBalanceRequest > balance) {
                log.info("Total amount request exceeds balance");
                throw new ChoiceVoucherProcessException(
                        MessageUtils.getMessage("evoucher.voucher.amount.request.exceeds.balance"),
                        ErrorCode.VOUCHER_LIMIT_AMOUNT);
            }

            log.info("Get Publish info with publishId={}", voucherParent.getEV());
            Publish publish = publishRepository.findById(voucherParent.getPublishId())
                    .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.publish.not.found"),
                            HttpStatus.BAD_REQUEST));

            Campaign campaign = publish.getCampaign();

            log.info("Get PublishDetail with publishDetailId={}", voucherParent.getPublishDetailId());
            PublishDetail publishDetail = publishDetailRepository.findById(voucherParent.getPublishDetailId())
                    .orElseThrow(() -> new CustomCodeException(
                            MessageUtils.getMessage("evoucher.publish.detail.not.found"),
                            ErrorCode.PUBLISH_NOT_FOUND));


            CustomerDto customer = customerBasicService.getValidCustomerById(publish.getCustomerId());
//            EndUserDto endUser = endUserBasicService.findPublishUser(publishDetail.getReceiverMobileNo(), publish.getSmsType(), customer.getCustomerName());
            EndUserDto endUser = endUserBasicService.findDtoById(publishDetail.getUserId());

            choiceHistory.setUserMobileNumber(endUser.getUserMobileNum());
            choiceHistory.setUserName(endUser.getUserNm());

            log.info("Get customer contract info with contractId: {}", campaign.getCustomerContractId());
            CustomerContract customerContract = customerContractRepository.findByIdAndValidYn(campaign.getCustomerContractId(), EnumValidYn.Y)
                    .orElseThrow(() -> new CustomCodeException(
                            MessageUtils.getMessage("evoucher.customerContract.not.found"),
                            ErrorCode.CUS_CONTRACT_NOT_FOUND));

            List<SettlementLog> settlementLogList = new ArrayList<>();

            goodsChoiceList.forEach(
                    goodsChoice -> {
                        if (!processingPins.containsKey(goodsChoice.getGoodsId())) {
                            processingPins.put(goodsChoice.getGoodsId(), new ArrayList<>());
                        }
                        log.info("Find goods choice with id: {}", goodsChoice.getGoodsId());
                        GoodDto goods = goodMap.get(goodsChoice.getGoodsId());

                        // validate good expire date
                        if (!GoodBasicService.EXTERNAL_GOOD.contains(goods.getSystem())) {
                            Date expiredDate = getGoodEndDate(goods.getPeriodType(), goods.getPeriodTerm(), goods.getPeriodExpireDate());
                            if (new Date().after(expiredDate)) {
                                log.error("good {} is expired at {}", goods.getId(), expiredDate);
                                throw new CustomCodeException(
                                        MessageUtils.getMessage(GoodBasicService.ErrorString.EXPIRE_GOOD),
                                        GoodBasicService.ErrorCode.EXPIRED_GOOD,
                                        HttpStatus.BAD_REQUEST);
                            }
                        }

                        log.info("Get supplier contract info with contractId: {}", goods.getSupplierContractId());
                        SupplierContract supplierContract = supplierContractRepository.findByIdAndValidYn(goods.getSupplierContractId(), EnumValidYn.Y)
                                .orElseThrow(() -> new CustomCodeException(
                                        MessageUtils.getMessage("evoucher.supplierContract.not.found"),
                                        ErrorCode.SUPP_CONTRACT_NOT_FOUND));

                        List<ExternalPin> externalPins = null;
                        Integer goodsChoiceQuantity = goodsChoice.getQuantity();

                        if (serviceFactory.isThirdPartyType(goods.getSystem())) {
                            externalPins = serviceFactory.
                                    getThirdPartyPinServiceByType(goods.getSystem())
                                    .getPinsForUsing(goods.getId(), goodsChoiceQuantity, new Date());

                            processingPins.get(goods.getId()).addAll(externalPins.stream().map(ExternalPin::getId).collect(Collectors.toList()));

                            listExternalPinSelected.addAll(externalPins);
                            if (CollectionUtils.isEmpty(externalPins) || externalPins.size() < goodsChoiceQuantity) {
                                log.error("pin is not enough {} < {}", externalPins.size(), goodsChoiceQuantity);
                                throw new ChoiceVoucherProcessException(
                                        MessageUtils.getMessage("evoucher.external.pin.not.found", goods.getGoodsName()),
                                        ErrorCode.PRODUCT_QUANTITY_NOT_ENOUGH);
                            }
                        }

                        generateListChoiceVoucher(
                                voucherGenerateInfo,
                                goodsChoiceQuantity,
                                voucherParent,
                                goods,
                                publish,
                                campaign,
                                publishDetail,
                                endUser,
                                customerContract,
                                supplierContract,
                                externalPins);

                    });
            // update balance of parent voucher
            serviceFactory.getGoodTypeServiceByType(voucherParent.getVoucherTypeCode())
                    .getPayingService().doPaying(voucherParent, totalBalanceRequest);

//            voucherParent.setBalance(DataUtils.roundingNumber(balance - totalBalanceRequest));
            log.info("voucher balance: {}", DataUtils.roundingNumber(balance - totalBalanceRequest));

            List<EVoucher> voucherResult = voucherServiceCommon
                    .saveInformationOfVoucherChoice(voucherGenerateInfo, voucherParent);

            log.info("Update ExternalPin's status to USED");
            updateExternalPinToUsed(listExternalPinSelected);

            choiceHistory.setStatus(VoucherChoiceHistoryStatus.SUCCESS);

            log.info("settlement log: {}", new Gson().toJson(settlementLogList));
            settlementLogRepository.saveAll(settlementLogList);

            return voucherResult.stream().map(EVoucher::getEV).collect(Collectors.toList());
        } catch (Exception e) {
            log.info("Error generate voucher choice with ev: {}", voucherChoiceRequest.getParentId());
            log.error(e.getMessage(), e);

            log.info("Process create choice voucher error: {}", e.getMessage());
            choiceHistory.setDescription(e.getMessage());
            choiceHistory.setStatus(VoucherChoiceHistoryStatus.FAIL);

            log.info("Delete list url shortLink");
            deleteListUrlShortLink(voucherGenerateInfo.getUrlShortLinks());

            log.info("Convert list external pin");
            convertExternalPinStatus(listExternalPinSelected);

            throw e;
        } finally {
            log.info("Save choice history for ev: {}", voucherChoiceRequest.getParentId());
            voucherChoiceHistoryService.saveVoucherChoiceHistory(choiceHistory);
            // release marked as processing pins
            for (Map.Entry<Long, List<Long>> entry : processingPins.entrySet()) {
                lockingService.removePrcDonePins(entry.getKey(), entry.getValue());
            }

            // release voucher purchasing
            lockingService.unlockPurchaseChoiceVoucherProcessor(voucherChoiceRequest.getParentId());
        }
    }

    @Override
    public List<String> processCreateChildVouchersV2(ChildVoucherRequest request) {
        log.info("Start creating child vouchers of {}", request.getParentId());
        // lock voucher purchasing
        lockingService.lockPurchaseChoiceVoucherProcessor(request.getParentId());

        VoucherChoiceHistory choiceHistory = new VoucherChoiceHistory(request);
        VoucherGenerateInfo voucherGenerateInfo = new VoucherGenerateInfo();
        List<ExternalPin> listExternalPinSelected = new ArrayList<>();
        HashMap<Long, List<Long>> processingPins = new HashMap<>();

        try {
            EVoucher voucherParent = voucherBasicService.findById(request.getParentId());
            //update voucher last exchange date

            var goodsChoiceList = request.getProducts();

            double balance = voucherParent.getBalance();
            Map<Long, GoodDto> goodMap = goodService.getGoodMapByIdIn(
                    goodsChoiceList.stream()
                            .map(ChildVoucherGoodRequest::getGoodsId)
                            .collect(Collectors.toList()
                            )
            );
            double totalBalanceRequest = goodsChoiceList.stream()
                    .mapToDouble(goodsChoice -> {
                        GoodDto goods = Optional.ofNullable(goodMap.get(goodsChoice.getGoodsId()))
                                .orElseThrow(() -> goodService.getNotFoundException(goodsChoice.getGoodsId()));
                        return DataUtils.roundingNumber(goodsChoice.getQuantity() * goods.getSellPrice());
                    })
                    .sum();


            if (totalBalanceRequest > balance) {
                log.error("Total amount requested ({}) exceeds parent voucher balance ({})", totalBalanceRequest, balance);
                throw new ChoiceVoucherProcessException(
                        MessageUtils.getMessage("evoucher.voucher.amount.request.exceeds.balance"),
                        ErrorCode.VOUCHER_LIMIT_AMOUNT);
            }

            log.info("Get Publish info with publishId={}", voucherParent.getEV());
            Publish publish = publishBasicService.findById(voucherParent.getPublishId());

            Campaign campaign = publish.getCampaign();

            log.info("Get PublishDetail with publishDetailId={}", voucherParent.getPublishDetailId());
            PublishDetail publishDetail = publishDetailBasicService.findById(voucherParent.getPublishDetailId());

            EndUserDto endUser = endUserBasicService.findDtoById(publishDetail.getUserId());

            choiceHistory.setUserMobileNumber(endUser.getUserMobileNum());
            choiceHistory.setUserName(endUser.getUserNm());

            log.info("Get customer contract info with contractId={}", campaign.getCustomerContractId());
            CustomerContract customerContract = customerContractRepository.findByIdAndValidYn(campaign.getCustomerContractId(), EnumValidYn.Y)
                    .orElseThrow(() -> new CustomCodeException(
                            MessageUtils.getMessage("evoucher.customerContract.not.found"),
                            ErrorCode.CUS_CONTRACT_NOT_FOUND));

            List<SettlementLog> settlementLogList = new ArrayList<>();

            goodsChoiceList.forEach(
                    goodsChoice -> {
                        if (!processingPins.containsKey(goodsChoice.getGoodsId())) {
                            processingPins.put(goodsChoice.getGoodsId(), new ArrayList<>());
                        }
                        log.info("Find child good with id={}", goodsChoice.getGoodsId());
                        GoodDto goods = goodMap.get(goodsChoice.getGoodsId());

                        serviceFactory.getGoodServiceByType(goods.getSystem())
                                .validateGoodExpiredDate(goods.getId(), goods.getPeriodType(), goods.getPeriodTerm(), goods.getPeriodExpireDate());

                        log.info("Get supplier contract info with contractId={}", goods.getSupplierContractId());
                        SupplierContract supplierContract = supplierContractRepository.findByIdAndValidYn(goods.getSupplierContractId(), EnumValidYn.Y)
                                .orElseThrow(() -> new CustomCodeException(
                                        MessageUtils.getMessage("evoucher.supplierContract.not.found"),
                                        ErrorCode.SUPP_CONTRACT_NOT_FOUND));

                        List<ExternalPin> externalPins = null;
                        Integer goodsChoiceQuantity = goodsChoice.getQuantity();

                        if (serviceFactory.isThirdPartyType(goods.getSystem())) {
                            externalPins = serviceFactory.
                                    getThirdPartyPinServiceByType(goods.getSystem())
                                    .getPinsForUsing(goods.getId(), goodsChoiceQuantity, new Date());

                            processingPins.get(goods.getId()).addAll(externalPins.stream().map(ExternalPin::getId).collect(Collectors.toList()));

                            listExternalPinSelected.addAll(externalPins);
                            if (CollectionUtils.isEmpty(externalPins) || externalPins.size() < goodsChoiceQuantity) {
                                log.error("Not enough pin available={} < requested={}", externalPins.size(), goodsChoiceQuantity);
                                throw new ChoiceVoucherProcessException(
                                        MessageUtils.getMessage("evoucher.external.pin.not.found", goods.getGoodsName()),
                                        ErrorCode.PRODUCT_QUANTITY_NOT_ENOUGH);
                            }
                        }

                        generateListChoiceVoucher(
                                voucherGenerateInfo,
                                goodsChoiceQuantity,
                                voucherParent,
                                goods,
                                publish,
                                campaign,
                                publishDetail,
                                endUser,
                                customerContract,
                                supplierContract,
                                externalPins);

                    });
            // update balance of parent voucher
            serviceFactory.getGoodTypeServiceByType(voucherParent.getVoucherTypeCode())
                    .getPayingService().doPaying(voucherParent, totalBalanceRequest);

//            voucherParent.setBalance(DataUtils.roundingNumber(balance - totalBalanceRequest));
            log.info("Parent voucher's remaining balance={}", DataUtils.roundingNumber(balance - totalBalanceRequest));

            List<EVoucher> voucherResult = voucherServiceCommon
                    .saveInformationOfVoucherChoice(voucherGenerateInfo, voucherParent);

            log.info("Update ExternalPin's status to USED");
            updateExternalPinToUsed(listExternalPinSelected);

            choiceHistory.setStatus(VoucherChoiceHistoryStatus.SUCCESS);

            log.info("settlement log={}", (settlementLogList));
            settlementLogRepository.saveAll(settlementLogList);

            return voucherResult.stream().map(EVoucher::getEV).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error generate voucher choice with ev={}", request.getParentId());
            log.error(e.getMessage(), e);

            log.error("Process create choice voucher error={}", e.getMessage());
            choiceHistory.setDescription(e.getMessage());
            choiceHistory.setStatus(VoucherChoiceHistoryStatus.FAIL);

            log.info("Delete list url shortLink");
            deleteListUrlShortLink(voucherGenerateInfo.getUrlShortLinks());

            log.info("Convert list external pin");
            convertExternalPinStatus(listExternalPinSelected);

            throw e;
        } finally {
            log.info("Save choice history for ev={}", request.getParentId());
            voucherChoiceHistoryService.saveVoucherChoiceHistory(choiceHistory);
            // release marked as processing pins
            for (Map.Entry<Long, List<Long>> entry : processingPins.entrySet()) {
                lockingService.removePrcDonePins(entry.getKey(), entry.getValue());
            }

            // release voucher purchasing
            lockingService.unlockPurchaseChoiceVoucherProcessor(request.getParentId());
        }
    }

    @Override
    @Transactional
    public void disableVoucher(VoucherDisableRequest voucherDisableRequest) {
        String ev = voucherDisableRequest.getEv();
        log.info("Get eVoucher info with eVoucherId: {}", ev);
        EVoucher eVoucher = eVoucherRepository.findById(ev)
                .orElseThrow(() -> new CustomCodeException(
                        "Evoucher not found with ev: " + ev,
                        HttpStatus.BAD_REQUEST
                ));

        Integer voucherDisableHistoryId = saveLogDisableHistory(voucherDisableRequest, eVoucher);
        eVoucher = updateVoucherStatusIsDisable(ev, eVoucher);
        sendToQueueDisableVoucherUpdateDatabaseFE(eVoucher, voucherDisableHistoryId);
    }

    @Override
    public Object getLatestData(EnumAssetType type, String id) {
        if (Objects.isNull(type)) {
            throw new CustomCodeException("Type is null or empty", HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(id)) {
            throw new CustomCodeException("Id is null or empty", HttpStatus.BAD_REQUEST);
        }
        if (EnumAssetType.VOUCHER == type) {
            return eVoucherRepository.findById(id)
                    .orElseThrow(() -> new CustomCodeException("Can not find voucher by id " + id, HttpStatus.INTERNAL_SERVER_ERROR));
        } else if (EnumAssetType.PUBLISH == type) {
            if (!NumberUtils.isDigits(id)) {
                throw new CustomCodeException("Publish Id is invalid", HttpStatus.BAD_REQUEST);
            }
            return publishRepository.findById(NumberUtils.toInt(id))
                    .orElseThrow(() -> new CustomCodeException("Can not find publish by id " + id, HttpStatus.INTERNAL_SERVER_ERROR));
        } else if (EnumAssetType.PIN == type) {
            return externalPinRepository.findByExternalPinNo(id);
        } else if (EnumAssetType.GOODS == type) {
            if (!NumberUtils.isDigits(id)) {
                throw new CustomCodeException("Goods Id is invalid", HttpStatus.BAD_REQUEST);
            }
            return goodsRepository.findById(NumberUtils.toLong(id))
                    .orElseThrow(() -> new CustomCodeException("Can not find goods by id " + id, HttpStatus.INTERNAL_SERVER_ERROR));
        } else if (EnumAssetType.PUBLISH_DETAILS == type) {
            if (!NumberUtils.isDigits(id)) {
                throw new CustomCodeException("Publish detail Id is invalid", HttpStatus.BAD_REQUEST);
            }
            return publishDetailRepository.findById(NumberUtils.toInt(id))
                    .orElseThrow(() -> new CustomCodeException("Can not find publishDetails by id " + id, HttpStatus.INTERNAL_SERVER_ERROR));

        }
        throw new CustomCodeException("Data is not available", HttpStatus.BAD_REQUEST);
    }

    private EVoucher updateVoucherStatusIsDisable(String ev, EVoucher eVoucher) {
        log.info("Update voucher status is disable with ev: {}", ev);
        eVoucher.setVoucherStatusCode(VoucherStatusCode.DISABLED);
        eVoucher = eVoucherRepository.save(eVoucher);
        return eVoucher;
    }

    private Integer saveLogDisableHistory(
            VoucherDisableRequest voucherDisableRequest,
            EVoucher eVoucher) {
        VoucherDisableHistory voucherDisableHistory = VoucherDisableHistory.builder()
                .ev(voucherDisableRequest.getEv())
                .memo(voucherDisableRequest.getReason())
                .backEndPreviousStatusCode(eVoucher.getVoucherStatusCode())
                .backEndUpdateResult(CompletedStatusCode.SUCCESS)
                .build();
        log.info("save log disable history with ev: {}", voucherDisableRequest.getEv());
        voucherDisableHistory = voucherDisableHistoryRepository.save(voucherDisableHistory);
        return voucherDisableHistory.getId();
    }

    private void sendToQueueDisableVoucherUpdateDatabaseFE(
            EVoucher eVoucher,
            Integer voucherDisableHistoryId) {
        log.info("Send to queue Disable voucher with ev: {} and historyId: {}",
                eVoucher.getEV(), voucherDisableHistoryId);
        var request = VoucherDisableProcessRequest.builder()
                .ev(eVoucher.getEV())
                .voucherDisableHistoryId(voucherDisableHistoryId)
                .build();
        rabbitTemplate.convertAndSend(queueDisableVoucher, request);
    }

    private void generateListChoiceVoucher(VoucherGenerateInfo voucherGenerateInfo,
                                           int quantity,
                                           EVoucher voucherParent,
                                           GoodDto goodsChoice,
                                           Publish publish,
                                           Campaign campaign,
                                           PublishDetail publishDetail,
                                           EndUserDto endUser,
                                           CustomerContract customerContract,
                                           SupplierContract supplierContract,
                                           List<ExternalPin> externalPins) {
        log.info("Generate {} child vouchers for ev={}", quantity, voucherParent.getEV());
        for (int i = 0; i < quantity; i++) {
            ExternalPin externalPin = !CollectionUtils.isEmpty(externalPins) ? externalPins.get(i) : null;
            createVoucherChoice(
                    voucherParent,
                    campaign,
                    publish,
                    customerContract,
                    supplierContract,
                    goodsChoice,
                    publishDetail,
                    endUser,
                    externalPin,
                    voucherGenerateInfo);
        }
    }

    public void deleteListUrlShortLink(List<String> urlShortLinks) {
        log.info("Start delete url short link was created: {}", urlShortLinks);
        // chia 3 luồng mỗi luồng xóa 1 url
        while (!CollectionUtils.isEmpty(urlShortLinks) && urlShortLinks.size() >= 3) {
            try {
                CompletableFuture<Void> thread1 =
                        CompletableFuture.runAsync(
                                () -> shortURLService.deleteUrlShortLink(getFirstShortLinkAndDelete(urlShortLinks)));
                CompletableFuture<Void> thread2 =
                        CompletableFuture.runAsync(
                                () -> shortURLService.deleteUrlShortLink(getFirstShortLinkAndDelete(urlShortLinks)));
                CompletableFuture<Void> thread3 =
                        CompletableFuture.runAsync(
                                () -> shortURLService.deleteUrlShortLink(getFirstShortLinkAndDelete(urlShortLinks)));

                CompletableFuture.allOf(thread1, thread2, thread3).join();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        }
        for (String urlShortLink : urlShortLinks) {
            try {
                shortURLService.deleteUrlShortLink(urlShortLink);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private String getFirstShortLinkAndDelete(List<String> listShortLink) {
        String shortLink = listShortLink.get(0);
        listShortLink.remove(shortLink);
        return shortLink;
    }

    public CompletableFuture<Boolean> createVoucher(Campaign campaign,
                                                    Publish publish,
                                                    CustomerContract customerContract,
                                                    SupplierContract supplierContract,
                                                    GoodDto goods,
                                                    PublishDetailRequest publishDetail,
                                                    VoucherGenerateInfo voucherGenerateInfo) {
        if (Objects.isNull(publishDetail)) return CompletableFuture.completedFuture(false);

        log.info("Get PublishDetail with publishDetailId: {}", publishDetail.getPublishDetailId());
        PublishDetail publishDetailDatabase = publishDetailBasicService.findById(publishDetail.getPublishDetailId());

        Optional<ExternalPin> externalPin;
        if (Objects.nonNull(publishDetailDatabase.getExternalPinId())) {
            externalPin = externalPinRepository.findById(publishDetailDatabase.getExternalPinId());
        } else {
            externalPin = Optional.empty();
        }

        boolean isPaperType = SMSType.PAPER == publish.getSmsType();
        boolean isDownload = SMSType.DOWNLOAD == publish.getSmsType();
        EndUserDto endUser = endUserBasicService.findDtoById(publishDetailDatabase.getUserId());
        Brand brand = brandRepository.findByIdAndValidYn(goods.getBrandId(), EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException("Can not find brand by id " + goods.getBrandId(), HttpStatus.INTERNAL_SERVER_ERROR));
        String serialNumber = makeSerialNumber(brand);

        // create voucherId
        String ev = UUID.randomUUID().toString();

        // create shortURL
        String urlShortLink = shortURLService.createShortURLForEVoucher(ev);
        voucherGenerateInfo.addUrlShortLinks(urlShortLink);

        // get voucher expired if externalPin's expireTime already exist or get goods's endDate
        Date voucherExpired = (externalPin.isPresent() && ObjectUtils.isNotEmpty(externalPin.get().getExpireTime()))
                ? externalPin.get().getExpireTime() : getGoodEndDate(goods.getPeriodType(), goods.getPeriodTerm(), goods.getPeriodExpireDate());

        Double voucherPrice = getPrice(publish, goods);
        // create eVoucher
        EVoucher eVoucher = EVoucher.builder()
                .eV(ev)
                .publishId(publish.getId())
                .publishDetailId(publishDetail.getPublishDetailId())
                .goodsId(goods.getId())
                .campaignId(campaign.getId())
                .userMobileNumber(isPaperType ? null : endUser.getUserMobileNum())
                .userName(endUser.getUserNm())
                .userId(publishDetailDatabase.getUserId())
                .testYn(publish.getTestSendYn())
                .creationDate(new Date())
                .expirationDate(voucherExpired)
                .publishDate(publish.getPublishDate())
                .shortLink(urlShortLink)
                .voucherTypeCode(goods.getGoodsType())
                .voucherStatusCode(VoucherStatusCode.NORMAL)
                .subject(publish.getMessageSubject())
                .content(publish.getMessageContent())
                .useInfo(goods.getUseInfo())
                .imageUrl(goods.getGoodsImgPath())
                .voucherPrice(voucherPrice)
                .discountRate(publish.getSellDiscountRate())
                .discountLimitPrice(publish.getSellPrice())
                .initAmount(voucherPrice)
                .balance(voucherPrice)
                .externalPinId(externalPin.map(ExternalPin::getId).orElse(null))
                .externalPinNo(externalPin.map(ExternalPin::getExternalPinNo).orElse(null))
                .externalPinType(getVoucherDisplayType(goods.getSystem(), brand.getDisplayType(), externalPin.orElse(null)))
                .system(goods.getSystem())
                .externalPinPassword(externalPin.map(ExternalPin::getPassword).orElse(null))
                .contentLink(publish.getContentLink())
                .contentImagePath(publish.getContentImagePath())
                .contentImageName(publish.getContentImageName())
                .parentVoucherToken(isDownload ? getChoiceToken(goods) : null)
                .serialNo(serialNumber)
                .usageCount(goods.getUsageCount())
                .usageRemainingCount(goods.getUsageCount())
                .voucherVersion(isDownload ? Constant.Common.VERSION_1 : Constant.Common.VERSION_2)
                .build();
        voucherGenerateInfo.addEVoucher(eVoucher);

        // check and save SettlementMethodCode.PUBLISH for Customer
        if (SettlementMethodCode.PER_PUBLISH.equals(publish.getSellSettlementMethodCode())) {
            log.info("Create SettlementLog for Customer with ev: {}", ev);
            SettlementLog settlementLog = buildSettlementLogWhenPublishForCustomer(
                    campaign, publish, customerContract, supplierContract, goods, publishDetailDatabase, endUser, eVoucher);
            voucherGenerateInfo.addSettlementLogs(settlementLog);
        }

        // check and save SettlementMethodCode.PUBLISH for Supplier
        if (SettlementMethodCode.PER_PUBLISH.equals(goods.getSettlementMethodCode())) {
            log.info("Create SettlementLog for Supplier with ev: {}", ev);
            SettlementLog settlementLog = buildSettlementLogWhenPublishForSupplier(
                    campaign, publish, supplierContract, goods, publishDetailDatabase, endUser, eVoucher);
            voucherGenerateInfo.addSettlementLogs(settlementLog);
        }

        return CompletableFuture.completedFuture(true);
    }

    public static VoucherDisplayType getVoucherDisplayType(SystemType system, String brandDisplayType, ExternalPin externalPin) {
        if  (EnumSet.of(SystemType.CHOICE, SystemType.BULK, SystemType.VNPT_EPAY).contains(system)) { // DEFAULT display type
            return  VoucherDisplayType.DEFAULT;
        } else if (SystemType.SYSTEM_TYPE_HAS_EXTERNAL_PIN.contains(system)) { // Handle by external pin display type
            return VoucherDisplayType.fromString(externalPin.getDisplayType().name());
        }
        return VoucherDisplayType.fromString(brandDisplayType); // Internal voucher, based on brand display type
    }

    private static Double getPrice(Publish publish, GoodDto goods) {
        return Objects.nonNull(publish.getSellPrice()) ? publish.getSellPrice() : goods.getSellPrice();
    }

    private static Double getChoicePrice(GoodDto goods) {
        return goods.getSellPrice();
    }

    private PublishDetailRequest getLastPublishDetailAndDelete(List<PublishDetailRequest> publishDetails) {
        PublishDetailRequest lastDTO = publishDetails.get(publishDetails.size() - 1);
        publishDetails.remove(lastDTO);
        return lastDTO;
    }

    private void validateGoods(GoodDto goods) {
        if (Objects.isNull(goods)) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.goods.not.found"),
                    HttpStatus.BAD_REQUEST);
        }
        if (!EnumValidYn.Y.equals(goods.getValidYn())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.goods.inactive"),
                    HttpStatus.BAD_REQUEST);
        }
        if (!GoodBasicService.EXTERNAL_GOOD.contains(goods.getSystem())) {
            // validate goods have expired sale
            Date goodsEndDate = getGoodEndDate(goods.getPeriodType(), goods.getPeriodTerm(), goods.getPeriodExpireDate());
            if (goodsEndDate.before(new Date())) {
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.goods.expired"),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }


    @Override
    public Date getGoodEndDate(PeriodType type, Integer term, String periodDate) {
        LocalDateTime goodsEndDate;
        if (PeriodType.FIXED_TERM.equals(type)) {
            goodsEndDate = LocalDateTime.now(ZoneId.systemDefault()).plusDays(term)
                    .with(LocalTime.MAX);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            goodsEndDate = LocalDate.parse(periodDate, formatter)
                    .atTime(LocalTime.MAX);
        }
        return Date.from(goodsEndDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void validateCampaign(Campaign campaign) {
        if (Objects.isNull(campaign)) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.not.found"),
                    HttpStatus.BAD_REQUEST);
        }
        // Campaign must be approved
        if (!ApproveStatus.APPRV.equals(campaign.getApproveStatusCode())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.has.not.been.approved"),
                    HttpStatus.BAD_REQUEST);
        }
        // Campaign's endDate after current date
        if (campaign.getEndDate().before(new Date())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.campaign.date.invalid.with.current.date"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private SettlementLog buildSettlementLogWhenPublishForCustomer(Campaign campaign,
                                                                   Publish publish,
                                                                   CustomerContract customerContract,
                                                                   SupplierContract supplierContract,
                                                                   GoodDto goods,
                                                                   PublishDetail publishDetail,
                                                                   EndUserDto endUser,
                                                                   EVoucher eVoucher) {
        return SettlementLog.builder()
                .ev(eVoucher.getEV())
                .settlementLogType(SettlementLogType.PER_PUBLISH)
                .publishId(publish.getId())
                .publishDetailId(publishDetail.getId())
                .transactionDate(new Date())
                .logCreateDate(new Date())
                .voucherTypeCode(eVoucher.getVoucherTypeCode())
                .goodsId(goods.getId())
                .customerId(customerContract.getCustomerId())
                .supplierId(supplierContract.getSupplierId())
                .brandId(goods.getBrandId())
                .userMobileNumber(Objects.nonNull(endUser) ? endUser.getUserMobileNum() : null)
                .settlementTarget(SettlementTarget.CUSTOMER)
                .settlementMethodCode(publish.getSellSettlementMethodCode())
                .listPrice(publish.getSellListPrice())
                .salesPrice(publish.getSellPrice())
                .discountRate(publish.getSellDiscountRate())
                .discountAmount(publish.getSellDiscountAmount())
                .vatIncludeYn(publish.getSellVatIncludeYn())
                .commissionRate(publish.getSellCommissionRate())
                .sendCost(publish.getSendCost())
                .remainBalance(eVoucher.getBalance())
                .campaignId(campaign.getId())
                .build();
    }

    private SettlementLog buildSettlementLogWhenPublishForSupplier(Campaign campaign,
                                                                   Publish publish,
                                                                   SupplierContract supplierContract,
                                                                   GoodDto goods,
                                                                   PublishDetail publishDetail,
                                                                   EndUserDto endUser,
                                                                   EVoucher eVoucher) {
        return SettlementLog.builder()
                .ev(eVoucher.getEV())
                .settlementLogType(SettlementLogType.PER_PUBLISH)
                .publishId(publish.getId())
                .publishDetailId(publishDetail.getId())
                .transactionDate(new Date())
                .logCreateDate(new Date())
                .voucherTypeCode(eVoucher.getVoucherTypeCode())
                .goodsId(goods.getId())
                .customerId(publish.getCustomerId())
                .supplierId(supplierContract.getSupplierId())
                .brandId(goods.getBrandId())
                .userMobileNumber(Objects.nonNull(endUser) ? endUser.getUserMobileNum() : null)
                .settlementTarget(SettlementTarget.SUPPLIER)
                .settlementMethodCode(goods.getSettlementMethodCode())
                .listPrice(goods.getListPrice())
                .salesPrice(goods.getSellPrice())
                .discountRate(goods.getSupplyDiscountRate())
                .discountAmount(goods.getSupplyDiscountAmount())
                .vatIncludeYn(goods.getVatIncludeYn())
                .commissionRate(goods.getSupplyCommissionRate())
                .remainBalance(eVoucher.getBalance())
                .campaignId(campaign.getId())
                .build();
    }

    private void createVoucherChoice(EVoucher voucherParent,
                                     Campaign campaign,
                                     Publish publish,
                                     CustomerContract customerContract,
                                     SupplierContract supplierContract,
                                     GoodDto goods,
                                     PublishDetail publishDetail,
                                     EndUserDto endUser,
                                     ExternalPin externalPin,
                                     VoucherGenerateInfo voucherGenerateInfo) {
        log.info("Create voucherChoice for voucherParent={}, targetUser={}, pin={}",
                voucherParent.getEV(),
                Objects.nonNull(endUser) ? endUser.getId() : null,
                Objects.nonNull(externalPin) ? externalPin.getExternalPinNo() : null);
        // create voucherId
        String ev = UUID.randomUUID().toString();

        // create shortURL
        String urlShortLink = shortURLService.createShortURLForEVoucher(ev);
        voucherGenerateInfo.addUrlShortLinks(urlShortLink);

        // get voucher expired if externalPin's expireTime already exist or get goods's endDate
        Date voucherExpired = GoodBasicService.EXTERNAL_GOOD.contains(goods.getSystem())
                ? externalPin.getExpireTime() : getGoodEndDate(goods.getPeriodType(), goods.getPeriodTerm(), goods.getPeriodExpireDate());

        Brand brand = brandRepository.findByIdAndValidYn(goods.getBrandId(), EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException("Can not find brand by id=" + goods.getBrandId(), HttpStatus.INTERNAL_SERVER_ERROR));

        EVoucher eVoucher = EVoucher.builder()
                .eV(ev)
                .publishId(publish.getId())
                .publishDetailId(publishDetail.getId())
                .goodsId(goods.getId())
                .campaignId(campaign.getId())
                .userMobileNumber(Objects.nonNull(endUser) ? endUser.getUserMobileNum() : null)
                .userName(Objects.nonNull(endUser) ? endUser.getUserNm() : null)
                .userId(Objects.nonNull(endUser) ? endUser.getId() : null)
                .testYn(publish.getTestSendYn())
                .creationDate(new Date())
                .expirationDate(voucherExpired)
                .publishDate(new Date())
                .shortLink(urlShortLink)
                .voucherTypeCode(goods.getGoodsType())
                .voucherStatusCode(VoucherStatusCode.NORMAL)
                .subject(publish.getMessageSubject())
                .content(publish.getMessageContent())
                .useInfo(goods.getUseInfo())
                .imageUrl(goods.getGoodsImgPath())
                .voucherPrice(getChoicePrice(goods))
                .discountRate(publish.getSellDiscountRate())
                .discountLimitPrice(customerContract.getSellDiscountAmount())
                .initAmount(getChoicePrice(goods))
                .balance(getChoicePrice(goods))
                .externalPinId(SystemType.SYSTEM_TYPE_HAS_EXTERNAL_PIN.contains(goods.getSystem()) ? externalPin.getId() : null)
                .externalPinNo(SystemType.SYSTEM_TYPE_HAS_EXTERNAL_PIN.contains(goods.getSystem()) ? externalPin.getExternalPinNo() : null)
                .externalPinType(getVoucherDisplayType(goods.getSystem(), brand.getDisplayType(), externalPin))
                .externalPinPassword(SystemType.SYSTEM_TYPE_HAS_EXTERNAL_PIN.contains(goods.getSystem()) ? externalPin.getPassword() : null)
                .system(goods.getSystem())
                .parentVoucherEv(voucherParent.getEV())
                .contentLink(publish.getContentLink())
                .contentImagePath(publish.getContentImagePath())
                .contentImageName(publish.getContentImageName())
                .usageCount(goods.getUsageCount())
                .usageRemainingCount(goods.getUsageCount())
                .serialNo(makeSerialNumber(brand))
                .voucherVersion(SMSType.DOWNLOAD.equals(publish.getSmsType()) ? Constant.Common.VERSION_1 : voucherParent.getVoucherVersion())
                .build();
        voucherGenerateInfo.addEVoucher(eVoucher);

        // check and save SettlementMethodCode.PUBLISH for Customer
        if (SettlementMethodCode.PER_PUBLISH.equals(publish.getSellSettlementMethodCode())) {
            log.info("Create SettlementLog for Customer with ev={}", ev);
            SettlementLog settlementLog = buildSettlementLogWhenPublishForCustomer(
                    campaign, publish, customerContract, supplierContract, goods, publishDetail, endUser, eVoucher);
            settlementLog.setParentEv(voucherParent.getEV());
            settlementLog.setSystem(voucherParent.getSystem());
            voucherGenerateInfo.addSettlementLogs(settlementLog);
        }

        // check and save SettlementMethodCode.PUBLISH for Supplier
        if (SettlementMethodCode.PER_PUBLISH.equals(goods.getSettlementMethodCode())) {
            log.info("Create SettlementLog for Supplier with ev={}", ev);
            SettlementLog settlementLog = buildSettlementLogWhenPublishForSupplier(
                    campaign, publish, supplierContract, goods, publishDetail, endUser, eVoucher);
            settlementLog.setParentEv(voucherParent.getEV());
            settlementLog.setSystem(voucherParent.getSystem());
            voucherGenerateInfo.addSettlementLogs(settlementLog);
        }
    }

    private void convertExternalPinStatus(List<ExternalPin> externalPins) {
        if (CollectionUtils.isEmpty(externalPins)) {
            log.info("List external Pin is empty");
            return;
        }

        log.info("Update external pin status to AVAILABLE for: {}",
                externalPins.stream().map(ExternalPin::getId).collect(Collectors.toList()));
        externalPins.forEach(externalPin -> externalPin.setStatus(ExternalPinStatus.AVAILABLE));
        externalPinRepository.saveAll(externalPins);
    }

    private void updateExternalPinToUsed(List<ExternalPin> externalPins) {
        if (CollectionUtils.isEmpty(externalPins)) {
            log.info("List external Pin is empty");
            return;
        }

        log.info("Update external pin status to USED for: {}",
                externalPins.stream().map(ExternalPin::getId).collect(Collectors.toList()));
        externalPins.forEach(externalPin -> externalPin.setStatus(ExternalPinStatus.USED));
        externalPinRepository.saveAll(externalPins);
    }


    /***
     * Create random SerialNumber
     * @return total 12 characters 2 alphabetic + 10 numeric
     */
    private String makeSerialNumber(Brand brand) {
        if (Objects.nonNull(brand.getSerialNumberTotalLength()) && Objects.nonNull(brand.getSerialNumberPrefix())) {
            String result = brand.getSerialNumberPrefix();
            int remaining = brand.getSerialNumberTotalLength() - brand.getSerialNumberPrefix().length();
            result += RandomStringUtils.randomNumeric(remaining);
            return result;
        }
        return RandomStringUtils.randomNumeric(10);
    }
    private String getChoiceToken(GoodDto goods) {
        if (GoodBasicService.PARENT_VOUCHER.contains(goods.getSystem())) {
            return DataUtils.getToken();
        }
        return null;
    }
}

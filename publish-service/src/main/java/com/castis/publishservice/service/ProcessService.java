package com.castis.publishservice.service;

import com.castis.publishservice.dto.*;
import com.castis.publishservice.dto.request.*;
import com.castis.publishservice.dto.response.BaseResponse;
import com.castis.publishservice.entity.EndUser;
import com.castis.publishservice.entity.Voucher;
import com.castis.publishservice.exception.defineException.BadRequestException;
import com.castis.publishservice.exception.defineException.CustomCodeException;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import com.castis.publishservice.repository.UserRepository;
import com.castis.publishservice.repository.VoucherRepository;
import com.castis.publishservice.utils.Constants;
import com.castis.publishservice.utils.Utils;
import com.castis.publishservice.utils.status.EnumYN;
import com.castis.publishservice.utils.status.PublishDetailStatus;
import com.castis.publishservice.utils.status.PublishStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.castis.publishservice.utils.Constants.ERROR_CODE.INTERNAL_ERROR_CODE;
import static com.castis.publishservice.utils.Constants.ERROR_CODE.VOUCHER_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessService {
    private final PublishService publishService;
    private final PublishDetailService detailService;
    private final PrepareDataService prepareDataService;
    private final QuartzJobService quartzService;
    private final VoucherRepository voucherRepository;
    private final DataService dataService;
    private final GoodsService goodsService;
    private final BrandService brandService;
    private final UserService userService;
    private final VoucherBasicService voucherBasicService;
    private final UserRepository userRepository;


    public void publish(PublishRequest publishRequest) throws BadRequestException, ServerRuntimeException {
        Long publishId = publishRequest.getPublishId();
        if (publishId == null) {
            throw new BadRequestException("publish id can not be null");
        }
        if (CollectionUtils.isEmpty(publishRequest.getUsers())) {
            log.error("users info is null");
            throw new BadRequestException("end users info can not be null");
        }
        PublishDTO publishDTO = publishService.findById(publishId);
        log.info("Start publishing for publishId={}, smsType={}, status={}",
                publishDTO.getId(), publishDTO.getSmsType(), publishDTO.getPublishStatusCode());
        asyncPublishing(publishDTO, publishRequest);
    }

    private void asyncPublishing(PublishDTO publishDTO, PublishRequest publishRequest) {
        Thread thread = new Thread(() -> {
            try {
                log.info("Save {} users to db .PublishId={}", publishRequest.getUsers().size(), publishDTO.getId());
                List<EndUserDto> users = userService.saveAllDto(publishRequest.getUsers());

                publishRequest.setUsers(users);
                //if is booking publish, create publish job
                if (publishDTO.getBookingYn().equalsIgnoreCase(EnumYN.Y.name())) {
                    //create publish job
                    log.info("Booking campaign, create publish job. PublishId={}", publishDTO.getId());
                    quartzService.createPublishSchedule(publishDTO, publishRequest);
                } else {
                    log.info("Not a booking campaign, publish immediately. PublishId={}", publishDTO.getId());
                    // if not publish voucher immediately
                    publishVouchers(publishRequest.getPublishId(), publishRequest);
                }
                //end of publish process
                log.info("Finish publishing voucher process. PublishId={}", publishDTO.getId());
            } catch (CustomCodeException e) {
                publishService.updatePublishStatus(publishDTO.getId(), PublishStatus.FAIL_PUBLISHING);
                log.error("Error when publishing voucher for publishId={}", publishDTO.getId());
                log.error(e.getMessage(), e);
                throw e;
            } catch (Exception e) {
                publishService.updatePublishStatus(publishDTO.getId(), PublishStatus.FAIL_PUBLISHING);
                log.error("Error when publishing voucher for publishId={}", publishDTO.getId());
                log.error(e.getMessage(), e);
                throw new ServerRuntimeException(e.getMessage(), e);
            }
        });
        thread.start();
    }

    public void cancelPublishing(List<CancelRequest> request) {
        log.info("cancel publishing publishes: {}", request);
        List<String> failCancel = new ArrayList<>();
        for (CancelRequest publish : request) {
            try {
                cancelPublishing(publish);
            } catch (Exception e) {
                log.error("error when cancel publish: " + publish.getPublishId());
                failCancel.add(publish.getPublishId().toString());
            }
        }
        if (failCancel.isEmpty()) {
            log.info("cancel publish successfully");
        } else {
            log.error("error when cancel publishes: {}", failCancel);
            throw new ServerRuntimeException("error when cancel publishes: " + String.join(",", failCancel));
        }
    }

    public void cancelPublishing(CancelRequest cancelRequest) {
        log.info("cancel publishing publish id={}", cancelRequest.getPublishId());
        PublishDTO publishDTO = publishService.findById(cancelRequest.getPublishId());
        if (publishDTO.getBookingYn().equalsIgnoreCase(EnumYN.Y.name())) {
            log.info("Booking publish type, need to cancel scheduled job and restore reserved pins");
            prepareDataService.restoreReservedPins(publishDTO, cancelRequest);
            log.info("Restore reserved pins successfully");
            quartzService.cancelScheduledJob(Constants.JOB_CONSTANTS.JOB_NAME_PREFIX.concat(publishDTO.getId().toString()), Constants.JOB_CONSTANTS.JOB_GROUP_PREFIX.concat(publishDTO.getCampaign().getId().toString()));
            log.info("cancel scheduled job successfully");
        }
    }

    public void publishVouchers(Long publishId, PublishRequest request) {
        PublishDTO publishDTO = publishService.findById(publishId);
        if (publishDTO.getPublishStatusCode() == PublishStatus.APPROVED) {
            // only publish approved campaign
            publishCampaign(publishDTO, request);
        } else {
            log.warn("Publish has status different with approved. publishId={}, status={}",
                    publishDTO.getId(), publishDTO.getPublishStatusCode());
        }
    }

    //    @Transactional
    public void publishCampaign(PublishDTO publishDTO, PublishRequest publishReq) {
        //update publish status, and publish date
        publishService.updatePublishStatusAndPublishDate(publishDTO.getId(), PublishStatus.PUBLISHING, new Date());

        try {
            //prepare and send to queue
            log.info("Start creating publish detail and vouchers of publishId={}", publishDTO.getId());
            prepareDataService.createPublishDetailAndVouchers(publishDTO, publishReq);
            publishService.updatePublishStatus(publishDTO.getId(), PublishStatus.GENERATING);
        } catch (CustomCodeException e) {
            //if has any exception, change status to fail publish and retry three times
            log.error(e.getMessage(), e);
            publishService.updatePublishStatus(publishDTO.getId(), PublishStatus.FAIL_PUBLISHING);
            throw e;
        } catch (Exception e) {
            //if has any exception, change status to fail publish and retry three times
            log.error(e.getMessage(), e);
            publishService.updatePublishStatus(publishDTO.getId(), PublishStatus.FAIL_PUBLISHING);
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }

    public BaseResponse voucherHandOver(HandOverQueueMessage transferRequest) throws ServerRuntimeException {
        if (transferRequest == null || StringUtils.isBlank(transferRequest.getOldEv())) {
            log.error("new user info is empty, can not hand over");
            throw new ServerRuntimeException("new user info is empty");
        }
        // create new publish detail
        Long detailId = voucherRepository.getDetailIdByVoucherId(transferRequest.getOldEv());
        if (Objects.isNull(detailId)) {
            throw new ServerRuntimeException("Null publish detail id");
        }
        PublishDetailDTO oldPublishDetailDto = detailService.getById(detailId);
        if (oldPublishDetailDto == null) {
            log.error("can not find dto with id: {}", detailId);
            throw new ServerRuntimeException("publish detail not found");
        }

        //remove old ìnfo, change to new user info
        PublishDetailDTO newPublishDetail = new PublishDetailDTO();
        detailService.transferVoucher(newPublishDetail, oldPublishDetailDto);

        // User
        EndUser newUser = obtainNewUser(transferRequest);
        //save encrypted number no
        newPublishDetail.setUserId(newUser.getId());
        newPublishDetail.setReceiverMobileNo(newUser.getUserMobileNum());
        newPublishDetail.setPublishStatusCd(PublishDetailStatus.STRT_PUB);
        PublishDetailDTO detail = detailService.save(newPublishDetail);
        try {
            // create voucher
            // decrypt number no before send to be
            transferRequest.getNewUser().setId(newUser.getId());
            log.info("Call service be for transferring voucher: {}", transferRequest);
            prepareDataService.handoverVoucher(detail, transferRequest);
            return new BaseResponse();
            // send publish info to queue
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            detail.setPublishStatusCd(PublishDetailStatus.FAIL_PUB);
            throw new ServerRuntimeException(e.getMessage());
        }
    }

    private EndUser obtainNewUser(HandOverQueueMessage newUserInfo) {
        EndUser newUser = new EndUser();
        newUser.setUserMobileNum(dataService.decrypt(newUserInfo.getNewUser().getUserMobileNum()));
        newUser.setUserNm(newUserInfo.getNewUser().getUserNm());
        newUser = userRepository.save(newUser);
        log.info("Saved new user with id: {}, mobileNo: {}, name: {}", newUser.getId(), newUser.getUserMobileNum(), newUser.getUserNm());
        return newUser;
    }

    public BaseResponse chooseChoiceItem(ChoiceChosenRequest choiceRequest) {
        log.info("Find parent Voucher with ev: {}", choiceRequest.getParentVoucherId());
        Voucher parentVoucher = voucherRepository.findById(choiceRequest.getParentVoucherId())
                .orElseThrow(
                        () -> {
                            log.error("can not find voucher with ev: {}", choiceRequest.getParentVoucherId());
                            return new CustomCodeException(
                                    "Voucher no found.",
                                    VOUCHER_NOT_FOUND,
                                    HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                );
        validateChoiceVoucherParent(parentVoucher);
        validateChoicePurchase(choiceRequest.getProducts());

        return prepareDataService.createChoiceItem(parentVoucher.getPublishId(), choiceRequest);
    }

    public BaseResponse chooseChoiceItemV2(PurchaseChildRequest choiceRequest) {
        log.info("V2 Purchase child voucher={}", Utils.toJson(choiceRequest));
        Voucher parentVoucher = voucherBasicService.findById(choiceRequest.getParentVoucherId());
        validateChoiceVoucherParent(parentVoucher);
        validateChoicePurchase(choiceRequest.getProducts());

        return prepareDataService.createChoiceItemV2(parentVoucher.getPublishId(), choiceRequest);
    }

    public void validateChoiceVoucherParent(Voucher parentVoucher) throws CustomCodeException {
        if (parentVoucher.getExpirationDate().before(new Date())) {
            log.error("Parent voucher is expired ev={}", parentVoucher.getId());
            throw new CustomCodeException("Parent voucher is expired.",
                    INTERNAL_ERROR_CODE,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void validateChoicePurchase(List<ChoiceChosenItem> choices)
            throws CustomCodeException {
        if (CollectionUtils.isEmpty(choices)) {
            log.error("voucher purchase data is empty");
            throw new CustomCodeException("Voucher purchase data is empty.",
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST);
        }

        if (choices.stream().anyMatch(o -> Objects.isNull(o.getGoodsId()))) {
            log.error("voucher purchase data contains null good id");
            throw new CustomCodeException("Good id can not be null.",
                    Constants.ERROR_CODE.CHOICE_GOOD_ID_NULL,
                    HttpStatus.BAD_REQUEST);
        }

        List<Long> goodsId = choices.stream().map(ChoiceChosenItem::getGoodsId).collect(Collectors.toList());
        List<GoodsDTO> allGoods = goodsService.findAllByIdIn(goodsId);

        if (allGoods.stream()
                .anyMatch(o -> (Objects.isNull(o.getValidYn()) || EnumYN.N.name().equalsIgnoreCase(o.getValidYn())))
        ) {
            log.error("goods {} are inactive", allGoods.stream()


                    .filter(o -> (Objects.isNull(o.getValidYn()) || EnumYN.N.name().equalsIgnoreCase(o.getValidYn())))
                    .map(GoodsDTO::getId)
                    .collect(Collectors.toList()));
            throw new CustomCodeException("Can not buy inactive good.",
                    Constants.ERROR_CODE.INACTIVE_GOOD,
                    HttpStatus.BAD_REQUEST);
        }

        Set<String> brandsId = allGoods.stream().map(GoodsDTO::getBrandId).collect(Collectors.toSet());
        List<BrandDTO> brands = brandService.findByIdIn(brandsId);

        if (!CollectionUtils.isEmpty(brands) && brands.stream()
                .anyMatch(o -> (Objects.isNull(o.getValidYn()) || EnumYN.N.name().equalsIgnoreCase(o.getValidYn())))) {
            log.error("brands {} are inactive", brands.stream()
                    .filter(o -> (Objects.isNull(o.getValidYn()) || EnumYN.N.name().equalsIgnoreCase(o.getValidYn())))
                    .map(BrandDTO::getId)
                    .collect(Collectors.toList()));
            throw new CustomCodeException("Can not buy good of inactive brand.",
                    Constants.ERROR_CODE.INACTIVE_BRAND,
                    HttpStatus.BAD_REQUEST);
        }

    }

}

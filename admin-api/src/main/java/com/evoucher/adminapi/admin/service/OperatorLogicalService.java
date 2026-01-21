package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.OperatorRequestRepository;
import com.evoucher.adminapi.admin.enums.OperatorRequestStatus;
import com.evoucher.adminapi.admin.mapper.OperatorRequestMapper;
import com.evoucher.adminapi.admin.service.models.*;
import com.evoucher.adminapi.admin.service.models.search_response.OperatorSearchRes;
import com.evoucher.adminapi.admin.validator_group.ApprovingGroup;
import com.evoucher.adminapi.admin.validator_group.CreatingGroup;
import com.evoucher.adminapi.auth.service.AuthService;
import com.evoucher.adminapi.cms.service.CustomerBasicService;
import com.evoucher.adminapi.cms.service.models.CustomerDTO;
import com.evoucher.adminapi.common.config.PropertyConverter;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.exception.DatabaseException;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.message.BaseResponse;
import com.evoucher.adminapi.common.utils.MessageUtils;
import com.evoucher.adminapi.good.service.GoodBasicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static com.evoucher.adminapi.common.utils.DataUtils.getPageInfo;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperatorLogicalService {
    private final OperatorRequestBasicService basicService;
    private final VoucherBasicService voucherService;
    private final PublishBasicService publishBasicService;
    private final CustomerBasicService customerBasicService;
    private final AuthService authService;
    private final PropertyConverter propertyConverter;
    private final OperatorRequestMapper mapper;

    private final OperatorRequestRepository repository;

    private static final int EXTEND_DAYS = 30;

    public static class ErrorCode {
        private ErrorCode() {
        }

        public static final String INVALID_STATUS = "operator.request.invalid.status";
    }

    public BaseResponse createNew(@Validated(CreatingGroup.class) OperatorRequestDto request) throws CustomCodeException {
        log.info("create new operator request: {}", request);
        try {
            VoucherDto voucher = voucherService.findDtoById(request.getEv());
            // validate extendable of voucher

            validateExtendableVoucher(voucher);
            Long requestingCount = basicService.countAllByEvAndStatus(request.getEv(), OperatorRequestStatus.REQUESTED);
            if (requestingCount > 0) {
                log.info("{} requests under approval", requestingCount);
                throw new CustomCodeException("Request is under approval", HttpStatus.BAD_REQUEST);
            }

            log.info("set publish id, good id from voucher");
            mapper.updateInfoFromVoucher(request, voucher);
            //set status requested to request
            log.info("set status to requested");
            request.setReqStatus(OperatorRequestStatus.REQUESTED);

            request = basicService.saveDto(request);
            log.info("operator request saved");
            return BaseResponse.ok(request.getReqId());
        } catch (EntityNotFoundException | CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public BaseResponse findRequestByEv(String ev) throws CustomCodeException {
        try {
            VoucherDto voucher = voucherService.findRequestVoucherById(ev);
            PublishDTO publish = publishBasicService.findPublishRequestById(voucher.getPublishId());
            CustomerDTO customer = customerBasicService.findCustomerRequestById(publish.getCustomerId());

            List<OperatorRequestDto> requests = basicService.findAllByEv(ev);
            VoucherApprovalRequest result = mapper.toVoucherRequests(voucher, publish, customer);
            if (!CollectionUtils.isEmpty(requests)) {
                OperatorRequestDto requesting = requests.stream().filter(o -> o.getReqStatus() == OperatorRequestStatus.REQUESTED).findFirst().orElse(null);

                result.setRequest(requesting);
                result.setApprovalHistory(requests);
            }
            return new BaseResponse(result);
        } catch (EntityNotFoundException | CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public BaseResponse approveRequest(@Validated(ApprovingGroup.class) OperatorRequestDto approveRequest)
            throws CustomCodeException {
        // validate status
        try {
            OperatorRequestDto request = basicService.findDtoById(approveRequest.getReqId());

            // only status Requested could be approved
            if (request.getReqStatus() != OperatorRequestStatus.REQUESTED) {
                log.error("request status {} is not allowed to extend", request.getReqStatus());
                throw new CustomCodeException(MessageUtils.getMessage(ErrorCode.INVALID_STATUS) + " " + request.getReqStatus(), HttpStatus.BAD_REQUEST);
            }

            VoucherDto voucher = voucherService.findDtoById(request.getEv());

            // validate extendable voucher
            validateExtendableVoucher(voucher);

            // update approval info
            mapper.updateApprovalInfo(request, approveRequest);

            if (approveRequest.getReqStatus() == OperatorRequestStatus.APPROVED) {
                // only extend voucher expire in case approve
                // do extend voucher expire date
                voucherService.extendVoucherExpiredDate(request.getEv(), EXTEND_DAYS);
            }

            // set approve user
            request.setApprover(authService.getLoggedInUserId());
            request.setApproveDate(new Date());
            // save approved request
            basicService.saveDto(request);

            return BaseResponse.ok(request.getReqId());

        } catch (EntityNotFoundException | CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void validateExtendableVoucher(VoucherDto voucher) throws CustomCodeException {
        VoucherBasicService.validateExtendable(voucher.getVoucherStatusCode());
        GoodBasicService.validateExtendable(voucher.getSystemType());
    }

    public BaseResponse searchOperatorRequest(FilterSearchAdmin filter, Integer pageSize, Integer page)
            throws CustomCodeException {
        // encrypt target number, target name for db searching
        filter.setTargetName(propertyConverter.convertToDatabaseColumnForSearch(filter.getTargetName()));
        filter.setTargetNumber(propertyConverter.convertToDatabaseColumnForSearch(filter.getTargetNumber()));

        log.info("search {} operator request at pag {} by filter {} ", pageSize, page, filter);
        Pageable pageable = getPageInfo(page, pageSize);
        BaseResponse response = new BaseResponse();
        try {


            long count = repository.countAllByFilter(filter);
            response.setTotalCount(count);
            log.info("search operator request return {} results", count);
            if (count > 0) {
                List<OperatorSearchRes> requests = repository.searchByFilter(filter, pageable);
                response.setData(requests);
            }
            return response;
        } catch (DatabaseException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}

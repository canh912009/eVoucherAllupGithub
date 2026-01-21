package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.VoucherStatusCode;
import com.evoucher.evoucherbe.dto.ActivationRequest;
import com.evoucher.evoucherbe.dto.VoucherDto;
import com.evoucher.evoucherbe.entity.EVoucher;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.exception.EntityNotFoundException;
import com.evoucher.evoucherbe.mapper.EVoucherMapper;
import com.evoucher.evoucherbe.repository.EVoucherRepository;
import com.evoucher.evoucherbe.service.typed.PayingService;
import com.evoucher.evoucherbe.service.typed.ServiceFactory;
import com.evoucher.evoucherbe.service.typed.type.GoodTypeAbstractService;
import com.evoucher.evoucherbe.utils.ErrorCode;
import com.evoucher.evoucherbe.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherBasicService extends EntityService<EVoucher, String, VoucherDto> {
    public static final String NOT_FOUND = "evoucher.voucher.not.found";
    private final EVoucherRepository repository;
    private final EVoucherMapper mapper;

    private final ServiceFactory serviceFactory;
    @Override
    public JpaRepository<EVoucher, String> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "voucher";
    }

    @Override
    public EntityNotFoundException getNotFoundException(String id) {
        return new EntityNotFoundException(
                MessageUtils.getMessage(NOT_FOUND), ErrorCode.VOUCHER_NOT_FOUND);
    }

    @Override
    public EVoucher toEntity(VoucherDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public VoucherDto toDto(EVoucher entity) {
        return mapper.toDto(entity);
    }
    public VoucherDto save(VoucherDto dto) throws CustomCodeException {
        EVoucher voucher = toEntity(dto);

        voucher = save(voucher);

        return toDto(voucher);
    }

    public VoucherDto setUsedStatus(VoucherDto dto) {
        EVoucher voucher = this.findById(dto.getEV());
        GoodTypeAbstractService goodTypeServiceByType = serviceFactory.getGoodTypeServiceByType(dto.getVoucherTypeCode());
        PayingService payingService = goodTypeServiceByType.getPayingService();

        if (goodTypeServiceByType.isMultipleTimesUsage() && payingService.isUsable(voucher)) {
            voucher.setVoucherStatusCode(VoucherStatusCode.PART_USED);
        } else {
            voucher.setVoucherStatusCode(VoucherStatusCode.USED);
        }
        log.info("update voucher expired date from {} to {}", voucher.getExpirationDate(), dto.getExpirationDate());
        voucher.setExpirationDate(dto.getExpirationDate());
        voucher = this.save(voucher);
        log.info("voucher {} status updated to {}", dto.getEV(), voucher.getVoucherStatusCode());
        return toDto(voucher);

    }

    /**
     * update voucher balance only
     * @param dto info payload
     * @param amount used amount
     * @throws CustomCodeException exception while saving voucher
     */
    public VoucherDto deduceBalance(VoucherDto dto, Long amount) {
        EVoucher voucher = this.findById(dto.getEV());
        GoodTypeAbstractService goodTypeServiceByType = serviceFactory.getGoodTypeServiceByType(dto.getVoucherTypeCode());
        PayingService payingService = goodTypeServiceByType.getPayingService();
        payingService.doPaying(voucher, amount.doubleValue());
        voucher = this.save(voucher);
        return toDto(voucher);
    }

    public VoucherDto topupBalance(VoucherDto dto, Long amount) {
        EVoucher voucher = this.findById(dto.getEV());
        GoodTypeAbstractService goodTypeServiceByType = serviceFactory.getGoodTypeServiceByType(dto.getVoucherTypeCode());
        PayingService payingService = goodTypeServiceByType.getPayingService();
        payingService.doPayingBack(voucher, amount.doubleValue());
        voucher.setVoucherStatusCode(VoucherStatusCode.NORMAL);
        voucher = this.save(voucher);
        return toDto(voucher);
    }

    /**
     * update voucher status, balance, expire date (if needed) after purchasing
     * @param dto info payload
     * @param purchaseValue used amount
     * @throws CustomCodeException exception while saving voucher
     */
    public VoucherDto updateExchangeCanceledVoucherStatus(VoucherDto dto, Long purchaseValue)
    throws CustomCodeException {
        EVoucher voucher = this.findById(dto.getEV());

        GoodTypeAbstractService goodTypeServiceByType = serviceFactory.getGoodTypeServiceByType(dto.getVoucherTypeCode());
        PayingService payingService = goodTypeServiceByType.getPayingService();
        // update voucher balance
        payingService.doPayingBack(voucher, purchaseValue.doubleValue());

        VoucherStatusCode updatedStatus;
        if (goodTypeServiceByType.isMultipleTimesUsage()) {
            if (payingService.isBackToNormalAfterCancel(voucher)) {
                log.info("multiple usage times voucher has balance equals to init amount");
                updatedStatus = VoucherStatusCode.NORMAL;
            } else {
                log.info("multiple usage times voucher has balance smaller than init amount");
                updatedStatus = VoucherStatusCode.PART_USED;
            }
        } else {
            log.info("update voucher status to normal");
            updatedStatus = VoucherStatusCode.NORMAL;
        }
        log.info("update voucher status from {} to: {}", voucher.getVoucherStatusCode(), updatedStatus);
        voucher.setVoucherStatusCode(updatedStatus);

        log.info("update voucher expired date from {} to {}", voucher.getExpirationDate(), dto.getExpirationDate());
        voucher.setExpirationDate(dto.getExpirationDate());
        voucher = this.save(voucher);
        log.info("voucher updated");
        return toDto(voucher);
    }

    @Transactional
    public VoucherDto updateActivationAndGet(ActivationRequest request, Date activationDate)
            throws EntityNotFoundException, CustomCodeException {
        log.info("update activation info to ev: {}", request);
        try {
            EVoucher voucher = findById(request.getEv());

            voucher.setActivationDate(activationDate);
            voucher.setUserMobileNumber(request.getPhoneNumber());

            save(voucher);

            return toDto(voucher);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

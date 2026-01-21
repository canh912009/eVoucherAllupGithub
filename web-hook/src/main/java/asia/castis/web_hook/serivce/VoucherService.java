package asia.castis.web_hook.serivce;

import asia.castis.web_hook.bean.dto.response.ur_box.UrBoxVoucherList;
import asia.castis.web_hook.bean.entity.Voucher;
import asia.castis.web_hook.common.SystemType;
import asia.castis.web_hook.common.VoucherStatusCode;
import asia.castis.web_hook.exception.defined.BadRequestException;
import asia.castis.web_hook.exception.defined.EntityNotFoundException;
import asia.castis.web_hook.exception.defined.ServerRuntimeException;
import asia.castis.web_hook.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository repository;

    public Voucher getLastUseGiftPopVoucherByTransactionId(@Valid @NotBlank Long pinId, List<VoucherStatusCode> usableStatuses)
            throws EntityNotFoundException, ServerRuntimeException {
        try {
            log.info("find gift pop voucher by pin id: {}", pinId);
            List<Voucher> vouchers = repository.findAllByExtPinIdAndSystemAndVoucherStatusCodeIn(pinId,
                    SystemType.GIFTPOP, usableStatuses);
            log.info("got: {}", vouchers);

            if (CollectionUtils.isEmpty(vouchers) || vouchers.size() != 1) {
                log.info("voucher not found by pin {} and status: {}", pinId, usableStatuses);
                throw new BadRequestException("Voucher info mismatch");
            }
            return vouchers.get(0);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage());
        }
    }

    public Voucher getLastUseWataneVoucherByTrackingId(@Valid @NotBlank Long pinId, List<VoucherStatusCode> usableStatuses)
            throws EntityNotFoundException, ServerRuntimeException {
        try {
            log.info("find watane voucher by pin id: {}", pinId);
            List<Voucher> vouchers = repository.findAllByExtPinIdAndSystemAndVoucherStatusCodeIn(pinId,
                    SystemType.WATANE, usableStatuses);
            log.info("got: {}", vouchers);

            if (CollectionUtils.isEmpty(vouchers) || vouchers.size() != 1) {
                log.info("voucher not found by pin {} and status: {}", pinId, usableStatuses);
                throw new BadRequestException("Voucher info mismatch");
            }
            return vouchers.get(0);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage());
        }
    }

    public List<Voucher> getAllByStatusInAndSystem(List<VoucherStatusCode> status, SystemType system)
            throws EntityNotFoundException, ServerRuntimeException{
        try {
            log.info("get vouchers by status in {} and system is {}", status, system);
            return repository.findAllByVoucherStatusCodeInAndSystem(status, system)
                    .orElseThrow(() -> {
                        log.error("not found");
                        return new EntityNotFoundException("Voucher not found");
                    });

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage());
        }
    }

    public List<Voucher> getAllByIdIn(List<VoucherStatusCode> status, SystemType system, List<String> ids)
            throws EntityNotFoundException, ServerRuntimeException{
        try {
            log.info("get vouchers by status in {} and system is {}", status, system);
            return repository.findAllByVoucherStatusCodeInAndSystemAndIdIn(status, system, ids)
                    .orElseThrow(() -> {
                        log.error("not found");
                        return new EntityNotFoundException("Voucher not found");
                    });

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage());
        }
    }
}

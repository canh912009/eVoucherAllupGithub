package asia.castis.web_hook.serivce;

import asia.castis.web_hook.bean.entity.ExtPin;
import asia.castis.web_hook.bean.entity.Voucher;
import asia.castis.web_hook.common.SystemType;
import asia.castis.web_hook.common.VoucherStatusCode;
import asia.castis.web_hook.exception.defined.EntityNotFoundException;
import asia.castis.web_hook.exception.defined.ServerRuntimeException;
import asia.castis.web_hook.repository.ExtPinRepository;
import asia.castis.web_hook.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExtPinService {
    private final ExtPinRepository repository;
    public ExtPin findByTransactionId(@Valid @NotBlank String pinTransactionId)
            throws EntityNotFoundException, ServerRuntimeException {
        try {
            Optional<ExtPin> voucherOptional =
                    repository.findByTransactionId(pinTransactionId);
            return voucherOptional.orElseThrow(() -> {
                log.error("can not find external pin by transaction id: {}", pinTransactionId);
                return new EntityNotFoundException("Not found: " + pinTransactionId);
            });
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }

    public List<ExtPin> findAllByPinNoIn(List<String> pinsNo)
            throws EntityNotFoundException, ServerRuntimeException {
        try {
            return repository.findAllByExtPinNoIn(pinsNo)
                    .orElseThrow(() -> {
                        log.warn("can not find pin had pin no in {}", pinsNo);
                        return new EntityNotFoundException("Not found Pin In: {}" + pinsNo);
                    });
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }

    public ExtPin findByTrackingId(@Valid @NotBlank String trackingId)
            throws EntityNotFoundException, ServerRuntimeException {
        try {
            Optional<ExtPin> voucherOptional =
                    repository.findByTrackingId(trackingId);
            return voucherOptional.orElseThrow(() -> {
                log.error("can not find external pin by tracking id: {}", trackingId);
                return new EntityNotFoundException("Not found: " + trackingId);
            });
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }
}

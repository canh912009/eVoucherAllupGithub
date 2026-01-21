package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.dto.response.LimitedCountHistoryResponse;
import asia.castis.evoucher.api.entity.VoucherExchangeHistory;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.repository.VoucherExchangeHistoryRepository;
import asia.castis.evoucher.api.service.LimitedCountVoucherService;
import asia.castis.evoucher.api.service.generator.impl.CommonGenerator;
import asia.castis.evoucher.api.utils.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.stream.Collectors;

import static asia.castis.evoucher.api.common.Constant.gson;

@Service
@RequiredArgsConstructor
@Slf4j
public class LimitedCountVoucherServiceImpl implements LimitedCountVoucherService {

    private final VoucherExchangeHistoryRepository voucherExchangeHistoryRepository;
    private final CommonGenerator commonGenerator;

    @Override
    public List<LimitedCountHistoryResponse> getHistory(String ev) {
        try {
            return voucherExchangeHistoryRepository.findAllByEvOrderByTransactionDateDesc(ev)
                    .stream().map(commonGenerator::toLcHistoryResponse)
                    .collect(Collectors.toList());
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage(), ErrorCode.EXCEPTION_WHILE_REQUESTING_FE);
        }
    }
}

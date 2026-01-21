package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.common.enums.SystemType;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequest;
import asia.castis.evoucher.api.service.BulkVoucherService;
import asia.castis.evoucher.api.service.PurchaseChildService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static asia.castis.evoucher.api.common.Constant.gson;

@Service
@Slf4j
@RequiredArgsConstructor
public class BulkVoucherServiceImpl<T extends ChosenRequest> implements BulkVoucherService<T> {
    private final PurchaseChildService<T> purchaseChildService;

    @Override
    public void chooseProduct(T chosenRequest) {
        log.info("Choose bulk product: {}", gson.toJson(chosenRequest));
        chosenRequest.setType(SystemType.BULK.name());
        purchaseChildService.chooseProduct(chosenRequest);
    }
}

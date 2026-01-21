package asia.castis.web_hook.client;

import asia.castis.web_hook.bean.dto.request.UpdatingVoucherReq;
import asia.castis.web_hook.bean.dto.request.UsingVoucherRequest;
import asia.castis.web_hook.bean.dto.response.BaseResponse;
import asia.castis.web_hook.bean.dto.response.BeServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "service-be", url = "${components.service-be.url}", configuration = ClientConfiguration.class)
public interface BeServiceClient {

    @PostMapping(value = "/use-voucher", consumes = "application/json", produces = "application/json")
    BaseResponse usingVoucher(@RequestBody UpdatingVoucherReq request);

    @PostMapping(value = "/update-status", consumes = "application/json", produces = "application/json")
    BaseResponse updateStatus(@RequestBody List<UpdatingVoucherReq> request);
}

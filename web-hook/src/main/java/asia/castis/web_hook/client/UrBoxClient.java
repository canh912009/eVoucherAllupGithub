package asia.castis.web_hook.client;

import asia.castis.web_hook.bean.dto.request.UrBoxBaseRequest;
import asia.castis.web_hook.bean.dto.request.UrBoxVoucherListReq;
import asia.castis.web_hook.bean.dto.response.ur_box.UrBoxSingleResponse;
import asia.castis.web_hook.bean.dto.response.ur_box.UrBoxVoucherList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "urBox",
        url = "${systems.ur-box.url}",
        configuration = ClientConfiguration.class)
public interface UrBoxClient {
    @GetMapping(value = "${systems.ur_box.paths.get-vouchers}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    UrBoxSingleResponse<List<UrBoxVoucherList>> getVoucherByTransactionsId(@SpringQueryMap UrBoxBaseRequest req);
}

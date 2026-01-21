package asia.castis.evoucherservicefe.common.client;

import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.Voucher;
import asia.castis.evoucherservicefe.config.ClientConfiguration;
import asia.castis.evoucherservicefe.publishrequest.dto.ChosenRequest;
import asia.castis.evoucherservicefe.publishrequest.dto.PublishResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "jplaceholder", url = "${castis.component.publish-service.url}", configuration = ClientConfiguration.class)
public interface PublishServiceClient {
    @PostMapping("/voucher/choose-choice")
    PublishResponse<List<Voucher>> chooseChoiceItem(@RequestBody ChosenRequest choiceRequest);
}

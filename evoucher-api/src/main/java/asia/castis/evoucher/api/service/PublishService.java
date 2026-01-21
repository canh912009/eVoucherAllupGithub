package asia.castis.evoucher.api.service;

import asia.castis.evoucher.api.dto.response.PublishDetails;

public interface PublishService {
    PublishDetails publishByActivationKey(String activationId);
}

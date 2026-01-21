package asia.castis.evoucherservicefe.publishrequest.utils;

import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.PublishDetail;
import asia.castis.evoucherservicefe.common.dto.publishreq.imcoming.RequestFromBE;
import asia.castis.evoucherservicefe.common.enums.EnumPublishType;
import asia.castis.evoucherservicefe.common.utils.Const;
import asia.castis.evoucherservicefe.common.utils.ValidateResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Service
public class PublishValidator {
    public ValidateResult validate(RequestFromBE request) {
        if (request.getPublishDetails() == null || request.getPublishDetails().isEmpty()) {
            return ValidateResult.builder().isValid(false).validationMessage("Null or empty publish details").build();
        }
        if (Objects.isNull(request.getSmsType()) || request.getSmsType().isEmpty()) {
            return ValidateResult.builder().isValid(false).validationMessage("SMS type is empty").build();
        }
        if (!isValidDate(request.getBookingDate())) {
            return ValidateResult.builder()
                    .isValid(false)
                    .validationMessage("Booking date doesn't follow the required format 'yyyy-MM-dd HH:mm:ss'")
                    .build();
        }
        if (!isValidDate(request.getPublishDate())) {
            return ValidateResult.builder()
                    .isValid(false)
                    .validationMessage("Publish date doesn't follow the required format 'yyyy-MM-dd HH:mm:ss'")
                    .build();
        }
        if (!isValidDate(request.getCancelDate())) {
            return ValidateResult.builder()
                    .isValid(false)
                    .validationMessage("Cancel date doesn't follow the required format 'yyyy-MM-dd HH:mm:ss'")
                    .build();
        }
        // Check publish campaign
        if (Objects.isNull(request.getCampaign())) {
            return ValidateResult.builder()
                    .isValid(false)
                    .validationMessage("Campaign is null")
                    .build();
        }
        if (!isValidDate(request.getCampaign().getStartDate())) {
            return ValidateResult.builder()
                    .isValid(false)
                    .validationMessage("Campaign start date doesn't follow the required format 'yyyy-MM-dd HH:mm:ss'")
                    .build();
        }
        if (!isValidDate(request.getCampaign().getEndDate())) {
            return ValidateResult.builder()
                    .isValid(false)
                    .validationMessage("Campaign end date doesn't follow the required format 'yyyy-MM-dd HH:mm:ss'")
                    .build();
        }
        if (!isValidDate(request.getCampaign().getEndDate())) {
            return ValidateResult.builder()
                    .isValid(false)
                    .validationMessage("Campaign end date doesn't follow the required format 'yyyy-MM-dd HH:mm:ss'")
                    .build();
        }
        if (request.getType() == EnumPublishType.TRANSFER) {
            if (request.getPublishDetails().size() > 1) { // Transfer publish must have only one voucher
                return ValidateResult.builder().isValid(false).validationMessage("Transfer publish must have only one voucher").build();
            }
            PublishDetail detail = request.getPublishDetails().get(0);
            if (Objects.isNull(detail.getVoucher().getOriginalVoucherId()) || detail.getVoucher().getOriginalVoucherId().isEmpty()) {
                return ValidateResult
                        .builder()
                        .isValid(false)
                        .validationMessage("Voucher of transfer publish must have originalVoucherId")
                        .build();
            }
        }
        // Is valid
        return ValidateResult.builder().isValid(true).build();
    }

    private boolean isValidDate(String date) {
        if (Objects.isNull(date) || date.isEmpty()) { // No need to check this case
            return true;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Const.COMMON_DATETIME_FORMAT);
        try {
            LocalDateTime.parse(date, formatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}

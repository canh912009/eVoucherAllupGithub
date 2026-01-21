package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Objects;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class CsTransferHistoryDTO implements Comparable<CsTransferHistoryDTO> {
    Long id;
    String type;
    String voucherUUID;
    String toVoucherUUID;
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    Date transferDate;
    String targetNumber;
    String targetName;
    String accessLink;
    String pinStatus;
    String pin;
    @JsonIgnore
    Date startDate;
    @JsonIgnore
    Date endDate;
    String transferStatusCode;
    @Override
    public boolean equals(Object o) {
        if (o instanceof CsTransferHistoryDTO) {
            CsTransferHistoryDTO object = (CsTransferHistoryDTO) o;
            return (this.voucherUUID.equals(object.getVoucherUUID())  && this.toVoucherUUID.equals(object.getToVoucherUUID())) || Objects.equals(this.id, ((CsTransferHistoryDTO) o).id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.voucherUUID, this.toVoucherUUID);
    }

    @Override
    public int compareTo(CsTransferHistoryDTO o) {
        return - this.transferDate.compareTo(o.transferDate);
    }
}

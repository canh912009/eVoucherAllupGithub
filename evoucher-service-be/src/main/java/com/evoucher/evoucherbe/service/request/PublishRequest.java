package com.evoucher.evoucherbe.service.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublishRequest {
//    @NotNull(message = "Contract ID is empty!")
//    private Integer contractId;
    @NotNull(message = "Publish ID is empty!")
    private Integer publishId;
//    @NotNull(message = "Campaign ID is empty!")
//    private Integer campaignId;
    @NotNull(message = "List Publish detail is empty!")
    private List<PublishDetailRequest> publishDetails;
}

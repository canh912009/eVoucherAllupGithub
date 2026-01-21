package com.evoucher.adminapi.admin.service.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PublishCancelRequest {
    private Integer publishId;
    private Integer numbersCount;
}

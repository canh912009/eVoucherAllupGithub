package com.evoucher.adminapi.admin.service.models;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PublishScheduleRegistration {
    private Integer publishId;
    private List<String> phoneNumbers;
}

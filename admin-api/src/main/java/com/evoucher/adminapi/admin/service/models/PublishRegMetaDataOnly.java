package com.evoucher.adminapi.admin.service.models;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PublishRegMetaDataOnly {
    private long publishId;
    private int numberOfVouchers;
    private List<String> phoneNumbers;
}

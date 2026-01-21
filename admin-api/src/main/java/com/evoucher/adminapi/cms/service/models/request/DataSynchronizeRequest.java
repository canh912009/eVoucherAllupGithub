package com.evoucher.adminapi.cms.service.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSynchronizeRequest<T> {
    private String action;
    private T payload;
}

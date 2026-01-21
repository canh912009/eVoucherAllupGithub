package com.evoucher.externalserviceapi.service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExternalPinRequest {

    @NotBlank(message = "External PIN NO is empty!")
    @Size(max = 100, message = "External PIN NO less than 100 characters!")
    private String externalPinNo;
}

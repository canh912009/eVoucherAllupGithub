package com.evoucher.adminapi.good.service.model.ur_box;

import lombok.Getter;

@Getter
public class UrBoxSingleResponse <T> extends UrBoxResponse{
    private T data;
}

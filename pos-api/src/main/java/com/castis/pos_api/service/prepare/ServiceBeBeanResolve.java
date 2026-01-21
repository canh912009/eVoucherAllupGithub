package com.castis.pos_api.service.prepare;

import com.castis.pos_api.dto.request.third_party.ServiceBeUsingVoucherReq;
import com.castis.pos_api.enum_constant.ExchangeType;

import java.util.Date;

public class ServiceBeBeanResolve {
    private ServiceBeBeanResolve(){}
    public static void setDefaultValueForExchange(ServiceBeUsingVoucherReq exchangeReq) {
        exchangeReq.setTransactionDate(new Date());
    }
    public static void setDefaultValueForCancel(ServiceBeUsingVoucherReq exchangeReq) {
        exchangeReq.setTransactionDate(new Date());
    }
}

package com.evoucher.adminapi.good.service.validation;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service("internalValidatingStrategy")
public class InternalValidatingStrategy implements ValidatingStrategy{
    @Override
    public boolean validateStartEndDate(Date startDate, Date endDate) {
        return false;
    }
}

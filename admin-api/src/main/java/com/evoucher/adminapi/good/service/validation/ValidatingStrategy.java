package com.evoucher.adminapi.good.service.validation;

import java.util.Date;

public interface ValidatingStrategy {
    boolean validateStartEndDate(Date startDate, Date endDate);
}

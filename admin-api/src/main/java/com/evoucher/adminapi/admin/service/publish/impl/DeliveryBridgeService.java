package com.evoucher.adminapi.admin.service.publish.impl;

import com.evoucher.adminapi.admin.dao.EVoucherRepository;
import com.evoucher.adminapi.admin.service.PublishDetailService;
import com.evoucher.adminapi.admin.service.PublishService;
import com.evoucher.adminapi.common.config.PropertyConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryBridgeService {
    protected final PublishDetailService publishDetailService;
    protected final PropertyConverter propertyConverter;
    protected final EVoucherRepository eVoucherRepository;
    protected final PublishService publishService;
}

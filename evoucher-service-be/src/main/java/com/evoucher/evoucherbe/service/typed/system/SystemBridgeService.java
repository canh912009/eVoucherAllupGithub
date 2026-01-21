package com.evoucher.evoucherbe.service.typed.system;

import com.evoucher.evoucherbe.client.PartnerServiceClient;
import com.evoucher.evoucherbe.client.PublishServiceClient;
import com.evoucher.evoucherbe.repository.ExternalPinRepository;
import com.evoucher.evoucherbe.service.EVoucherService;
import com.evoucher.evoucherbe.service.PublishServiceMockingService;
import com.evoucher.evoucherbe.service.typed.LockingService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
public class SystemBridgeService {
    private final ExternalPinRepository externalPinRepository;
    private final LockingService lockingService;
    private final PublishServiceMockingService publishMockingService;
    private final PublishServiceClient publishServiceClient;
    private final PartnerServiceClient partnerServiceClient;
    private final EVoucherService voucherService;

}

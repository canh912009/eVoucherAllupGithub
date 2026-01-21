package com.evoucher.adminapi.good.service;

import com.evoucher.adminapi.admin.enums.SMSType;
import com.evoucher.adminapi.admin.service.publish.impl.PublishHandlingStrategy;
import com.evoucher.adminapi.common.enums.GoodsType;
import com.evoucher.adminapi.common.enums.SystemType;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.good.service.typed_service.type.GoodTypeAbstractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.EnumSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodServiceFactory {
    private final ApplicationContext context;

    public static final Set<SystemType> DIRECT_USAGE = EnumSet.of(SystemType.WATANE, SystemType.UR_BOX, SystemType.GIFTPOP, SystemType.INTERNAL, SystemType.EXTERNAL, SystemType.VNPT_EPAY, SystemType.XPAY);
    public static final Set<SystemType> PARENT_GOOD_SYSTEM = EnumSet.of(SystemType.CHOICE, SystemType.BULK);
    public static final Set<GoodsType> PARENT_GOOD_TYPE = EnumSet.of(GoodsType.CH, GoodsType.BK);
    public static final Set<SystemType> VALIDATING_PERIOD = EnumSet.of(SystemType.INTERNAL, SystemType.CHOICE, SystemType.BULK);
    public static final Set<SystemType> INTEGRATED_GOOD = EnumSet.of(SystemType.GIFTPOP, SystemType.UR_BOX, SystemType.VNPT_EPAY, SystemType.XPAY, SystemType.WATANE);
    public static final Set<SystemType> REQUIRE_PIN_BEFORE_APPROVE = EnumSet.of(SystemType.EXTERNAL);
    public static final Set<SystemType> DIRECT_TYPE_HAS_PERIOD = EnumSet.of(SystemType.INTERNAL, SystemType.VNPT_EPAY, SystemType.XPAY);


    public IntegrationPinService getIntegratedSerByType(SystemType type) {
        log.info("get integration service for: {}", type);
        return context.getBean(type.name().toLowerCase(), IntegrationPinService.class);
    }
    public static boolean isSyncType(SystemType type) {
        log.info("check sync type: {}", type);
        boolean result = IntegrationPinService.syncTypes.contains(type);
        log.info("{} is sync type: {}", type, result);
        return result;
    }

    public ParentGoodService getParentServiceBySystem(SystemType systemType) {
        if (!PARENT_GOOD_SYSTEM.contains(systemType)) {
            log.error("{} type is not a parent good type", systemType);
            throw new CustomCodeException(systemType.name() + "is not a parent good type", HttpStatus.BAD_REQUEST);
        }
        return context.getBean(systemType.name().toLowerCase(), ParentGoodService.class);
    }
    public GoodService getServiceBySystem(SystemType systemType) {
        return context.getBean(systemType.name().toLowerCase(), GoodService.class);
    }


    // product type service factory
    public GoodTypeAbstractService getGoodTypeServiceByType(@Valid @NotNull GoodsType type) {
        String serviceName;
        switch (type) {
            case LC:
                serviceName = "lcTypeService";
                break;
            case BK:
                serviceName = "bkTypeService";
                break;
            case CH:
                serviceName = "chTypeService";
                break;
            case PP:
                serviceName = "ppTypeService";
                break;
            case SI:
                serviceName = "siTypeService";
                break;
            default:
                throw new CustomCodeException(
                        type + " type is not supported", HttpStatus.INTERNAL_SERVER_ERROR
                );
        }
        return context.getBean(serviceName, GoodTypeAbstractService.class);
    }
    // product type service factory
    public PublishHandlingStrategy getDeliveryServiceBySmsType(@Valid @NotNull SMSType type) {
        String serviceName;
        switch (type) {
            case DOWNLOAD:
                serviceName = "download";
                break;
            case SMS:
                serviceName = "sms";
                break;
            case PAPER:
                serviceName = "paper";
                break;
            case ZALO:
                serviceName = "zalo";
                break;
            case EMAIL:
                serviceName = "email";
                break;
            default:
                log.error("{} type is not supported", type);
                throw new CustomCodeException(
                        type + " type is not supported", HttpStatus.INTERNAL_SERVER_ERROR
                );
        }
        return context.getBean(serviceName, PublishHandlingStrategy.class);
    }
}

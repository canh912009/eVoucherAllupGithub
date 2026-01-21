package com.evoucher.evoucherbe.service.typed;

import com.evoucher.evoucherbe.common.enums.SystemType;
import com.evoucher.evoucherbe.common.enums.VoucherTypeCode;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.service.typed.type.GoodTypeAbstractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceFactory {
    private final ApplicationContext context;
    private static final Set<SystemType> GOOD_SERVICE_LIST = new HashSet<>();

    public static void registerGoodService(SystemType type) {
        GOOD_SERVICE_LIST.add(type);
    }
    public static boolean isSupported(SystemType type) {
        return GOOD_SERVICE_LIST.contains(type);
    }
    public boolean isThirdPartyType(SystemType type) {
        boolean isThirdPartyType = IntegratedPinService.supported.contains(type);
        log.info(isThirdPartyType ? "{} is third-party type" : "{} isn't third-party type", type);
        return isThirdPartyType;
    }
    public IntegratedPinService getThirdPartyPinServiceByType(SystemType type) {
        if (!isThirdPartyType(type)) {
            throw new CustomCodeException("service type: " + type.name() + " is not supported", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return context.getBean(type.name(), IntegratedPinService.class);
    }

    public AbstractGoodService getGoodServiceByType(SystemType type) {
        return context.getBean(type.name(), AbstractGoodService.class);
    }

    public GoodTypeAbstractService getGoodTypeServiceByType(@Valid @NotNull VoucherTypeCode type) {
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
}

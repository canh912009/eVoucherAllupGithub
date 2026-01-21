package com.evoucher.adminapi.common.utils;

import com.evoucher.adminapi.cms.service.GoodsServiceImpl;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.SortableObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Common {
    private Common(){}
    public static void setDisplayIndexForValid(Collection< ? extends SortableObject> objects, Integer startWith) {
        if (CollectionUtils.isEmpty(objects)) return;
        AtomicInteger displayIndex = new AtomicInteger(startWith);
        objects.forEach(o -> {
                    log.info("   {}: {}", o.getNameAndIdentify(), displayIndex.get());
                    o.setDisplayIndex(displayIndex.getAndIncrement());
                });
    }

    private static final HashMap<Integer, String> ERROR_MESSAGE_MAP = new HashMap<>();
    public static void addMsg(int code, String msgCode) {
        ERROR_MESSAGE_MAP.put(code, msgCode);
    }
    public static String getErrorMsgByCode(int code) {
        return ERROR_MESSAGE_MAP.get(code);
    }
}

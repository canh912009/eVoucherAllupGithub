package com.evoucher.adminapi.cms.utils;

import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CmsDataUtil {

    public static String createId(List<String> listId, String idPrefix, int idMaximum, String result) {
        if (CollectionUtils.isEmpty(listId)) {
            return result;
        } else if (listId.size() >= idMaximum) {
            log.info("ID is Maximum");
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.id.maximum"), HttpStatus.BAD_REQUEST);
        } else {
            int idFirst = Integer.parseInt(listId.get(0).replace(idPrefix, ""));
            if (idFirst > 1) {
                return result;
            }

            for (int i = 0; i < listId.size(); i++) {
                int idCurrent = Integer.parseInt(listId.get(i).replace(idPrefix, ""));
                int idNext = getIdNext(listId, i, idPrefix);

                if (idNext == 0 || idCurrent+1 != idNext) {
                    result = genIdResult(idPrefix, idCurrent+1, idMaximum);
                    return result;
                }
            }
        }
        return null;
    }

    public static String getSupplierIdByAdminCorpId(String adminCorpId) {
        Pattern pattern = Pattern.compile("^([^-]+)");
        Matcher matcher = pattern.matcher(adminCorpId);

        return matcher.find() ? matcher.group(1) : null;
    }

    public static String getBrandIdByAdminCorpId(String adminCorpId) {
        Pattern pattern = Pattern.compile("^(.+-[^-]+)");
        Matcher matcher = pattern.matcher(adminCorpId);

        return matcher.find() ? matcher.group(1) : null;
    }

    private static int getIdNext(List<String> listId, int i, String prefix) {
        try {
            return Integer.parseInt(listId.get(i + 1).replace(prefix, ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private static String genIdResult(String idPrefix, int idSuffixes, int idMaximum) {
        if (idSuffixes < 10 && idMaximum < 1000) return idPrefix + "00" + idSuffixes;
        if (idSuffixes < 100 && idMaximum < 1000) return idPrefix + "0" + idSuffixes;
        if (idSuffixes < 10 && idMaximum > 1000) return idPrefix + "000" + idSuffixes;
        if (idSuffixes < 100 && idMaximum > 1000) return idPrefix + "00" + idSuffixes;
        if (idSuffixes < 1000 && idMaximum > 1000) return idPrefix + "0" + idSuffixes;

        return idPrefix + idSuffixes;
    }

    public static String convertFileName(String name) {
        return System.currentTimeMillis() + name;
    }
}

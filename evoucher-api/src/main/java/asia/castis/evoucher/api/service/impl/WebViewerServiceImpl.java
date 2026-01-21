package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.dto.response.VoucherResponseWrapper;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.service.version1.VoucherService;
import asia.castis.evoucher.api.service.WebViewerService;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import kr.fxd.urlybud.Client;
import kr.fxd.urlybud.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Configuration
@Service
@Slf4j
@RequiredArgsConstructor
public class WebViewerServiceImpl implements WebViewerService {
    @Value("${urlyBud}")
    String budUrl;
    @Value("${tokenBud}")
    String token;

    private final VoucherService voucherService;

    @Override
    public VoucherResponseWrapper getVoucher(String shortLink) {
        return voucherService.getVoucherDetails(getEvFromShortLink(shortLink));
    }

    @Override
    public String getEvFromShortLink(String shortLink) {
        try {
            log.info("Get voucher details link={}", shortLink);
            Map linkResult = getDataFromShortLinkService(shortLink);
            log.info("Get voucher details data={}", linkResult);
            JSONArray responseData = (JSONArray) linkResult.get("Data");
            if (Objects.isNull(responseData)) {
                log.error("Invalid short link, shortLink={}", shortLink);
                throw new ApplicationException(ResponseString.INVALID_SHORT_LINK, ErrorCode.INVALID_SHORT_LINK);
            }
            return ((JSONObject) responseData.get(0)).get("Etc").toString();
        } catch (ApplicationException e) {
            log.error("Get voucher details error", e);
            throw e;
        } catch (Exception e) {
            log.error("Get voucher details error", e);
            throw new ApplicationException(e.getMessage(), ErrorCode.VOUCHER_NOT_FOUND);
        }
    }

    private Map getDataFromShortLinkService(String shortLink) {
        try {
            Client c = new Client(budUrl, token);
            Data d = new Data();
            d.put("S", shortLink);
            return c.call("/link/get", d);
        } catch (Exception e) {
            log.error(String.format("Get shortlink error, shotlink=%s, msg=%s", shortLink, e.getMessage()), e);
            throw new ApplicationException(ResponseString.INVALID_SHORT_LINK, ErrorCode.INVALID_SHORT_LINK);
        }
    }
}

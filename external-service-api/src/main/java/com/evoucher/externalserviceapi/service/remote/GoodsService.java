package com.evoucher.externalserviceapi.service.remote;

import com.evoucher.externalserviceapi.common.config.LoggedInClientContext;
import com.evoucher.externalserviceapi.common.exception.ClientNotLoggedInException;
import com.evoucher.externalserviceapi.common.exception.CustomCodeException;
import com.evoucher.externalserviceapi.common.exception.GenericError;
import com.evoucher.externalserviceapi.common.message.BaseResponse;
import com.evoucher.externalserviceapi.common.utils.CommonUtils;
import com.evoucher.externalserviceapi.common.utils.ConstantUtils;
import com.evoucher.externalserviceapi.service.model.response.GoodsInfoResponse;
import com.evoucher.externalserviceapi.service.model.response.GoodsResponse;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsService {

    @Value("${servers.admin-api}")
    private String adminApiPath;

    private final CloseableHttpClient httpClient;


    public BaseResponse getAllGoods()
            throws IOException, ClientNotLoggedInException {
        String token = LoggedInClientContext.loggedInClientToken();
        HttpGet httpGet = new HttpGet(adminApiPath + "partner/goods");
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("Authorization", token);

        log.info("Get all brand");
        HttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
        log.info("Get all brand response: {}", jsonResponse);

        if (statusCode == 200) {
            if (StringUtils.isEmpty(jsonResponse)) {
                return BaseResponse.builder()
                        .data(new ArrayList<>())
                        .totalCount(0)
                        .build();
            }

            TypeToken<List<GoodsResponse>> type = new TypeToken<>() {};
            List<GoodsResponse> goodsResponses = ConstantUtils.gson.fromJson(jsonResponse, type.getType());
            return BaseResponse.builder()
                    .data(goodsResponses)
                    .totalCount(goodsResponses.size())
                    .build();
        } else {
            GenericError genericError = CommonUtils.convertResponseError(jsonResponse);
            throw new CustomCodeException(genericError.getMessage(), HttpStatus.valueOf(statusCode));
        }
    }

    public BaseResponse getGoodsById(String goodsId)
            throws IOException, ClientNotLoggedInException {
        String token = LoggedInClientContext.loggedInClientToken();
        HttpGet httpGet = new HttpGet(adminApiPath + "partner/goods/" + goodsId);
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("Authorization", token);

        log.info("Get goods with id: {}", goodsId);
        HttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
        log.info("Get goods response: {}", jsonResponse);

        if (statusCode == 200) {
            GoodsInfoResponse goods = ConstantUtils.gson.fromJson(jsonResponse, GoodsInfoResponse.class);
            return BaseResponse.builder()
                    .data(goods)
                    .build();
        } else {
            GenericError genericError = CommonUtils.convertResponseError(jsonResponse);
            throw new CustomCodeException(genericError.getMessage(), HttpStatus.valueOf(statusCode));
        }
    }
}

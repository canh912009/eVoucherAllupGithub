package com.evoucher.externalserviceapi.service.remote;

import com.evoucher.externalserviceapi.common.config.LoggedInClientContext;
import com.evoucher.externalserviceapi.common.exception.ClientNotLoggedInException;
import com.evoucher.externalserviceapi.common.exception.CustomCodeException;
import com.evoucher.externalserviceapi.common.exception.GenericError;
import com.evoucher.externalserviceapi.common.message.BaseResponse;
import com.evoucher.externalserviceapi.common.utils.CommonUtils;
import com.evoucher.externalserviceapi.common.utils.ConstantUtils;
import com.evoucher.externalserviceapi.service.model.response.BrandResponse;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrandService {

    @Value("${servers.admin-api}")
    private String adminApiPath;

    private final CloseableHttpClient httpClient;


    public BaseResponse getAllBrand()
            throws IOException, ClientNotLoggedInException {
        String token = LoggedInClientContext.loggedInClientToken();
        HttpGet httpGet = new HttpGet(adminApiPath + "partner/brand");
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

            TypeToken<List<BrandResponse>> type = new TypeToken<>() {};
            List<BrandResponse> brandResponses = ConstantUtils.gson.fromJson(jsonResponse, type.getType());
            return BaseResponse.builder()
                    .data(brandResponses)
                    .totalCount(brandResponses.size())
                    .build();
        } else {
            GenericError genericError = CommonUtils.convertResponseError(jsonResponse);
            throw new CustomCodeException(genericError.getMessage(), HttpStatus.valueOf(statusCode));
        }
    }

    public BaseResponse getBrandById(String brandId)
            throws IOException, ClientNotLoggedInException {
        String token = LoggedInClientContext.loggedInClientToken();
        HttpGet httpGet = new HttpGet(adminApiPath + "partner/brand/" + brandId);
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("Authorization", token);

        log.info("Get brand with id: {}", brandId);
        HttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
        log.info("Get brand response: {}", jsonResponse);

        if (statusCode == 200) {
            BrandResponse brandResponse = ConstantUtils.gson.fromJson(jsonResponse, BrandResponse.class);
            return BaseResponse.builder()
                    .data(brandResponse)
                    .build();
        } else {
            GenericError genericError = CommonUtils.convertResponseError(jsonResponse);
            throw new CustomCodeException(genericError.getMessage(), HttpStatus.valueOf(statusCode));
        }
    }
}

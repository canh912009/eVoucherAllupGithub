package com.evoucher.externalserviceapi.service.remote;

import com.evoucher.externalserviceapi.common.config.LoggedInClientContext;
import com.evoucher.externalserviceapi.common.exception.ClientNotLoggedInException;
import com.evoucher.externalserviceapi.common.exception.CustomCodeException;
import com.evoucher.externalserviceapi.common.exception.GenericError;
import com.evoucher.externalserviceapi.common.message.BaseResponse;
import com.evoucher.externalserviceapi.common.utils.CommonUtils;
import com.evoucher.externalserviceapi.common.utils.ConstantUtils;
import com.evoucher.externalserviceapi.service.model.request.AdminLoginRequest;
import com.evoucher.externalserviceapi.service.model.request.AuthLogin;
import com.evoucher.externalserviceapi.service.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    @Value("${servers.admin-api}")
    private String adminApiPath;

    private final CloseableHttpClient httpClient;


    public BaseResponse login(AuthLogin authLogin) throws IOException {
        AdminLoginRequest requestEntity = AdminLoginRequest.builder()
                .phoneNumber(authLogin.getPhoneNumber())
                .password(authLogin.getPassword())
                .build();

        String jsonRequest = ConstantUtils.gson.toJson(requestEntity);
        StringEntity entity = new StringEntity(jsonRequest);

        HttpPost httpPost = new HttpPost(adminApiPath + "auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(entity);

        log.info("Login with account request: {}", jsonRequest);
        HttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
        log.info("Login response: {}", jsonResponse);

        if (statusCode == 200) {
            ApiResponse apiResponse = ConstantUtils.gson.fromJson(jsonResponse, ApiResponse.class);
            return BaseResponse.builder()
                    .data(apiResponse.getData())
                    .build();
        } else {
            GenericError genericError = CommonUtils.convertResponseError(jsonResponse);
            throw new CustomCodeException(genericError.getMessage(), HttpStatus.valueOf(statusCode));
        }
    }

    public BaseResponse logout() throws ClientNotLoggedInException, IOException {
        String token = LoggedInClientContext.loggedInClientToken();
        HttpPost httpPost = new HttpPost(adminApiPath + "auth/logout");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Authorization", token);

        HttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");

        if (statusCode == 200) {
            return new BaseResponse(jsonResponse);
        } else {
            GenericError genericError = CommonUtils.convertResponseError(jsonResponse);
            throw new CustomCodeException(genericError.getMessage(), HttpStatus.valueOf(statusCode));
        }
    }
}

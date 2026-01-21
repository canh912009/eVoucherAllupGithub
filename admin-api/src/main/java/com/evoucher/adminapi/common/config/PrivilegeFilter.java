package com.evoucher.adminapi.common.config;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.message.BaseResponse;
import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.utils.DataUtils;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;


@Component
@Slf4j
public class PrivilegeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if (request.getRequestURI().equals("/auth/login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        try {
            UserPrincipal user = LoggedInUserContext.getLoggedInUser();
            String adminType = user.getAdminType();
            EnumRole role = Enum.valueOf(EnumRole.class, adminType);
            String adminCorpId = user.getAdminCorpId();

            if (EnumRole.ROLE_ADMIN.equals(role)
                    || EnumRole.ROLE_OPERATOR.equals(role)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            // GET or PUT Supplier
            if (Pattern.matches("^/suppliers/[^/]+", uri)) {
                switch (method) {
                    case "PUT":
                    case "GET": {
                        String supplierId = uri.split("/")[2];
                        if (!EnumRole.ROLE_SUPPLIER.equals(role)
                                || !supplierId.equals(adminCorpId)) {
                            log.info("Account {} does not have permission!", user.getUsername());
                            CustomAccessDeniedHandler.setResponseHandleForbidden((HttpServletResponse) servletResponse);
                            return;
                        }
                        break;
                    }
                    default:
                        log.info("Account {} does not have permission!", user.getUsername());
                        CustomAccessDeniedHandler.setResponseHandleForbidden((HttpServletResponse) servletResponse);
                        return;
                }
            }
            // GET, PUT, DELETE  Brand
            if (Pattern.matches("^/brands/[^/]+", uri)) {
                String brandId = uri.split("/")[2];
                switch (role) {
                    case ROLE_SUPPLIER:
                        String supplierId = DataUtils.getSupplierIdByBrandId(brandId);
                        if (!adminCorpId.equals(supplierId)) {
                            log.info("Account {} does not have permission!", user.getUsername());
                            CustomAccessDeniedHandler.setResponseHandleForbidden((HttpServletResponse) servletResponse);
                            return;
                        }
                        break;
                    case ROLE_BRAND: {
                        switch (method) {
                            case "GET":
                            case "PUT": {
                                if (!brandId.equals(adminCorpId)) {
                                    log.info("Account {} does not have permission!", user.getUsername());
                                    CustomAccessDeniedHandler.setResponseHandleForbidden((HttpServletResponse) servletResponse);
                                    return;
                                }
                                break;
                            }
                            default:
                                log.info("Account {} does not have permission!", user.getUsername());
                                CustomAccessDeniedHandler.setResponseHandleForbidden((HttpServletResponse) servletResponse);
                                return;
                        }
                        break;
                    }
                    default:
                        log.info("Account {} does not have permission!", user.getUsername());
                        CustomAccessDeniedHandler.setResponseHandleForbidden((HttpServletResponse) servletResponse);
                        return;
                }
            }

            // GET, PUT, DELETE Store
            if (Pattern.matches("^/stores/[^/]+", uri)) {
                String storeId = uri.split("/")[2];
                switch (role) {
                    case ROLE_SUPPLIER:
                        String supplierId = DataUtils.getSupplierIdByStoreId(storeId);
                        if (!adminCorpId.equals(supplierId)) {
                            log.info("Account {} does not have permission!", user.getUsername());
                            CustomAccessDeniedHandler.setResponseHandleForbidden((HttpServletResponse) servletResponse);
                            return;
                        }
                        break;
                    case ROLE_BRAND: {
                        String brandId = DataUtils.getBrandIdByStoreId(storeId);
                        if (!adminCorpId.equals(brandId)) {
                            log.info("Account {} does not have permission!", user.getUsername());
                            CustomAccessDeniedHandler.setResponseHandleForbidden((HttpServletResponse) servletResponse);
                            return;
                        }
                        break;
                    }
                    default:
                        log.info("Account {} does not have permission!", user.getUsername());
                        CustomAccessDeniedHandler.setResponseHandleForbidden((HttpServletResponse) servletResponse);
                        return;
                }
            }

            // GET or PUT Customer
            if (Pattern.matches("^/customer/[^/]+", uri)) {
                switch (method) {
                    case "PUT":
                    case "GET": {
                        String customerId = uri.split("/")[2];
                        if (!EnumRole.ROLE_CUSTOMER.equals(role)
                                || !customerId.equals(adminCorpId)) {
                            log.info("Account {} does not have permission!", user.getUsername());
                            CustomAccessDeniedHandler.setResponseHandleForbidden((HttpServletResponse) servletResponse);
                            return;
                        }
                        break;
                    }
                    default:
                        log.info("Account {} does not have permission!", user.getUsername());
                        CustomAccessDeniedHandler.setResponseHandleForbidden((HttpServletResponse) servletResponse);
                        return;
                }
            }
        } catch (Exception e) {
            log.error("Authority user filter error: {}", e.getMessage());
            setResponseHandleInternalServerError(servletResponse);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void setResponseHandleInternalServerError(ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        baseResponse.setMessage(MessageUtils.getMessage("evoucher.message.error"));

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        httpServletResponse.getWriter().write(Constant.gson.toJson(baseResponse));
    }
}

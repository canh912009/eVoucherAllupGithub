package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.dao.DashboardRepository;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.evoucher.adminapi.common.utils.DataUtils.getPageInfo;
import static com.evoucher.adminapi.common.utils.DateUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardServiceImpl implements DashboardService{
    final DashboardRepository repository;
    @Override
    public List<Map<String, Object>> getSupplierChartData(String startDate, String endDate) throws CustomCodeException {
        log.info("get supplier chart data in {}~{}", startDate, endDate);
        try {
            Date startCondition = atStartOfDay(getDateFromStringWithCommonFormat(startDate));
            Date endCondition = atEndOfDay(getDateFromStringWithCommonFormat(endDate));
            return repository.getSupplierChartData(startCondition, endCondition);
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("exception when get supplier chart data");
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Map<String, Object>> getCustomerChartData(String startDate, String endDate) throws CustomCodeException {
        log.info("get customer chart data in {} ~ {}", startDate, endDate);
        try {
            Date startCondition = atStartOfDay(getDateFromStringWithCommonFormat(startDate));
            Date endCondition = atEndOfDay(getDateFromStringWithCommonFormat(endDate));
            return repository.getCustomerChartData(startCondition, endCondition);
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("exception when get supplier chart data");
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<Map<String, Object>> getCustomerTableData(Integer offset, Integer pageSize) throws CustomCodeException {
        log.info("get customer table data with offset: {}, page size: {}", offset, pageSize);
        try {
            Pageable pageable = getPageInfo(offset, pageSize);
            List<Map<String, Object>> result = repository.getCustomerTableData(pageable);
            if (result.isEmpty()) {
                return Page.empty();
            }

            long countCampaign = repository.countCustomerTableData();
            log.info("success with {} results", countCampaign);
            return new PageImpl<>(result, pageable, countCampaign);
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("exception when get customer table data with offset: " + offset + " and page size: " + pageSize);
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Map<String, Object>> getCustomersCampaignChartData(String campaignId) throws CustomCodeException {
        log.info("get customer's campaign chart data by campaign id: {}", campaignId);
        if (Objects.isNull(campaignId)) {
            log.error("campaign id is null");
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.customer.null.id"), HttpStatus.BAD_REQUEST);
        }
        try {
            return repository.getCampaignChartData(campaignId);
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("exception when get customer's campaign chart data by campaignId: " + campaignId);
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<Map<String, Object>> getItemTableData(String supplierId, Integer offset, Integer pageSize) throws CustomCodeException {
        log.info("get item table data with offset: {}, page size: {}, param: {}", offset, pageSize, supplierId);
        try {
            Pageable pageable = getPageInfo(offset, pageSize);

            String paramValue = getIdParamByRoleAndLoggedInUser(supplierId, List.of(EnumRole.ROLE_OPERATOR, EnumRole.ROLE_ADMIN), List.of(EnumRole.ROLE_SUPPLIER, EnumRole.ROLE_BRAND));

            UserPrincipal user = LoggedInUserContext.getLoggedInUser();

            if (EnumRole.ROLE_BRAND.toString().equals(user.getAdminType())) {
                List<Map<String, Object>> result = repository.getBrandItemTableData(paramValue, pageable);
                if (result.isEmpty()) {
                    return Page.empty();
                }
                long count = repository.countBrandItemTableData(paramValue);
                log.info("success with {} results", count);
                return new PageImpl<>(result, pageable, count);
            } else {
                List<Map<String, Object>> result = repository.getItemTableData(paramValue, pageable);
                if (result.isEmpty()) {
                    return Page.empty();
                }
                long count = repository.countItemTableData(paramValue);
                log.info("success with {} results", count);
                return new PageImpl<>(result, pageable, count);
            }

        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("exception when get item table data with offset: " + offset + " and page size: " + pageSize);
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<Map<String, Object>> getBrandTableData(String supplierId, Integer offset, Integer pageSize) throws CustomCodeException {
        log.info("get item brand data with offset: {}, page size: {}", offset, pageSize);
        try {
            String paramValue = getIdParamByRoleAndLoggedInUser(supplierId, List.of(EnumRole.ROLE_OPERATOR, EnumRole.ROLE_ADMIN), List.of(EnumRole.ROLE_SUPPLIER));

            Pageable pageable = getPageInfo(offset, pageSize);
            List<Map<String, Object>> result = repository.getBrandTableData(paramValue, pageable);
            if (result.isEmpty()) {
                return Page.empty();
            }

            long count = repository.countBrandTableData(paramValue);
            log.info("success with {} results", count);
            return new PageImpl<>(result, pageable, count);
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("exception when get brand table data with offset: " + offset + " and page size: " + pageSize);
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<Map<String, Object>> getStoreTableData(String brandId, Integer offset, Integer pageSize) throws CustomCodeException {
        log.info("get item store data with offset: {}, page size: {}, param: {}", offset, pageSize, brandId);
        try {
            String paramValue = getIdParamByRoleAndLoggedInUser(brandId, List.of(EnumRole.ROLE_OPERATOR, EnumRole.ROLE_ADMIN, EnumRole.ROLE_SUPPLIER), List.of(EnumRole.ROLE_BRAND));

            Pageable pageable = getPageInfo(offset, pageSize);
            List<Map<String, Object>> result = repository.getStoreTableData(paramValue, pageable);
            if (result.isEmpty()) {
                return Page.empty();
            }

            long count = repository.countStoreTableData(paramValue);
            log.info("success with {} results", count);
            return new PageImpl<>(result, pageable, count);
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("exception when get store table data with offset: " + offset + " and page size: " + pageSize);
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Map<String, Object>> getStoreGoodsChartData(String storeId, String startDate, String endDate) throws CustomCodeException {
        log.info("get store's goods chart data by store id: {} in {} ~ {}", storeId, startDate, endDate);
        try {
            String paramValue = getIdParamByRoleAndLoggedInUser(storeId, List.of(EnumRole.ROLE_OPERATOR, EnumRole.ROLE_ADMIN, EnumRole.ROLE_SUPPLIER, EnumRole.ROLE_BRAND), List.of(EnumRole.ROLE_STORE));

            Date startCondition = atStartOfDay(getDateFromStringWithCommonFormat(startDate));
            Date endCondition = atEndOfDay(getDateFromStringWithCommonFormat(endDate));
            return repository.getStoreGoodsChartData(paramValue, startCondition, endCondition);
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("exception when get store's goods chart data by store id: " + storeId + " startDate: " + startDate + "endDate" + endDate);
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<Map<String, Object>> getCampaignTableData(String customerId, String startDate, String endDate, Integer offset, Integer pageSize) throws CustomCodeException {
        log.info("get item campaign table data with startDate: {}, endDate: {}, offset: {}, page size: {}",startDate, endDate, offset, pageSize);
        try {
            Pageable pageable = getPageInfo(offset, pageSize);
            String paramValue = getIdParamByRoleAndLoggedInUser(customerId, List.of(EnumRole.ROLE_OPERATOR, EnumRole.ROLE_ADMIN), List.of(EnumRole.ROLE_CUSTOMER));


            Date startCondition = atStartOfDay(getDateFromStringWithCommonFormat(startDate));
            Date endCondition = atEndOfDay(getDateFromStringWithCommonFormat(endDate));
            List<Map<String, Object>> result = repository.getCampaignTableData(paramValue, startCondition, endCondition, pageable);
            if (result.isEmpty()) {
                return Page.empty();
            }

            long count = repository.countCampaignTableDataCount(paramValue, startCondition, endCondition);
            log.info("success with {} results", count);
            return new PageImpl<>(result, pageable, count);
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("exception when get store table data with offset: " + offset + " and page size: " + pageSize);
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<Map<String, Object>> getDeliveryTableData(String customerId, String startDate, String endDate, Integer offset, Integer pageSize) throws CustomCodeException {
        log.info("get deliveru table data with startDate: {}, endDate: {}, offset: {}, page size: {}",startDate, endDate, offset, pageSize);
        try {
            Pageable pageable = getPageInfo(offset, pageSize);
            String paramValue = getIdParamByRoleAndLoggedInUser(customerId, List.of(EnumRole.ROLE_OPERATOR, EnumRole.ROLE_ADMIN), List.of(EnumRole.ROLE_CUSTOMER));



            Date startCondition = atStartOfDay(getDateFromStringWithCommonFormat(startDate));
            Date endCondition = atEndOfDay(getDateFromStringWithCommonFormat(endDate));
            List<Map<String, Object>> result = repository.getDeliveryStatusTableData(paramValue, startCondition, endCondition, pageable);
            if (result.isEmpty()) {
                return Page.empty();
            }

            long count = repository.countDeliveryStatusTableData(paramValue, startCondition, endCondition);
            log.info("success with {} results", count);
            return new PageImpl<>(result, pageable, count);
        } catch (CustomCodeException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("exception when get delivery table data with offset: " + offset + " and page size: " + pageSize);
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    private String getIdParamByRoleAndLoggedInUser(String originParam, List<EnumRole> needToPassParam, List<EnumRole> getRoleByUser) throws CustomCodeException {
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();

        for (EnumRole role: needToPassParam) {
            if (role.toString().equals(user.getAdminType())) {
                if (originParam == null || originParam.isEmpty()) {
                    log.error(String.format("user with role %s can not must pass a not empty id param", role));
                    throw new CustomCodeException("id param can not be null", HttpStatus.BAD_REQUEST);
                }
                return originParam;
            }
        }

        for (EnumRole role: getRoleByUser) {
            if (role.toString().equals(user.getAdminType())) {
                return user.getAdminCorpId();
            }
        }
        throw new CustomCodeException("user not have authorized", HttpStatus.UNAUTHORIZED);
    }
}

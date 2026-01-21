package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.service.models.FilterSearchAdmin;
import com.evoucher.adminapi.admin.service.models.search_response.OperatorSearchRes;
import com.evoucher.adminapi.common.exception.DatabaseException;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface OperatorReqRepoCustom {
    List<OperatorSearchRes> searchByFilter(FilterSearchAdmin filter, Pageable pageable) throws DatabaseException;
    long countAllByFilter(FilterSearchAdmin filter) throws DatabaseException;
}

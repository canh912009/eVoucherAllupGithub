package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.service.models.FilterSearchAdmin;
import com.evoucher.adminapi.admin.service.models.SearchPublishResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PublishRepositoryCustom {
    List<SearchPublishResponse> searchPublish(FilterSearchAdmin filterSearchAdmin, Pageable pageable);

    long countPublish(FilterSearchAdmin filterSearchAdmin);
}

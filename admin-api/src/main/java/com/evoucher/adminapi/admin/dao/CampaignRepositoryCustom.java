package com.evoucher.adminapi.admin.dao;

import com.evoucher.adminapi.admin.service.models.FilterSearchAdmin;
import com.evoucher.adminapi.admin.service.models.SearchCampaignResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampaignRepositoryCustom {

    List<SearchCampaignResponse> searchCampaign(FilterSearchAdmin filterSearchAdmin, Pageable pageable);
    long countCampaign(FilterSearchAdmin filterSearchAdmin);
}

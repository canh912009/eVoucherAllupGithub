package com.evoucher.adminapi.admin.service;

import com.evoucher.adminapi.admin.service.models.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface CampaignService {
    CampaignDTO findById(Integer id);

    CampaignDTO createCampaign(CampaignRequest campaignRequest);

    CampaignDTO updateCampaign(Integer id, CampaignRequest campaignRequest);

    Integer deleteCampaignById(Integer id);

    Page<SearchCampaignResponse> searchCampaign(FilterSearchAdmin filterSearchAdmin);

    Integer updateStatusCampaign(Integer id, ApproveRequest approveRequest);
}

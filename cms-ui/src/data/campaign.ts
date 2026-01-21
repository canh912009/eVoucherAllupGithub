import Router, { useRouter } from 'next/router';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { campaignClient } from './client/crud-client';

import {
  BaseResponse,
  PathOptions,
  CampaignPaginator,
  CampaignQueryOptions,
  Campaign,
} from '@/types';
import { API_ENDPOINTS } from './client/api-endpoints';
import { toast } from 'react-toastify';
import { useTranslation } from 'react-i18next';
import { Routes } from '@/config/routes';
import { Config } from '@/config';

export const useCampaignsQuery = (
  pathOps: PathOptions,
  options: Partial<CampaignQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<CampaignPaginator, Error>(
    [API_ENDPOINTS.CAMPAIGN_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return campaignClient._paginated(
        API_ENDPOINTS.CAMPAIGN_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    campaigns: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useCampaignQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<Campaign>, Error>(
    [API_ENDPOINTS.CAMPAIGN, id],
    () => campaignClient.get(id)
  );

  return {
    campaign: data?.data,
    error,
    loading: isLoading,
  };
};

export const useCreateCampaignMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();

  return useMutation(campaignClient.create, {
    onSuccess: async () => {
      const generateRedirectUrl = Routes.campaigns.list;
      await Router.push(generateRedirectUrl, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CAMPAIGN);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateCampaignMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(campaignClient.update, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.campaigns.list;
      await router.push(`${generateRedirectUrl}/${data?.data?.id}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CAMPAIGN);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const usePatchCampaignMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(campaignClient.patch, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.campaigns.list;
      await router.push(`${generateRedirectUrl}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Refetch of page list
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CAMPAIGN_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useDeleteCampaignMutation = () => {
  const queryClient = useQueryClient();
  const { t } = useTranslation();

  return useMutation(campaignClient.delete, {
    onSuccess: () => {
      toast.success(t('common:successfully-deleted'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CAMPAIGN_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

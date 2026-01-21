import Router, { useRouter } from 'next/router';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { toast } from 'react-toastify';
import { useTranslation } from 'next-i18next';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { campaignClient, deliveryClient } from './client/crud-client';

import {
  BaseResponse,
  Campaign,
  PathOptions,
  Delivery,
  DeliveryPaginator,
  DeliveryQueryOptions,
} from '@/types';
import { Routes } from '@/config/routes';
import { API_ENDPOINTS } from './client/api-endpoints';
import { Config } from '@/config';

export const useDeliveriesQuery = (
  pathOps: PathOptions,
  options: Partial<DeliveryQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<DeliveryPaginator, Error>(
    [API_ENDPOINTS.DELIVERY_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return deliveryClient._paginated(
        API_ENDPOINTS.DELIVERY_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    deliveries: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useDeliveryQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<Delivery>, Error>(
    [API_ENDPOINTS.DELIVERY, id],
    () => deliveryClient.get(id)
  );

  return {
    delivery: data?.data,
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

export const useCreateDeliveryMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(deliveryClient.create, {
    onSuccess: async () => {
      const generateRedirectUrl = Routes.delivery.list;
      await Router.push(generateRedirectUrl, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.DELIVERY);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateDeliveryMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(deliveryClient.update, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.delivery.list;
      await router.push(`${generateRedirectUrl}/${data?.data?.id}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.DELIVERY);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const usePatchDeliveryMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(deliveryClient.patch, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.delivery.list;
      await router.push(`${generateRedirectUrl}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Refetch of page list
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.DELIVERY_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useDeleteDeliveryMutation = () => {
  const queryClient = useQueryClient();
  const { t } = useTranslation();

  return useMutation(deliveryClient.delete, {
    onSuccess: () => {
      toast.success(t('common:successfully-deleted'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.DELIVERY_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

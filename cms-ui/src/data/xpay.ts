import {
  BaseResponse,
  PathOptions,
  VNPTEPayBalance,
  VNPTEPayBrandPaginator,
  VNPTEPayProvidersQueryOptions,
  VNPTEPayGiftPaginator,
  VNPTEPayPINPaginator, Category, VNPTEPayProvider,
} from '@/types';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { useTranslation } from 'react-i18next';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { toast } from 'react-toastify';
import { API_ENDPOINTS } from './client/api-endpoints';
import {
  vnptEpayBalanceClient,
  vnptEpayProvidersClient,
  vnptEpayGiftClient,
  vnptEpayPINClient, xpayProviderClient,
} from './client/crud-client';
import {categoryClient} from "@/data/client/categories";
import {useRouter} from "next/router";
import {Routes} from "@/config/routes";
import {Config} from "@/config";

export const useProvidersXpayListQuery = (
  pathOps: PathOptions,
  options: Partial<VNPTEPayProvidersQueryOptions>,
) => {
  const { data, error, isLoading } = useQuery<VNPTEPayBrandPaginator, Error>(
    [API_ENDPOINTS.XPAY_PROVIDERS_SEACH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return vnptEpayProvidersClient._paginated(
        API_ENDPOINTS.XPAY_PROVIDERS_SEACH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    providersList: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useProviderXpayQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<VNPTEPayProvider>, Error>(
    [API_ENDPOINTS.XPAY_PROVIDER, id],
    () => xpayProviderClient.get(id)
  );

  return {
    provider: data?.data,
    error,
    loading: isLoading,
  };
};

export const useUpdateProviderXpayMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(xpayProviderClient.updateNoId, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.xpayProvider.list;
      await router.push(`${generateRedirectUrl}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },

    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.XPAY_PROVIDER);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useGiftListQuery = (
  pathOps: PathOptions,
  options: Partial<VNPTEPayProvidersQueryOptions>,
  enabled?: boolean
) => {
  const { data, error, isLoading } = useQuery<VNPTEPayGiftPaginator, Error>(
    [API_ENDPOINTS.XPAY_GIFT_SEACH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return vnptEpayGiftClient._paginated(
        API_ENDPOINTS.XPAY_GIFT_SEACH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
      enabled: enabled,
    }
  );

  return {
    giftList: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const usePINListQuery = (
  pathOps: PathOptions,
  options: Partial<VNPTEPayProvidersQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<VNPTEPayPINPaginator, Error>(
    [API_ENDPOINTS.XPAY_PURCHASE_SEACH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return vnptEpayPINClient._paginated(
        API_ENDPOINTS.XPAY_PURCHASE_SEACH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    pinList: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useBalanceQuery = () => {
  const { data, error, isLoading } = useQuery<
    BaseResponse<any>,
    Error
  >([API_ENDPOINTS.XPAY_BALANCE], () => vnptEpayBalanceClient.get(''));

  return {
    balance: data?.data ,
    error,
    loading: isLoading,
  };
};

export const useCreateVNPTEPayPinPurchaseMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();

  return useMutation(vnptEpayPINClient.create, {
    onSuccess: async () => {
      toast.success(t('common:successfully-created'));
    },
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.XPAY_BALANCE);
      queryClient.invalidateQueries(API_ENDPOINTS.XPAY_PURCHASE_SEACH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

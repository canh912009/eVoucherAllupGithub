import Router, { useRouter } from 'next/router';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { toast } from 'react-toastify';
import { useTranslation } from 'next-i18next';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { customerContractClient } from './client/crud-client';

import {
  BaseResponse,
  PathOptions,
  CustomerContract,
  CustomerContractPaginator,
  CustomerContractQueryOptions,
} from '@/types';
import { Routes } from '@/config/routes';
import { API_ENDPOINTS } from './client/api-endpoints';
import { Config } from '@/config';

export const useCustomerContractsQuery = (
  pathOps: PathOptions,
  options: Partial<CustomerContractQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<CustomerContractPaginator, Error>(
    [API_ENDPOINTS.CUSTOMER_CONTRACT_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return customerContractClient._paginated(
        API_ENDPOINTS.CUSTOMER_CONTRACT_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    customerContracts: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useCustomerContractQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<
    BaseResponse<CustomerContract>,
    Error
  >([API_ENDPOINTS.CUSTOMER_CONTRACT, id], () =>
    customerContractClient.get(id)
  );

  return {
    customerContract: data?.data,
    error,
    loading: isLoading,
  };
};

export const useCreateCustomerContractMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();

  return useMutation(customerContractClient.create, {
    onSuccess: async () => {
      const generateRedirectUrl = Routes.customerContracts.list;
      await Router.push(generateRedirectUrl, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CUSTOMER_CONTRACT);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateCustomerContractMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(customerContractClient.update, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.customerContracts.list;
      await router.push(`${generateRedirectUrl}/${data?.data?.id}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CUSTOMER_CONTRACT);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const usePatchCustomerContractMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();

  return useMutation(customerContractClient.patch, {
    onSuccess: async (data) => {
      toast.success(t('common:successfully-updated'));
    },
    // Refetch of page detail
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CUSTOMER_CONTRACT);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useDeleteCustomerContractMutation = () => {
  const queryClient = useQueryClient();
  const { t } = useTranslation();

  return useMutation(customerContractClient.delete, {
    onSuccess: () => {
      toast.success(t('common:successfully-deleted'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CUSTOMER_CONTRACT_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

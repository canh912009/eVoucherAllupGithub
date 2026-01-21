import Router, { useRouter } from 'next/router';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { toast } from 'react-toastify';
import { useTranslation } from 'next-i18next';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { customerClient } from './client/crud-client';

import {
  BaseResponse,
  PathOptions,
  Customer,
  CustomerPaginator,
  CustomerQueryOptions,
} from '@/types';
import { Routes } from '@/config/routes';
import { API_ENDPOINTS } from './client/api-endpoints';
import { Config } from '@/config';

export const useCustomersQuery = (
  pathOps: PathOptions,
  options: Partial<CustomerQueryOptions>
) => {
  // console.log('options = ',options)
  const { data, error, isLoading } = useQuery<CustomerPaginator, Error>(
    [API_ENDPOINTS.CUSTOMER_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return customerClient._paginated(
        API_ENDPOINTS.CUSTOMER_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    customers: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useCustomerQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<Customer>, Error>(
    [API_ENDPOINTS.CUSTOMER, id],
    () => customerClient.get(id)
  );

  return {
    customer: data?.data,
    error,
    loading: isLoading,
  };
};

export const useCreateCustomerMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();

  return useMutation(customerClient.create, {
    onSuccess: async () => {
      const generateRedirectUrl = Routes.customers.list;
      await Router.push(generateRedirectUrl, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CUSTOMER);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateCustomerMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(customerClient.update, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.customers.list;
      await router.push(`${generateRedirectUrl}/${data?.data?.id}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CUSTOMER);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const usePatchCustomerMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(customerClient.patch, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.customers.list;
      await router.push(`${generateRedirectUrl}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Refetch of page list
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CUSTOMER_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useDeleteCustomerMutation = () => {
  const queryClient = useQueryClient();
  const { t } = useTranslation();

  return useMutation(customerClient.delete, {
    onSuccess: () => {
      toast.success(t('common:successfully-deleted'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CUSTOMER_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

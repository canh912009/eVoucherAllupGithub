import Router, { useRouter } from 'next/router';
import {
  useQuery,
  useMutation,
  useQueryClient,
} from 'react-query';
import { toast } from 'react-toastify';
import { useTranslation } from 'next-i18next';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { supplierClient } from './client/crud-client';

import {
  BaseResponse,
  PathOptions,
  Supplier,
  SupplierPaginator,
  SupplierQueryOptions,
} from '@/types';
import { Routes } from '@/config/routes';
import { API_ENDPOINTS } from './client/api-endpoints';
import { Config } from '@/config';

export const useSuppliersQuery = (
  pathOps: PathOptions,
  options: Partial<SupplierQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<SupplierPaginator, Error>(
    [API_ENDPOINTS.SUPPLIER_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return supplierClient._paginated(
        API_ENDPOINTS.SUPPLIER_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    suppliers: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useSupplierQuery = (id: string, enabled?: boolean) => {
  const { data, error, isLoading } = useQuery<BaseResponse<Supplier>, Error>(
    [API_ENDPOINTS.SUPPLIER, id ],
    () => supplierClient.get(id),
    { enabled: enabled }
  );

  return {
    supplier: data?.data,
    error,
    loading: isLoading,
  };
};

export const useCreateSupplierMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(supplierClient.create, {
    onSuccess: async () => {
      const generateRedirectUrl = Routes.suppliers.list;
      await Router.push(generateRedirectUrl, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.SUPPLIER);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateSupplierMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(supplierClient.update, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.suppliers.list;
      await router.push(`${generateRedirectUrl}/${data?.data?.id}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.SUPPLIER);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const usePatchSupplierMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(supplierClient.patch, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.suppliers.list;
      await router.push(`${generateRedirectUrl}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Refetch of page list
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.SUPPLIER_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useDeleteSupplierMutation = () => {
  const queryClient = useQueryClient();
  const { t } = useTranslation();

  return useMutation(supplierClient.delete, {
    onSuccess: () => {
      toast.success(t('common:successfully-deleted'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.SUPPLIER_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

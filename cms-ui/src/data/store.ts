import Router, { useRouter } from 'next/router';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { toast } from 'react-toastify';
import { useTranslation } from 'next-i18next';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { storeClient } from './client/crud-client';

import {
  BaseResponse,
  PathOptions,
  Store,
  StorePaginator,
  StoreQueryOptions,
} from '@/types';
import { Routes } from '@/config/routes';
import { API_ENDPOINTS } from './client/api-endpoints';
import { Config } from '@/config';

export const useStoresQuery = (
  pathOps: PathOptions,
  options: Partial<StoreQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<StorePaginator, Error>(
    [API_ENDPOINTS.STORE_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return storeClient._paginated(
        API_ENDPOINTS.STORE_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    stores: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useStoreQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<Store>, Error>(
    [API_ENDPOINTS.STORE, id],
    () => storeClient.get(id)
  );

  return {
    store: data?.data,
    error,
    loading: isLoading,
  };
};

export const useCreateStoreMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(storeClient.create, {
    onSuccess: async () => {
      const generateRedirectUrl = Routes.stores.list;
      await Router.push(generateRedirectUrl, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.STORE);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateStoreMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(storeClient.update, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.stores.list;
      await router.push(`${generateRedirectUrl}/${data?.data?.id}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.STORE);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useDeleteStoreMutation = () => {
  const queryClient = useQueryClient();
  const { t } = useTranslation();

  return useMutation(storeClient.delete, {
    onSuccess: () => {
      toast.success(t('common:successfully-deleted'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.STORE_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

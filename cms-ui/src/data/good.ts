import Router, { useRouter } from 'next/router';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { toast } from 'react-toastify';
import { useTranslation } from 'next-i18next';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { goodClient } from './client/crud-client';

import {
  BaseResponse,
  PathOptions,
  Good,
  GoodPaginator,
  GoodQueryOptions, Category, Brands,
} from '@/types';
import { Routes } from '@/config/routes';
import { API_ENDPOINTS } from './client/api-endpoints';
import { Config } from '@/config';

export const useGoodsQuery = (
  pathOps: PathOptions,
  options: Partial<GoodQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<GoodPaginator, Error>(
    [API_ENDPOINTS.GOOD_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return goodClient._paginated(
        API_ENDPOINTS.GOOD_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    goods: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};
export const useGoodsChoicePopupQuery = (
  pathOps: PathOptions,
  options: Partial<GoodQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<GoodPaginator, Error>(
    [API_ENDPOINTS.GOOD_CHOICE_POPUP_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return goodClient._paginated(
        API_ENDPOINTS.GOOD_CHOICE_POPUP_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    goods: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useGoodQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<Good>, Error>(
    [API_ENDPOINTS.GOOD, id],
    () => goodClient.get(id)
  );

  return {
    good: data?.data,
    error,
    loading: isLoading,
  };
};

export const useCreateGoodMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();

  return useMutation(goodClient.create, {
    onSuccess: async () => {
      const generateRedirectUrl = Routes.goods.list;
      await Router.push(generateRedirectUrl, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.GOOD);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateGoodMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(goodClient.update, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.goods.list;
      await router.push(`${generateRedirectUrl}/${data?.data?.id}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.GOOD);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const usePatchGoodMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();

  return useMutation(goodClient.patch, {
    onSuccess: async (data) => {
      toast.success(t('common:successfully-updated'));
    },
    // Refetch of page list
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.GOOD_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useDeleteGoodMutation = () => {
  const queryClient = useQueryClient();
  const { t } = useTranslation();

  return useMutation(goodClient.delete, {
    onSuccess: () => {
      toast.success(t('common:successfully-deleted'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.GOOD_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

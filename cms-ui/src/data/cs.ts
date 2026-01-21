import {
  BaseResponse,
  Cs, CsExtendQueryOptions,
  CsPaginator,
  CsQueryOptions,
  PathOptions,
} from '@/types';
import { toast } from 'react-toastify';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { API_ENDPOINTS } from './client/api-endpoints';
import {
  csApproveExtendClient,
  csClient,
  csDisableClient, csExtendClient,
  csResendClient,
} from './client/crud-client';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import { Routes } from '@/config/routes';
import { Config } from '@/config';

export const useCsListQuery = (
  pathOps: PathOptions,
  options: Partial<CsQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<CsPaginator, Error>(
    [API_ENDPOINTS.CS_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return csClient._paginated(
        API_ENDPOINTS.CS_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    csList: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useCsExtendListQuery = (
  pathOps: PathOptions,
  options: Partial<CsExtendQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<CsPaginator, Error>(
    [API_ENDPOINTS.CS_EXTEND_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return csClient._paginated(
        API_ENDPOINTS.CS_EXTEND_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    csExtendList: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useCsQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<Cs>, Error>(
    [API_ENDPOINTS.CS_PIN_DETAIL, id],
    () => csClient.get(id)
  );

  return {
    cs: data?.data,
    error,
    loading: isLoading,
  };
};

export const useCsExtendQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<Cs>, Error>(
    [API_ENDPOINTS.CS_EXTEND_DETAIL, id],
    () => csExtendClient.get(id)
  );

  return {
    csExtend: data?.data,
    error,
    loading: isLoading,
  };
};

export const useUpdateCsDisableMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(csDisableClient.create, {
    onSuccess: async (data, variables) => {
      const generateRedirectUrl = Routes.cs.list;
      await router.push(
        `${generateRedirectUrl}/${variables?.ev}`,
        undefined,
        {
          locale: Config.defaultLanguage,
        }
      );
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CS_PIN_DETAIL);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useApproveExtendCs = (typeRequestExtend? : boolean) => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  // @ts-ignore
  return useMutation(typeRequestExtend ? csApproveExtendClient.create : csApproveExtendClient.patch, {
    onSuccess: async (data, variables) => {
      const generateRedirectUrl = Routes.csApproval.list;
      await router.push(
        `${generateRedirectUrl} `,
        undefined,
        {
          locale: Config.defaultLanguage,
        }
      );
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CS_EXTEND_DETAIL);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateCsResendMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(csResendClient.create, {
    onSuccess: async (data, variables) => {
      const generateRedirectUrl = Routes.cs.list;
      await router.push(
        `${generateRedirectUrl}/${variables?.ev}`,
        undefined,
        {
          locale: Config.defaultLanguage,
        }
      );
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CS_PIN_DETAIL);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

import Router, { useRouter } from 'next/router';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { toast } from 'react-toastify';
import { useTranslation } from 'next-i18next';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { codeClient } from './client/crud-client';

import {
  BaseResponse,
  PathOptions,
  Code,
  CodePaginator,
  CodeQueryOptions,
} from '@/types';
import { Routes } from '@/config/routes';
import { API_ENDPOINTS } from './client/api-endpoints';
import { Config } from '@/config';


export const useCodesQuery = (
  pathOps: PathOptions,
  options: Partial<CodeQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<CodePaginator, Error>(
    [API_ENDPOINTS.CODE_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return codeClient._paginated(
        API_ENDPOINTS.CODE_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    codes: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useCodeQuery = (codeGroupId: string, id: string) => {
  id = `${codeGroupId}/${id}`;
  const { data, error, isLoading } = useQuery<BaseResponse<Code>, Error>(
    [API_ENDPOINTS.CODE, id],
    () => codeClient.get(id)
  );

  return {
    code: data?.data,
    error,
    loading: isLoading,
  };
};

export const useCreateCodeMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(codeClient.create, {
    onSuccess: async () => {
      const generateRedirectUrl = Routes.codes.list;
      await Router.push(generateRedirectUrl, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CODE);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateCodeMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(codeClient.update, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.codes.list;
      await router.push(`${generateRedirectUrl}/${data?.data?.id}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CODE);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useDeleteCodeMutation = () => {
  const queryClient = useQueryClient();
  const { t } = useTranslation();

  return useMutation(codeClient.delete, {
    onSuccess: () => {
      toast.success(t('common:successfully-deleted'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CODE_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

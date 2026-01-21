import Router, { useRouter } from 'next/router';
import { Admin, AdminPaginator, AdminQueryOptions, BaseResponse, PathOptions } from "@/types";
import { API_ENDPOINTS } from "./client/api-endpoints";
import {
  useQuery,
  useMutation,
  useQueryClient,
} from 'react-query';
import { adminClient } from "./client/crud-client";
import { mapPaginatorData } from "@/utils/data-mappers-ev";
import { toast } from 'react-toastify';
import { useTranslation } from 'next-i18next';
import { Routes } from '@/config/routes';
import { Config } from '@/config';


export const useAdminsQuery = (
  pathOps: PathOptions,
  options: Partial<AdminQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<AdminPaginator, Error>(
    [API_ENDPOINTS.ME, pathOps, options],
    ({ queryKey, pageParam }) => {
      return adminClient._paginated(
        API_ENDPOINTS.ADMIN_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );
  return {
    admins: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useAdminQuery = (
  id: string
) => {
  const { data, error, isLoading } = useQuery<BaseResponse<Admin>, Error>(
    [API_ENDPOINTS.SUPPLIER, id],
    () => adminClient.get(id)
  );
  return {
    admin: data?.data,
    error,
    loading: isLoading,
  };
};

export const useCreateAdminMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();

  return useMutation(adminClient.create, {
    onSuccess: async () => {
      const generateRedirectUrl = Routes.adminUser.list;
      await Router.push(generateRedirectUrl, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.ME);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateAdminMutation = (isChangePassword: boolean) => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(adminClient.updateAdminInfo, {
    onSuccess: async (data) => {
      if (!isChangePassword) {
        const generateRedirectUrl = Routes.adminUser.list;
        await router.push(`${generateRedirectUrl}/${data?.data}`, undefined, {
          locale: Config.defaultLanguage,
        });
      }
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.ME);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};


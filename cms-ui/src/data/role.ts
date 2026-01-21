import {
  BaseResponse,
  PathOptions, Role, RoleQueryOptions, RolesPaginator
} from "@/types";
import {useMutation, useQuery, useQueryClient} from "react-query";
import {API_ENDPOINTS} from "@/data/client/api-endpoints";
import {rolesClient, supplierClient} from "@/data/client/crud-client";
import {mapPaginatorData} from "@/utils/data-mappers-ev";
import {useTranslation} from "next-i18next";
import {toast} from "react-toastify";
import Router, {useRouter} from "next/router";
import {Routes} from "@/config/routes";
import {Config} from "@/config";

export const useRolesQuery = (
  pathOps?: PathOptions,
  options?: Partial<RoleQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<RolesPaginator, Error>(
    [API_ENDPOINTS.ROLE_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return rolesClient._paginated(
        API_ENDPOINTS.ROLE_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    roles: data?.data ?? [],
    // @ts-ignore
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useRoleQuery = (roleCode: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<Role>, Error>(
    [API_ENDPOINTS.ROLE, roleCode ],
    () => rolesClient.get(roleCode)
  );

  return {
    role: data?.data,
    error,
    loading: isLoading,
  };
};

export const useDeleteRoleMutation = () => {
  const queryClient = useQueryClient();
  const { t } = useTranslation();

  return useMutation(rolesClient.delete, {
    onSuccess: () => {
      toast.success(t('common:successfully-deleted'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.ROLE_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useCreateRoleMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(rolesClient.create, {
    onSuccess: async () => {
      const generateRedirectUrl = Routes.roles.list;
      await Router.push(generateRedirectUrl, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.ROLE);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateRoleMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(rolesClient.update, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.roles.list;
      await router.push(`${generateRedirectUrl}/${data?.data?.roleCode}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.ROLE);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

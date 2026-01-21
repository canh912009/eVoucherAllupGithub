import {
  BaseResponse, MenuGroup,
  MenuGroupQueryOptions,
  MenuGroupsPaginator,
  PathOptions, Supplier,
} from "@/types";
import {useMutation, useQuery, useQueryClient} from "react-query";
import {API_ENDPOINTS} from "@/data/client/api-endpoints";
import {menuGroupsClient, supplierClient} from "@/data/client/crud-client";
import {mapPaginatorData} from "@/utils/data-mappers-ev";
import {useTranslation} from "next-i18next";
import {toast} from "react-toastify";
import Router, {useRouter} from "next/router";
import {Routes} from "@/config/routes";
import {Config} from "@/config";

export const useMenuGroupsQuery = (
  pathOps?: PathOptions,
  options?: Partial<MenuGroupQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<MenuGroupsPaginator, Error>(
    [API_ENDPOINTS.MENU_GROUP_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return menuGroupsClient._paginated(
        API_ENDPOINTS.MENU_GROUP_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    menuGroups: data?.data ?? [],
    // @ts-ignore
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useMenuGroupQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<MenuGroup>, Error>(
    [API_ENDPOINTS.MENU_GROUP, id ],
    () => menuGroupsClient.get(id)
  );

  return {
    menuGroup: data?.data,
    error,
    loading: isLoading,
  };
};

export const useDeleteMenuGroupMutation = () => {
  const queryClient = useQueryClient();
  const { t } = useTranslation();

  return useMutation(menuGroupsClient.delete, {
    onSuccess: () => {
      toast.success(t('common:successfully-deleted'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.MENU_GROUP_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useCreateMenuGroupMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(menuGroupsClient.create, {
    onSuccess: async () => {
      const generateRedirectUrl = Routes.menuGroups.list;
      await Router.push(generateRedirectUrl, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.MENU_GROUP);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateMenuGroupMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(menuGroupsClient.update, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.menuGroups.list;
      await router.push(`${generateRedirectUrl}/${data?.data?.id}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.MENU_GROUP);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

import {
  BaseResponse, Menu, MenuQueryOptions, MenusPaginator,
  PathOptions,
} from "@/types";
import {useMutation, useQuery, useQueryClient} from "react-query";
import {API_ENDPOINTS} from "@/data/client/api-endpoints";
import {menusClient } from "@/data/client/crud-client";
import {mapPaginatorData} from "@/utils/data-mappers-ev";
import {useTranslation} from "next-i18next";
import {toast} from "react-toastify";
import Router, {useRouter} from "next/router";
import {Routes} from "@/config/routes";
import {Config} from "@/config";

export const useMenusQuery = (
  pathOps: PathOptions,
  options?: Partial<MenuQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<MenusPaginator, Error>(
    [API_ENDPOINTS.MENU_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return menusClient._paginated(
        API_ENDPOINTS.MENU_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    menus: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useMenuQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<Menu>, Error>(
    [API_ENDPOINTS.MENU, id ],
    () => menusClient.get(id)
  );

  return {
    menu: data?.data ,
    error,
    loading: isLoading,
  };
};

export const useDeleteMenuMutation = () => {
  const queryClient = useQueryClient();
  const { t } = useTranslation();

  return useMutation(menusClient.delete, {
    onSuccess: () => {
      toast.success(t('common:successfully-deleted'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.MENU_SEARCH);
      queryClient.invalidateQueries(API_ENDPOINTS.MENU_GROUP);
      queryClient.invalidateQueries(API_ENDPOINTS.ROLE);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useCreateMenuMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(menusClient.create, {
    onSuccess: async () => {
      const generateRedirectUrl = Routes.menus.list;
      await Router.push(generateRedirectUrl, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.MENU);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateMenuMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(menusClient.update, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.menus.list;
      await router.push(`${generateRedirectUrl}/${data?.data?.id}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.MENU);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

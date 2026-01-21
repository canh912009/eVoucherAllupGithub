import Router, { useRouter } from 'next/router';
import {
    useQuery,
    useMutation,
    useQueryClient,
} from 'react-query';
import { toast } from 'react-toastify';
import { useTranslation } from 'next-i18next';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { brandsClient } from './client/crud-client';

import {
    PathOptions,
    Brands,
    BrandsPaginator,
    BrandsQueryOptions,
    BaseResponse
} from '@/types';
import { Routes } from '@/config/routes';
import { API_ENDPOINTS } from './client/api-endpoints';
import { Config } from '@/config';

export const useBrandsQuery = (
    pathOps: PathOptions,
    options: Partial<BrandsQueryOptions>,
    enabled?: boolean
) => {
    // console.log('options = ',options)
    const { data, error, isLoading } = useQuery<BrandsPaginator, Error>(
        [API_ENDPOINTS.BRANDS_SEARCH, pathOps, options],
        ({ queryKey, pageParam }) => {
            return brandsClient._paginated(
                API_ENDPOINTS.BRANDS_SEARCH,
                pathOps,
                Object.assign({}, queryKey[2], pageParam)
            );
        },
        {
            keepPreviousData: true,
            enabled: enabled,
        }
    );
    return {
        brands: data?.data ?? [],
        paginatorInfo: mapPaginatorData(pathOps, data),
        error,
        loading: isLoading,
    };
};

export const useCreateBrandsMutation = () => {
    const { t } = useTranslation();
    const queryClient = useQueryClient();
    const router = useRouter();

    return useMutation(brandsClient.create, {
        onSuccess: async () => {
            const generateRedirectUrl = Routes.brands.list;
            await Router.push(generateRedirectUrl, undefined, {
                locale: Config.defaultLanguage,
            });
            toast.success(t('common:successfully-created'));
        },
        onSettled: () => {
            queryClient.invalidateQueries(API_ENDPOINTS.BRANDS);
        },
        onError: (error: any) => {
            toast.error(t(`common:${error?.response?.data.message}`));
        },
    });
};

export const useUpdateBrandsMutation = () => {
    const { t } = useTranslation();
    const queryClient = useQueryClient();
    const router = useRouter();

    return useMutation(brandsClient.update, {
        onSuccess: async (data) => {
            const generateRedirectUrl = Routes.brands.list;
            await router.push(`${generateRedirectUrl}/${data?.data?.id}`, undefined, {
                locale: Config.defaultLanguage,
            });
            toast.success(t('common:successfully-updated'));
        },
        // Always refetch after error or success:
        onSettled: () => {
            queryClient.invalidateQueries(API_ENDPOINTS.BRANDS);
        },
        onError: (error: any) => {
            toast.error(t(`common:${error?.response?.data.message}`));
        },
    });
};

export const useBrandInfoQuery = (id: string) => {
    const { data, error, isLoading } = useQuery<BaseResponse<Brands>, Error>(
        [API_ENDPOINTS.BRANDS, id],
        () => brandsClient.get(id)
    );
    return {
        brands: data?.data,
        error,
        loading: isLoading,
    };
}

export const useUpdateBrandQuery = (id: string) => {
    const { data, error, isLoading } = useQuery<BaseResponse<Brands>, Error>(
        [API_ENDPOINTS.BRANDS, id],
        () => brandsClient.get(id)
    );

    return {
        brands: data?.data,
        error,
        loading: isLoading,
    };
};

export const useDeleteBrandMutation = () => {
    const queryClient = useQueryClient();
    const { t } = useTranslation();

    return useMutation(brandsClient.delete, {
        onSuccess: () => {
            toast.success(t('common:successfully-deleted'));
        },
        // Always refetch after error or success:
        onSettled: () => {
            queryClient.invalidateQueries(API_ENDPOINTS.BRANDS);
        },
        onError: (error: any) => {
            toast.error(t(`common:${error?.response?.data.message}`));
        },
    });
};

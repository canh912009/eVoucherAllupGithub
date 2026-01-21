import { Config } from '@/config';
import { Routes } from '@/config/routes';
import {
  BaseResponse, Brands,
  Category,
  CategoryPaginator,
  CategoryQueryOptions, Good,
  PathOptions,
} from '@/types';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import Router, { useRouter } from 'next/router';
import { useTranslation } from 'react-i18next';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { toast } from 'react-toastify';
import { API_ENDPOINTS } from './client/api-endpoints';
import { categoryClient } from './client/categories';

export const useCategoriesQuery = (
  pathOps: PathOptions,
  options: Partial<CategoryQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<CategoryPaginator, Error>(
    [API_ENDPOINTS.CATEGORY, pathOps, options],
    ({ queryKey, pageParam }) =>
      categoryClient._paginated(
        API_ENDPOINTS.CATEGORY_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      ),
    {
      keepPreviousData: true,
    }
  );
  return {
    categories: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useCategoryQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<Category>, Error>(
    [API_ENDPOINTS.CATEGORY, id],
    () => categoryClient.get(id)
  );

  return {
    category: data?.data,
    error,
    loading: isLoading,
  };
};

//CATEGORY_SEARCH
export const useCategorySearchQuery = (
  pathOps: PathOptions,
  options: Partial<CategoryQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<CategoryPaginator, Error>(
    [API_ENDPOINTS.CATEGORY_SEARCH, options],
    ({ queryKey, pageParam }) =>
      categoryClient._paginated(
        API_ENDPOINTS.CATEGORY_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      ),
    {
      keepPreviousData: true,
    }
  );
  return {
    categories: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useCreateCategoryMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();

  return useMutation(categoryClient.create, {
    onSuccess: async () => {
      const generateRedirectUrl = Routes.category.list;
      await Router.push(generateRedirectUrl, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-created'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CATEGORY);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useUpdateCategoryMutation = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const router = useRouter();

  return useMutation(categoryClient.update, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.category.list;
      await router.push(`${generateRedirectUrl}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-updated'));
    },

    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CATEGORY);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const useDeleteCategoryMutation = () => {
  const queryClient = useQueryClient();
  const { t } = useTranslation();
  const router = useRouter();

  return useMutation(categoryClient.delete, {
    onSuccess: async (data) => {
      const generateRedirectUrl = Routes.category.list;
      await router.push(`${generateRedirectUrl}`, undefined, {
        locale: Config.defaultLanguage,
      });
      toast.success(t('common:successfully-deleted'));
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries(API_ENDPOINTS.CATEGORY_SEARCH);
    },
    onError: (error: any) => {
      toast.error(t(`common:${error?.response?.data.message}`));
    },
  });
};

export const sortBulkCategoriesByDisplayIndex = (list: Category[])  => {
  // Lọc và sắp xếp categories
  const sortedList = list?.filter((category: Category) => category.validYn === "Y")
      .sort((a, b) => (a.displayIndex ?? 0) - (b.displayIndex ?? 0));

  // @ts-ignore
  sortedList?.forEach((category: Category)  => {
    // Thêm trường categoryName
    category.categoryName = category.category?.categoryName ?? '';

    if (category.bulkBrands) {
      category.bulkBrands = category.bulkBrands
          .filter((brand: Brands) => brand.validYn === "Y")
          .sort((a, b) => (a.displayIndex ?? 0) - (b.displayIndex ?? 0));

      category.bulkBrands.forEach((brand: Brands) => {
        brand.brandName = brand.brand?.brandName ?? '';

        if (brand.bulkGoods) {
          // @ts-ignore
          brand.bulkGoods = brand.bulkGoods
              .filter((good: Good) => good.validYn === "Y")
              .sort((a, b) => (a.displayIndex ?? 0) - (b.displayIndex ?? 0));

          brand.bulkGoods.forEach((good: Good)  => {
            good.goodsName = good.goods?.goodsName ?? '';
          });
        }
      });
    }
  });

  return sortedList ?? [];
}

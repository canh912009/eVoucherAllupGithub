import {
  GiftPopBrandPaginator,
  GiftPopBrandQueryOptions,
  PathOptions,
} from '@/types';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { useQuery } from 'react-query';
import { API_ENDPOINTS } from './client/api-endpoints';
import { giftPopBrandClient, giftPopGoodClient } from './client/crud-client';

export const useBrandListQuery = (
  pathOps: PathOptions,
  options: Partial<GiftPopBrandQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<GiftPopBrandPaginator, Error>(
    [API_ENDPOINTS.GIFTPOP_BRAND_SEACH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return giftPopBrandClient._paginated(
        API_ENDPOINTS.GIFTPOP_BRAND_SEACH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    providersList: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const getBrandList = async () => {
  // console.log('getBrandList');
  const response = await giftPopBrandClient._paginated(
    API_ENDPOINTS.GIFTPOP_BRAND_SEACH,
    null as unknown as PathOptions,
    null as unknown as Partial<GiftPopBrandQueryOptions>
  );
  return response?.data;
};

export const getGoodList = async (brandCode: string) => {
  // console.log('getGoodList', brandCode);
  const response = await giftPopGoodClient.getGoodList(brandCode);
  return response?.data;
};

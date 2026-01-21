import {
  PathOptions,
  UrBoxBrandPaginator,
  UrBoxBrandQueryOptions,
} from '@/types';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { useQuery } from 'react-query';
import { API_ENDPOINTS } from './client/api-endpoints';
import { urBoxBrandClient, urBoxGoodClient } from './client/crud-client';

export const useBrandListQuery = (
  pathOps: PathOptions,
  options: Partial<UrBoxBrandQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<UrBoxBrandPaginator, Error>(
    [API_ENDPOINTS.URBOX_BRAND_SEACH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return urBoxBrandClient._paginated(
        API_ENDPOINTS.URBOX_BRAND_SEACH,
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
  const response = await urBoxBrandClient._paginated(
    API_ENDPOINTS.URBOX_BRAND_SEACH,
    null as unknown as PathOptions,
    null as unknown as Partial<UrBoxBrandQueryOptions>
  );
  return response?.data;
};

export const getGoodList = async (brandCode: string) => {
  // console.log('getGoodList', brandCode);
  const response = await urBoxGoodClient.getGoodList(brandCode);
  return response?.data;
};

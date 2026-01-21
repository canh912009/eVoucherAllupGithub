import { useQuery } from 'react-query';
import { API_ENDPOINTS } from './client/api-endpoints';
import { stockClient } from './client/crud-client';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import {PathOptions, StockPaginator, StockQueryOptions} from "@/types";
import {tr} from "date-fns/locale";

export const useStockListQuery = (
  pathOps: PathOptions,
  options: Partial<StockQueryOptions>
) => {
  const { data, error, isLoading } = useQuery<StockPaginator, Error>(
    [API_ENDPOINTS.STOCK_LIST, pathOps, options],
    ({ queryKey }) =>
      stockClient._paginated(
        API_ENDPOINTS.STOCK_LIST,
        pathOps,
        Object.assign({}, queryKey[2])
      ),
    {
      keepPreviousData: true,
    }
  );

  return {
    stockList: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useStockAllQuery = (options: Partial<StockQueryOptions>, enabled: boolean = false) => {
  const { data, error, isLoading } = useQuery(
    [API_ENDPOINTS.STOCK_LIST, null, options],
    ({ queryKey }) =>
      stockClient._paginated(
        API_ENDPOINTS.STOCK_LIST,
        undefined,
        Object.assign({}, queryKey[2])
      ),
    {
      keepPreviousData: true,
      enabled: enabled
    }
  );

  return {
    stockAll: data?.data ?? [],
    errorAll: error,
    loadingAll: isLoading,
  };
};


export const getStockListSummary = async (params?: { insertedAtFrom?: string }) => {
  const response = await stockClient._paginated(
    API_ENDPOINTS.STOCK_LIST_SUM,
    null as unknown as PathOptions,
    params || {}
  );
  return response?.data;
};
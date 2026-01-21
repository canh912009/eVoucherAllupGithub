import { API_ENDPOINTS } from '@/data/client/api-endpoints';
import {
  brandTableClient,
  campaignTableClient,
  customerCampaignChartClient,
  customerChartClient,
  customerTableClient,
  deliveryTableClient,
  itemTableClient,
  goodChartClient,
  storeTableClient,
  supplierChartClient
} from '@/data/client/crud-client';
import {
  BaseResponse,
  BrandTablePaginator,
  CampaignTablePaginator,
  CustomerCampaignItem,
  CustomerChartItem,
  CustomerTablePaginator,
  DeliveryTablePaginator,
  ItemTablePaginator,
  PathOptions,
  DashboardTimeQueryOptions as SearchValue,
  GoodChartItem,
  StoreTablePaginator,
  SupplierChartItem
} from '@/types';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { useQuery } from 'react-query';

export const useSupplierChartQuery = (options: Partial<SearchValue> = {}) => {
  const { data, error, isLoading } = useQuery<BaseResponse<SupplierChartItem[]>, Error>(
    [API_ENDPOINTS.DASHBOARD_SUPPLIER_CHART_ADMIN, options],
    ({ queryKey, pageParam }) => supplierChartClient.all(Object.assign({}, queryKey[1], pageParam)) ,
    {
      keepPreviousData: true,
    }
  );

  return {
    supplierData: data?.data ?? [],
    error,
    loading: isLoading,
  };
};

export const useCustomerChartQuery = (options: Partial<SearchValue> = {}) => {
  const { data, error, isLoading } = useQuery<BaseResponse<CustomerChartItem[]>, Error>(
    [API_ENDPOINTS.DASHBOARD_CUSTOMER_CHART_ADMIN, options],
    ({ queryKey, pageParam }) => customerChartClient.all(Object.assign({}, queryKey[1], pageParam)) ,
    {
      keepPreviousData: true,
    }
  );

  return {
    customerData: data?.data ?? [],
    error,
    loading: isLoading,
  };
};

export const useGoodChartQuery = (options: Partial<SearchValue> = {}) => {
  const { data, error, isLoading } = useQuery<BaseResponse<GoodChartItem[]>, Error>(
    [API_ENDPOINTS.DASHBOARD_GOOD_CHART_ADMIN, options],
    ({ queryKey, pageParam }) => goodChartClient.all(Object.assign({}, queryKey[1], pageParam)) ,
    {
      keepPreviousData: true,
    }
  );

  return {
    data: data?.data ?? [],
    error,
    loading: isLoading,
  };
};

export const useCustomerTableQuery = ( pathOps: PathOptions ) => {
  const { data, error, isLoading } = useQuery<CustomerTablePaginator, Error>(
    [API_ENDPOINTS.DASHBOARD_CUSTOMER_TABLE_ADMIN, pathOps],
    ({ queryKey, pageParam }) => {
      return customerTableClient._paginated(
        API_ENDPOINTS.DASHBOARD_CUSTOMER_TABLE_ADMIN,
        pathOps
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    customerTableData: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useCustomerCampaignQuery = (options: Partial<SearchValue> = {}) => {
  const { data, error, isLoading } = useQuery<BaseResponse<CustomerCampaignItem[]>, Error>(
    [API_ENDPOINTS.DASHBOARD_CUSTOMER_CAMPAIGN_ADMIN, options],
    ({ queryKey, pageParam }) => customerCampaignChartClient.all(Object.assign({}, queryKey[1], pageParam)) ,
    {
      keepPreviousData: true,
    }
  );
  return {
    customersCampaign: data?.data,
    error,
    isLoading: isLoading,
  };
};

export const useItemTableQuery = (
  pathOps: PathOptions,
  options: Partial<SearchValue>
) => {
  const { data, error, isLoading } = useQuery<ItemTablePaginator, Error>(
    [API_ENDPOINTS.DASHBOARD_ITEM_TABLE, pathOps, options],
    ({ queryKey, pageParam }) => {
      return itemTableClient._paginated(
        API_ENDPOINTS.DASHBOARD_ITEM_TABLE,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    itemTableData: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useBrandTableQuery = (
  pathOps: PathOptions,
  options: Partial<SearchValue>
) => {
  const { data, error, isLoading } = useQuery<BrandTablePaginator, Error>(
    [API_ENDPOINTS.DASHBOARD_BRAND_TABLE, pathOps, options],
    ({ queryKey, pageParam }) => {
      return brandTableClient._paginated(
        API_ENDPOINTS.DASHBOARD_BRAND_TABLE,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    brandTableData: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useStoreTableQuery = (
  pathOps: PathOptions,
  options: Partial<SearchValue>
) => {
  const { data, error, isLoading } = useQuery<StoreTablePaginator, Error>(
    [API_ENDPOINTS.DASHBOARD_STORE_TABLE, pathOps, options],
    ({ queryKey, pageParam }) => {
      return storeTableClient._paginated(
        API_ENDPOINTS.DASHBOARD_STORE_TABLE,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    storeTableData: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useCampaignTableQuery = (
  pathOps: PathOptions,
  options: Partial<SearchValue>
) => {
  const { data, error, isLoading } = useQuery<CampaignTablePaginator, Error>(
    [API_ENDPOINTS.DASHBOARD_CAMPAIGN_TABLE, pathOps, options],
    ({ queryKey, pageParam }) => {
      return campaignTableClient._paginated(
        API_ENDPOINTS.DASHBOARD_CAMPAIGN_TABLE,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    campaignTableData: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

export const useDeliveryTableQuery = (
  pathOps: PathOptions,
  options: Partial<SearchValue>
) => {
  const { data, error, isLoading } = useQuery<DeliveryTablePaginator, Error>(
    [API_ENDPOINTS.DASHBOARD_DELIVERY_TABLE, pathOps, options],
    ({ queryKey, pageParam }) => {
      return deliveryTableClient._paginated(
        API_ENDPOINTS.DASHBOARD_DELIVERY_TABLE,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    deliveryTableData: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

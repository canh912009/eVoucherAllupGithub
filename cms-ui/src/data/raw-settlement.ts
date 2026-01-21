import {
  BaseResponse,
  PathOptions,
  RawSettlement,
  RawSettlementPaginator,
  RawSettlementQueryOptions as SearchValue,
} from "@/types";
import { useQuery, } from "react-query";
import {API_ENDPOINTS} from "@/data/client/api-endpoints";
import {rawSettlement } from "@/data/client/crud-client";
import {mapPaginatorData} from "@/utils/data-mappers-ev";

export const useRawSettlementQuery = (
  pathOps: PathOptions,
  options: Partial<SearchValue>
) => {
  const { data, error, isLoading } = useQuery<RawSettlementPaginator, Error>(
    [API_ENDPOINTS.RAW_SETTLEMENT_SEARCH, pathOps, options],
    ({ queryKey, pageParam }) => {
      return rawSettlement._paginated(
        API_ENDPOINTS.RAW_SETTLEMENT_SEARCH,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    raws: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};

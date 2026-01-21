import { useQuery } from 'react-query';
import { codeGroupClient } from './client/crud-client';
import {
  BaseResponse,
  CodeGroup,
} from '@/types';
import { API_ENDPOINTS } from './client/api-endpoints';


export const useCodeGroupQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<CodeGroup>, Error>(
    [API_ENDPOINTS.CODE_GROUP, id],
    () => codeGroupClient.get(id)
  );

  return {
    codeGroup: data?.data,
    error,
    loading: isLoading,
  };
};

export const useCodeGroupSettlementMethodQuery = (id: string) => {
  const { data, error, isLoading } = useQuery<BaseResponse<CodeGroup>, Error>(
    [API_ENDPOINTS.CODE_GROUP, id],
    () => codeGroupClient.get(id)
  );

  return {
    codeGroupSettlementMethod: data?.data,
    error,
    loading: isLoading,
  };
};

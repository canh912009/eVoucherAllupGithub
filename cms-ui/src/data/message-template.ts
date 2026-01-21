import {
  MessageTemplate,
  MessageTemplatePaginator,
  PathOptions
} from '@/types';
import { mapPaginatorData } from '@/utils/data-mappers-ev';
import { useQuery } from 'react-query';
import { API_ENDPOINTS } from './client/api-endpoints';
import { messageTemplateClient } from './client/crud-client';

export const useMessageTemplatesQuery = (
  pathOps: PathOptions,
  options: Partial<MessageTemplate>
) => {
  const _endpoint = `${API_ENDPOINTS.MESSAGE_TEMPLATE}/get-all`;
  const { data, error, isLoading } = useQuery<MessageTemplatePaginator, Error>(
    [_endpoint, pathOps, options],
    ({ queryKey, pageParam }) => {
      return messageTemplateClient._paginated(
        _endpoint,
        pathOps,
        Object.assign({}, queryKey[2], pageParam)
      );
    },
    {
      keepPreviousData: true,
    }
  );

  return {
    messageTemplates: data?.data ?? [],
    paginatorInfo: mapPaginatorData(pathOps, data),
    error,
    loading: isLoading,
  };
};
